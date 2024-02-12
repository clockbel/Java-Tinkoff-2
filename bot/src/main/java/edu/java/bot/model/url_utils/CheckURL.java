package edu.java.bot.model.url_utils;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CheckURL {
    private CheckURL() {
    }

    private static final String CORRECT_INPUT = "OK";
    private static final String INCORRECT_INPUT = "BAD URL";
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckURL.class);

    public static String checkURL(String inputURL) {
        String status = INCORRECT_INPUT;
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(inputURL))
                .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == HttpURLConnection.HTTP_OK) {
                status = CORRECT_INPUT;
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return status;
    }
}
