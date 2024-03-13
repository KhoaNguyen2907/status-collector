#!/bin/bash
# check-elastic.sh

echo "Checking Elastic cluster!"

curlResult=$(curl -s -o /dev/null -I -w "%{http_code}" http://elastic-1:9200/)

echo "result status code:" "$curlResult"

while [[ ! $curlResult == "200" ]]; do
  >&2 echo "Elastic cluster is not up yet!"
  sleep 2
  curlResult=$(curl -s -o /dev/null -I -w "%{http_code}" http://elastic-1:9200/)
done

echo "Elastic cluster has started."

start-service.sh


