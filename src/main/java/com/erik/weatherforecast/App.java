package com.erik.weatherforecast;

import static com.erik.weatherforecast.WeatherDataRetriever.retrieveHourlyForecast;
import static com.erik.weatherforecast.WeatherDataRetriever.waukesha;
import static com.erik.weatherforecast.WeatherDataRetriever.onlyNextTwelveHours;
import static com.erik.weatherforecast.WeatherDataSummarizer.averageTemperature;
import static com.erik.weatherforecast.WeatherDataSummarizer.buildSummary;
import static com.erik.weatherforecast.WeatherDataSummarizer.precipitationPotentialDescription;
;

import java.util.List;

import tk.plogitech.darksky.forecast.model.DataPoint;

public class App {

    public static void main(String[] args) throws Exception {

        List<DataPoint> hourlyForecastData = onlyNextTwelveHours(retrieveHourlyForecast(waukesha()));

        System.out.println(String.format("The precipitation potential is %s and the average temperature is %.2f degrees Celsius",
               precipitationPotentialDescription(hourlyForecastData),
               averageTemperature(hourlyForecastData)));

        buildSummary(hourlyForecastData).forEach(summary -> System.out.println(summary));
    }
}
