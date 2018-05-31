package com.erik.weatherforecast;

import tk.plogitech.darksky.forecast.model.DataPoint;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class WeatherDataSummarizer {

    private static final ZoneId TIME_ZONE_US_CENTRAL = ZoneId.of("US/Central");


    private static boolean precipitationPotentialHigh(List<DataPoint> dataPoints) {
        return dataPoints.stream().anyMatch(dataPoint -> dataPoint.getPrecipProbability() >= 0.3);
    }

    public static String precipitationPotentialDescription(List<DataPoint> dataPoints) {
        return precipitationPotentialHigh(dataPoints) ? "high" : "low";
    }

    public static double averageTemperature(List<DataPoint> dataPoints) {
        return getTemperatures(dataPoints).stream().mapToDouble(Double::doubleValue).average().orElse(0);
        // another option would be getApparentTemperature instead of getTemperature
    }

    private static List<Double> getTemperatures(List<DataPoint> dataPoints) {
        return dataPoints.stream().map(DataPoint::getTemperature).collect(Collectors.toList());
    }

    private static List<Double> getPrecipitationProbabilities(List<DataPoint> dataPoints) {
        return dataPoints.stream().map(DataPoint::getPrecipProbability).collect(Collectors.toList());
    }

    public static List<String> buildSummary(List<DataPoint> dataPoints) {
        return dataPoints.stream().map(dataPoint -> String.format("%s: %s %.2f°C %.2f",
                formatDateTime(dataPoint.getTime()), dataPoint.getSummary(), dataPoint.getTemperature(), dataPoint.getPrecipProbability()))
                .collect(Collectors.toList());
    }

    public static String formatDateTime(Instant instant) {
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.US)
                .withZone(TIME_ZONE_US_CENTRAL).format(instant);
    }
}
