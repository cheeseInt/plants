package ch.cheese.plants;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class PlantImporter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String loginUrl = "https://web.fyta.de/api/auth/login";
    private final String plantsListUrl = "https://web.fyta.de/api/user-plant";
    private final String plantMeasurementsBaseUrl = "https://web.fyta.de/api/user-plant/measurements/";

    private String accessToken;

    /**
     * Main method to import plants.
     * @return a Map of PlantEntry (static plant info) to Plant (dynamic measurements).
     */
    public Map<PlantEntry, Plant> importPlants() throws IOException {
        authenticate(); // Step 1: Get Access Token
        List<PlantEntry> plantEntries = fetchPlantEntries(); // Step 2: Get PlantEntry list
        Map<PlantEntry, Plant> plantData = new HashMap<>();

        int count = 1;
        for (PlantEntry plantEntry : plantEntries) {
            log.info("[" + count++ + "/" + plantEntries.size() + "] Importing measurements for plant ID: " + plantEntry.getId() + " (" + plantEntry.getNickname() + ")");
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(plantData));

            Plant plant = fetchPlantMeasurements(plantEntry.getId());
            plantData.put(plantEntry, plant);

        }

        log.info("Finished importing all plants.");
        log.info("Total number of plants: " + plantData.size());

        return plantData;
    }

    /**
     * Authenticate and store the access token.
     */
    private void authenticate() throws IOException {
        URL url = new URL(loginUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String body = """
                {
                  "email": "cheese_int@me.com",
                  "password": "zurha2-hahrIt-dywzeb"
                }
                """;

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes());
        }

        try (InputStream inputStream = conn.getInputStream()) {
            JsonNode node = objectMapper.readTree(inputStream);
            this.accessToken = node.get("access_token").asText();
            log.info("Authentication successful, token received.");
        }
    }

    /**
     * Fetch static plant information.
     * @return list of PlantEntry
     */
    private List<PlantEntry> fetchPlantEntries() throws IOException {
        URL url = new URL(plantsListUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

        List<PlantEntry> entries = new ArrayList<>();
        try (InputStream inputStream = conn.getInputStream()) {
            MyPlants myPlants = objectMapper.readValue(inputStream, MyPlants.class);
            entries.addAll(myPlants.getPlants());
        }
        return entries;
    }

    /**
     * Fetch sensor measurements for a plant ID.
     * @param plantId the plant ID
     * @return Plant with measurement data
     */
    private Plant fetchPlantMeasurements(Integer plantId) throws IOException {
        String urlString = plantMeasurementsBaseUrl + plantId;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        MeasurementSearchRequest requestBody = new MeasurementSearchRequest(
                new MeasurementSearchRequest.Search("week")
        );

        try (OutputStream os = conn.getOutputStream()) {
            objectMapper.writeValue(os, requestBody);
        }

        try (InputStream inputStream = conn.getInputStream()) {
            return objectMapper.readValue(inputStream, Plant.class);
        }
    }
}
