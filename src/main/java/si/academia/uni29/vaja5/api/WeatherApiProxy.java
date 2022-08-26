package si.academia.uni29.vaja5.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
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

    // constructor
    public WeatherApiProxy(String url, String apiKey) {     // za stringBuilder ( zgradi url )
        this.url = url;
        this.apiKey = apiKey;
    }

    // method that returns WeatherData
    public WeatherData getHistoricalData(String location, Date dtm) throws IOException, InterruptedException {

        // 1. client
        HttpClient client = HttpClient.newHttpClient(); // used to send request & retrieve response

        StringBuilder uriSb = new StringBuilder(this.url);                          // proxy URL
        uriSb.append("history.json?key=");
        uriSb.append(URLEncoder.encode(this.apiKey, StandardCharsets.UTF_8));       // proxy API Key
        uriSb.append("&q=");
        uriSb.append(URLEncoder.encode(location, StandardCharsets.UTF_8));
        uriSb.append("&dt=");
        uriSb.append(new SimpleDateFormat("yyyy-MM-dd").format(dtm));

        // 2. request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriSb.toString()))
                .header("accept", "application/json")
                .GET()
                .build();   // bulids together & returns an HttpRequest
        
        // 3. response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());         //  synchronous
        // HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());  // asynchronous
        // System.out.println("RESPONSE BODY: \n");
        // System.out.println(response.body());
        if (response.statusCode() != 200) {
            throw new IOException();
        }
        // mapper
        ObjectMapper mapper = new ObjectMapper();
        WeatherData data = mapper.readValue(response.body(), WeatherData.class);        // DataInput src, Class<T> valueType


        // save into file
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String path = "weatherHistory\\";
        path += "historyData-" + location + ".json";    // relative path
        mapper.writeValue(new File(path), data);

        return data;

    }

    public WeatherDataCurrent getCurrentData(String location) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        StringBuilder uriSb = new StringBuilder(this.url);
        uriSb.append("current.json?key=");
        uriSb.append(URLEncoder.encode(this.apiKey, StandardCharsets.UTF_8));
        uriSb.append("&q=");
        uriSb.append(URLEncoder.encode(location, StandardCharsets.UTF_8));
        uriSb.append("&aqi=yes");
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriSb.toString()))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException();
        }

        ObjectMapper mapper = new ObjectMapper();
        WeatherDataCurrent data = mapper.readValue(response.body(), WeatherDataCurrent.class);     // (DataInput src, Class<T> valueType)

        // save into file
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String path = "weatherCurrent\\";
        path += "currentData-" + location + ".json";    // relative path
        mapper.writeValue(new File(path), data);
        
        return data;
    }

}
