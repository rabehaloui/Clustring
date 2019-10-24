package com.alouirabeh.test.clustring

import com.alouirabeh.test.clustring.ClusteringContext._
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object Variables {

  /** Create SparkSession */
  implicit lazy val spark: SparkSession = SparkSession
    .builder()
    .appName(appName)

    /** Prevent Spark SQL to use its own Parquet support */
    .config("spark.sql.hive.convertMetastoreParquet", "false")

    /** The default mode is STATIC, Overwrite partition if exists */
    /** Dynamic Partitioning , provide a column whose values become the values of the partitions */
    .config("spark.sql.sources.partitionOverwriteMode", "dynamic")

    /** Load data into partitions dynamically */
    .config("hive.exec.dynamic.partition", "true")
    .config("hive.exec.dynamic.partition.mode", "nonstrict")
    .config("spark.sql.shuffle.partitions", NB_DEFAULT_PARTITION * sparkExecutorCores * sparkNumExecutors)
    .enableHiveSupport()
    .getOrCreate()

  /** Create Logger */
  lazy val logger: Logger = Logger.getLogger("MainClustering")
  logger.setLevel(Level.INFO)

  /** Set number of clusters and seeds */
  val numberOfClusters: Int = ClusteringContext.clustersNb
  val numberOfSeeds: Int = ClusteringContext.seedsNb
  val resultsPath: String = ClusteringContext.resultsPath
  val brisbaneCityBikePath: String = ClusteringContext.brisbaneCityBikePath

  /** Number of cores to partitioned data */
  val NB_DEFAULT_PARTITION: Int = 3

}
