package com.erik.weatherforecast;

import tk.plogitech.darksky.forecast.model.DataPoint;

import java.time.Instant;

abstract class WeatherDataTest {


    static DataPoint dataPoint(Double precipitationProbability, Double temperature, Instant time, String summary) {
        DataPoint dataPoint = new DataPoint();
        dataPoint.setPrecipProbability(precipitationProbability);
        dataPoint.setTemperature(temperature);
        dataPoint.setTime(time);
        dataPoint.setSummary(summary);
        return dataPoint;
    }
}
