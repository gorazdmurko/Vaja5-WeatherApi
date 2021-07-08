package si.academia.uni29.vaja5.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherApiProxy {

    private String url;
    private String apiKey;

    public WeatherApiProxy(String url, String apiKey) {
        this.url = url;
        this.apiKey = apiKey;
    }

    public WeatherData getHistoricalData(String location, Date dtm) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        StringBuilder uriSb = new StringBuilder(this.url);
        uriSb.append("history.json?key=");
        uriSb.append(URLEncoder.encode(this.apiKey, StandardCharsets.UTF_8));
        uriSb.append("&q=");
        uriSb.append(URLEncoder.encode(location, StandardCharsets.UTF_8));
        uriSb.append("&dt=");
        uriSb.append(new SimpleDateFormat("yyyy-MM-dd").format(dtm));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriSb.toString()))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException();
        }
        ObjectMapper objMap = new ObjectMapper();
        WeatherData wdata = objMap.readValue(response.body(), WeatherData.class);
        return wdata;
    }

}
