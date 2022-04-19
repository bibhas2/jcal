# jcal a iCalendar Invite Generator for Java

[iCalendar](https://www.ietf.org/rfc/rfc2445.txt) is a comprehensive specififcation that can be difficult to understand at times. It also has some strange requirements like line length limit that seem archaic by today's standard. Generating a basic iCalendar invite can become a daunting task. This is why we created ``jcal``. It makes it super easy to generate an invite that you can then send via e-mail.

## Project Goals

If all you need to do is generate a iCalendar document and send an invitation then jcal should do the work for you. Our goal is to make it easy to achieve 80% of the most common scenarios. For more complicated needs you should consider using [iCal4J](https://www.ical4j.org/). 

Here are a few advantages of jcal.

1. The project has zero dependency and small code base. 
2. It uses modern ``java.time.*`` classes for time formatting and conversion.

