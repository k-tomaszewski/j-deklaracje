# j-deklaracje
The project was planned as a Java tool for submitting Polish tax declarations into official e-Deklaracje system. 
Polish government published specification of a document submitting interface that can be used by any external system to submit Polish tax
declarations on behalf of a tax payer.

**As of 2023 this is still not ready and ended rather as certificate fetching and verification use case.**

## Goal
The basic goal is to provide an open source, Java-based software that can submit an annual tax declaration into 
the information system of the Polish Ministry of Finance given that:
- one has XML file representing a tax declaration
- one is going to authenticate using the amount of income from previous year tax declaration (so no digital signature required)

The software must be able to fetch official submitting confirmation document "UPO".

## Documentation
* Basic specification: [https://www.podatki.gov.pl/e-deklaracje/dokumentacja-it/]

## Technologies
- Java 11
- Apache CXF, cxf-codegen-plugin 
- Maven
