package com.github.dreamdevelopments.dreamapi.utils;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Metrics {

    public HttpResponse<String> sendRequest(@NotNull String url, @NotNull RequestType requestType) throws IOException, InterruptedException {
        return sendRequest(url, requestType, null);
    }

    public HttpResponse<String> sendRequest(@NotNull String url, @NotNull RequestType requestType, @Nullable HashMap<String, String> arguments) throws IOException, InterruptedException {
        String requestBody = "";
        if(arguments != null && !arguments.isEmpty()) {
            StringBuilder requestBodyBuilder = new StringBuilder();
            for (Map.Entry<String, String> entry : arguments.entrySet()) {
                requestBodyBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
                requestBodyBuilder.append("=");
                requestBodyBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
                requestBodyBuilder.append("&");
            }
            requestBodyBuilder.deleteCharAt(requestBodyBuilder.length() - 1);
            requestBody = requestBodyBuilder.toString();
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public enum RequestType {
        GET,
        POST,
        PUT
    }


}
