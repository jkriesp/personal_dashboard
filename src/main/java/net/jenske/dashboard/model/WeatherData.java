package net.jenske.dashboard.model;

public class WeatherData {
    private String summary;
    private double temperature;

    public WeatherData(String summary, double temperature) {
        this.summary = summary;
        this.temperature = temperature;
    }

    public String getSummary() {
        return summary;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
