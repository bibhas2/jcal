package com.webage.jcal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.TimeZone;

import org.junit.Test;

public class AppTest {

    @Test
    public void testLineBreak() {
        var sb = new StringBuilder("012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
        var result = "012345678901234567890123456789012345678901234567890123456789012345678901234\r\n 56789012345678901234567890123456789012345678901234567890123456789012345678\r\n 90123456789012345678901234567890123456789012345678901234567890123456789012\r\n 34567890123456789";

        Util.breakLine(sb);

        assertEquals(result, sb.toString());

        sb = new StringBuilder("abc123");
        Util.breakLine(sb);

        assertEquals("abc123", sb.toString());
    }

    @Test
    public void testEscape() {
        var result = Util.escapeText("Hello,Mama;Papa\nkaka\\mama\npapa");

        assertEquals("Hello\\,Mama\\;Papa\\nkaka\\\\mama\\npapa", result);
    }

    @Test
    public void testOutputProperty() {
        StringBuilder output = new StringBuilder();

        Util.outputProperty(output, "PROPNAME;", "01234567890123\n4567890123456789012,34567890123456789;0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");

        var str = output.toString();

        //Check training CRLF
        assertTrue(str.endsWith("\r\n"));

        //Make sure the ; in property name was not escaped.
        assertEquals(str.indexOf("PROPNAME;"), 0);

        var parts = str.split("\r\n");

        assertEquals(parts.length, 4);
        assertEquals(parts[0].length(), 75);
        assertEquals(parts[1].length(), 75);
        assertEquals(parts[2].length(), 75);
        assertEquals(parts[3].length(), 33);
    }

    @Test
    public void testBasicEvent() {
        var tz = TimeZone.getTimeZone("America/New_York");
        var ev = new VEvent();

        ev.setOrganizer("abc", "abc@example.com");
        ev.setStartDateTime(LocalDateTime.of(2022, 11, 2, 9, 30), tz);
        ev.setEndDateTime(LocalDateTime.of(2022, 11, 2, 11, 30), tz);
        ev.setUID("uid-001");
        ev.setSummary("A test event, of immense importance.");

        StringBuilder output = new StringBuilder();

        ev.output(output);

        var str = output.toString();

        assertTrue(str.contains("DTSTART;TZID=America/New_York:20221102T093000\r\n"));
        assertTrue(str.contains("DTEND;TZID=America/New_York:20221102T113000\r\n"));
        assertTrue(str.contains("SUMMARY:A test event\\, of immense importance.\r\n"));
    }

    @Test
    public void testAllDayEvent() {
        var ev = new VEvent();

        //2 day long event
        var startDate = LocalDate.of(2022, 11, 2);
        var endDate = startDate.plusDays(2);
        
        ev.setStartDate(startDate);
        ev.setEndDate(endDate);
        ev.setOrganizer("abc", "abc@example.com");
        ev.setUID("uid-001");

        StringBuilder output = new StringBuilder();

        ev.output(output);

        var str = output.toString();

        assertTrue(str.contains("DTSTART;VALUE=DATE:20221102\r\n"));
        assertTrue(str.contains("DTEND;VALUE=DATE:20221104\r\n"));
    }

    @Test
    public void testAttendees() {
        var tz = TimeZone.getTimeZone("America/New_York");
        var ev = new VEvent();

        ev.setStartDateTime(LocalDateTime.of(2022, 11, 2, 9, 30), tz);
        ev.setOrganizer("abc", "abc@example.com");
        ev.setUID("uid-001");
        ev.addAttendee("abc.def@xxxxyyyy.com");
        ev.addAttendee("Mno Pqr", "mno.pqr@xxxxyyyy.com");

        StringBuilder output = new StringBuilder();

        ev.output(output);

        var str = output.toString();

        assertTrue(str.contains("ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE;CN=abc.def@xx\r\n xxyyyy.com:mailto:abc.def@xxxxyyyy.com\r\n"));
        assertTrue(str.contains("ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE;CN=Mno Pqr:ma\r\n ilto:mno.pqr@xxxxyyyy.com\r\n"));
    }

    @Test
    public void testRepeat() {
        var tz = TimeZone.getTimeZone("America/New_York");
        var ev = new VEvent();

        ev.setStartDateTime(LocalDateTime.of(2022, 11, 2, 9, 30), tz);

        ev.setOrganizer("abc", "abc@example.com");
        ev.setUID("uid-001");

        StringBuilder output = new StringBuilder();

        ev.setRepeatFrequency(FrequencyType.DAILY);
        ev.setRepeatCount(2);

        ev.output(output);

        var str = output.toString();

        assertTrue(str.contains("RRULE:FREQ=DAILY;COUNT=2\r\n"));

        output.setLength(0);

        ev.setRepeatCount(Optional.empty());
        ev.setRepeatUntil(LocalDateTime.of(2022, 11, 4, 0, 00), tz);
        ev.output(output);

        str = output.toString();
        
        assertTrue(str.contains("RRULE:FREQ=DAILY;UNTIL=20221104T040000Z\r\n"));
    }

    @Test
    public void testBuilder() {
        var tz = TimeZone.getTimeZone("America/New_York");
        var ev = VEvent
            .builder()
            .uid("uid-1")
            .organizer("abc", "xyz@example.com")
            .starts(LocalDateTime.of(2022, 11, 2, 9, 30), tz)
            .ends(LocalDateTime.of(2022, 11, 2, 10, 0), tz)
            .summary("Test event")
            .attendee("Bugs Bunny", "bugs.bunny@wb.com")
            .attendee("daffy.duck@wb.com")
            .build();

        var str = VCalendar.builder()
            .event(ev)
            .build()
            .toString();

        assertTrue(str.contains("DTSTART;TZID=America/New_York:20221102T093000\r\n"));
    }

    @Test
    public void testVTimeZone() throws FileNotFoundException, IOException {
        String vTimeZone = "BEGIN:VTIMEZONE\r\n" + 
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
            "END:VTIMEZONE\r\n";

        var ev = VEvent
            .builder()
            .uid("uid-1")
            .organizer("abc", "xyz@example.com")
            .starts(LocalDateTime.of(2024, 10, 25, 9, 30), "Romance Standard Time")
            .ends(LocalDateTime.of(2024, 10, 25, 16, 0), "Romance Standard Time")
            .repeats(FrequencyType.DAILY)
            .until(LocalDateTime.of(2024, 10, 29, 16, 0))
            .summary("Test event")
            .attendee("Bugs Bunny", "bugs.bunny@wb.com")
            .attendee("daffy.duck@wb.com")
            .build();

        var str = VCalendar.builder()
            .vTimeZone(vTimeZone)
            .event(ev)
            .build()
            .toString();

        try (var fs = new FileOutputStream("test.ics")) {
            fs.write(str.getBytes());

            fs.flush();
        }
    }

    @Test
    public void testOutlookTimeZone() throws FileNotFoundException, IOException {
        var ev = VEvent
            .builder()
            .uid("uid-1")
            .organizer("abc", "xyz@example.com")
            .starts(LocalDateTime.of(2024, 10, 25, 9, 30), OutlookTimeZone.TZ_ROMANCE_STANDARD_TIME)
            .ends(LocalDateTime.of(2024, 10, 25, 16, 0), OutlookTimeZone.TZ_ROMANCE_STANDARD_TIME)
            .repeats(FrequencyType.DAILY)
            .until(LocalDateTime.of(2024, 10, 29, 16, 0))
            .summary("Test event")
            .attendee("Bugs Bunny", "bugs.bunny@wb.com")
            .attendee("daffy.duck@wb.com")
            .build();

        var str = VCalendar.builder()
            .outlookVTimeZone(OutlookTimeZone.TZ_ROMANCE_STANDARD_TIME)
            .event(ev)
            .build()
            .toString();

        try (var fs = new FileOutputStream("test.ics")) {
            fs.write(str.getBytes());

            fs.flush();
        }
    }

    @Test
    public void testEmail() {
        var tz = TimeZone.getTimeZone("America/New_York");
        var ev = VEvent
            .builder()
            .uid("uid-1")
            .organizer("abc", "xyz@example.com")
            .starts(LocalDateTime.of(2022, 11, 2, 9, 30), tz)
            .ends(LocalDateTime.of(2022, 11, 2, 10, 0), tz)
            .summary("Test event")
            .build();
    
        var iCal = VCalendar.builder()
            .event(ev)
            .build()
            .toString();
        System.out.println(iCal);
        // System.out.println(new String(Base64.getEncoder().encode(b), StandardCharsets.UTF_8));
    }

    @Test
    public void multipleEvents() {
        var tz = TimeZone.getTimeZone("America/New_York");
        var ev1 = VEvent
            .builder()
            .uid("uid-1")
            .organizer("abc", "xyz@example.com")
            .starts(LocalDateTime.of(2022, 11, 2, 9, 30), tz)
            .ends(LocalDateTime.of(2022, 11, 2, 10, 0), tz)
            .summary("Test event 1")
            .build();
        var ev2 = VEvent
            .builder()
            .uid("uid-2")
            .organizer("abc", "xyz@example.com")
            .starts(LocalDateTime.of(2022, 11, 5, 16, 30), tz)
            .ends(LocalDateTime.of(2022, 11, 5, 17, 0), tz)
            .summary("Test event 2")
            .build();
    
        var str = VCalendar.builder()
            .event(ev1)
            .event(ev2)
            .build()
            .toString();

        assertTrue(str.contains("DTSTART;TZID=America/New_York:20221102T093000\r\n"));
        assertTrue(str.contains("DTSTART;TZID=America/New_York:20221105T163000\r\n"));
    }

    @Test
    public void testLocation() {
        var tz = TimeZone.getTimeZone("America/New_York");
        var ev = VEvent
            .builder()
            .uid("uid-1")
            .organizer("abc", "xyz@example.com")
            .starts(LocalDateTime.of(2022, 10, 21, 17, 30), tz)
            .ends(LocalDateTime.of(2022, 10, 21, 20, 30), tz)
            .summary("Test event")
            .location("TEST LOCATION")
            .build();

        var str = VCalendar.builder()
            .event(ev)
            .build()
            .toString();

        assertTrue(str.contains("LOCATION:TEST LOCATION\r\n"));
    }

    @Test
    public void testLocationURL() {
        var tz = TimeZone.getTimeZone("America/New_York");
        var ev = VEvent
            .builder()
            .uid("uid-1")
            .organizer("abc", "xyz@example.com")
            .starts(LocalDateTime.of(2022, 10, 21, 17, 30), tz)
            .ends(LocalDateTime.of(2022, 10, 21, 20, 30), tz)
            .summary("Test event")
            .location("TEST LOCATION", "https://goo.gl/maps/MzhntySdbstb7Yfd8")
            .build();

        var str = VCalendar.builder()
            .event(ev)
            .build()
            .toString();

        assertTrue(str.contains("LOCATION;ALTREP=\"https://goo.gl/maps/MzhntySdbstb7Yfd8\":TEST LOCATION\r\n"));
    }
}
