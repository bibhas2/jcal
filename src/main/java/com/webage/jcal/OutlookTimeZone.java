package com.webage.jcal;

import java.util.HashMap;
import java.util.Optional;

/**
    * These are non-standard time zone IDs used by Windows
    * and Outlook. A full list is available from:
    * https://learn.microsoft.com/en-us/windows-hardware/manufacture/desktop/default-time-zones?view=windows-11
    *
    * For maximum compatibility with Outlook try to use these time zones.
    */
public enum OutlookTimeZone {
    /**
     * Romance Standard Time
     */
    TZ_ROMANCE_STANDARD_TIME("Romance Standard Time"),
    /**
     * Singapore Standard Time
     */
    TZ_SINGAPORE_STANDARD_TIME("Singapore Standard Time"),
    /**
     * India Standard Time
     */
    TZ_INDIA_STANDARD_TIME("India Standard Time"),
    /**
     * FLE Standard Time
     */
    TZ_FLE_STANDARD_TIME("FLE Standard Time"),
    /**
     * GMT Standard Time
     */
    TZ_GMT_STANDARD_TIME("GMT Standard Time");

    private final String tzId;

    private static final HashMap<String, String> vTimeZoneMap;
    private static final HashMap<String, OutlookTimeZone> ianaTzIdMap;

    static {
        vTimeZoneMap = new HashMap<>();

        vTimeZoneMap.put(TZ_ROMANCE_STANDARD_TIME.toString(), 
            "BEGIN:VTIMEZONE\r\n" + 
            "TZID:Romance Standard Time\r\n" + 
            "BEGIN:STANDARD\r\n" + 
            "DTSTART:16010101T030000\r\n" + 
            "TZOFFSETFROM:+0200\r\n" + 
            "TZOFFSETTO:+0100\r\n" + 
            "RRULE:FREQ=YEARLY;INTERVAL=1;BYDAY=-1SU;BYMONTH=10\r\n" + 
            "END:STANDARD\r\n" + 
            "BEGIN:DAYLIGHT\r\n" + 
            "DTSTART:16010101T020000\r\n" + 
            "TZOFFSETFROM:+0100\r\n" + 
            "TZOFFSETTO:+0200\r\n" + 
            "RRULE:FREQ=YEARLY;INTERVAL=1;BYDAY=-1SU;BYMONTH=3\r\n" + 
            "END:DAYLIGHT\r\n" + 
            "END:VTIMEZONE\r\n");

        vTimeZoneMap.put(TZ_SINGAPORE_STANDARD_TIME.toString(), 
            "BEGIN:VTIMEZONE\r\n" + 
            "TZID:Singapore Standard Time\r\n" + 
            "BEGIN:STANDARD\r\n" + 
            "DTSTART:16010101T000000\r\n" + 
            "TZOFFSETFROM:+0800\r\n" + 
            "TZOFFSETTO:+0800\r\n" + 
            "END:STANDARD\r\n" + 
            "END:VTIMEZONE\r\n");

        vTimeZoneMap.put(TZ_FLE_STANDARD_TIME.toString(), 
            "BEGIN:VTIMEZONE\r\n" + 
            "TZID:FLE Standard Time\r\n" + 
            "BEGIN:STANDARD\r\n" + 
            "DTSTART:16011028T040000\r\n" + 
            "RRULE:FREQ=YEARLY;BYDAY=-1SU;BYMONTH=10\r\n" + 
            "TZOFFSETFROM:+0300\r\n" + 
            "TZOFFSETTO:+0200\r\n" + 
            "END:STANDARD\r\n" + 
            "BEGIN:DAYLIGHT\r\n" + 
            "DTSTART:16010325T030000\r\n" + 
            "RRULE:FREQ=YEARLY;BYDAY=-1SU;BYMONTH=3\r\n" + 
            "TZOFFSETFROM:+0200\r\n" + 
            "TZOFFSETTO:+0300\r\n" + 
            "END:DAYLIGHT\r\n" + 
            "END:VTIMEZONE\r\n");

        vTimeZoneMap.put(TZ_GMT_STANDARD_TIME.toString(), 
            "BEGIN:VTIMEZONE\r\n" + 
            "TZID:GMT Standard Time\r\n" + 
            "BEGIN:STANDARD\r\n" + 
            "DTSTART:16011028T020000\r\n" + 
            "RRULE:FREQ=YEARLY;BYDAY=-1SU;BYMONTH=10\r\n" + 
            "TZOFFSETFROM:+0100\r\n" + 
            "TZOFFSETTO:-0000\r\n" + 
            "END:STANDARD\r\n" + 
            "BEGIN:DAYLIGHT\r\n" + 
            "DTSTART:16010325T010000\r\n" + 
            "RRULE:FREQ=YEARLY;BYDAY=-1SU;BYMONTH=3\r\n" + 
            "TZOFFSETFROM:-0000\r\n" + 
            "TZOFFSETTO:+0100\r\n" + 
            "END:DAYLIGHT\r\n" + 
            "END:VTIMEZONE\r\n");

        vTimeZoneMap.put(TZ_INDIA_STANDARD_TIME.toString(), 
            "BEGIN:VTIMEZONE\r\n" + 
            "TZID:India Standard Time\r\n" + 
            "BEGIN:STANDARD\r\n" + 
            "DTSTART:16010101T000000\r\n" + 
            "TZOFFSETFROM:+0530\r\n" + 
            "TZOFFSETTO:+0530\r\n" + 
            "END:STANDARD\r\n" + 
            "END:VTIMEZONE\r\n");


        ianaTzIdMap = new HashMap<>();

        ianaTzIdMap.put("Asia/Singapore", TZ_SINGAPORE_STANDARD_TIME);
        ianaTzIdMap.put("Asia/Kolkata", TZ_INDIA_STANDARD_TIME);
        ianaTzIdMap.put("Europe/Lisbon", TZ_GMT_STANDARD_TIME);
        ianaTzIdMap.put("Europe/Paris", TZ_ROMANCE_STANDARD_TIME);
        ianaTzIdMap.put("Europe/Helsinki", TZ_FLE_STANDARD_TIME);
    }

    private OutlookTimeZone(String tzId) {
        this.tzId = tzId;
    }
    
    @Override
    public String toString() {
        return tzId;
    }

    /**
     * Gets the VTIMEZONE section for this time zone.
     * @return The VTIMEZONE section wrapped in BEGIN:VTIMEZONE and END:VTIMEZONE.
     */
    public String toVTimeZone() {
        return vTimeZoneMap.get(tzId);
    }

    /**
     * Converts a IANA time zone ID to OutlookTimeZone.
     * A list of IANA ids can be found at:
     * 
     * https://en.wikipedia.org/wiki/List_of_tz_database_time_zones
     * 
     * @param tzId IANA time zone id. Such as "Europe/Lisbon"
     * @return The matching OutlookTimeZone.
     */
    public static Optional<OutlookTimeZone> fromIANATimeZone(String tzId) {
        return Optional.ofNullable(ianaTzIdMap.get(tzId));
    }
}
