package net.jenske.dashboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.cache.annotation.Cacheable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class WeatherService {

    @Autowired
    private CacheManager cacheManager;

    @Value("${weather.api.baseurl}")
    private String weatherApiUrl;

    @Cacheable(value = "weatherCache", key = "'WeatherData-Trondheim'")
    public String getWeatherForTrondheim() {
        String expires = null;
        String lastModified = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("user-agent", "MyWeatherApp/1.0 jenskristian@jenske.net");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.getForEntity(weatherApiUrl, String.class, entity);
            HttpHeaders responseHeaders = response.getHeaders();

            expires = responseHeaders.getFirst("Expires");
            lastModified = responseHeaders.getFirst("Last-Modified");

            // Evict cache based on Expires header
            if (expires != null) {
                try {
                    // Assuming the Expires header is in HTTP-date format
                    SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
                    Date expiresAt = formatter.parse(expires);
                    evictCache("WeatherData-Trondheim", expiresAt);
                } catch (ParseException e) {
                    System.out.println("Error parsing Expires header: " + e.getMessage());
                }
            }

            return response.getBody();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return "Error fetching weather data";
        }
    }

    private void evictCache(String key, Date expiresAt) {
        // Convert expiration time to a delay in milliseconds
        long delay = expiresAt.getTime() - System.currentTimeMillis();
        Executors.newScheduledThreadPool(1).schedule(() ->
                        cacheManager.getCache("weatherCache").evict(key),
                delay, TimeUnit.MILLISECONDS);
    }
}
