# Clustering
Static geographical information of CityBike's stations in Brisbane

Application to perform an clustering based on the latitude & longitude

We use as a technique stack : Scala 2.11 & Spark 2.3

# Config file
You have to fill in the config file all spark, oozie and paths args

Many important args like : 

resultsPath = A REMPLIR/result

brisbaneCityBikePath = A REMPLIR/Brisbane_CityBike.json

clustersNb = 3

seedsNb = 1

### Launching oozie coordinator by day
`oozie job --oozie {{ server_oozie }} \
 -config {{ application_path }}/conf/application.properties \
 -D oozie.coord.application.path={{oozieApplicationCPath}} -run`

### Run with spark-submit
`spark-submit --class com.alouirabeh.test.clustring.Main \
   --master yarn  \
   --deploy-mode cluster  \
   --num-executors 6  \
   --executor-memory 10G \
   --files ../conf/application.properties \
    Clustring-1.0.0-SNAPSHOT-spark.jar`
