# Clean Architecture Study

Study on clean architecture based on micro-services and events queue.

This study has two main goals:

- provide an example of a clean architecture implementation, based on the Ports and Adapters pattern.
- provide an example of sharing data asynchronously between two services via a message broker like RabbitMq.

## Ports and Adapters architecture

This type of architecture is also known as [Hexagonal Architecture](https://codesoapbox.dev/ports-adapters-aka-hexagonal-architecture-explained/)

Given the fact that this is a very simple example, it is understandable to ask why make such a complicated architecture for some simple CRUD services.

However, this is an example on how to start designing larger, more complex applications.

The main benefit of a clean architecture is that the domain business logic has no dependencies on the other modules. This allows for code that is easier to maintain and also migrate to other technologies or frameworks, or split into microservices,

## Event sourcing architecture

This type of architecture allows sharing data asynchronously between applications. This is done via events published on a message broker like RabbitMq. It is a publish-subscribe mechanism, where we have a producer and one or many consumers of the events.

Each time there is an update on the service that owns the data, an event is published via the message broker. All the application that listen to the topic or exchange for that event type will receive the message and update their copy of the data.

This kind of approach allows loose coupling between applications or services, which make them more resilient in case the other services are down for whatever reason.

However, it does come with the drawback of potentially stale data for a short duration of time. That is why this approach should be used with data that doesn't change very fast.

This approach also implements the [Outbox Pattern](https://dzone.com/articles/implementing-the-outbox-pattern), which prevents the loss of events in case of communication problems with the queue.

## Service 1 - Currency exchange

Exposes endpoints for CRUD operations on exchange rates.

### Build

```shell
docker build --tag currency-exchange ./currency-exchange
docker-compose up
```

### Test

Execute the requests from postman folder collection, or:

- Create: `POST` at [localhost:8081/exchange-rate/RON/EUR/4.91](localhost:8081/exchange-rate/RON/EUR/4.9)
- Read: `GET` at [localhost:8081/exchange-rate/RON/EUR](localhost:8081/exchange-rate/RON/EUR)
- Update: `PUT` at [localhost:8081/exchange-rate/RON/EUR/4.92](localhost:8081/exchange-rate/EUR/USD/1.13)
- Delete: `DELETE` at [localhost:8081/exchange-rate/RON/EUR](localhost:8081/exchange-rate/RON/EUR)
