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
    private Optional<Integer> sequence = Optional.empty();
    private Optional<String> summary = Optional.empty();
    private Optional<FrequencyType> repeatFrequency = Optional.empty();
    private Optional<LocalDateTime> repeatUntil = Optional.empty();
    private Optional<Integer> repeatInterval = Optional.empty();
    private Optional<Integer> repeatCount = Optional.empty();
    private Optional<StatusType> status = Optional.of(StatusType.CONFIRMED);
    private List<String> attendeeList = new ArrayList<>();
    
    VEvent() {
        dateTimeStamp = Util.convertAndFormatUTC(LocalDateTime.now(), TimeZone.getDefault());
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
    public Optional<Integer> getSequence() {
        return sequence;
    }
    public void setSequence(int sequence) {
        this.sequence = Optional.of(sequence);
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

    public Optional<String> getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdOn, TimeZone timeZone) {
        this.createdDate = Optional.of(Util.convertAndFormatUTC(createdOn, timeZone));
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

    public Optional<FrequencyType> getRepeatFrequency() {
        return repeatFrequency;
    }

    public void setRepeatFrequency(FrequencyType repeatFrequency) {
        this.repeatFrequency = Optional.of(repeatFrequency);
    }

    public Optional<LocalDateTime> getRepeatUntil() {
        return repeatUntil;
    }

    public void setRepeatUntil(LocalDateTime until, TimeZone tz) {
        this.repeatUntil = Optional.of(Util.toUTC(until, tz));
    }

    public void setRepeatUntil(LocalDateTime untilUTC) {
        this.repeatUntil = Optional.of(untilUTC);
    }

    public Optional<Integer> getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(int repeatInterval) {
        this.repeatInterval = Optional.of(repeatInterval);
    }

    public Optional<Integer> getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = Optional.of(repeatCount);
    }

    public void setRepeatCount(Optional<Integer> repeatCount) {
        this.repeatCount = repeatCount;
    }

    public void output(StringBuilder sb) {
        sb.append("BEGIN:VEVENT\r\n");

        outputStartDate(sb);

        getEndDateTime().ifPresent(dt -> {
            sb.append("DTEND;");
            sb.append(dt);
            sb.append("\r\n");
        });

        outputRepeatRule(sb);
        
        sb.append(String.format("DTSTAMP:%s\r\n", getDateTimeStamp()));

        Util.outputProperty(sb, "ORGANIZER;", getOrganizer());
        Util.outputProperty(sb, "UID:", getUID());

        getCreatedDate().ifPresent(c -> sb.append(String.format("CREATED:%s\r\n", c)));

        getSequence().ifPresent(s -> sb.append(String.format("SEQUENCE:%d\r\n", s)));

        getStatus().ifPresent(s -> sb.append(String.format("STATUS:%s\r\n", s.getStatus())));

        getSummary().ifPresent(s -> Util.outputProperty(sb, "SUMMARY:", s));
        getDescription().ifPresent(d -> Util.outputProperty(sb, "DESCRIPTION:", d));

        getAttendeeList().forEach(a -> Util.outputProperty(sb, "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE;", a));

        sb.append("END:VEVENT\r\n");
    }

    private void outputRepeatRule(StringBuilder sb) {
        getRepeatFrequency().ifPresent(f -> {
            sb.append("RRULE:");
            sb.append(String.format("FREQ=%s", f.getFrequency()));
            getRepeatUntil().ifPresent(u -> sb.append(String.format(";UNTIL=%s", Util.formatUTC(u))));
            getRepeatCount().ifPresent(c -> sb.append(String.format(";COUNT=%d", c)));
            getRepeatInterval().ifPresent(i -> sb.append(String.format(";INTERVAL=%d", i)));
            sb.append("\r\n");
        });
    }

    private void outputStartDate(StringBuilder sb) {
        sb.append("DTSTART;");
        sb.append(getStartDateTime());
        sb.append("\r\n");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private VEvent event = new VEvent();

        public Builder organizer(String organizer) {
            event.setOrganizer(organizer);

            return this;
        }
        public Builder organizer(String name, String email) {
            event.setOrganizer(name, email);

            return this;
        }
        public Builder description(String description) {
            event.setDescription(description);

            return this;
        }
        public Builder uid(String uid) {
            event.setUID(uid);

            return this;
        }
        public Builder starts(LocalDateTime startDateTime, TimeZone timeZone) {
            event.setStartDateTime(startDateTime, timeZone);

            return this;
        }
        public Builder starts(LocalDate startDate) {
            event.setStartDate(startDate);

            return this;
        }
        public Builder ends(LocalDateTime endDateTime, TimeZone timeZone) {
            event.setEndDateTime(endDateTime, timeZone);

            return this;
        }
        public Builder ends(LocalDate endDate) {
            event.setEndDate(endDate);

            return this;
        }
        public Builder status(StatusType status) {
            event.setStatus(status);

            return this;
        }
        public Builder summary(String summary) {
            event.setSummary(summary);

            return this;
        }
        public Builder createdOn(LocalDateTime createdOn, TimeZone timeZone) {
            event.setCreatedDate(createdOn, timeZone);

            return this;
        }
        public Builder attendee(String name, String email) {
            event.addAttendee(name, email);

            return this;
        }
        public Builder attendee(String email) {
            event.addAttendee(email);

            return this;
        }
        public Builder repeats(FrequencyType repeatFrequency) {
            event.setRepeatFrequency(repeatFrequency);

            return this;
        }
        public Builder until(LocalDateTime until, TimeZone tz) {
            event.setRepeatUntil(until, tz);

            return this;
        }
        public Builder until(LocalDateTime untilUTC) {
            event.setRepeatUntil(untilUTC);

            return this;
        }
        public Builder repeatInterval(int repeatInterval) {
            event.setRepeatInterval(repeatInterval);

            return this;
        }
        public Builder repeatCount(int repeatCount) {
            event.setRepeatCount(repeatCount);

            return this;
        }
        public VEvent build() {
            return event;
        }
    }
}
