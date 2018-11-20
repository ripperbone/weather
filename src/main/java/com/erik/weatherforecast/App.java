package com.erik.weatherforecast;

import static com.erik.weatherforecast.WeatherDataRetriever.retrieveHourlyForecast;
import static com.erik.weatherforecast.WeatherDataRetriever.waukesha;
import static com.erik.weatherforecast.WeatherDataSummarizer.averageTemperature;
import static com.erik.weatherforecast.WeatherDataSummarizer.buildSummary;
import static com.erik.weatherforecast.WeatherDataSummarizer.precipitationPotentialDescription;
;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import tk.plogitech.darksky.forecast.model.HourlyDataPoint;

public class App {

    public static void main(String[] args) throws Exception {

        // Only show data for next 12 hours.
        List<HourlyDataPoint> hourlyForecastData = retrieveHourlyForecast(waukesha())
                .stream().filter(dataPoint -> dataPoint.getTime().isBefore(Instant.now().plus(12, ChronoUnit.HOURS)))
                .collect(Collectors.toList());


        System.out.println(String.format("The precipitation potential is %s and the average temperature is %.2f degrees Celsius",
               precipitationPotentialDescription(hourlyForecastData),
               averageTemperature(hourlyForecastData)));

        buildSummary(hourlyForecastData).forEach(summary -> System.out.println(summary));
    }
}
