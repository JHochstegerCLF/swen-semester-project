# Media Ratings Platform Protocol

Link to [Github](https://github.com/JHochstegerCLF/n-semester-project)

## Structure

The Files are separated into 5 overaching folders:

* Presentation (API definition and Routing Logic)
* Service (Logic of the application -> verification, conversion, filtering)
* Persistence (Concerned with saving of Data -> currently just a List but later DB)
* Models (All Data-objects and Enums)
* Converter (Services to serialize/deserialize)

### Presentation

The Http Server is created in Server.java there the handlers for:

* `/api/users`
* `/api/media`
* `/api/ratings`

are registered.

There are 3 different handlers:
* UserHandler
* MediaHandler
* RatingHandler

all extending the BaseHandler with holds the logic for routing to the correct method in the 3 handlers.
The handlers itself only implement the Methods and annotate them According to the HttpMethod and endpoint to which they belong to.

Some helper classes like Context or Response where also created to simplify the transfer of data between layers.

### Service

The Service Layer holds the main business logic for the Application. anytime something needs to be validated or modified before use it happpens in the Service layer. 

### Persistence

The Persistence Layer has the single responsibility to save or retrieve data. Modification or conversion should be done in the Service layer.

### Models

### Converter

The Converter currently only holds one Mapper for Json but could be expanded to allow for mapping to XML and others.

## Decisions

Due to my prior experience with SpringBoot the decision was made to try and emulate the style of this framework since it provides a simple and modifiable way of defining rest apis.

For this I looked into Annotations and created my own for the different HttpMethods and the Authentication.
The BaseHandler which all other Handlers should extend handles the logic for routing and Authentication based on the aforementioned Annotations using reflections.
This also gives the option to output the api calls and which function was called for this endpoint for easy debugging.

Another important feature I wanted to use was Dependency Injection (DI). This would simplify the Process of accessing other Classes/Layers and
would also allow me to simply provide Singletons for the Repositories due to the fact that they currently use a List saved in the Repository Instance.

With these design decisions the process of implementing different endpoints is very simple since you don't have to think about Passing instances of services and a giant switch case for the different endpoints is also not needed. 

## Class Diagram

![Class Diagram](diagram.png)