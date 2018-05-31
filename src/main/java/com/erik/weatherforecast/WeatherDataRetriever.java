package com.erik.weatherforecast;

import com.erik.weatherforecast.exception.ApiKeyMissingException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import tk.plogitech.darksky.api.jackson.DarkSkyJacksonClient;
import tk.plogitech.darksky.forecast.*;
import tk.plogitech.darksky.forecast.model.DataPoint;
import tk.plogitech.darksky.forecast.model.Forecast;




public class WeatherDataRetriever {

    private static final Class THIS_CLASS = WeatherDataRetriever.class.getClass();

    private static final DarkSkyJacksonClient CLIENT = new DarkSkyJacksonClient();
    private static final String API_KEY_PROPERTY = "apiKey";


    public static GeoCoordinates waukesha() {
        return new GeoCoordinates(new Longitude(-88.2319), new Latitude(43.010));
    }

    public static List<DataPoint> retrieveHourlyForecast(GeoCoordinates location) throws ForecastException {
        return retrieveForecast(location).getHourly().getData();
    }

    public static List<DataPoint> onlyNextTwelveHours(List<DataPoint> dataPoints) {
        return dataPoints.stream().filter(dataPoint ->
                dataPoint.getTime().isBefore(Instant.now().plus(12, ChronoUnit.HOURS))).collect(Collectors.toList());
    }

    private static Forecast retrieveForecast(GeoCoordinates location) throws ForecastException {

        if (System.getProperty(API_KEY_PROPERTY) == null || System.getProperty(API_KEY_PROPERTY).length() < 1) {
            throw new ApiKeyMissingException();
        }

        return CLIENT.forecast(new ForecastRequestBuilder()
                .key(new APIKey(System.getProperty(API_KEY_PROPERTY)))
                .location(location)
                .language(ForecastRequestBuilder.Language.en)
                .units(ForecastRequestBuilder.Units.si) // Use Units.us for Farenheit, Units.si for Celsius
                .build());
    }
}