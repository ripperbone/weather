package com.erik.weatherforecast;

import tk.plogitech.darksky.forecast.model.HourlyDataPoint;

import java.time.Instant;

abstract class WeatherDataTest {


    static HourlyDataPoint dataPoint(Double precipitationProbability, Double temperature, Instant time, String summary) {
        HourlyDataPoint dataPoint = new HourlyDataPoint();
        dataPoint.setPrecipProbability(precipitationProbability);
        dataPoint.setTemperature(temperature);
        dataPoint.setTime(time);
        dataPoint.setSummary(summary);
        return dataPoint;
    }
}
