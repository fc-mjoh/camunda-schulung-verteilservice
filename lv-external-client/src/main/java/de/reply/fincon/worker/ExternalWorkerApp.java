package de.reply.fincon.worker;

import de.reply.fincon.configuration.ExternalTaskProperties;
import de.reply.fincon.handler.ExternalTaskHandler;
import jakarta.annotation.PostConstruct;
import org.camunda.bpm.client.ExternalTaskClient;
import org.springframework.stereotype.Component;

@Component
public class ExternalWorkerApp {

    private final ExternalTaskClient externalTaskClient;
    private final ExternalTaskProperties externalTaskProperties;

    public ExternalWorkerApp(ExternalTaskClient externalTaskClient, ExternalTaskProperties externalTaskProperties) {
        this.externalTaskClient = externalTaskClient;
        this.externalTaskProperties = externalTaskProperties;
    }

    @PostConstruct
    public void init() {
        externalTaskClient.subscribe(externalTaskProperties.subscribeTopic())
                .lockDuration(externalTaskProperties.lockDuration())
                .handler(new ExternalTaskHandler(externalTaskProperties))
                .open();
    }

}