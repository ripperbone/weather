package com.erik.weatherforecast;

import tk.plogitech.darksky.forecast.model.DataPoint;
import tk.plogitech.darksky.forecast.model.HourlyDataPoint;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class WeatherDataSummarizer {

    private static final ZoneId TIME_ZONE_US_CENTRAL = ZoneId.of("US/Central");

    public static double celsiusToFahrenheit(double celsius) {
        return ((celsius * 9 / 5) + 32);
    }

    public static boolean precipitationPotentialHigh(List<HourlyDataPoint> dataPoints) {
        return dataPoints.stream().anyMatch(dataPoint -> dataPoint.getPrecipProbability() >= 0.1);
    }

    public static String precipitationPotentialDescription(List<HourlyDataPoint> dataPoints) {
        return precipitationPotentialHigh(dataPoints) ? "high" : "low";
    }

    public static double averageTemperature(List<HourlyDataPoint> dataPoints) {
        return getTemperatures(dataPoints).stream().mapToDouble(Double::doubleValue).average().orElse(0);
        // another option would be getApparentTemperature instead of getTemperature
    }

    private static List<Double> getTemperatures(List<HourlyDataPoint> dataPoints) {
        return dataPoints.stream().map(DataPoint::getTemperature).collect(Collectors.toList());
    }

    private static List<Double> getPrecipitationProbabilities(List<HourlyDataPoint> dataPoints) {
        return dataPoints.stream().map(DataPoint::getPrecipProbability).collect(Collectors.toList());
    }

    public static List<String> buildSummary(List<HourlyDataPoint> dataPoints) {
        return dataPoints.stream().map(dataPoint -> String.format("%s: %s %.2f°C %.2f",
                formatDateTime(dataPoint.getTime()), dataPoint.getSummary(), dataPoint.getTemperature(), dataPoint.getPrecipProbability()))
                .collect(Collectors.toList());
    }

    public static String formatDateTime(Instant instant) {
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.US)
                .withZone(TIME_ZONE_US_CENTRAL).format(instant);
    }
}
