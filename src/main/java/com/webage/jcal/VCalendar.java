package com.webage.jcal;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VCalendar {
    private MethodType method = MethodType.REQUEST;
    private String productId = "jcal";
    private List<VEvent> eventList = new ArrayList<>();
    private List<String> vTimeZones = new ArrayList<>();

    public MethodType getMethod() {
        return method;
    }
    public void setMethod(MethodType method) {
        this.method = method;
    }
    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public List<VEvent> getEventList() {
        return eventList;
    }
    public void setEventList(List<VEvent> eventList) {
        this.eventList = eventList;
    }
    public void addEvent(VEvent event) {
        getEventList().add(event);
    }
    public List<String> getVTimeZones() {
        return vTimeZones;
    }
    public void setVTimeZones(List<String> vTimeZones) {
        this.vTimeZones = vTimeZones;
    }
    public void addVTimeZone(String vTimeZone) {
        this.vTimeZones.add(vTimeZone);
    }

    public void addOutlookTimeZone(OutlookTimeZone tz) {
        this.vTimeZones.add(tz.toVTimeZone());
    }

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

    public ByteArrayInputStream toInputStream() {
        var buff = toUTF8();
        
        return new ByteArrayInputStream(buff.array(), 0, buff.limit());
    }

    public ByteBuffer toUTF8() {
        StringBuilder sb = new StringBuilder();

        output(sb);

        return StandardCharsets.UTF_8
            .encode(CharBuffer.wrap(sb));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private VCalendar cal = new VCalendar();

        public Builder method(MethodType method) {
            cal.setMethod(method);

            return this;
        }
        public Builder event(VEvent event) {
            cal.addEvent(event);

            return this;
        }
        public Builder vTimeZone(String vTimeZone) {
            cal.addVTimeZone(vTimeZone);

            return this;
        }
        public Builder outlookVTimeZone(OutlookTimeZone tz) {
            cal.addOutlookTimeZone(tz);

            return this;
        }
        public VCalendar build() {
            return cal;
        }
    }
}
