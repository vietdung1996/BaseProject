package com.example.core.util

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

/**
 * Utility class for handling date and time conversions
 * Provides compatibility for devices running below API 26
 */
object TimeUtils {
    // Common date format patterns
    const val PATTERN_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val PATTERN_ISO_8601_NO_MS = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    const val PATTERN_DATE_ONLY = "yyyy-MM-dd"
    const val PATTERN_TIME_ONLY = "HH:mm:ss"
    const val PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss"
    const val PATTERN_DATE_TIME_12H = "yyyy-MM-dd hh:mm:ss a"
    const val PATTERN_DISPLAY_DATE = "dd MMM yyyy"
    const val PATTERN_DISPLAY_DATE_TIME = "dd MMM yyyy, HH:mm"
    const val PATTERN_MONTH_YEAR = "MMM yyyy"
    
    private val UTC = TimeZone.getTimeZone("UTC")
    private val DEFAULT_LOCALE = Locale.US
    
    /**
     * Creates a ThreadLocal SimpleDateFormat with the given pattern
     */
    @SuppressLint("SimpleDateFormat")
    private fun getFormatter(pattern: String, timeZone: TimeZone = UTC): SimpleDateFormat {
        return SimpleDateFormat(pattern, DEFAULT_LOCALE).apply {
            this.timeZone = timeZone
        }
    }
    
    /**
     * Converts a String to Date using the specified pattern
     * @param dateString The date string to parse
     * @param pattern The date format pattern
     * @param timeZone Optional timezone, defaults to UTC
     * @return The parsed Date or null if parsing fails
     */
    fun stringToDate(dateString: String?, pattern: String, timeZone: TimeZone = UTC): Date? {
        if (dateString.isNullOrEmpty()) return null
        
        return try {
            getFormatter(pattern, timeZone).parse(dateString)
        } catch (e: ParseException) {
            null
        }
    }
    
    /**
     * Converts a Date to String using the specified pattern
     * @param date The date to format
     * @param pattern The date format pattern
     * @param timeZone Optional timezone, defaults to UTC
     * @return The formatted date string or empty string if date is null
     */
    fun dateToString(date: Date?, pattern: String, timeZone: TimeZone = UTC): String {
        if (date == null) return ""
        
        return getFormatter(pattern, timeZone).format(date)
    }
    
    /**
     * Converts a timestamp (milliseconds since epoch) to a formatted date string
     * @param timestamp Milliseconds since epoch
     * @param pattern The date format pattern
     * @param timeZone Optional timezone, defaults to UTC
     * @return The formatted date string
     */
    fun timestampToString(timestamp: Long, pattern: String, timeZone: TimeZone = UTC): String {
        val date = Date(timestamp)
        return dateToString(date, pattern, timeZone)
    }
    
    /**
     * Reformats a date string from one pattern to another
     * @param dateString The date string to convert
     * @param fromPattern The source date format pattern
     * @param toPattern The target date format pattern
     * @param fromTimeZone Source timezone
     * @param toTimeZone Target timezone
     * @return The reformatted date string or empty string if conversion fails
     */
    fun reformatDate(
        dateString: String?,
        fromPattern: String,
        toPattern: String,
        fromTimeZone: TimeZone = UTC,
        toTimeZone: TimeZone = UTC
    ): String {
        if (dateString.isNullOrEmpty()) return ""
        
        val date = stringToDate(dateString, fromPattern, fromTimeZone) ?: return ""
        return dateToString(date, toPattern, toTimeZone)
    }
    
    /**
     * Gets the current date and time as a Date object
     * @param timeZone Optional timezone, defaults to UTC
     * @return The current Date
     */
    fun getCurrentDate(timeZone: TimeZone = UTC): Date {
        return Calendar.getInstance(timeZone).time
    }
    
    /**
     * Gets the current date and time as a formatted string
     * @param pattern The date format pattern
     * @param timeZone Optional timezone, defaults to UTC
     * @return The formatted current date string
     */
    fun getCurrentDateString(pattern: String, timeZone: TimeZone = UTC): String {
        return dateToString(getCurrentDate(timeZone), pattern, timeZone)
    }
    
