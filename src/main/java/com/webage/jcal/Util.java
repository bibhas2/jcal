package com.webage.jcal;

import java.security.cert.CRL;
import java.util.Map;
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
    }
}
