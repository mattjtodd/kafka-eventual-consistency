# Using CQRS and Event Sourcing for Multiple Persistence Sink Eventual Consistency

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

The send some messages to the topic

```
mvn exec:exec@SimpleProducerExample
```

Output should be something like:

```
Apr 01, 2016 2:03:39 PM org.apache.kafka.common.config.AbstractConfig logAll
INFO: ProducerConfig values:
	compression.type = none
	metric.reporters = []
	metadata.max.age.ms = 300000
	metadata.fetch.timeout.ms = 60000
	reconnect.backoff.ms = 50
	sasl.kerberos.ticket.renew.window.factor = 0.8
	bootstrap.servers = [localhost:9092]
	retry.backoff.ms = 100
	sasl.kerberos.kinit.cmd = /usr/bin/kinit
	buffer.memory = 33554432
	timeout.ms = 30000
	key.serializer = class org.apache.kafka.common.serialization.StringSerializer
	sasl.kerberos.service.name = null
	sasl.kerberos.ticket.renew.jitter = 0.05
	ssl.keystore.type = JKS
	ssl.trustmanager.algorithm = PKIX
	block.on.buffer.full = false
	ssl.key.password = null
	max.block.ms = 60000
	sasl.kerberos.min.time.before.relogin = 60000
	connections.max.idle.ms = 540000
	ssl.truststore.password = null
	max.in.flight.requests.per.connection = 5
	metrics.num.samples = 2
	client.id =
	ssl.endpoint.identification.algorithm = null
	ssl.protocol = TLS
	request.timeout.ms = 30000
	ssl.provider = null
	ssl.enabled.protocols = [TLSv1.2, TLSv1.1, TLSv1]
	acks = 1
	batch.size = 16384
	ssl.keystore.location = null
	receive.buffer.bytes = 32768
	ssl.cipher.suites = null
	ssl.truststore.type = JKS
	security.protocol = PLAINTEXT
	retries = 0
	max.request.size = 1048576
	value.serializer = class org.apache.kafka.common.serialization.StringSerializer
	ssl.truststore.location = null
	ssl.keystore.password = null
	ssl.keymanager.algorithm = SunX509
	metrics.sample.window.ms = 30000
	partitioner.class = class org.apache.kafka.clients.producer.internals.DefaultPartitioner
	send.buffer.bytes = 131072
	linger.ms = 0

Apr 01, 2016 2:03:39 PM org.apache.kafka.common.utils.AppInfoParser$AppInfo <init>
INFO: Kafka version : 0.9.0.1
Apr 01, 2016 2:03:39 PM org.apache.kafka.common.utils.AppInfoParser$AppInfo <init>
INFO: Kafka commitId : 23c69d62a0cabf06
Completed Successfully, sent: 10
Apr 01, 2016 2:03:40 PM org.apache.kafka.clients.producer.KafkaProducer close
INFO: Closing the Kafka producer with timeoutMillis = 9223372036854775807 ms.
```

To start the simple consumer which will get those messages and then await more messages until shutdown.

```
brew install elasticsearch
```

After installation start elastic search server from bin directory under Elastic search installation location.
To test elastic search run the following ```http://localhost:9200/``` from browser or using curl

```
./elasticsearch
```

The following url will retrieve all the messages from the elastic search index

```
http://localhost:9200/messages/_search?pretty=true&q=*:*
```

Then trigger the Kafka consumer,

```
mvn exec:exec@SimpleConsumerExample
```

Which should yield

```
Received the following messages
Apr 01, 2016 2:08:27 PM org.apache.kafka.common.config.AbstractConfig logAll
INFO: ConsumerConfig values:
	metric.reporters = []
	metadata.max.age.ms = 300000
	value.deserializer = class org.apache.kafka.common.serialization.StringDeserializer
	group.id = mygroup
	partition.assignment.strategy = [org.apache.kafka.clients.consumer.RangeAssignor]
	reconnect.backoff.ms = 50
	sasl.kerberos.ticket.renew.window.factor = 0.8
	max.partition.fetch.bytes = 1048576
	bootstrap.servers = [localhost:9092]
	retry.backoff.ms = 100
	sasl.kerberos.kinit.cmd = /usr/bin/kinit
	sasl.kerberos.service.name = null
	sasl.kerberos.ticket.renew.jitter = 0.05
	ssl.keystore.type = JKS
	ssl.trustmanager.algorithm = PKIX
	enable.auto.commit = true
	ssl.key.password = null
	fetch.max.wait.ms = 500
	sasl.kerberos.min.time.before.relogin = 60000
	connections.max.idle.ms = 540000
	ssl.truststore.password = null
	session.timeout.ms = 30000
	metrics.num.samples = 2
	client.id =
	ssl.endpoint.identification.algorithm = null
	key.deserializer = class org.apache.kafka.common.serialization.StringDeserializer
	ssl.protocol = TLS
	check.crcs = true
	request.timeout.ms = 40000
	ssl.provider = null
	ssl.enabled.protocols = [TLSv1.2, TLSv1.1, TLSv1]
	ssl.keystore.location = null
	heartbeat.interval.ms = 3000
	auto.commit.interval.ms = 5000
	receive.buffer.bytes = 32768
	ssl.cipher.suites = null
	ssl.truststore.type = JKS
	security.protocol = PLAINTEXT
	ssl.truststore.location = null
	ssl.keystore.password = null
	ssl.keymanager.algorithm = SunX509
	metrics.sample.window.ms = 30000
	fetch.min.bytes = 1
	send.buffer.bytes = 131072
	auto.offset.reset = latest

Apr 01, 2016 2:08:27 PM org.apache.kafka.common.utils.AppInfoParser$AppInfo <init>
INFO: Kafka version : 0.9.0.1
Apr 01, 2016 2:08:27 PM org.apache.kafka.common.utils.AppInfoParser$AppInfo <init>
INFO: Kafka commitId : 23c69d62a0cabf06
Received a message: ed617cb1-0a27-4b07-bcdf-847d0f06bd48 count 0
Received a message: 9e5d490b-ce60-436f-b5b9-f2de9eea0316 count 1
Received a message: e01408e3-eb8d-4074-9212-fa189e97b7fd count 2
Received a message: 7acdbf95-a182-437a-8def-0ee4191315cf count 3
Received a message: af54cd8b-c128-4ac8-ab49-6386f7a573bd count 4
Received a message: d30160c2-5297-4018-b8ed-1269e24a954f count 5
Received a message: 2e50114d-ce05-408c-ba6e-ece72f27b963 count 6
Received a message: 8b520771-c90a-4f83-ba8c-de0e79c9e025 count 7
Received a message: 9ef9f265-7681-42a1-8a55-d281ee01f232 count 8
Received a message: 419b252b-7e4b-4b5a-9ac6-32230aa637a9 count 9
```

Subsequent calls to `mvn exec:exec@SimpleProducerExample` will be visible in the consumer's console.



