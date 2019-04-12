package com.erik.weatherforecast.persistence;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.time.Instant;

public class RedisWeatherPersistence implements IWeatherPersistence {

    private static final String KEY_TEMPERATURE = "weather:temperature";
    private static final String KEY_PRECIPITATION = "weather:precipitation";
    private static final String KEY_DATE = "weather:date";

    public RedisWeatherPersistence() {
    }

    @Override
    public void setWeatherData(Double averageTemperature, Boolean highPrecipitationPotential, Instant date) {
        RedisClient redisClient = RedisClient.create("redis://localhost");
        StatefulRedisConnection<String, String> redisConnection = redisClient.connect();

        RedisCommands<String, String> redisCommands = redisConnection.sync();
        redisCommands.set(KEY_TEMPERATURE, averageTemperature.toString());
        redisCommands.set(KEY_PRECIPITATION, highPrecipitationPotential.toString());
        redisCommands.set(KEY_DATE, date.toString());

        // clean up
        redisConnection.close();
        redisClient.shutdown();
    }
}
