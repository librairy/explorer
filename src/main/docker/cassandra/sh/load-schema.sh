#!/usr/bin/env bash

while : ;do
  sleep 5;
  echo "=======> checking cassandra status"
  # Get the status of this machine from the Cassandra nodetool
  #STATUS=`nodetool status | grep 'UN' | awk '{print $1}'`
  STATUS=`nodetool statusthrift`
  echo $STATUS
  # Once the status is Up and Normal, then we are ready
  #if [ $STATUS = "UN" ]; then
  if [ $STATUS = "running" ]; then
        echo "===================================================="
        echo "loading librairy schema"
        echo "===================================================="
        cqlsh -f /tmp/librairy-schema.cql
        echo "===================================================="
        echo "librairy schema loaded successfully"
        echo "===================================================="
        break
  fi

done
