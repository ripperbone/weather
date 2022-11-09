package com.erik.weatherforecast;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import tk.plogitech.darksky.api.jackson.DarkSkyJacksonClient;
import tk.plogitech.darksky.forecast.APIKey;
import tk.plogitech.darksky.forecast.ForecastException;
import tk.plogitech.darksky.forecast.model.HourlyDataPoint;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class WeatherDataRetrieverTest extends WeatherDataTest {


    @Mock
    DarkSkyJacksonClient darkSkyJacksonClient;

    WeatherDataRetriever weatherDataRetriever;

    private static final Instant NOW = Instant.now();

    @Before
    public void setUp() {
        weatherDataRetriever = new WeatherDataRetriever(new APIKey("FAKE_API_KEY"), darkSkyJacksonClient);
    }

    @Test
    public void whenDataRetrieved_thenResultsContainExpectedData() throws Exception {


        List<HourlyDataPoint> expectedDataPoints = Arrays.asList(
                dataPoint(0.5, 20.0, NOW, "test1"),
                dataPoint(0.6, 21.0, NOW, "test2"));




        when(darkSkyJacksonClient.forecast(any())).thenReturn(forecastData(expectedDataPoints));

        List<HourlyDataPoint> results = weatherDataRetriever.retrieveHourlyForecast(geoCoordinatesForTest());

        assertThat(results, hasSize(2));
        assertThat(results, hasItem(expectedDataPoints.get(0)));
        assertThat(results, hasItem(expectedDataPoints.get(1)));


    }

    @Test
    public void whenDataUnableToBeRetrieved_thenRetryUntilSuccess() throws Exception {


        List<HourlyDataPoint> expectedDataPoints = Arrays.asList(
                dataPoint(0.5, 20.0, NOW, "test1"),
                dataPoint(0.6, 21.0, NOW, "test2"));


        when(darkSkyJacksonClient.forecast(any()))
                .thenThrow(ForecastException.class).thenThrow(ForecastException.class).thenReturn(forecastData(expectedDataPoints));

        List<HourlyDataPoint> results = weatherDataRetriever.retrieveHourlyForecast(geoCoordinatesForTest());

        verify(darkSkyJacksonClient, times(3)).forecast(any());
        assertThat(results, hasSize(2));

    }
}
