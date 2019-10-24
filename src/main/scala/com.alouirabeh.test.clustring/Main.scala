package com.alouirabeh.test.clustring

import com.alouirabeh.test.clustring.Utils._
import com.alouirabeh.test.clustring.Variables._
import org.apache.spark.ml.clustering.{KMeans, KMeansModel}
import org.apache.spark.ml.evaluation.ClusteringEvaluator
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.DataFrame

object Main {

  def main(args: Array[String]): Unit = {

    val Lat: String = "latitude"
    val Lng: String = "longitude"
    val Features: String = "features"
    val listColumn: List[String] = List(Lat, Lng)

    /** Preparing the input data */
    val brisbaneCityBikeInitialDF: DataFrame = readJson(brisbaneCityBikePath, multiline = true)
    val brisbaneCityBikeDF: DataFrame = brisbaneCityBikeInitialDF
      .select(Lat, Lng)

    logger.info("Caching DataFrame")
    brisbaneCityBikeDF.cache()

    val brisbaneCityBikeCastedDF: DataFrame = castToDoubleColumns(listColumn)(brisbaneCityBikeDF)

    val brisbaneCityBikeFilteredDF: DataFrame = removeNullColumns(listColumn)(brisbaneCityBikeCastedDF)

    val assembler: VectorAssembler =
      new VectorAssembler()
        .setInputCols(Array(Lat, Lng))
        .setOutputCol(Features)

    val brisbaneCityBikeTransDF: DataFrame = assembler.transform(brisbaneCityBikeFilteredDF).select(Features)

    /** Training model with KMeans */
    logger.info("Number of clusters: " + ClusteringContext.clustersNb)
    logger.info("Number of seeds: " + ClusteringContext.seedsNb)

    val kmeans: KMeans = new KMeans()
      .setK(numberOfClusters)
      .setSeed(numberOfSeeds)

    val model: KMeansModel = kmeans.fit(brisbaneCityBikeTransDF)

    /** Make predictions */
    val predictions: DataFrame = model.transform(brisbaneCityBikeTransDF)

    logger.info("Fist 5 predictions: ")
    predictions.take(5).foreach(logger.info(_))

    /** Evaluate clustering by computing Silhouette score */
    val evaluator: ClusteringEvaluator = new ClusteringEvaluator()
    val silhouetteScore: Double = evaluator.evaluate(predictions)
    logger.info(s"Silhouette with squared euclidean distance = $silhouetteScore")

    /** Evaluate clustering by computing Within Set Sum of Squared Errors */
    val squaredErrors: Double = model.computeCost(brisbaneCityBikeTransDF)
    logger.info(s"Set Sum of Squared Errors = $squaredErrors")

    /** Show the result */
    logger.info("Cluster Centres: ")
    model.clusterCenters.foreach(logger.info(_))

    /** Save clusters in HDFS as a text file */
    logger.info("Saving model")
    model.save(resultsPath)

    /** Close Spark */
    logger.info("Closing Spark")
    spark.close
  }
}
