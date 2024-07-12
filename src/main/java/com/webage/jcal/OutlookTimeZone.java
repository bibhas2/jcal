package com.webage.jcal;

import java.util.HashMap;

/**
    * These are non-standard time zone IDs used by Windows
    * and Outlook. A full list is available from Windows
    * registry key: HKEY_LOCAL_MACHINE\Software\Microsoft\Windows NT\CurrentVersion\Time Zones
    */
public enum OutlookTimeZone {
    TZ_ROMANCE_STANDARD_TIME("Romance Standard Time"),
    TZ_SINGAPORE_STANDARD_TIME("Singapore Standard Time"),
    TZ_INDIA_STANDARD_TIME("India Standard Time"),
    TZ_HELSINKI_STANDARD_TIME("FLE Standard Time"),
    TZ_GMT_STANDARD_TIME("GMT Standard Time");

    private final String tzId;

    private static final HashMap<String, String> vTimeZoneMap;

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
}
