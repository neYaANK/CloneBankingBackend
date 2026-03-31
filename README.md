# CloneBanking Backend

API application that provides core backend functionality for a web-banking like making payments, opening and closing cards, logging in as a user, different currencies and loans are implemented with JWT and 2FA (based on SMS via Twilio).  

## How It's Made:

**Tech used:** Java, Spring Boot, Hibernate, MySQL/H2, Twilio API

The development started from designing a proper DB scheme. Then the log in endpoint was implemented with the 2FA. Firstly, I went for a TOTP 2FA, but later I changed it to SMS 2FA with the help of Twilio as it was more comfortable for the end user. Then the main part was implemented with the loans, cards, payments, where one of the challenges was the modification of the database scheme due to unexpected use case. Keeping track of loans also was the challenge as I had to learn about async 

## Lessons Learned:

This is my first somewhat big Spring Boot project and looking back at it I can say, that I should have went with the TDD as it could speed up the development a lot - instead of doing manual testing each time, I could just run a test suite and shorten feedback time. 
Also, it would be good to have Javadocs as after a while it's hard to remember what's happening at some parts of the code.
Learning about separation of concerns improved my understanding too and now I know at which layers should I distribute different stuff like validation, business logic and persistance.
More descriptive commits would be also nice to have.
