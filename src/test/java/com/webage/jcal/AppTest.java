package com.webage.jcal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
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
        var cal = new VCalendar();
        var tz = TimeZone.getTimeZone("America/New_York");
        var ev = new VEvent();

        ev.setStartDateTime(LocalDateTime.of(2022, 11, 2, 9, 30), tz);
        ev.setEndDateTime(LocalDateTime.of(2022, 11, 2, 11, 30), tz);
        ev.setOrganizer("Bibhas Bhattacharya", "bibhas.bhattacharya@webagesolutions.com");
        ev.setUID("uid-001");
        ev.setCreatedDate(LocalDateTime.of(2021, 10, 2, 9, 15), tz);
        ev.setStatus(StatusType.CONFIRMED);
        ev.setSummary("A test event, of immense importance.");

        cal.addEvent(ev);

        StringBuilder output = new StringBuilder();

        cal.output(output);

        System.out.println(output.toString());
    }
}
