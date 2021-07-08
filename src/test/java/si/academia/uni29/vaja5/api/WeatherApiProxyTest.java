package si.academia.uni29.vaja5.api;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static org.junit.Assert.*;

public class WeatherApiProxyTest {

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void testGetHistoricalData() throws IOException, InterruptedException {
        Date xDaysAgo = Date.from( Instant.now().minus(Duration.ofDays(1)) );
        WeatherApiProxy proxy = new WeatherApiProxy("http://api.weatherapi.com/v1/", "3cc0e117d583487d94a84732210807");
        WeatherData wdata = proxy.getHistoricalData("Maribor", xDaysAgo);
        assertNotNull(wdata);

        System.out.println("\nGORAZD MURKO");
        System.out.println(wdata.getForecast().getForecastday().getClass());
        System.out.println(wdata.getLocation().getName());
    }
}