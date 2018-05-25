package com.erik.weatherforecast;

import static com.erik.weatherforecast.WeatherForecast.retrieveHourlyForecast;
import static com.erik.weatherforecast.WeatherForecast.waukesha;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import tk.plogitech.darksky.forecast.ForecastException;
import tk.plogitech.darksky.forecast.model.DataPoint;

public class App {

    private static final ZoneId TIME_ZONE_US_CENTRAL = ZoneId.of("US/Central");


    private static boolean precipitationPotentialHigh(List<DataPoint> dataPoints) {
        return dataPoints.stream().anyMatch(dataPoint -> dataPoint.getPrecipProbability() > 0.5);
    }

    private static String precipitationPotentialDescription(List<DataPoint> dataPoints) {
        return precipitationPotentialHigh(dataPoints) ? "high" : "low";
    }

    private static double averageTemperature(List<DataPoint> dataPoints) {
        return getTemperatures(dataPoints).stream().mapToDouble(Double::doubleValue).average().orElse(0);
        // another option would be getApparentTemperature instead of getTemperature
    }

    private static List<Double> getTemperatures(List<DataPoint> dataPoints) {
        return dataPoints.stream().map(DataPoint::getTemperature).collect(Collectors.toList());
    }

    private static List<Double> getPrecipitationProbabilities(List<DataPoint> dataPoints) {
        return dataPoints.stream().map(DataPoint::getPrecipProbability).collect(Collectors.toList());
    }

    private static List<String> buildSummary(List<DataPoint> dataPoints) {
        return dataPoints.stream().map(dataPoint -> String.format("%s: %s %.2f°C %.2f",
                formatDateTime(dataPoint.getTime()), dataPoint.getSummary(), dataPoint.getTemperature(), dataPoint.getPrecipProbability())).collect(Collectors.toList());
    }

    private static String formatDateTime(Instant instant) {
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.US)
                .withZone(TIME_ZONE_US_CENTRAL).format(instant);
    }

    public static void main(String[] args) throws Exception {

        try {
            List<DataPoint> hourlyForecastData = retrieveHourlyForecast(waukesha()).stream().filter(dataPoint ->
                    dataPoint.getTime().isBefore(Instant.now().plus(8, ChronoUnit.HOURS))).collect(Collectors.toList());

            System.out.println(String.format("The precipitation potential is %s and the average temperature is %.2f degrees Celsius",
                   precipitationPotentialDescription(hourlyForecastData),
                   averageTemperature(hourlyForecastData)));

            buildSummary(hourlyForecastData).forEach(summary -> System.out.println(summary));

        } catch (ForecastException ex) {
            System.out.println("Could not retrieve weather data " + ex);
        }
    }
}
