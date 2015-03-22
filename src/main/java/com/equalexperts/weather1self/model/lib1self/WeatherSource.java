package com.equalexperts.weather1self.model.lib1self;

public enum WeatherSource {
    OWM("openweathermap.org"), WU("wunderground.com");

    private String weatherSourceWebsite;

    private WeatherSource(String weatherSourceWebsite) {
        this.weatherSourceWebsite = weatherSourceWebsite;
    }

    public String getWeatherSourceWebsite() {
        return weatherSourceWebsite;
    }
}
