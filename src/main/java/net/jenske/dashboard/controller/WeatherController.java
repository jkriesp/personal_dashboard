package net.jenske.dashboard.controller;

import net.jenske.dashboard.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/weather/trondheim")
    public String getWeatherForTrondheim() {
        return weatherService.getWeatherForTrondheim();
    }
}
