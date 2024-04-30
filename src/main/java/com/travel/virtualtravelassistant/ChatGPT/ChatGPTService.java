package com.travel.virtualtravelassistant.ChatGPT;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class ChatGPTService {
    private static ChatGPTService instance;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String url;

    private ChatGPTService(Properties properties) {
        this.apiKey = properties.getProperty("openai.api.key");
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            throw new IllegalArgumentException("API key is missing in properties.");
        }

        this.url = properties.getProperty("openai.completions.url");
        if (this.url == null || this.url.isEmpty()) {
            throw new IllegalArgumentException("URL is missing in properties.");
        }

        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public static synchronized ChatGPTService getInstance(Properties properties) {
        if (instance == null) {
            instance = new ChatGPTService(properties);
        }
        return instance;
    }

    public CompletableFuture<String> generateResponse(String prompt) {
        CompletableFuture<String> futureResponse = new CompletableFuture<>();
        String requestBody = String.format("""
            {
              "model": "gpt-3.5-turbo",
              "messages": [{"role": "user", "content": "%s"}],
              "temperature": 0.7,
              "max_tokens": 100
            }
            """, prompt);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(body -> {
                    try {
                        JsonNode rootNode = objectMapper.readTree(body);
                        return rootNode.get("choices").get(0).get("message").get("content").asText();
                    } catch (Exception e) {
                        futureResponse.completeExceptionally(e);
                        return null;
                    }
                })
                .thenAccept(response -> {
                    if (response != null) futureResponse.complete(response);
                })
                .exceptionally(ex -> {
                    futureResponse.completeExceptionally(ex);
                    return null;
                });

        return futureResponse;
    }
}
