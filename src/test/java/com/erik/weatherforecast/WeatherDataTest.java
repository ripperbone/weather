package com.erik.weatherforecast;

import tk.plogitech.darksky.forecast.GeoCoordinates;
import tk.plogitech.darksky.forecast.model.Forecast;
import tk.plogitech.darksky.forecast.model.Hourly;
import tk.plogitech.darksky.forecast.model.HourlyDataPoint;
import tk.plogitech.darksky.forecast.model.Latitude;
import tk.plogitech.darksky.forecast.model.Longitude;

import java.time.Instant;
import java.util.List;

abstract class WeatherDataTest {


    static HourlyDataPoint dataPoint(Double precipitationProbability, Double temperature, Instant time, String summary) {
        HourlyDataPoint dataPoint = new HourlyDataPoint();
        dataPoint.setPrecipProbability(precipitationProbability);
        dataPoint.setTemperature(temperature);
        dataPoint.setTime(time);
        dataPoint.setSummary(summary);
        return dataPoint;
    }

    static GeoCoordinates geoCoordinatesForTest() {
        return new GeoCoordinates(new Longitude(2.294385), new Latitude(48.858533));
    }

    static Forecast forecastData(List<HourlyDataPoint> dataPoints) {
        Hourly hourly = new Hourly();
        hourly.setData(dataPoints);
        Forecast forecast = new Forecast();
        forecast.setHourly(hourly);
        return forecast;
    }
}
