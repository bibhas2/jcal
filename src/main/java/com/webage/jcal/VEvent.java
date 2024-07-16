package com.webage.jcal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

/**
 * A class that represents a VEVENT
 */
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
    
    /**
     * Create a VEVENT
     */
    VEvent() {
        dateTimeStamp = Util.convertAndFormatUTC(LocalDateTime.now(), TimeZone.getDefault());
    }

    /**
     * Get the ORGANIZER line
     * @return the ORGANIZER line
     */
    public String getOrganizer() {
        return organizer;
    }

    /**
     * Sets the ORGANIZER line from the email of the organizer
     * @param organizerEmail the email of the organizer
     */
    public void setOrganizer(String organizerEmail) {
        this.organizer = String.format("CN=%s:mailto:%s", organizerEmail, organizerEmail);
    }

    /**
     * Sets the ORGANIZER line from the email and name of the organizer
     * @param name name of the organizer
     * @param email email of the organizer
     */
    public void setOrganizer(String name, String email) {
        this.organizer = String.format("CN=%s:mailto:%s", name, email);
    }

    /**
     * Get the DESCRIPTION text.
     * @return the DESCRIPTION text
     */
    public Optional<String> getDescription() {
        return description;
    }
    /**
     * Sets the DESCRIPTION text
     * @param description the DESCRIPTION text
     */
    public void setDescription(String description) {
        this.description = Optional.of(description);
    }
    /**
     * Get the UID
     * @return the UID
     */
    public String getUID() {
        return uid;
    }
    /**
     * Sets the UID
     * @param uid the UID
     */
    public void setUID(String uid) {
        this.uid = uid;
    }
    /**
     * Get the DTSTART line
     * @return the DTSTART line
     */
    public String getStartDateTime() {
        return startDateTime;
    }
    /**
     * Configures the DTSTART line
     * @param startDateTime the start date and time of the event
     * @param timeZone the time zone
     */
    public void setStartDateTime(LocalDateTime startDateTime, TimeZone timeZone) {
        this.startDateTime = Util.formatLocalDateTime(startDateTime, timeZone);
    }
    /**
     * Configures the DTSTART line
     * @param startDateTime the start date and time of the event
     * @param timeZoneId A time zone ID. 
     */
    public void setStartDateTime(LocalDateTime startDateTime, String timeZoneId) {
        this.startDateTime = Util.formatLocalDateTime(startDateTime, timeZoneId);
    }
    /**
     * Configures the DTSTART line with a date but no time. This is a day long event.
     * @param startDate The start date.
     */
    public void setStartDate(LocalDate startDate) {
        this.startDateTime = Util.formatLocalDate(startDate);
    }
    /**
     * Get the DTEND line
     * @return the DTEND line
     */
    public Optional<String> getEndDateTime() {
        return endDateTime;
    }
    /**
     * Sets the the DTEND line
     * @param endDateTime the end date and time
     * @param timeZone the time zone
     */
    public void setEndDateTime(LocalDateTime endDateTime, TimeZone timeZone) {
        this.endDateTime = Optional.of(Util.formatLocalDateTime(endDateTime, timeZone));
    }
    /**
     * Sets the the DTEND line
     * @param endDateTime the end date and time
     * @param timeZoneId the time zone ID
     */
    public void setEndDateTime(LocalDateTime endDateTime, String timeZoneId) {
        this.endDateTime = Optional.of(Util.formatLocalDateTime(endDateTime, timeZoneId));
    }
    /**
     * Sets the the DTEND line with just a date and not time.
     * @param endDate the end date
     */
    public void setEndDate(LocalDate endDate) {
        this.endDateTime = Optional.of(Util.formatLocalDate(endDate));
    }
    /**
     * Get the sequence number of the event
     * @return the sequence number of the event
     */
    public Optional<Integer> getSequence() {
        return sequence;
    }
    /**
     * Set the sequence number of the event
     * @param sequence the sequence number of the event
     */
    public void setSequence(int sequence) {
        this.sequence = Optional.of(sequence);
    }
    /**
     * Get the STATUS line of the event
     * @return the STATUS line of the event
     */
    public Optional<StatusType> getStatus() {
        return status;
    }
    /**
     * Set the the STATUS line of the event
     * @param status the STATUS line
     */
    public void setStatus(StatusType status) {
        this.status = Optional.of(status);
    }
    /**
     * Get the SUMMARY line of the event
     * @return the SUMMARY line of the event
     */
    public Optional<String> getSummary() {
        return summary;
    }
    /**
     * Set the SUMMARY line of the event
     * @param summary the SUMMARY line of the event
     */
    public void setSummary(String summary) {
        this.summary = Optional.of(summary);
    }

    /**
     * Get the CREATED line.
     * @return the CREATED line
     */
    public Optional<String> getCreatedDate() {
        return createdDate;
    }

    /**
     * Sets the CREATED line. The value is converted to UTC from the supplied parameters.
     * 
     * @param createdOn The creation date
     * @param timeZone Time zone
     */
    public void setCreatedDate(LocalDateTime createdOn, TimeZone timeZone) {
        this.createdDate = Optional.of(Util.convertAndFormatUTC(createdOn, timeZone));
    }

    /**
     * Get the DTSTAMP line
     * @return the DTSTAMP line
     */
    public String getDateTimeStamp() {
        return dateTimeStamp;
    }

    /**
     * Sets the DTSTAMP line
     * @param dateTimeStamp the DTSTAMP line
     */
    public void setDateTimeStamp(String dateTimeStamp) {
        this.dateTimeStamp = dateTimeStamp;
    }

    /**
     * Add a new ATTENDEE line.
     * @param name Name of attendee
     * @param email Email of attendee
     */
    public void addAttendee(String name, String email) {
        attendeeList.add(String.format("CN=%s:mailto:%s",
            name, email));
    }

    /**
     * Add a new ATTENDEE line.
     * @param email Email of attendee
     */
    public void addAttendee(String email) {
        attendeeList.add(String.format("CN=%s:mailto:%s",
            email, email));
    }

    /**
     * Get a list of all ATTENDEE lines
     * @return a list of all ATTENDEE lines
     */
    public List<String> getAttendeeList() {
        return attendeeList;
    }

    /**
     * Sets a list of all ATTENDEE lines
     * @param attendeeList a list of all ATTENDEE lines
     */
    public void setAttendeeList(List<String> attendeeList) {
        this.attendeeList = attendeeList;
    }

    /**
     * Get the FREQ of a RRULE
     * @return the FREQ of a RRULE
     */
    public Optional<FrequencyType> getRepeatFrequency() {
        return repeatFrequency;
    }

    /**
     * Sets the FREQ of a RRULE
     * @param repeatFrequency the FREQ of a RRULE
     */
    public void setRepeatFrequency(FrequencyType repeatFrequency) {
        this.repeatFrequency = Optional.of(repeatFrequency);
    }

    /**
     * Get the UNTIL of a RRULE
     * @return the UNTIL of a RRULE
     */
    public Optional<LocalDateTime> getRepeatUntil() {
        return repeatUntil;
    }

    /**
     * Sets the UNTIL of a RRULE. It is converted to UTC from the supplied parameter.
     * @param until The date and time
     * @param tz The time zone
     */
    public void setRepeatUntil(LocalDateTime until, TimeZone tz) {
        this.repeatUntil = Optional.of(Util.toUTC(until, tz));
    }

    /**
     * Sets the UNTIL of a RRULE.
     * @param untilUTC The date and time in UTC
     */
    public void setRepeatUntil(LocalDateTime untilUTC) {
        this.repeatUntil = Optional.of(untilUTC);
    }

    /**
     * Get the repeat interval of a RRULE
     * @return the repeat interval of a RRULE
     */
    public Optional<Integer> getRepeatInterval() {
        return repeatInterval;
    }

    /**
     * Sets the repeat interval of a RRULE
     * @param repeatInterval the repeat interval of a RRULE
     */
    public void setRepeatInterval(int repeatInterval) {
        this.repeatInterval = Optional.of(repeatInterval);
    }

    /**
     * Get the repeat count of a RRULE
     * @return the repeat count of a RRULE
     */
    public Optional<Integer> getRepeatCount() {
        return repeatCount;
    }

    /**
     * Sets the repeat count of a RRULE
     * @param repeatCount the repeat count of a RRULE
     */
    public void setRepeatCount(int repeatCount) {
        this.repeatCount = Optional.of(repeatCount);
    }

    /**
     * Sets the repeat count of a RRULE
     * @param repeatCount the repeat count of a RRULE
     */
    public void setRepeatCount(Optional<Integer> repeatCount) {
        this.repeatCount = repeatCount;
    }

    /**
     * Get the LOCATION line
     * @return the LOCATION line
     */
    public Optional<String> getLocation() {
        return location;
    }

    /**
     * Set the LOCATION line
     * @param location the LOCATION line
     */
    public void setLocation(String location) {
        this.location = Optional.of(location);
    }

    /**
     * Get the URL associated with the LOCATION
     * @return the URL associated with the LOCATION
     */
    public Optional<String> getLocationURL() {
        return locationURL;
    }

    /**
     * Set the URL associated with the LOCATION
     * @param locationURL the URL associated with the LOCATION
     */
    public void setLocationURL(String locationURL) {
        this.locationURL = Optional.of(locationURL);
    }

    /**
     * Writes a VEVENT section to the StringBuilder
     * @param sb the StringBuilder
     */
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

    /**
     * Returns a builder that makes it a litle easier to construct a VEvent.
     * @return a builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A builder class that makes it a litle easier to construct a VEvent.
     */
    public static class Builder {
        private VEvent event = new VEvent();

        /**
         * Sets the organizer of the event. This field is mandatory.
         * 
         * @param organizerEmail The email address of the organizer.
         * @return a builder
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
         * @return a builder
         */
        public Builder organizer(String name, String email) {
            event.setOrganizer(name, email);

            return this;
        }

        /**
         * Sets a detailed description of the event.
         * 
         * @param description a detailed description of the event
         * @return a builder
         */
        public Builder description(String description) {
            event.setDescription(description);

            return this;
        }

        /**
         * Sets the globally unique ID of the event. This field is mandatory.
         * 
         * @param uid the globally unique ID of the event
         * @return a builder
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
         * @return a builder
         */
        public Builder starts(LocalDateTime startDateTime, TimeZone timeZone) {
            event.setStartDateTime(startDateTime, timeZone);

            return this;
        }
        /**
         * Sets the start date and time of the event using a time zone ID defined in the VCalendar.
         * @param startDateTime the start date and time
         * @param timeZoneId the time zone ID as defined in the VCalendar
         * @return a builder
         */
        public Builder starts(LocalDateTime startDateTime, String timeZoneId) {
            event.setStartDateTime(startDateTime, timeZoneId);

            return this;
        }
        /**
         * Sets the start date and time of the event using an Outlook time zone ID.
         * @param startDateTime the start date and time
         * @param tz the time zone ID as defined in the VCalendar
         * @return a builder
         */
        public Builder starts(LocalDateTime startDateTime, OutlookTimeZone tz) {
            event.setStartDateTime(startDateTime, tz.toString());

            return this;
        }

        /**
         * Sets the start date of the event. Use this to create a day long event.
         * Please note that iCalendar does not take a time zone for day long event.
         * 
         * @param startDate The start date of a day long event.
         * @return a builder
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
         * @return a builder
         */
        public Builder ends(LocalDateTime endDateTime, TimeZone timeZone) {
            event.setEndDateTime(endDateTime, timeZone);

            return this;
        }
        /**
         * Sets the end date and time of an event using a time zone ID defined in the VCalendar.
         * @param endDateTime the end date and time
         * @param timeZoneId the time zone ID defined in the VCalendar
         * @return a builder
         */
        public Builder ends(LocalDateTime endDateTime, String timeZoneId) {
            event.setEndDateTime(endDateTime, timeZoneId);

            return this;
        }

        /**
         * Sets the end date and time of an event using an Outlook time zone ID.
         * @param endDateTime the end date and time
         * @param tz the time zone ID defined in the VCalendar
         * @return a builder
         */
        public Builder ends(LocalDateTime endDateTime, OutlookTimeZone tz) {
            event.setEndDateTime(endDateTime, tz.toString());

            return this;
        }
        /**
         * Sets the end date of a day long event. This date is exclusive.
         * For example, if the last day of the event is Nov 2, 2022, then
         * you should set this date to Nov 3, 2022.
         * 
         * @param endDate The end date
         * @return a builder
         */
        public Builder ends(LocalDate endDate) {
            event.setEndDate(endDate);

            return this;
        }

        /**
         * The status of the event. Default is StatusType.CONFIRMED.
         * 
         * @param status The status of the event
         * @return a builder
         */
        public Builder status(StatusType status) {
            event.setStatus(status);

            return this;
        }

        /**
         * Set a short summary for the event. 
         * 
         * @param summary a short summary for the event
         * @return a builder
         */
        public Builder summary(String summary) {
            event.setSummary(summary);

            return this;
        }

        /**
         * Set when this event was created in your backend system.
         * 
         * @param createdOn The date and time the event was created on
         * @param timeZone Time zone
         * @return a builder
         */
        public Builder createdOn(LocalDateTime createdOn, TimeZone timeZone) {
            event.setCreatedDate(createdOn, timeZone);

            return this;
        }

        /**
         * Add a new attendee to the event.
         * 
         * @param name the name of the attendee
         * @param email the email of the attendee
         * @return a builder
         */
        public Builder attendee(String name, String email) {
            event.addAttendee(name, email);

            return this;
        }

        /**
         * Add a new attendee to the event.
         * 
         * @param email the email of the attendee
         * @return a builder
         */
        public Builder attendee(String email) {
            event.addAttendee(email);

            return this;
        }

        /**
         * Sets the repeat frequency of the event.
         * 
         * @param repeatFrequency the repeat frequency
         * @return a builder
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
         * @return a builder
         */
        public Builder until(LocalDateTime until, TimeZone tz) {
            event.setRepeatUntil(until, tz);

            return this;
        }

        /**
         * Sets how long the event will keep repeating. The event will no longer
         * repeat after this date and time. The date and time is specified in UTC.
         * 
         * @param untilUTC The date and time in UTC
         * @return a builder
         */
        public Builder until(LocalDateTime untilUTC) {
            event.setRepeatUntil(untilUTC);

            return this;
        }

        /**
         * Sets the repeat interval.
         * 
         * @param repeatInterval the repeat interval
         * @return a builder
         */
        public Builder repeatInterval(int repeatInterval) {
            event.setRepeatInterval(repeatInterval);

            return this;
        }

        /**
         * How many times the event will repeat. Either set this or the until date but not both.
         * 
         * @param repeatCount The repeat count
         * @return a builder
         */
        public Builder repeatCount(int repeatCount) {
            event.setRepeatCount(repeatCount);

            return this;
        }

        /**
         * Set the location information.
         * 
         * @param location The name of the location. For example an address.
         * @return a builder
         */
        public Builder location(String location) {
            event.setLocation(location);

            return this;
        }

        /**
         * Sets the location name and a link URL.
         * @param location The location name
         * @param locationURL A URL that has more information about the location. For example, a Google Maps link.
         * @return a builder
         */
        public Builder location(String location, String locationURL) {
            event.setLocation(location);
            event.setLocationURL(locationURL);

            return this;
        }

        /**
         * Returns a fully constructed and initialized event.
         * 
         * @return the VEvent object.
         */
        public VEvent build() {
            return event;
        }
    }
}
