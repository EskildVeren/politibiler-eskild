# Politibiler

A great introduction

Due to the context of this task, this README assumes the reader knows Norwegian. 

## Getting started

## AI Usage

## Assumptions

### Choosing status when assigning a mission

Looking at the dummy data, the same mission (oppdrag) does not always have the same status. Two of the cars has mission "Trafikkulykke", but one has "På vei til hendelse" as status, while the other has "I oppdrag". Another observation is that many cars has one of two common statuses, "Tilgjengelig" or "I oppdrag". I will therefore use "I oppdrag" as the default status when assigning a mission. The system should also allow the API user to assign a custom status, though that will be optional. That is unless the assigned mission is an empty string, in which case I will assign the car "Tilgjengelig" as the default status. 

### Handling language

The code for the repository will mainly be in english, though for properties in the PoliceCar and PoliceCarResponse classes are in Norwegian. This is because I wanted to match the original format of the data for consistency reasons. It would be confusing if the police cars are modelled in Norwegian in most systems, while being in English in this system.

### Folder structure

### Usage of Enums

Enums was not used, as I lack the domain knowledge to create them. Though I think that fields such as status, oppdrag, modell and merke could use them. 

### ID will always be a numeric ID

This is because it matches the data from the API endpoint, and makes the system easier to develop. This is useful for the chosen method of sorting ids. 

## Technology

## Docs