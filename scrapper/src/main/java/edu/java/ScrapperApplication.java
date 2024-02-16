package edu.java;

import edu.java.client.GitHubClient;
import edu.java.client.StackOverflowClient;
import edu.java.configuration.ApplicationConfig;
import edu.java.response.QuestionResponse;
import edu.java.response.RepositoryResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class ScrapperApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ScrapperApplication.class, args);
        GitHubClient gitHubClient = context.getBean(GitHubClient.class);
        Mono<RepositoryResponse> userResponseMono = gitHubClient.fetchRepository("clockbel", "Java-Tinkoff");
        userResponseMono.subscribe(user -> {
            System.out.println("Репозиторий: " + user.getFullName() + "Дата создания: " + user.getCreatedAt());
        });
        StackOverflowClient stackOverflowClient = context.getBean(StackOverflowClient.class);
        Mono<QuestionResponse> questionResponseMono = stackOverflowClient.fetchQuestion(1566516);
        questionResponseMono.subscribe(questionResponse -> {
            System.out.println("Номер вопроса: " + questionResponse.getItems().get(0).getQuestionId());
        });
    }
}
