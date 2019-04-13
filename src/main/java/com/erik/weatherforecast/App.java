package com.erik.weatherforecast;

import static com.erik.weatherforecast.WeatherDataRetriever.waukesha;
import static com.erik.weatherforecast.WeatherDataSummarizer.averageTemperature;
import static com.erik.weatherforecast.WeatherDataSummarizer.buildSummary;
import static com.erik.weatherforecast.WeatherDataSummarizer.precipitationPotentialDescription;
import static com.erik.weatherforecast.WeatherDataSummarizer.precipitationPotentialHigh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import com.erik.weatherforecast.exception.ApiKeyMissingException;
import com.erik.weatherforecast.persistence.RedisWeatherPersistence;
import tk.plogitech.darksky.forecast.APIKey;
import tk.plogitech.darksky.forecast.model.HourlyDataPoint;

public class App {

    private static final Class THIS_CLASS = WeatherDataRetriever.class.getClass();
    private static final String API_KEY_FILE = "/api_key.txt";

    public static void main(String[] args) throws Exception {

        // Only show data for next 12 hours.
        List<HourlyDataPoint> hourlyForecastData = (new WeatherDataRetriever(getApiKey())).retrieveHourlyForecast(waukesha())
                .stream().filter(dataPoint -> dataPoint.getTime().isBefore(Instant.now().plus(12, ChronoUnit.HOURS)))
                .collect(Collectors.toList());

        (new RedisWeatherPersistence())
                .setWeatherData(averageTemperature(hourlyForecastData), precipitationPotentialHigh(hourlyForecastData), Instant.now());

        System.out.println(String.format("The precipitation potential is %s and the average temperature is %.2f degrees Celsius",
               precipitationPotentialDescription(hourlyForecastData),
               averageTemperature(hourlyForecastData)));

        buildSummary(hourlyForecastData).forEach(summary -> System.out.println(summary));
    }

    private static APIKey getApiKey() {
        InputStream apiKeyInputStream = THIS_CLASS.getResourceAsStream(API_KEY_FILE);

        if (apiKeyInputStream == null) {
            throw new ApiKeyMissingException("API key file is missing.");
        }

        try {
            return new APIKey((new BufferedReader(new InputStreamReader(apiKeyInputStream))).readLine());
        } catch (IOException ex) {
            throw new ApiKeyMissingException(ex);
        }
    }
}
