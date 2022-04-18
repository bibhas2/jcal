package com.webage.jcal;

import static org.junit.Assert.assertEquals;

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

        //Make sure the ; in property name was not escaped.
        assertEquals(str.indexOf("PROPNAME;"), 0);

        var parts = str.split("\r\n");

        assertEquals(parts.length, 4);
        assertEquals(parts[0].length(), 75);
        assertEquals(parts[1].length(), 75);
        assertEquals(parts[2].length(), 75);
        assertEquals(parts[3].length(), 33);
    }
}
