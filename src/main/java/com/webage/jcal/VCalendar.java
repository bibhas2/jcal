package com.webage.jcal;

import java.util.ArrayList;
import java.util.List;

public class VCalendar {
    private MethodType method = MethodType.REQUEST;
    private String productId = "jcal";
    private List<VEvent> eventList = new ArrayList<>();

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
        event.setSequence(getEventList().size() + 1);

        getEventList().add(event);
    }

    public void output(StringBuilder sb) {
        sb.append("BEGIN:VCALENDAR\r\n");

        Util.outputProperty(sb, "PRODID:", getProductId());
        
        sb.append("VERSION:2.0\r\n");
        sb.append("CALSCALE:GREGORIAN\r\n");

        sb.append(String.format("METHOD:%s\r\n", getMethod().getMethod()));

        getEventList().forEach(event -> event.output(sb));

        sb.append("END:VCALENDAR\r\n");
    }
}
