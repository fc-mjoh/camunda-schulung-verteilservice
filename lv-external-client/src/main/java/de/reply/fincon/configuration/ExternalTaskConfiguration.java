package de.reply.fincon.configuration;

import org.camunda.bpm.client.ExternalTaskClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ExternalTaskProperties.class)
public class ExternalTaskConfiguration {

    @Bean
    public ExternalTaskClient externalTaskClient(final ExternalTaskProperties externalTaskProperties) {
        return ExternalTaskClient.create()
                .baseUrl(externalTaskProperties.urlRestEngine())
                .workerId(externalTaskProperties.workerId())
                .lockDuration(externalTaskProperties.lockDuration())
                .build();
    }

}