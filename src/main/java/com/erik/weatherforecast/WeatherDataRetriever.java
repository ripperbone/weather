package com.erik.weatherforecast;

import com.erik.weatherforecast.exception.ApiKeyMissingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import tk.plogitech.darksky.api.jackson.DarkSkyJacksonClient;
import tk.plogitech.darksky.forecast.APIKey;
import tk.plogitech.darksky.forecast.ForecastException;
import tk.plogitech.darksky.forecast.ForecastRequestBuilder;
import tk.plogitech.darksky.forecast.GeoCoordinates;
import tk.plogitech.darksky.forecast.model.Forecast;
import tk.plogitech.darksky.forecast.model.HourlyDataPoint;
import tk.plogitech.darksky.forecast.model.Latitude;
import tk.plogitech.darksky.forecast.model.Longitude;


public class WeatherDataRetriever {

    private static final Class THIS_CLASS = WeatherDataRetriever.class.getClass();

    private static final DarkSkyJacksonClient CLIENT = new DarkSkyJacksonClient();
    private static final String API_KEY_FILE = "/api_key.txt";


    public static GeoCoordinates waukesha() {
        return new GeoCoordinates(new Longitude(-88.2319), new Latitude(43.010));
    }

    public static List<HourlyDataPoint> retrieveHourlyForecast(GeoCoordinates location) throws ForecastException {
        return retrieveForecast(location).getHourly().getData();
    }

    private static Forecast retrieveForecast(GeoCoordinates location) throws ForecastException {


        InputStream apiKeyInputStream = THIS_CLASS.getResourceAsStream(API_KEY_FILE);

        if (apiKeyInputStream == null) {
            throw new ApiKeyMissingException("API key file is missing.");
        }

        String apiKey;
        try {
            apiKey = (new BufferedReader(new InputStreamReader(apiKeyInputStream))).readLine();
        } catch (IOException ex) {
            throw new ApiKeyMissingException(ex);
        }

        return CLIENT.forecast(new ForecastRequestBuilder()
                .key(new APIKey(apiKey))
                .location(location)
                .language(ForecastRequestBuilder.Language.en)
                .units(ForecastRequestBuilder.Units.si) // Use Units.us for Farenheit, Units.si for Celsius
                .build());
    }
}
