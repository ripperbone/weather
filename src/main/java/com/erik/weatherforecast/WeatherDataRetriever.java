package com.erik.weatherforecast;

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

    private final DarkSkyJacksonClient client;
    private final APIKey apiKey;

    public WeatherDataRetriever(APIKey apiKey) {

        this.apiKey = apiKey;
        this.client = new DarkSkyJacksonClient();
    }

    public WeatherDataRetriever(APIKey apiKey, DarkSkyJacksonClient client) {
        this.apiKey = apiKey;
        this.client = client;
    }

    public static GeoCoordinates waukesha() {
        return new GeoCoordinates(new Longitude(-88.2319), new Latitude(43.010));
    }

    public List<HourlyDataPoint> retrieveHourlyForecast(GeoCoordinates location) throws ForecastException, InterruptedException {
        return retrieveForecast(location).getHourly().getData();
    }

    private Forecast retrieveForecast(GeoCoordinates location) throws ForecastException, InterruptedException {

        final int maxAttempts = 5;
        int attemptCounter = 0;

        while (true) {
            try {
                return client.forecast(new ForecastRequestBuilder()
                        .key(apiKey)
                        .location(location)
                        .language(ForecastRequestBuilder.Language.en)
                        .units(ForecastRequestBuilder.Units.si) // Use Units.us for Farenheit, Units.si for Celsius
                        .build());
            } catch (ForecastException ex) {

                if (attemptCounter < maxAttempts) {
                    System.out.println("Exception when retrieving forecast! Details:" + ex);
                    Thread.sleep(10000);
                    attemptCounter++;
                    continue;
                } else {
                    throw ex; // Exit if we can't get the data.
                }
            }
        }
    }
}
