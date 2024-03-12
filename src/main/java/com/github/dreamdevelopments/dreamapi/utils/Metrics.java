package com.github.dreamdevelopments.dreamapi.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@Getter
public class Metrics {

    private static final String CONTACT = "https://dream-devs.com/discord";

    @Getter
    private static Metrics instance;

    private final JavaPlugin plugin;
    private final boolean shutdownAllowed;
    private final String resourceName;

    private final Platform platform;

    private int keyStatus;

    private String key;

    private final NamespacedKey namespacedKey;


    public Metrics(JavaPlugin plugin, String resourceName, boolean shutdownAllowed) {
        instance = this;

        this.plugin = plugin;
        this.shutdownAllowed = shutdownAllowed;
        this.resourceName = resourceName;

        this.namespacedKey = new NamespacedKey("dream", "key_" + resourceName);

        if(Platform.BUILTBYBIT.platformPlaceholder.equalsIgnoreCase("true"))
            platform = Platform.BUILTBYBIT;
        else if(Platform.POLYMART.platformPlaceholder.equalsIgnoreCase("1"))
            platform = Platform.POLYMART;
        else
            platform = Platform.SPIGOT;

        try {
            initializeKey();
        } catch(Exception e) {
            plugin.getLogger().log(Level.SEVERE, "There was an error while initializing the metrics for this resource.");
            plugin.getLogger().log(Level.SEVERE, "If the problem persists, please contact us: " + CONTACT);
            e.printStackTrace();
        }
    }

    private void saveKey(@NotNull String key) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> Bukkit.getWorlds().forEach(
                world -> world.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, key)
        ));
    }

    private void removeKey() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> Bukkit.getWorlds().forEach(
                world -> world.getPersistentDataContainer().remove(namespacedKey)
        ));
    }

    private void initializeKey() throws IOException, InterruptedException {
        Map<String, String> arguments = platform.getArguments();
        arguments.put("resource", resourceName);
        HttpResponse<String> httpResponse = sendRequest("https://api.dream-devs.com/v2/license/generate", RequestType.POST, arguments);
        keyStatus = httpResponse.statusCode();
        JsonObject response = JsonParser.parseString(httpResponse.body()).getAsJsonObject();
        switch (keyStatus) {
            case HttpURLConnection.HTTP_NOT_FOUND: {
                plugin.getLogger().warning("There was an error while checking the validity of this resource.");
                plugin.getLogger().warning("This can be caused by unauthorized modifications to the plugin.");
                plugin.getLogger().warning("If this is not fixed, the resource might temporarily stop working.");
                plugin.getLogger().warning("Please try to re-download this plugin from the official source.");
                plugin.getLogger().warning("If the problem persists, please contact us: " + CONTACT);
                break;
            }
            case HttpURLConnection.HTTP_OK: {
                key = response.get("key").getAsString();
                plugin.getLogger().info("Metrics initialized successfully.");
                break;
            }
            case HttpURLConnection.HTTP_FORBIDDEN: {
                plugin.getLogger().log(Level.SEVERE, "This instance has been blocked by the resource's developer.");
                plugin.getLogger().log(Level.SEVERE, "Please try to re-download it from the official source.");
                plugin.getLogger().log(Level.SEVERE, "If you think this is a mistake, please contact us: " + CONTACT);
                if(response.get("links") != null) {
                    plugin.getLogger().log(Level.SEVERE, "---");
                    response.get("links").getAsJsonArray().forEach(element -> plugin.getLogger().log(Level.SEVERE, element.getAsString()));
                }
                removeKey();
                shutdown();
                break;
            }
            default: {
                if(response.get("key") != null)
                    key = response.get("key").getAsString();
                else {
                    plugin.getLogger().log(Level.SEVERE, "There was an error while initializing the metrics for this resource.");
                    plugin.getLogger().log(Level.SEVERE, "If the problem persists, please contact us: " + CONTACT);
                }
                break;
            }
        }
        if(key != null) {
            saveKey(key);
        }
    }

    @Nullable
    public String getVerificationCode() {
        if(this.key == null)
            return null;
        Map<String, String> arguments = platform.getArguments();
        arguments.put("key", key);
        try {
            HttpResponse<String> httpResponse = sendRequest("https://api.dream-devs.com/v2/license/get_code", RequestType.POST, arguments);
            JsonObject response = JsonParser.parseString(httpResponse.body()).getAsJsonObject();
            if(httpResponse.statusCode() == HttpURLConnection.HTTP_OK) {
                return response.get("code").getAsString();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void shutdown() {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            if(shutdownAllowed)
                Bukkit.shutdown();
            else
                Bukkit.getPluginManager().disablePlugin(this.plugin);
        }, 10);
    }

    @Getter
    public enum Platform {

        SPIGOT("spigot", "%%__USER__%%","%%__RESOURCE__%%", "%__NONCE__%%", null, null, null),
        BUILTBYBIT("builtbybit", "%%__USER__%%", "%%__RESOURCE__%%", "%%__NONCE__%%", "%%__TIMESTAMP__%%", null, "%%__BUILTBYBIT__%%"),
        POLYMART("polymart", "%%__USER__%%", "%%__RESOURCE__%%", "%%__NONCE__%%", "%%__TIMESTAMP__%%", "%%__LICENSE__%%", "%%__POLYMART__%%");

        @NotNull
        private final String identifier;
        @Nullable
        private final String id;
        @Nullable
        private final String resourceId;
        @Nullable
        private final String signature;
        @Nullable
        private final String time;
        @Nullable
        private final String key;
        private final String platformPlaceholder;

        Platform(
                @NotNull String identifier,
                @Nullable String id, @Nullable String resourceId, @Nullable String signature,
                @Nullable String time, @Nullable String key,
                @Nullable String platformPlaceholder) {
            this.identifier = identifier;
            this.id = id;
            this.resourceId = resourceId;
            this.signature = signature;
            this.time = time;
            this.key = key;
            this.platformPlaceholder = platformPlaceholder;
        }

        public Map<String, String> getArguments() {
            var arguments = new HashMap<String, String>();
            arguments.put("platform", this.getIdentifier());
            arguments.put("resourceId", this.getResourceId());
            arguments.put("user", this.getId());
            arguments.put("nonce", this.getSignature());
            if(this.getTime() != null)
                arguments.put("timestamp", this.getTime());
            if(this.getKey() != null)
                arguments.put("key", this.getKey());
            return arguments;
        }

    }

    public static HttpResponse<String> sendRequest(@NotNull String url, @NotNull RequestType requestType) throws IOException, InterruptedException {
        return sendRequest(url, requestType, null);
    }

    public static HttpResponse<String> sendRequest(@NotNull String url, @NotNull RequestType requestType, @Nullable Map<String, String> arguments) throws IOException, InterruptedException {
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

        final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(8)).build();
        final URI uri = URI.create(url);
        final String header = "application/x-www-form-urlencoded";
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(uri).header("Content-Type", header);
        switch (requestType) {
            case GET:
                requestBuilder.GET();
                break;
            case POST:
                requestBuilder.POST(HttpRequest.BodyPublishers.ofString(requestBody));
                break;
            case PUT:
                requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(requestBody));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + requestType);
        }
        return client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
    }

    public enum RequestType {
        GET,
        POST,
        PUT
    }

}
