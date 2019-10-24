#!/usr/bin/env bash

spark-submit --class com.alouirabeh.test.clustring.Main \
  --master yarn  \
  --deploy-mode cluster  \
  --num-executors 6  \
  --executor-memory 10G \
  --files ../conf/application.properties \
   Clustring-1.0.0-SNAPSHOT-spark.jar
