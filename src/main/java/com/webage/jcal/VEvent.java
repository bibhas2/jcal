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
    private Optional<String> location = Optional.empty();
    private Optional<String> locationURL = Optional.empty();
    
    VEvent() {
        dateTimeStamp = Util.convertAndFormatUTC(LocalDateTime.now(), TimeZone.getDefault());
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizerEmail) {
        this.organizer = String.format("CN=%s:mailto:%s", organizerEmail, organizerEmail);
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

    public Optional<String> getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = Optional.of(location);
    }

    public Optional<String> getLocationURL() {
        return locationURL;
    }

    public void setLocationURL(String locationURL) {
        this.locationURL = Optional.of(locationURL);
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

        getLocation().ifPresent(loc -> {
            getLocationURL().ifPresentOrElse(locURL -> {
                Util.outputProperty(sb, String.format("LOCATION;ALTREP=\"%s\":", locURL), loc);
            },
            () -> {
                Util.outputProperty(sb, "LOCATION:", loc);
            });
        });

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

        /**
         * Sets the organizer of the event. This field is mandatory.
         * 
         * @param organizerEmail The email address of the organizer.
         * @return
         */
        public Builder organizer(String organizerEmail) {
            event.setOrganizer(organizerEmail);

            return this;
        }
        /**
         * Sets the name and email of the organizer. This field is mandatory.
         * 
         * @param name The name of the organizer.
         * @param email The email address of the organizer.
         * @return
         */
        public Builder organizer(String name, String email) {
            event.setOrganizer(name, email);

            return this;
        }

        /**
         * Sets a detailed description of the event.
         * 
         * @param description
         * @return
         */
        public Builder description(String description) {
            event.setDescription(description);

            return this;
        }

        /**
         * Sets the globally unique ID of the event. This field is mandatory.
         * 
         * @param uid
         * @return
         */
        public Builder uid(String uid) {
            event.setUID(uid);

            return this;
        }

        /**
         * Sets the start date and time of the event.
         * 
         * @param startDateTime the start date and time.
         * @param timeZone The time zone of the event.
         * @return
         */
        public Builder starts(LocalDateTime startDateTime, TimeZone timeZone) {
            event.setStartDateTime(startDateTime, timeZone);

            return this;
        }

        /**
         * Sets the start date of the event. Use this to create a day long event.
         * Please note that iCalendar does not take a time zone for day long event.
         * 
         * @param startDate The start date of a day long event.
         * @return
         */
        public Builder starts(LocalDate startDate) {
            event.setStartDate(startDate);

            return this;
        }

        /**
         * Sets the end date and time of an event.
         * 
         * @param endDateTime the end date and time.
         * @param timeZone The time zone of the event.
         * @return
         */
        public Builder ends(LocalDateTime endDateTime, TimeZone timeZone) {
            event.setEndDateTime(endDateTime, timeZone);

            return this;
        }

        /**
         * Sets the end date of a day long event. This date is exclusive.
         * For example, if the last day of the event is Nov 2, 2022, then
         * you should set this date to Nov 3, 2022.
         * 
         * @param endDate
         * @return
         */
        public Builder ends(LocalDate endDate) {
            event.setEndDate(endDate);

            return this;
        }

        /**
         * The status of the event. Default is StatusType.CONFIRMED.
         * 
         * @param status
         * @return
         */
        public Builder status(StatusType status) {
            event.setStatus(status);

            return this;
        }

        /**
         * Set a short summary for the event. 
         * 
         * @param summary
         * @return
         */
        public Builder summary(String summary) {
            event.setSummary(summary);

            return this;
        }

        /**
         * Set when this event was created in your backend system.
         * 
         * @param createdOn
         * @param timeZone
         * @return
         */
        public Builder createdOn(LocalDateTime createdOn, TimeZone timeZone) {
            event.setCreatedDate(createdOn, timeZone);

            return this;
        }

        /**
         * Add a new attendee to the event.
         * 
         * @param name
         * @param email
         * @return
         */
        public Builder attendee(String name, String email) {
            event.addAttendee(name, email);

            return this;
        }

        /**
         * Add a new attendee to the event.
         * 
         * @param email
         * @return
         */
        public Builder attendee(String email) {
            event.addAttendee(email);

            return this;
        }

        /**
         * Sets the repeat frequency of the event.
         * 
         * @param repeatFrequency
         * @return
         */
        public Builder repeats(FrequencyType repeatFrequency) {
            event.setRepeatFrequency(repeatFrequency);

            return this;
        }

        /**
         * Sets how long the event will keep repeating. The event will no longer
         * repeat after this date and time. Note: iCalendar takes the UNTIL property
         * in UTC only. Here, the method will convert the date and time to UTC for you.
         * 
         * @param until The date and time.
         * @param tz The time zone. 
         * @return
         */
        public Builder until(LocalDateTime until, TimeZone tz) {
            event.setRepeatUntil(until, tz);

            return this;
        }

        /**
         * Sets how long the event will keep repeating. The event will no longer
         * repeat after this date and time. The date and time is specified in UTC.
         * 
         * @param untilUTC
         * @return
         */
        public Builder until(LocalDateTime untilUTC) {
            event.setRepeatUntil(untilUTC);

            return this;
        }

        public Builder repeatInterval(int repeatInterval) {
            event.setRepeatInterval(repeatInterval);

            return this;
        }

        /**
         * How many times the event will repeat. Either set this or the until date but not both.
         * 
         * @param repeatCount
         * @return
         */
        public Builder repeatCount(int repeatCount) {
            event.setRepeatCount(repeatCount);

            return this;
        }

        /**
         * Set the location information.
         * 
         * @param location The name of the location. For example an address.
         * @return
         */
        public Builder location(String location) {
            event.setLocation(location);

            return this;
        }

        /**
         * Sets the location name and a link URL.
         * @param location The location name
         * @param locationURL A URL that has more information about the location. For example, a Google Maps link.
         * @return
         */
        public Builder location(String location, String locationURL) {
            event.setLocation(location);
            event.setLocationURL(locationURL);

            return this;
        }

        /**
         * Returns a fully constructed and initialized event.
         * 
         * @return
         */
        public VEvent build() {
            return event;
        }
    }
}
