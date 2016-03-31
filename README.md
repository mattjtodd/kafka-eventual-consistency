# Using CQRS and Event Sourcing for Multiple Persistence Sink Eventual Consistency

### Installing and starting Kafka (Instructions for OSX only :))

[Apache Kafka] (http://kafka.apache.org)

Exec the following command in your bash or zsh terminal

```
brew install kafka
```

This will also install all dependencies, like [Zookeeper] (https://zookeeper.apache.org) which is required to run the server.

```
zkserver start
```

This will start zookeeper.

```
kafka-server-start /usr/local/etc/kafka/server.properties
```

This will start a single node Kafka cluster, watch those logs fly!

