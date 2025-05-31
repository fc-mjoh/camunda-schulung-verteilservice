package de.reply.fincon.handler;

import de.reply.fincon.configuration.ExternalTaskProperties;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.Map;

public class ExternalTaskHandler implements org.camunda.bpm.client.task.ExternalTaskHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(ExternalTaskHandler.class);
    private final ExternalTaskProperties externalTaskProperties;

    public ExternalTaskHandler(ExternalTaskProperties externalTaskProperties) {
        this.externalTaskProperties = externalTaskProperties;
    }

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        LOGGER.info("Executing external task {}", externalTask.getId());
        String payload = externalTask.getVariable("payload");
        ResponseEntity<String> retrieve = RestClient.create(externalTaskProperties.url())
                .post().body(payload).retrieve().toEntity(String.class);

        if (retrieve.getStatusCode().is2xxSuccessful()) {
            externalTaskService.setVariables(externalTask, Map.of("successful", true));
        } else {
            externalTaskService.setVariables(externalTask, Map.of("successful", false));
        }

        externalTaskService.complete(externalTask);
    }
}