    /**
     * Adds a specified amount of time to a date
     * @param date The base date
     * @param amount The amount to add
     * @param unit The calendar field (Calendar.YEAR, Calendar.MONTH, etc.)
     * @return The new date after addition
     */
    fun addTime(date: Date, amount: Int, unit: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(unit, amount)
        return calendar.time
    }
    
    /**
     * Gets the difference between two dates in the specified time unit
     * @param startDate The earlier date
     * @param endDate The later date
     * @param timeUnit The time unit for the result (e.g., TimeUnit.DAYS)
     * @return The difference in the specified unit, or 0 if either date is null
     */
    fun getDateDiff(startDate: Date?, endDate: Date?, timeUnit: TimeUnit): Long {
        if (startDate == null || endDate == null) return 0
        
        val diffInMillis = endDate.time - startDate.time
        return timeUnit.convert(diffInMillis, TimeUnit.MILLISECONDS)
    }
    
    /**
     * Checks if a date is today
     * @param date The date to check
     * @param timeZone Optional timezone, defaults to UTC
     * @return True if the date is today, false otherwise
     */
    fun isToday(date: Date?, timeZone: TimeZone = UTC): Boolean {
        if (date == null) return false
        
        val today = Calendar.getInstance(timeZone)
        val calendar = Calendar.getInstance(timeZone)
        calendar.time = date
        
        return (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR))
    }
    
    /**
     * Gets a relative time string (e.g., "2 hours ago", "Yesterday", etc.)
     * @param date The date to convert
     * @param timeZone Optional timezone, defaults to UTC
     * @return A human-readable relative time string
     */
    fun getRelativeTimeString(date: Date?, timeZone: TimeZone = UTC): String {
        if (date == null) return ""
        
        val now = Calendar.getInstance(timeZone).timeInMillis
        val time = date.time
        val diff = now - time
        
        return when {
            diff < TimeUnit.MINUTES.toMillis(1) -> "Just now"
            diff < TimeUnit.HOURS.toMillis(1) -> {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
                "$minutes minute${if (minutes > 1) "s" else ""} ago"
            }
            diff < TimeUnit.DAYS.toMillis(1) -> {
                val hours = TimeUnit.MILLISECONDS.toHours(diff)
                "$hours hour${if (hours > 1) "s" else ""} ago"
            }
            diff < TimeUnit.DAYS.toMillis(2) -> "Yesterday"
            diff < TimeUnit.DAYS.toMillis(7) -> {
                val days = TimeUnit.MILLISECONDS.toDays(diff)
                "$days day${if (days > 1) "s" else ""} ago"
            }
            else -> dateToString(date, PATTERN_DISPLAY_DATE, timeZone)
        }
    }
    
    /**
     * Converts a date to a different timezone
     * @param date The date to convert
     * @param fromTimeZone Source timezone
     * @param toTimeZone Target timezone
     * @return The converted date
     */
    fun convertTimeZone(date: Date, fromTimeZone: TimeZone, toTimeZone: TimeZone): Date {
        val sourceCalendar = Calendar.getInstance(fromTimeZone)
        sourceCalendar.time = date
        
        val targetCalendar = Calendar.getInstance(toTimeZone)
        targetCalendar.set(
            sourceCalendar.get(Calendar.YEAR),
            sourceCalendar.get(Calendar.MONTH),
            sourceCalendar.get(Calendar.DAY_OF_MONTH),
            sourceCalendar.get(Calendar.HOUR_OF_DAY),
            sourceCalendar.get(Calendar.MINUTE),
            sourceCalendar.get(Calendar.SECOND)
        )
        targetCalendar.set(Calendar.MILLISECOND, sourceCalendar.get(Calendar.MILLISECOND))
        
        return targetCalendar.time
    }
    
    /**
     * Extracts components from a date (year, month, day, etc.)
     * @param date The date to extract from
     * @param field The calendar field to extract (Calendar.YEAR, Calendar.MONTH, etc.)
     * @return The extracted value
     */
    fun getDateComponent(date: Date, field: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(field)
    }
} 