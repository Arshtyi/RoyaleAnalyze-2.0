package org.arshtyi.royaleanalyze2.royaleanalyze2.time;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class provides a method to get the current time in a specific format.
 * It uses the LocalDateTime and DateTimeFormatter classes from the java.time
 * package.
 * It is designed to be used as a utility class, so it cannot be instantiated.
 * It is also designed to be thread-safe.
 * It is part of the RoyaleAnalyze2 application.
 */
public class GetTime {
    /**
     * This method returns the current time formatted as "M/d H:m:s".
     * It uses the LocalDateTime and DateTimeFormatter classes from the java.time
     * package.
     * 
     * @param void
     * 
     * @return The current time formatted as "M/d H:m:s".
     */
    public static String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d H:m:s");
        return now.format(formatter);
    }
}
