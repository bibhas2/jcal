package com.webage.jcal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    private static final int MAX_LINE_LENGTH = 75;
    private static final Map<String, String> escapades = Map.of(
        "\\", "\\\\\\\\",
        ";", "\\\\;",
        ",", "\\\\,",
        "\n", "\\\\n");
    private static final Pattern escapePattern = Pattern.compile("(\\\\|;|,|\\n)");
    private static DateTimeFormatter localDateFormatter = new DateTimeFormatterBuilder()
        .appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
        .appendValue(ChronoField.MONTH_OF_YEAR, 2)
        .appendValue(ChronoField.DAY_OF_MONTH, 2)
        .toFormatter();
    private static DateTimeFormatter localDateTimeFormatter = new DateTimeFormatterBuilder()
        .appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
        .appendValue(ChronoField.MONTH_OF_YEAR, 2)
        .appendValue(ChronoField.DAY_OF_MONTH, 2)
        .appendLiteral("T")
        .appendValue(ChronoField.HOUR_OF_DAY, 2)
        .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
        .appendLiteral("00")
        .toFormatter();
    private static DateTimeFormatter utcDateFormatter = new DateTimeFormatterBuilder()
        .appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
        .appendValue(ChronoField.MONTH_OF_YEAR, 2)
        .appendValue(ChronoField.DAY_OF_MONTH, 2)
        .appendLiteral("T")
        .appendValue(ChronoField.HOUR_OF_DAY, 2)
        .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
        .appendLiteral("00Z")
        .toFormatter();

    public static String formatLocalDateTime(LocalDateTime dt, TimeZone tz) {
        return formatLocalDateTime(dt, tz.getID());
    }

    public static String formatLocalDateTime(LocalDateTime dt, String tzId) {
        return String.format("TZID=%s:%s", tzId, localDateTimeFormatter.format(dt));
    }

    public static String formatLocalDate(LocalDate dt) {
        return String.format("VALUE=DATE:%s", localDateFormatter.format(dt));
    }

    /**
     * Takes a date and time in a given timezone and then converts it to UTC
     * date and time and then formats it in UTC "Z" format.
     * 
     * @param dt The date time in a given time zone.
     * @param tz The timezone.
     * @return UTC formatted string. Example: 20201110T091030Z
     */
    public static String convertAndFormatUTC(LocalDateTime dt, TimeZone tz) {
        var local = dt.atZone(ZoneId.of(tz.getID()));
        var utcTime = local.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        
        return utcDateFormatter.format(utcTime);
    }

    /**
     * Formats a date in UTC "Z" format. The date is expected to be already
     * in UTC and no conversion is done.
     * 
     * @param dt
     * @return
     */
    public static String formatUTC(LocalDateTime dt) {
        return utcDateFormatter.format(dt);
    }

    public static void breakLine(StringBuilder sb) {
        var insertionIdx = MAX_LINE_LENGTH;
        var remainingLength = sb.length() - insertionIdx;
        var CRLF_SPACE = "\r\n ";

        while (remainingLength > 0) {
            sb.insert(insertionIdx, CRLF_SPACE);

            /*
            Move the insertion index. Keep in mind that the 
            leading space counts in the maxmum line length limit.
            But the trailing \r\n is excluded from the length limit.
            */
            insertionIdx = insertionIdx + (CRLF_SPACE.length() - 1) + MAX_LINE_LENGTH;
            remainingLength = sb.length() - insertionIdx;
        }
    }

    public static String escapeText(String str) {
        StringBuilder sb = new StringBuilder(str.length());

        appendEscaped(sb, str);

        return sb.toString();
    }

    public static void appendEscaped(StringBuilder sb, String str) {
        Matcher matcher = escapePattern.matcher(str);

        while (matcher.find()) {
            var token = matcher.group(1);

            matcher.appendReplacement(sb, escapades.get(token));
        }

        matcher.appendTail(sb);
    }

    public static void outputProperty(StringBuilder output, String propName, String value) {
        StringBuilder sb = new StringBuilder(propName.length() + value.length());

        sb.append(propName);

        appendEscaped(sb, value);

        breakLine(sb);

        output.append(sb);
        output.append("\r\n");
    }

    public static LocalDateTime toUTC(LocalDateTime dt, TimeZone tz) {
        var local = dt.atZone(ZoneId.of(tz.getID()));
        var utcTime = local.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();

        return utcTime;
    }
}
