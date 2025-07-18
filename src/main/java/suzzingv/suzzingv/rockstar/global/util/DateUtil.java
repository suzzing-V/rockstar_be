package suzzingv.suzzingv.rockstar.global.util;

import java.time.LocalDateTime;
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
}
