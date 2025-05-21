# Assignment - CBC Senior Mobile Developer
This is a take-home assignment which will help us get a better understanding of how you approach a cross-platform development task. We will 
look at your architectural decisions, algorithmic design, coding style and overall problem solving skills.

## Project
A private GitHub repository has been created for you with the shell of a KMP project already set up.  We would like you to create a KMP module that 
provides the business logic and User Interface for a component that displays news content information that is fetched from the supplied 
endpoint.  You will need to set up an Android and iOS app that uses this module as well.  The base structure needed to complete all 
3 pieces is present in the repository.

### Part One: The Compose Multiplatform (CMP) module
Create a module that provides a component used to display a list of formatted news story items.  This component must be able to be used by both iOS 
and Android native mobile applications. It is not a standalone app, it is a component that can be integrated in an app. You can be creative in how 
the component looks and is implemented within the constraints given below.  Each item listed is required for the assessment to be considered complete.
- UI is created with Compose Multiplatform.  
- Story data is read from this endpoint: https://cbcmusic.github.io/assessment-tmp/data/data.json.
- Results are displayed in a list. You can style each list item’s user interface however you want, but each item must include a headline, an image and the date it was published.
- Allow users to paginate through loaded content.
- Page size is configurable.
- If the app goes offline after having loaded data, it can still show the previously loaded results.
- Include a minimum of 3 unit tests.

### Part Two: The apps
The repository already has the base structure set up for an Android and iOS application.  Complete the implementation of each of these so that it 
makes use of the module you have created in Part One.

### Part Three: The Documentation
Create a document that you could use to onboard a developer who is new to your project. Highlight the architectural choices you made and why, 
any other important technical insights and any instructions needed to use your component. It can be a short 500 word readme.md document, an 
annotated visual diagram or something else entirely, depending on your preferred documentation style.  If you wish to do a readme.md document, you are welcome
to overwrite this project description.  You have been sent a PDF version of this information as well.

***Important Rules:***
- *You and you alone should complete your submission.*
- *Refrain from using any official CBC branding in your application.*
- *Do not share your work with anyone outside of CBC.*

## Submission
If you have any questions or concerns about the requirements, rules or submission, please reach out to our Talent Acquisition Specialist ***Marium Qadir*** 
(marium.qadir@cbc.ca) and the hiring Senior Engineering Manager ***Kristen Elliott*** (kristen.elliott@cbc.ca).

Once you are finished, please inform Marium and Kristen via email.

