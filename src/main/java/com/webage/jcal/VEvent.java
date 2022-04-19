package com.webage.jcal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

public class VEvent {
    private String organizer;
    private Optional<String> description = Optional.empty();
    private String uid;
    private String startDateTime;
    private Optional<String> endDateTime = Optional.empty();
    private Optional<String> createdDate = Optional.empty();
    private String dateTimeStamp;
    private int sequence;
    private Optional<String> summary = Optional.empty();
    private Optional<LocalDateTime> recurUntil = Optional.empty();
    private Optional<FrequencyType> recurFrequency = Optional.empty();
    private Optional<StatusType> status = Optional.empty();
    private List<String> attendeeList = new ArrayList<>();
    
    VEvent() {
        dateTimeStamp = Util.formatUTC(LocalDateTime.now(), TimeZone.getDefault());
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public void setOrganizer(String name, String email) {
        this.organizer = String.format("CN=%s:mailto:%s", name, email);
    }

    public Optional<String> getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = Optional.of(description);
    }
    public String getUID() {
        return uid;
    }
    public void setUID(String uid) {
        this.uid = uid;
    }
    public String getStartDateTime() {
        return startDateTime;
    }
    public void setStartDateTime(LocalDateTime startDateTime, TimeZone timeZone) {
        this.startDateTime = Util.formatLocalDateTime(startDateTime, timeZone);
    }
    public void setStartDate(LocalDate startDate) {
        this.startDateTime = Util.formatLocalDate(startDate);
    }
    public Optional<String> getEndDateTime() {
        return endDateTime;
    }
    public void setEndDateTime(LocalDateTime endDateTime, TimeZone timeZone) {
        this.endDateTime = Optional.of(Util.formatLocalDateTime(endDateTime, timeZone));
    }
    public void setEndDate(LocalDate endDate) {
        this.endDateTime = Optional.of(Util.formatLocalDate(endDate));
    }
    public int getSequence() {
        return sequence;
    }
    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
    public Optional<StatusType> getStatus() {
        return status;
    }
    public void setStatus(StatusType status) {
        this.status = Optional.of(status);
    }
    public Optional<String> getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = Optional.of(summary);
    }
    public Optional<FrequencyType> getRecurFrequency() {
        return recurFrequency;
    }
    public void setRecurFrequency(Optional<FrequencyType> recurFrequency) {
        this.recurFrequency = recurFrequency;
    }
    public Optional<LocalDateTime> getRecurUntil() {
        return recurUntil;
    }
    public void setRecurUntil(Optional<LocalDateTime> recurUntil) {
        this.recurUntil = recurUntil;
    }

    public Optional<String> getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdOn, TimeZone timeZone) {
        this.createdDate = Optional.of(Util.formatUTC(createdOn, timeZone));
    }

    public String getDateTimeStamp() {
        return dateTimeStamp;
    }

    public void setDateTimeStamp(String dateTimeStamp) {
        this.dateTimeStamp = dateTimeStamp;
    }

    public void addAttendee(String name, String email) {
        attendeeList.add(String.format("CN=%s:mailto:%s",
            name, email));
    }

    public void addAttendee(String email) {
        attendeeList.add(String.format("CN=%s:mailto:%s",
            email, email));
    }

    public List<String> getAttendeeList() {
        return attendeeList;
    }

    public void setAttendeeList(List<String> attendeeList) {
        this.attendeeList = attendeeList;
    }

    public void output(StringBuilder sb) {
        sb.append("BEGIN:VEVENT\r\n");

        outputStartDate(sb);

        getEndDateTime().ifPresent(dt -> {
            sb.append("DTEND;");
            sb.append(dt);
            sb.append("\r\n");
        });

        sb.append(String.format("DTSTAMP:%s\r\n", getDateTimeStamp()));

        Util.outputProperty(sb, "ORGANIZER;", getOrganizer());
        Util.outputProperty(sb, "UID:", getUID());

        getCreatedDate().ifPresent(c -> sb.append(String.format("CREATED:%s\r\n", c)));

        sb.append(String.format("SEQUENCE:%d\r\n", getSequence()));

        getStatus().ifPresent(s -> sb.append(String.format("STATUS:%s\r\n", s.getStatus())));

        getSummary().ifPresent(s -> Util.outputProperty(sb, "SUMMARY:", s));
        getDescription().ifPresent(d -> Util.outputProperty(sb, "DESCRIPTION:", d));

        getAttendeeList().forEach(a -> Util.outputProperty(sb, "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE;", a));

        sb.append("END:VEVENT\r\n");
    }

    private void outputStartDate(StringBuilder sb) {
        sb.append("DTSTART;");
        sb.append(getStartDateTime());
        sb.append("\r\n");
    }
}
