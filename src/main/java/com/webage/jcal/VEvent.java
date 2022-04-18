package com.webage.jcal;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.TimeZone;

public class VEvent {
    Optional<String> organizerName = Optional.empty();
    String organizerEmail;
    String description;
    String uid;
    TimeZone timeZone;
    LocalDateTime startDateTime;
    LocalDateTime endDateTime;
    int sequence;
    String status = "CONFIRMED";
    String summery;
    Optional<String> recurFrequency = Optional.empty();
    Optional<LocalDateTime> recurUntil = Optional.empty();
    
    public Optional<String> getOrganizerName() {
        return organizerName;
    }
    public void setOrganizerName(Optional<String> organizerName) {
        this.organizerName = organizerName;
    }
    public void setOrganizerName(String organizerName) {
        this.organizerName = Optional.of(organizerName);
    }
    public String getOrganizerEmail() {
        return organizerEmail;
    }
    public void setOrganizerEmail(String organizerEmail) {
        this.organizerEmail = organizerEmail;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public TimeZone getTimeZone() {
        return timeZone;
    }
    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }
    public int getSequence() {
        return sequence;
    }
    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getSummery() {
        return summery;
    }
    public void setSummery(String summery) {
        this.summery = summery;
    }
    public Optional<String> getRecurFrequency() {
        return recurFrequency;
    }
    public void setRecurFrequency(Optional<String> recurFrequency) {
        this.recurFrequency = recurFrequency;
    }
    public Optional<LocalDateTime> getRecurUntil() {
        return recurUntil;
    }
    public void setRecurUntil(Optional<LocalDateTime> recurUntil) {
        this.recurUntil = recurUntil;
    }

    
}
