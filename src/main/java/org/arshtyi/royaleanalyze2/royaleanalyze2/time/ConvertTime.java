package org.arshtyi.royaleanalyze2.royaleanalyze2.time;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class ConvertTime {
    private ConvertTime() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static int calculateTimeDifference(String time) {
        try {
            // Try direct parsing first
            Instant timestamp = Instant.parse(time);
            Instant now = Instant.now();

            // Calculate difference in seconds
            return (int) now.until(timestamp, ChronoUnit.SECONDS);
        } catch (Exception e) {
            // If direct parsing fails, use a custom formatter
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss.SSSX");
            ZonedDateTime zdt = ZonedDateTime.parse(time, formatter);
            Instant timestamp = zdt.toInstant();
            Instant now = Instant.now();

            // Calculate difference in seconds
            return (int) now.until(timestamp, ChronoUnit.SECONDS);
        }
    }

    public static String convertTime(int time) {
        // Calculate each time unit
        int weeks = time / (7 * 24 * 60 * 60);
        time %= (7 * 24 * 60 * 60);

        int days = time / (24 * 60 * 60);
        time %= (24 * 60 * 60);

        int hours = time / (60 * 60);
        time %= (60 * 60);

        int minutes = time / 60;
        int seconds = time % 60;

        // Build the result string
        StringBuilder result = new StringBuilder();

        if (weeks > 0) {
            result.append(weeks).append("w");
        }

        if (days > 0) {
            result.append(days).append("d");
        }

        if (hours > 0) {
            result.append(hours).append("h");
        }

        if (minutes > 0) {
            result.append(minutes).append("m");
        }

        if (seconds > 0 || result.length() == 0) {
            result.append(seconds).append("s");
        }
        return result.toString();
    }
}
