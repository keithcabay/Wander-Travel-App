package com.travel.virtualtravelassistant;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class defaultImages {

    private final String accessKey;

    public defaultImages() {
        accessKey = readAccessKeyFromJsonFile();
    }

    private String readAccessKeyFromJsonFile() {
        try {
            FileInputStream serviceAccountStream = new FileInputStream("src/main/resources/adminSDK.json");
            JsonObject json = JsonParser.parseReader(new InputStreamReader(serviceAccountStream)).getAsJsonObject();
            return json.get("Unsplash_key").getAsString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read access key from JSON file", e);
        }
    }

    private String city = "Tokyo";

    public List<String> fetchImageUrls() {
        List<String> imageUrls = new ArrayList<>();
        try {
            String apiUrl = "https://api.unsplash.com/search/photos?query=" + city + "&client_id=" + accessKey;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JsonElement root = JsonParser.parseString(response.toString());
            if (root.isJsonObject()) {
                JsonObject rootObject = root.getAsJsonObject();
                JsonArray resultsArray = rootObject.getAsJsonArray("results");
                if (resultsArray != null) {
                    for (JsonElement result : resultsArray) {
                        if (result.isJsonObject()) {
                            JsonObject resultObject = result.getAsJsonObject();
                            JsonElement urls = resultObject.get("urls");
                            if (urls != null && urls.isJsonObject()) {
                                JsonObject urlsObject = urls.getAsJsonObject();
                                JsonElement regularUrl = urlsObject.get("regular");
                                if (regularUrl != null && regularUrl.isJsonPrimitive()) {
                                    String imageUrl = regularUrl.getAsString();
                                    imageUrls.add(imageUrl);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageUrls;
    }
}
