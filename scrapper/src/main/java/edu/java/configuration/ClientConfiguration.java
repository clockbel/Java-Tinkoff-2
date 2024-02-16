package edu.java.configuration;

import edu.java.client.GitHubClient;
import edu.java.client.StackOverflowClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {

    @Bean
    public GitHubClient gitHubClient() {
        return new GitHubClient(WebClient.builder().baseUrl("https://api.github.com").build());
    }

    @Bean
    public StackOverflowClient stackOverflowClient() {
        return new StackOverflowClient(WebClient.builder().baseUrl("https://api.stackexchange.com").build());
    }
}
