## Microservices for Architects
#### A course by Raj   raj@acloudfan.com
#### Release date: May 1st, 2021

**Branch:** saga

#### Test SAGA
* MUST setup the Kafka configuration in 
**com.acme.infra.kafka.KafkaConfiguration** class

* MUST setup the MongoDB parameters in
**com.acme.infra.mongodb.MongoDBBase** class

#### SAGA Simulation Class
**com.acme.saga.util.ServiceSimulator**
* Simulates an external service call
* Introduces delays by a random duration
* Set the chance of success 

#### Testing steps
Assumption: You have setup the cloud Karafka cluster
1. Setup topic : bookingsaga
2. Use kafka browser to observe consumer messages
3. Launch the services. These services listen on topics and processes topics of interest and ignores certain topics
   - BookingSaga
   - PaymentService
   - ReservationService
4. Run the test with different booking_id
   **com.acme.simulation.saga.RunSagaTest**

