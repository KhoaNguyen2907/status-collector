#!/bin/bash

echo "checking kafka"
apt-get update -y

yes | apt-get install kafkacat

kafkacatResult=$(kafkacat -L -b kafka-broker-1:9092)

while [[ $kafkacatResult == *"Failed to query metadata"* ]]; do
  >&2 echo "Kafka broker is not up yet!"
  sleep 2
  kafkacatResult=$(kafkacat -L -b kafka-broker-1:9092)
done

echo "Kafka broker has started."

start-service.sh