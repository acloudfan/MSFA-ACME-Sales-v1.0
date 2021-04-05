## Microservices for Architects
#### A course by Raj   raj@acloudfan.com
#### Release date: May 1st, 2021

**Branch:** data


#### PostgreSQL
https://www.elephantsql.com/
This is where we will create an instance of the PostgreSQL instance for ACME tests


#### PostreSQL Infra class
**com.acme.infra.postgresql.JDBCBase**
* This is where the parameters for the JDBC connections need to be set
* Copy the URL | User | Password page from the details page for yuor DB instance
* Test the setup before proceeding forward
  - By executing the class**com.acme.sales.model.tests.repo.JDBCRepoTest**

* IF this is NOT setup correctly you will get errors from PostgreSQL

#### Setup MongoDB Instance
https://cloud.mongodb.com/
* This is where we will create an instance of MongoDB

**com.acme.infra.mongodb.MongoDBBase**
* This is the class in which you need to setup the MongoDB parameter
* Test the setup before proceeding forward
  - By executing the class**com.acme.sales.model.tests.repo.MongoDBRepoTest**

* IF this is NOT setup correctly you will get errors from MongoDB

#### CQS Pattern Exercise - v1

* Must have the PostgreSQL DB setup

**com.acme.sales.model.tests.cqrs.v1.TestCommand** 
is the main class for testing the v1 Command implementation

**com.acme.sales.model.tests.cqrs.v1.TestQuery** 
is the main class for testing the v1 Query implementation

#### CQRS Pattern Exercise - v2
* Must have the PostgreSQL DB setup 
* Must have the MongoDB setup
* Must have the RabbitMQ setup as described below:


##### Setup Rabbit MQ

1. Create an exchange with the name **acme.sales.topic**
2. Create a Queue with the name **proposal.reader.queue*
3. Setup the binding in the exchange
   * To Queue = **proposal.reader.queue**
   * Binding key = **proposal.update**


**com.acme.sales.model.tests.cqrs.v2.TestCommand** 
is the main class for testing the v2 Command implementation. Executing this class will generate and event will show up in Rabbit MQ

* MUST setup the RabbitMQ URL in the class before executing it
  **com.acme.sales.model.cqrs.v2.command.CreateProposalCommand**




**com.acme.sales.model.tests.cqrs.v2.TestSubscriber**
* MUST setup the RabbitMQ URL in this class - before executing
* This class listens for messages - on receiving an event it populates the MongoDB
 

**com.acme.sales.model.tests.cqrs.v2.TestQuery** 
* This class pulls data from the MongoDB instance; this data is 
populated by the subscriber


#### Reliable Messaging Pattern - CQRS v3
**com.acme.sales.model.tests.cqrs.v3.TestCommand** 
* Writes to the JDBC DB and reliably publishes a message
* Manages the events to be published in a table


**com.acme.sales.model.tests.cqrs.v3.TestSubscriber**
* Receieves the message and updates DB 
* Checks for DB
 