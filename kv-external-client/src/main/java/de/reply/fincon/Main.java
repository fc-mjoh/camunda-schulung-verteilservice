package de.reply.fincon;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.spin.json.SpinJsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.camunda.spin.DataFormats.json;
import static org.camunda.spin.Spin.S;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        ExternalTaskClient client = ExternalTaskClient.create().baseUrl("http://localhost:8081/engine-rest")
                .asyncResponseTimeout(10000) // long polling timeout
                .build();

        client.subscribe("kv-topic")
                .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {

                    String payload = externalTask.getVariable("payload");

                    WebTarget webTarget;
                    Client restClient = ClientBuilder.newClient();
                    webTarget = restClient.target("http://localhost:9080/kv");

                    try (Response t = webTarget.request()
                            .accept(MediaType.APPLICATION_JSON).post(Entity.json(payload))) {

                        int status = t.getStatus();
                    }

                    // Complete the task
                    externalTaskService.complete(externalTask);

                }).open();
    }
}