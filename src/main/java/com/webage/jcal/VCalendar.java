package com.webage.jcal;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a VCALENDAR calendar invite.
 * This is the root element of an iCal file.
 */
public class VCalendar {
    private MethodType method = MethodType.REQUEST;
    private String productId = "jcal";
    private List<VEvent> eventList = new ArrayList<>();
    private List<String> vTimeZones = new ArrayList<>();

    /**
     * Returns the METHOD
     * @return The method
     */
    public MethodType getMethod() {
        return method;
    }
    /**
     * Sets the METHOD
     * @param method the method
     */
    public void setMethod(MethodType method) {
        this.method = method;
    }
    /**
     * Gets the PRODID
     * @return The PRODID
     */
    public String getProductId() {
        return productId;
    }
    /**
     * Sets the PRODID
     * @param productId the PRODID
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }
    /**
     * Gets the list of VEVENT in this invite
     * @return the list of VEVENT
     */
    public List<VEvent> getEventList() {
        return eventList;
    }
    /**
     * Sets the list of VEVENT in this invite
     * @param eventList the list of VEVENT
     */
    public void setEventList(List<VEvent> eventList) {
        this.eventList = eventList;
    }
    /**
     * Adds a VEVENT to the invite
     * @param event a VEVENT
     */
    public void addEvent(VEvent event) {
        getEventList().add(event);
    }
    /**
     * Get all the VTIMEZONE defined in this invite
     * @return all the VTIMEZONE
     */
    public List<String> getVTimeZones() {
        return vTimeZones;
    }
    /**
     * Sets the list of all VTIMEZONE defined in this invite
     * @param vTimeZones lis of all VTIMEZONE
     */
    public void setVTimeZones(List<String> vTimeZones) {
        this.vTimeZones = vTimeZones;
    }
    /**
     * Adds a VTIMEZONE to this invite
     * @param vTimeZone a VTIMEZONE
     */
    public void addVTimeZone(String vTimeZone) {
        this.vTimeZones.add(vTimeZone);
    }

    /**
     * A conveience method that adds a predefined VTIMEZONE used by Outlook
     * to this invite. If an event uses OutlookTimeZone then you must add it to the
     * invite by calling this method.
     * @param tz the OutlookTimeZone
     */
    public void addOutlookTimeZone(OutlookTimeZone tz) {
        this.vTimeZones.add(tz.toVTimeZone());
    }

    /**
     * Writes the calendar invite to a StringBuilder.
     * 
     * @param sb a StringBuilder
     */
    public void output(StringBuilder sb) {
        sb.append("BEGIN:VCALENDAR\r\n");

        Util.outputProperty(sb, "PRODID:", getProductId());
        
        sb.append("VERSION:2.0\r\n");
        sb.append("CALSCALE:GREGORIAN\r\n");

        sb.append(String.format("METHOD:%s\r\n", getMethod().getMethod()));

        vTimeZones.forEach(tz -> {
            sb.append(tz);
        });

        getEventList().forEach(event -> event.output(sb));

        sb.append("END:VCALENDAR\r\n");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        output(sb);

        return sb.toString();
    }

    /**
     * Writes the calendar invite to a stream. Others can read from
     * the stream byte by byte.
     * @return A ByteArrayInputStream
     */
    public ByteArrayInputStream toInputStream() {
        var buff = toUTF8();
        
        return new ByteArrayInputStream(buff.array(), 0, buff.limit());
    }

    /**
     * Writes the calendar invite to a UTF-8 encoded ByteBuffer
     * 
     * @return the ByteBuffer.
     */
    public ByteBuffer toUTF8() {
        StringBuilder sb = new StringBuilder();

        output(sb);

        return StandardCharsets.UTF_8
            .encode(CharBuffer.wrap(sb));
    }

    /**
     * Returns a Builder that makes it a little easier to create a VCalendar.
     * @return the Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A class that makes it a little easier to create a VCalendar
     */
    public static class Builder {
        private VCalendar cal = new VCalendar();

        /**
         * Sets the METHOD
         * @param method the method
         * @return the builder
         */
        public Builder method(MethodType method) {
            cal.setMethod(method);

            return this;
        }
        /**
         * Adds a VEVENT to the invite
         * @param event a VEVENT
         * @return the builder
         */
        public Builder event(VEvent event) {
            cal.addEvent(event);

            return this;
        }
        /**
         * Adds a VTIMEZONE to the invite
         * @param vTimeZone a VTIMEZONE
         * @return the builder
         */
        public Builder vTimeZone(String vTimeZone) {
            cal.addVTimeZone(vTimeZone);

            return this;
        }
        /**
         * Adds a predefined VTIMEZONE supported by Outlook.
         * @param tz the VTIMEZONE supported by Outlook
         * @return the builder
         */
        public Builder outlookVTimeZone(OutlookTimeZone tz) {
            cal.addOutlookTimeZone(tz);

            return this;
        }
        /**
         * Returns a VCalendar instance.
         * @return a VCalendar instance
         */
        public VCalendar build() {
            return cal;
        }
    }
}
