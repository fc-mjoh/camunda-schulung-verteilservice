
package de.reply.fincon;

import org.camunda.bpm.client.ExternalTaskClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class DokApiExternalClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(DokApiExternalClient.class);

    public static void main(String[] args) {
        ExternalTaskClient client = ExternalTaskClient.create().baseUrl("http://localhost:8081/engine-rest")
                .asyncResponseTimeout(10000) // long polling timeout
                .build();

        client.subscribe("dok-api-topic")
                .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {
                    externalTaskService.setVariables(externalTask, Map.of("successful", true));
                    externalTaskService.complete(externalTask);

                }).open();
    }

}