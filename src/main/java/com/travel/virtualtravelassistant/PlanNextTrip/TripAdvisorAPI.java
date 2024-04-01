package com.travel.virtualtravelassistant.PlanNextTrip;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TripAdvisorAPI {

    public static void main(String[] args) {
        try {
            String apiKey = "2175F302BAC646F9991CD7DF16E2ECA6";
            String locationId = "YOUR_LOCATION_ID_HERE";
            String apiUrl = "https://api.tripadvisor.com/data/1.0/location/" + locationId + "/reviews?key=" + apiKey;

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

            System.out.println("Response: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}