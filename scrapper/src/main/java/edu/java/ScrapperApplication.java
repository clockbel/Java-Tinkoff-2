package edu.java;

import edu.java.configuration.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
@EnableScheduling
public class ScrapperApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScrapperApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ScrapperApplication.class, args);
    }
}
