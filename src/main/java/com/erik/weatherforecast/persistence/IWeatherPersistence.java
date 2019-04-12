package com.erik.weatherforecast.persistence;

import java.time.Instant;

public interface IWeatherPersistence {


    void setWeatherData(Double averageTemperature, Boolean highPrecipitationPotential, Instant date);
}
