package com.greeting.service;

import java.time.LocalTime;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingService {

    private static final String MORNING = "Morning";
    private static final String AFTERNOON = "Afternoon";
    private static final String EVENING = "Evening";

    public String greet(String name) {
        String formatted_name = name.trim().toUpperCase();
        String timeOfDay = gettimeofday();
        return "Hello " + formatted_name + "! Good " + timeOfDay + "!";
    }

    private String gettimeofday() {
        int hour = LocalTime.now().getHour();
        if (hour >= 5 && hour < 12) {
            return MORNING;
        } else if (hour >= 12 && hour < 17) {
            return AFTERNOON;
        } else {
            return EVENING;
        }
    }
}
