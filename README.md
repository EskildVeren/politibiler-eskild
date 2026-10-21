# Politibiler

Due to the context of this task, this README assumes the reader knows Norwegian. 

## Getting started

## Technology

The system uses Spring Boot and is coded in Java. Java with Spring Boot is chosen, as it is my preferred way of building backend systems. The database of choice is SQLite. This was due to the simple nature of the database, and that there was little work to set it up. In a production scenario I would probably not use SQLite, but I think it is perfect for this case. 

## AI Usage

Claude has been used for assitance to build this project. I asked for help when setting up the project and met confusing errors. I also needed some help to get started with the testing, as I was unsure about the syntax. 

## Assumptions

These are the assumptions made when creating the project

### Choosing status when assigning a mission

Looking at the dummy data, the same mission (oppdrag) does not always have the same status. Two of the cars has mission "Trafikkulykke", but one has "På vei til hendelse" as status, while the other has "I oppdrag". Another observation is that many cars has one of two common statuses, "Tilgjengelig" or "I oppdrag". I will therefore use "I oppdrag" as the default status when assigning a mission. The system should also allow the API user to assign a custom status, though that will be optional. That is unless the assigned mission is an empty string, in which case I will assign the car "Tilgjengelig" as the default status. 

### Handling language

The code for the repository will mainly be in english, though for properties in the PoliceCar and PoliceCarResponse classes are in Norwegian. This is because I wanted to match the original format of the data for consistency reasons. It would be confusing if the police cars are modelled in Norwegian in most systems, while being in English in this system.

### Usage of Enums

Enums was not used, as I lack the domain knowledge to create them. Though I think that fields such as status, oppdrag, modell and merke could use them. 

### ID will always be a numeric ID

This is because it matches the data from the API endpoint, and makes the system easier to develop. This is useful for the chosen method of sorting ids. 

### Oppdrag cannot be null

I made this assumption due to there being no occurance of this in the dummy data. That decision could change if I had more domain knowledge or knowledge of the system. I did not set up a lot of time philosophizing about this due to this just being a short task. 

## Folder structure

I like to grow the folder structure naturally over time. Since the project does not have a lot of classes, I have not created a lot of folders. If I had more of one type of class, I would have created more folders. That can be seen in the model folder, which contains three classes of a similar purpose. 

## Testing

The application uses unit testing with Junit, with the components being mocked using Mockito. The Httpclient tool [Httpiness](https://github.com/bognikol/httpiness) was used for this purpose, and the file containing the requests is in [httpclient-testing.json](httpclient-testing.json). The [cors-test](cors-test) frontend is there to test that the CORS configuration was correctly set up. It tests that the website is able to make a request to get all available police cars. It is 100% ai generated, as I found writing it out of scope regarding what I should spend time on. 

## Docs

Some more documentation can be found in the [docs folder](/docs). The most interesting file is probably the [file](/docs/before-coding.md) describing the work and thoughts done before coding started