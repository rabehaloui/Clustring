package com.alouirabeh.test.clustring

import com.typesafe.config.{Config, ConfigFactory}

object ClusteringContext {

  private val ClusteringConf: Config = {
    ConfigFactory.invalidateCaches()
    ConfigFactory.load()
  }

  val clustersNb: Int = ClusteringConf.getInt("clustersNb")
  val seedsNb: Int = ClusteringConf.getInt("seedsNb")
  val resultsPath: String = ClusteringConf.getString("resultsPath")
  val brisbaneCityBikePath: String = ClusteringConf.getString("brisbaneCityBikePath")

  /** Spark parameters */
  val sparkExecutorCores: Int = ClusteringConf.getInt("sparkExecutorCores")
  val sparkNumExecutors: Int = ClusteringConf.getInt("sparkNumExecutors")
  val appName: String= ClusteringConf.getString("appName")

}