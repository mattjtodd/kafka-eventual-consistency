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

Run a quick producer / consumer from this project in java

```
mvn clean compile exec:exec@SimpleProducerConsumerExample
```

This will output something like:

```
Apr 01, 2016 11:43:28 AM org.apache.kafka.common.config.AbstractConfig logAll
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

Apr 01, 2016 11:43:29 AM org.apache.kafka.common.utils.AppInfoParser$AppInfo <init>
INFO: Kafka version : 0.9.0.1
Apr 01, 2016 11:43:29 AM org.apache.kafka.common.utils.AppInfoParser$AppInfo <init>
INFO: Kafka commitId : 23c69d62a0cabf06
Sent the following number of messages: 10
Apr 01, 2016 11:43:29 AM org.apache.kafka.clients.producer.KafkaProducer close
INFO: Closing the Kafka producer with timeoutMillis = 9223372036854775807 ms.
Received the following messages
Apr 01, 2016 11:43:29 AM org.apache.kafka.common.config.AbstractConfig logAll
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

Apr 01, 2016 11:43:29 AM org.apache.kafka.common.utils.AppInfoParser$AppInfo <init>
INFO: Kafka version : 0.9.0.1
Apr 01, 2016 11:43:29 AM org.apache.kafka.common.utils.AppInfoParser$AppInfo <init>
INFO: Kafka commitId : 23c69d62a0cabf06
d2bd1db2-b30a-4ea8-9da1-58a19fae1f03 count 0
97ebffd7-0a02-46e3-9382-4e29a974c638 count 1
0ec56e9d-11e8-4d00-aae9-2b923c3d648d count 2
8bc7d6ff-06d4-4b99-ab87-23bcf1405f73 count 3
5f4871b1-2eb0-42a2-9204-97e078234eeb count 4
39d8ee60-6c5d-44d4-a9df-59dc8e5e3fc5 count 5
6a44188f-49b6-489b-915c-72c824a98f3e count 6
808f9c21-fe4e-481f-b61d-89071ed92e26 count 7
466fc3a3-5b7c-43e3-92a3-ff828e900abe count 8
7a26d66f-7a88-4024-aaa0-c77e9dc82e0d count 9
```

