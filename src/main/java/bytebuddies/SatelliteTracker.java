package bytebuddies;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SatelliteTracker {
    private final String apiKey = "TC2NEH-3ZZ2TQ-F6FNC7-57J6";

    public HttpResponse<String> getTracking() {
        HttpRequest apiRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.n2yo.com/rest/v1/satellite/positions/48859/60.3141036/5.3441681/0/1/&apiKey=" + apiKey))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();

        try {
            HttpResponse<String> response = httpClient.send(apiRequest,HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) System.out.println("ERROR");

            return  response;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
