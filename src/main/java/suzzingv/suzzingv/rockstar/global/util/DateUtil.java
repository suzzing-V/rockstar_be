package suzzingv.suzzingv.rockstar.global.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtil {
    public static String toMMDDEHHMM(LocalDateTime startDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd E HH:mm", Locale.ENGLISH);
        return startDate.format(formatter).toUpperCase();
    }

    public static String toMMDDHHMM(LocalDateTime startDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd HH:mm", Locale.ENGLISH);
        return startDate.format(formatter).toUpperCase();
    }

    public static int getDaysAgo(LocalDateTime date) {
        LocalDateTime now = LocalDateTime.now();
        Period period = Period.between(date.toLocalDate(), now.toLocalDate());

        return period.getDays();
    }

    public static long getHoursAgo(LocalDateTime date) {
        LocalDateTime now = LocalDateTime.now();
        Duration timeDiff = Duration.between(date.toLocalTime(), now.toLocalTime());

        return timeDiff.toHours();
    }

    public static long getMinutesAgo(LocalDateTime date) {
        LocalDateTime now = LocalDateTime.now();
        Duration timeDiff = Duration.between(date.toLocalTime(), now.toLocalTime());

        return timeDiff.toMinutes();
    }

    public static long getSecondsAgo(LocalDateTime date) {
        LocalDateTime now = LocalDateTime.now();
        Duration timeDiff = Duration.between(date.toLocalTime(), now.toLocalTime());

        return timeDiff.toSeconds();
    }
}
