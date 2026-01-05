package com.harel.automation.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for date operations and formatting
 */
public class DateUtils {
    
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
    
    /**
     * Format date to dd/MM/yyyy format
     */
    public static String formatDate(LocalDate date) {
        return date.format(DEFAULT_FORMATTER);
    }
    
    /**
     * Format date for display purposes
     */
    public static String formatDateForDisplay(LocalDate date) {
        return date.format(DISPLAY_FORMATTER);
    }
    
    /**
     * Calculate number of days between two dates (inclusive)
     */
    public static int calculateDaysBetween(LocalDate startDate, LocalDate endDate) {
        return (int) java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
    
    /**
     * Get date X days from today
     */
    public static LocalDate getDaysFromToday(int days) {
        return LocalDate.now().plusDays(days);
    }
    
    /**
     * Get date X days from a specific date
     */
    public static LocalDate getDaysFromDate(LocalDate date, int days) {
        return date.plusDays(days);
    }
}

