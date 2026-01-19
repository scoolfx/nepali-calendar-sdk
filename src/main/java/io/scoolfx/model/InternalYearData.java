package io.scoolfx.model;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * Internal representation of a Nepali Year's configuration.
 * Used for JSON deserialization and lookup logic.
 */
public record InternalYearData(
        int year,               // BS Year (e.g., 2082)
        LocalDate startDate,    // The AD date when Baisakh 1 starts
        int[] monthLengths      // Array of 12 integers representing days in each month
) {
    /**
     * Calculates the AD end date of this Nepali year.
     */
    public LocalDate endDate() {
        int totalDays = Arrays.stream(monthLengths).sum();
        return startDate.plusDays(totalDays - 1);
    }

    /**
     * Helper to get the length of a specific month (1-indexed).
     */
    public int getMonthLength(int month) {
        return monthLengths[month - 1];
    }
}