package com.tui.github.repository.config;

import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GitHubClientConfiguration {

    @ConfigurationProperties("github-api")
    public record GitHubClientProperties(String url,
                                         String version,
                                         String token) {
    }
    @Bean
    public WebClient webClient(GitHubClientProperties properties) {
        return WebClient.builder()
                .baseUrl(properties.url())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("X-GitHub-Api-Version", properties.version())
                .defaultHeaders(h -> {
                    if (Strings.isNotBlank(properties.token())) {
                        h.setBearerAuth(properties.token());
                    }
                })
                .build();
    }
}
