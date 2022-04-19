package com.webage.jcal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

        assertTrue(str.contains("RRULE:FREQ=DAILY;COUNT=2;\r\n"));

        output.setLength(0);

        ev.setRepeatCount(Optional.empty());
        ev.setRepeatUntil(LocalDateTime.of(2022, 11, 4, 0, 00), tz);
        ev.output(output);

        str = output.toString();
        
        assertTrue(str.contains("RRULE:FREQ=DAILY;UNTIL=20221104T040000Z;\r\n"));
    }

    @Test
    public void testBuilder() {
        var tz = TimeZone.getTimeZone("America/New_York");
        var ev = VEvent
            .builder()
            .uid("uid-1")
            .organizer("abc", "xyz@example.com")
            .starts(LocalDateTime.of(2022, 11, 2, 9, 30), tz)
            .summary("Test event")
            .build();

        StringBuilder output = new StringBuilder();

        ev.output(output);

        System.out.println(output.toString());
    }
}
