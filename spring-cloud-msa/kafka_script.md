## Zookeeper 및 Kafka 서버 구동  

```
$KAFKA_HOME/bin/zookeeper-server-start.sh  $KAFKA_HOME/config/zookeeper.properties  
$KAFKA_HOME/bin/kafka-server-start.sh  $KAFKA_HOME/config/server.propertis  
```

<br>

## Topic 생성  

```
$KAFKA_HOME/bin/kafka-topic.sh --create --topic quickstart-events --bootstrap-server localhost:9092 --partitions 1  
```

<br>

## Topic 목록 확인  

```
$KAFKA_HOME/bin/kafka-topics.sh --bootstrap-server localhost:9092 --list  
```

<br>

## Topic 정보 확인   

```
$KAFKA_HOME/bin/kafka-topics.sh --describe --topic quickstart-events --bootstrap-server localhost:9092    
```

<br>

## producer 실행  

```
$KAFKA_HOME/bin/kafka-console-producer.sh --broker-list localhost:9092 --topic quickstart-events
```

<br>

## consumer 실행  

```
$KAFKA_HOME/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic quickstart-events --from-beginning
```
