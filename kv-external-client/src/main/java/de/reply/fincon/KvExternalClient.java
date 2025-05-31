package de.reply.fincon;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.camunda.bpm.client.ExternalTaskClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KvExternalClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(KvExternalClient.class);

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