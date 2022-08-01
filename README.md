# iCalendar Invite Generator for Java

[iCalendar](https://www.ietf.org/rfc/rfc2445.txt) is a comprehensive specification that can be difficult to understand at times. It also has a few strange requirements like line length limit that seem archaic by today's standard. On top of that, email clients like Gmail and Outlook are unforgiving when it comes to parsing an iCalendar request. Generating a correct iCalendar invite can become a daunting task. This is why we created ``jcal``. This package makes it super easy to generate an invite that you can then send via e-mail.

## Project Goals

If all you need to do is generate a iCalendar document and send an invitation then jcal should do the work for you. Our goal is to make it easy to achieve 80% of the most common scenarios. For more complicated needs you should consider using [iCal4J](https://www.ical4j.org/). 

Here are a few advantages of jcal.

1. The project has zero dependency and small code base. 
2. It uses modern ``java.time.*`` classes for time formatting and conversion.

## Quick Example

This will create an event for Nov 2, 2022 from 9:30AM to 10:00AM Eastern Time.

```java
import com.webage.jcal.*;

public void basicEvent() {
    var tz = TimeZone.getTimeZone("America/New_York");
    var ev = VEvent
        .builder()
        .uid("uid-1")
        .organizer("abc", "xyz@example.com")
        .starts(LocalDateTime.of(2022, 11, 2, 9, 30), tz)
        .ends(LocalDateTime.of(2022, 11, 2, 10, 0), tz)
        .summary("Test event")
        .build();

    var iCal = VCalendar.builder()
        .event(ev)
        .build()
        .toString();
    
    System.out.println(iCal);
}
```

This will output:

```
VERSION:2.0
CALSCALE:GREGORIAN
METHOD:REQUEST
BEGIN:VEVENT
DTSTART;TZID=America/New_York:20221102T093000
DTEND;TZID=America/New_York:20221102T100000
DTSTAMP:20220421T191400Z
ORGANIZER;CN=abc:mailto:xyz@example.com
UID:uid-1
STATUS:CONFIRMED
SUMMARY:Test event
END:VEVENT
END:VCALENDAR
```

Note: iCalendar requires each line to end with "\r\n" (CRLF). jcal will do this for you. But care must be taken not to alter the line endings when attaching the calendar in an email. To be safe always base64 encode the mail attachment. 

## Using jcal

Add a dependency to your ``pom.xml``.

```xml
<dependency>
  <groupId>io.github.bibhas2</groupId>
  <artifactId>jcal</artifactId>
  <version>1.1.0</version>
</dependency>

```

## Cookbook
jcal was designed to achieve common scenarios easily. Below are some of these scenarios.

### Mandatory Fields
The following fields of an event are mandatory.

- UID - A globally unique ID for the event.
- Organizer - You can supply a name and email. An RSVP from a invitee is emailed back to this address.
- Start date and time. While this is mandatory, the end date and time is not. If you do not supply an end date and time then a reminder or task type event is created that does not occupy any space in the calendar.

Although, summary is not a mandatory property, it is highly recommended that you supply a summary. This short title shows up in the recipient's calendar.

### Add Attendees to an Event

Use the ``attendee()`` method of the builder to add attendees to an event.

```java
import com.webage.jcal.*;

public void eventWithAttendees() {
    var tz = TimeZone.getTimeZone("America/New_York");
    var ev = VEvent
        .builder()
        .uid("uid-1")
        .organizer("abc", "xyz@example.com")
        .starts(LocalDateTime.of(2022, 11, 2, 9, 30), tz)
        .ends(LocalDateTime.of(2022, 11, 2, 10, 0), tz)
        .summary("Test event")
        .attendee("Bugs Bunny", "bugs.bunny@wb.com")
        .attendee("daffy.duck@wb.com")
        .build();

    var iCal = VCalendar.builder()
        .event(ev)
        .build()
        .toString();
}
```

### Day Long Event
To create a day long event supply ``LocalDate`` (instead of ``LocaldateTime``) to the ``starts()`` and ``ends()`` methods. Note: iCalendar does not take a time zone reference for day long events.

The following will create a two day long event for:

- Nov 2, 2022 and 
- Nov 3, 2022.

```java
public void dayLongEvent() {
    var startDate = LocalDate.of(2022, 11, 2);
    var endDate = startDate.plusDays(2);

    var ev = VEvent
        .builder()
        .uid("uid-1")
        .organizer("abc", "xyz@example.com")
        .starts(startDate)
        .ends(endDate)
        .summary("Test event")
        .build();

    var iCal = VCalendar.builder()
        .event(ev)
        .build()
        .toString();
}
```

Note, how the end date is actually Nov 4, 2022. Basically, the event ends at the beginning of Nov 4.

### Multiple Events
The following adds two events:

- Nov 2, 2022 from 9:30AM to 10:00AM Eastern Time.
- Nov 5, 2022 from 4:30PM to 5:00PM Eastern time.

```java
public void multipleEvents() {
    var tz = TimeZone.getTimeZone("America/New_York");
    var ev1 = VEvent
        .builder()
        .uid("uid-1")
        .organizer("abc", "xyz@example.com")
        .starts(LocalDateTime.of(2022, 11, 2, 9, 30), tz)
        .ends(LocalDateTime.of(2022, 11, 2, 10, 0), tz)
        .summary("Test event 1")
        .build();
    var ev2 = VEvent
        .builder()
        .uid("uid-2")
        .organizer("abc", "xyz@example.com")
        .starts(LocalDateTime.of(2022, 11, 5, 16, 30), tz)
        .ends(LocalDateTime.of(2022, 11, 5, 17, 0), tz)
        .summary("Test event 2")
        .build();

    var iCal = VCalendar.builder()
        .event(ev1)
        .event(ev2)
        .build()
        .toString();
}
```

### Daily Repeating Event
The following will create an event that repeats 3 days in a row.

- April 24, 2022 from 9:00AM to 9:30AM Eastern Time.
- April 25, 2022 from 9:00AM to 9:30AM Eastern Time.
- April 26, 2022 from 9:00AM to 9:30AM Eastern Time.

Notice how the start and end date/time are for the first day only.

```java
var tz = TimeZone.getTimeZone("America/New_York");
var ev = VEvent
    .builder()
    .uid("uid-4")
    .organizer("abc", "xyz@example.com")
    .starts(LocalDateTime.of(2022, 4, 24, 9, 0), tz)
    .ends(LocalDateTime.of(2022, 4, 24, 9, 30), tz)
    .repeats(FrequencyType.DAILY)
    .repeatCount(3)
    .summary("Test event")
    .build();
```

The same can be achieved by supplying a repeat until date. In the example above, we should set the repeat until date to the beginning of April 27th, 2022.

```java
var tz = TimeZone.getTimeZone("America/New_York");
var ev = VEvent
    .builder()
    .uid("uid-4")
    .organizer("abc", "xyz@example.com")
    .starts(LocalDateTime.of(2022, 4, 24, 9, 0), tz)
    .ends(LocalDateTime.of(2022, 4, 24, 9, 30), tz)
    .repeats(FrequencyType.DAILY)
    .until(LocalDateTime.of(2022, 4, 27, 0, 0), tz)
    .summary("Test event")
    .build();
```

Note: iCalendar takes the until date and time in UTC only. Here jcal will correctly convert the time from the given time zone to UTC.

### Set Location
You can supply a name or address of a location like this.

```java
var tz = TimeZone.getTimeZone("America/New_York");
var ev = VEvent
    .builder()
    .uid("uid-4")
    .organizer("abc", "xyz@example.com")
    .starts(LocalDateTime.of(2022, 4, 24, 9, 0), tz)
    .ends(LocalDateTime.of(2022, 4, 24, 9, 30), tz)
    .summary("Test event")
    .location("First St SE, Washington, DC 20004")
    .build();
```

Optionally, you can supply a URL link that has more information about the location. For example, a Google Maps link.

```java
var tz = TimeZone.getTimeZone("America/New_York");
var ev = VEvent
    .builder()
    .uid("uid-4")
    .organizer("abc", "xyz@example.com")
    .starts(LocalDateTime.of(2022, 4, 24, 9, 0), tz)
    .ends(LocalDateTime.of(2022, 4, 24, 9, 30), tz)
    .summary("Test event")
    .location("First St SE, Washington, DC 20004", "https://goo.gl/maps/MzhntySdbstb7Yfd8")
    .build();
```