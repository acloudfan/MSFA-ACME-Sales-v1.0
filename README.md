## Microservices for Architects
#### A course by Raj   raj@acloudfan.com
#### Release date: May 1st, 2021

**Branch:** events


#### Free AMQP/RabbitMQ Broker instance
https://www.cloudamqp.com/

#### UML for Events Framework
/uml/util.events

#### Static class Broker Patterns
com.acme.sales.model.tests.event.staticbroker.MainTest

#### Main Test class - Payment Received Events processing
com.acme.sales.model.tests.event.audit.PaymentAuditTest

#### Main Test class - Payment Audit Event
com.acme.sales.model.tests.event.audit.PaymentAuditTest

#### Model Messaging classes in package
com.acme.sales.model.utils.event.messaging

#### Rabbit MQ Implementation classes
com.acme.infra.rabbitmq.PubSubService

* Test class with main()
com.acme.infra.rabbitmq.PubSubServiceTest

#### Integration Events Test
Required an update to the FakeRepo for BookingConfirmation

com.acme.sales.model.tests.fake.repo.BookingConfirmationRepoFake

This class now emits an integration event