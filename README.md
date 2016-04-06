# Using Kafka for Multiple Persistence Sink & Eventual Consistency

### Installing and starting Kafka (Instructions for OSX only :))

[Apache Kafka] (http://kafka.apache.org)

Exec the following command in your bash or zsh terminal

```
brew install kafka
```

This will also install all dependencies, like [Zookeeper] (https://zookeeper.apache.org) which is required to run the server.

Clone this project and then jump into it.  The following commands are all from the project workdir.

```
zookeeper-server-start config/zookeeper.properties
```

This will start zookeeper.

```
kafka-server-start config/server.properties
```

This will start a single node Kafka cluster, watch those logs fly!

There's a spring boot app which contains both of the data sinks for the consumers to publish to which can be started by

```
mvn clean package spring-boot:run
```

There's a very simple web based viewer for the contents of the two store which can be access via

```
http://localhost:8080
```

You'll need to click the `Search` button each time to get the latest results.

To send a message to the topic named `casemanager`

```
echo '{"eventId" : "8", "name" : "Cyril"}' | kafka-console-producer --broker-list localhost:9092 --topic casemanager
```

This will put the json message into that topic ready for the consumers to pick off and populate the appropriate sinks.

Start the JPA consumer using:

```
mvn clean compile -Dproducer.properties=jpa-consumer.properties exec:exec@SimpleConsumerExample
```

Once this is started if you re-run the search in the UI you should see the entry `Cyril` for the RDBMS column.

Start the Elastic consumer using:

```
mvn clean compile -Dproducer.properties=elastic-consumer.properties exec:exec@SimpleConsumerExample
```

Again a re-run of the search will reveal that the Elastic sink has been populated with the same data.




