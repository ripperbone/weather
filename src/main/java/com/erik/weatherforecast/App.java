package com.erik.weatherforecast;

import static com.erik.weatherforecast.WeatherForecast.retrieveHourlyForecast;
import static com.erik.weatherforecast.WeatherForecast.waukesha;

import java.util.List;
import java.util.stream.Collectors;

import tk.plogitech.darksky.forecast.ForecastException;
import tk.plogitech.darksky.forecast.model.DataPoint;

public class App {


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

    public static void main(String[] args) throws Exception {

        try {
            List<DataPoint> hourlyForecastData = retrieveHourlyForecast(waukesha()).subList(0, 8);





            System.out.println(String.format("The precipitation potential is %s and the average temperature is %.2f degrees Celsius",
                   precipitationPotentialDescription(hourlyForecastData),
                   averageTemperature(hourlyForecastData)));

            System.out.println("Precipitation");
            System.out.println(getPrecipitationProbabilities(hourlyForecastData));

            System.out.println("Temperature");
            System.out.println(getTemperatures(hourlyForecastData));
        } catch (ForecastException ex) {
            System.out.println("Could not retrieve weather data " + ex);
        }
    }
}
