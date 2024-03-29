package com.erik.weatherforecast;

import static com.erik.weatherforecast.WeatherDataSummarizer.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import tk.plogitech.darksky.forecast.model.HourlyDataPoint;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;


public class WeatherDataSummarizerTest extends WeatherDataTest {

    private static final Instant NOW = Instant.now();


    @Test
    public void whenConvertingCelsiusToFahrenheit_thenExpectedValueIsReturned() {
        assertEquals(32, celsiusToFahrenheit(0), 0);
        assertEquals(69.8, celsiusToFahrenheit(21), 0);
        assertEquals(65.2208, celsiusToFahrenheit(18.456), 0);
        assertEquals(152.6, celsiusToFahrenheit(67), 0);
        assertEquals(23, celsiusToFahrenheit(-5), 0);
        assertEquals(-4, celsiusToFahrenheit(-20), 0);
    }

    @Test
    public void whenWeatherDataContainsChanceOfPrecipitation_thenExpectedMessageIsReturned() {

        List<HourlyDataPoint> dataPoints = Arrays.asList(
                dataPoint(0.1, 60.0, NOW, "Clear"),
                dataPoint(0.2, 40.0, NOW, "Clear"),
                dataPoint(0.3, 70.0, NOW, "Rain"));

        assertEquals("high", precipitationPotentialDescription(dataPoints));

    }

    @Test
    public void whenWeatherDataHasLowChanceOfPrecipitation_thenExpectedMessageIsReturned() {
        List<HourlyDataPoint> dataPoints = Arrays.asList(
                dataPoint(0.01, 60.0, NOW, "Clear"),
                dataPoint(0.01, 40.0, NOW, "Clear"),
                dataPoint(0.02, 70.0, NOW, "Clear"));

        assertEquals("low", precipitationPotentialDescription(dataPoints));

    }

    @Test
    public void whenRetrievingAverageTemperature_thenExpectedValueIsReturned() {
        List<HourlyDataPoint> dataPoints = Arrays.asList(
                dataPoint(0.1, 60.0, NOW, "Clear"),
                dataPoint(0.2, 90.0, NOW, "Clear"),
                dataPoint(0.3, 30.0, NOW, "Rain"));

        assertEquals(60.0, averageTemperature(dataPoints), 0);
    }

    @Test
    public void whenRetrievingSummary_thenExpectedSummaryIsReturned() {


        List<HourlyDataPoint> dataPoints = Arrays.asList(
                dataPoint(0.1, 60.0, NOW, "Clear"),
                dataPoint(0.2, 90.0, NOW, "Clear"),
                dataPoint(0.3, 30.0, NOW, "Rain"));

        List<String> summaries = buildSummary(dataPoints);

        assertEquals(3, summaries.size());

        assertThat(summaries, hasItem(containsString("Clear 60.00°C 0.1")));
        assertThat(summaries, hasItem(containsString("Clear 90.00°C 0.2")));
        assertThat(summaries, hasItem(containsString("Rain 30.00°C 0.3")));
    }

}
