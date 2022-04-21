# jcal a iCalendar Invite Generator for Java

[iCalendar](https://www.ietf.org/rfc/rfc2445.txt) is a comprehensive specififcation that can be difficult to understand at times. It also has some strange requirements like line length limit that seem archaic by today's standard. Generating a basic iCalendar invite can become a daunting task. This is why we created ``jcal``. It makes it super easy to generate an invite that you can then send via e-mail.

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
}
```

## Using jcal

Add a dependency to your ``pom.xml``.

```xml

```

## Cookbook
jcal was designed to achieve common scenarios easily. Below are some of these scenarios.

### Invite Attendees

Use the ``attendee()`` method to add attendees to an event.

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
To create a day long event supply ``LocalDate`` to ``starts()`` and ``ends()``. Note: icalendar does not take a time zone reference for day long events.

The following will create a two day long event for Nov 11, 2022 and Nov 12, 2022.

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