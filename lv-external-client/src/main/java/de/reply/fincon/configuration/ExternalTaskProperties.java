package de.reply.fincon.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "task")
public record ExternalTaskProperties(
        String urlRestEngine,
        String subscribeTopic,
        String workerId,
        Integer asyncResponseTimeout,
        Integer lockDuration,
        String url
) {
}