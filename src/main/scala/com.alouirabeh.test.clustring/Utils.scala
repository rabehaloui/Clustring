package com.alouirabeh.test.clustring

import org.apache.spark.sql.functions.col
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.{DataFrame, SparkSession}

object Utils {

  /** Read json file */
  def readJson(path: String, multiline: Boolean)(implicit spark: SparkSession): DataFrame = spark
    .read
    .option("multiline", multiline)
    .json(path)

  /** Convert set of string column to double */
  def castToDoubleColumns(list: List[String]): DataFrame => DataFrame = df =>
    list.foldLeft(df)(
      (acc, clm) => acc.withColumn(clm, col(clm).cast(DoubleType))
    )

  /** Filter columns to remove null values */
  def removeNullColumns(list: List[String]): DataFrame => DataFrame = df =>
    list.foldLeft(df)(
      (acc, clm) => acc.filter(col(clm).isNotNull)
    )
}
