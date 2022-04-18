package com.webage.jcal;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.TimeZone;

public class VEvent {
    String organizer;
    Optional<String> description = Optional.empty();
    String uid;
    String startDateTime;
    String endDateTime;
    Optional<String> createdDate = Optional.empty();
    String dateTimeStamp;
    int sequence;
    Optional<String> summary = Optional.empty();
    Optional<LocalDateTime> recurUntil = Optional.empty();
    Optional<FrequencyType> recurFrequency = Optional.empty();
    Optional<StatusType> status = Optional.empty();
    
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
        this.startDateTime = Util.formatLocal(startDateTime, timeZone);
    }
    public String getEndDateTime() {
        return endDateTime;
    }
    public void setEndDateTime(LocalDateTime endDateTime, TimeZone timeZone) {
        this.endDateTime = Util.formatLocal(endDateTime, timeZone);;
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

    public void output(StringBuilder sb) {
        sb.append("BEGIN:VEVENT\r\n");

        outputStartDate(sb);
        outputEndDate(sb);

        sb.append(String.format("DTSTAMP:%s\r\n", getDateTimeStamp()));

        Util.outputProperty(sb, "ORGANIZER;", getOrganizer());
        Util.outputProperty(sb, "UID:", getUID());

        getCreatedDate().ifPresent(c -> sb.append(String.format("CREATED:%s\r\n", c)));

        sb.append(String.format("SEQUENCE:%d\r\n", getSequence()));

        getStatus().ifPresent(s -> sb.append(String.format("STATUS:%s\r\n", s.getStatus())));

        getSummary().ifPresent(s -> Util.outputProperty(sb, "SUMMARY:", s));
        getDescription().ifPresent(d -> Util.outputProperty(sb, "DESCRIPTION:", d));

        sb.append("END:VEVENT\r\n");
    }

    private void outputEndDate(StringBuilder sb) {
        sb.append("DTEND;");
        sb.append(getEndDateTime());
        sb.append("\r\n");
    }

    private void outputStartDate(StringBuilder sb) {
        sb.append("DTSTART;");
        sb.append(getStartDateTime());
        sb.append("\r\n");
    }
}
