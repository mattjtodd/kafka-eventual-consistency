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

Now produce some messages to a topic

First of all compile the project

```
mvn clean compile
```

The send a message to a topic

```
echo '{"id" : "8", "eventId" : "8", "name" : "Nigel"}' | kafka-console-producer --broker-list localhost:9092 --topic casemanager
```

```
mvn -Dkafka.groupId=jpa-cases -Dkafka.topic=casemanager exec:exec@SimpleConsumerExample
```

```
mvn -Dkafka.groupId=elastic-cases -Dkafka.topic=casemanager exec:exec@SimpleConsumerExample
```




