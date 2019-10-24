package com.alouirabeh.test.clustring

import com.alouirabeh.test.clustring.Utils.{readJson, castToDoubleColumns,removeNullColumns}
import com.holdenkarau.spark.testing.{DataFrameSuiteBase, SharedSparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.types.{DoubleType, StringType, StructField, StructType}
import org.scalatest.FunSuite

class UtilsSpec extends FunSuite with SharedSparkContext with DataFrameSuiteBase {

  test("Read Json file") {
    val jsonDF: DataFrame = readJson("src/test/resources/Brisbane_CityBike.json", multiline = true)(spark)

    assert(jsonDF.
      where(col("id") === "122.0")
      .select("latitude")
      .first()
      .getString(0) == "-27.482279")
  }

  test("Cast columns to double function") {

    val inputSchema: StructType = new StructType()
      .add(StructField("id", StringType, nullable = true))
      .add(StructField("a", StringType, nullable = true))
      .add(StructField("b", StringType, nullable = true))

    val inputRdd: RDD[Row] = spark.sparkContext.parallelize(
      Seq(
        Row("123", "14.0", "12.5"),
        Row("124", null, "0.5"),
        Row("125", "2.6", null),
        Row("126", "4.0", null)
      )
    )

    val inputDf: DataFrame = spark.createDataFrame(inputRdd, inputSchema)

    val outputSchema: StructType = new StructType()
      .add(StructField("id", StringType, nullable = true))
      .add(StructField("a", DoubleType, nullable = true))
      .add(StructField("b", DoubleType, nullable = true))

    val outputRdd: RDD[Row] = spark.sparkContext.parallelize(
      Seq(
        Row("123", 14.0, 12.5),
        Row("124", null, 0.5),
        Row("125", 2.6, null),
        Row("126", 4.0, null)
      )
    )

    val outputDf: DataFrame = spark.createDataFrame(outputRdd, outputSchema)

    assertDataFrameEquals(outputDf, castToDoubleColumns(List("a", "b"))(inputDf))
  }

  test("Remove null from columns") {

    val inputSchema: StructType = new StructType()
      .add(StructField("id", StringType, nullable = true))
      .add(StructField("a", StringType, nullable = true))
      .add(StructField("b", StringType, nullable = true))

    val inputRdd: RDD[Row] = spark.sparkContext.parallelize(
      Seq(
        Row("123", "14.0", "12.5"),
        Row("124", null, "0.5"),
        Row("125", "2.6", null),
        Row("126", "4.0", null)
      )
    )

    val inputDf: DataFrame = spark.createDataFrame(inputRdd, inputSchema)

    val outputSchema: StructType = new StructType()
      .add(StructField("id", StringType, nullable = true))
      .add(StructField("a", StringType, nullable = true))
      .add(StructField("b", StringType, nullable = true))

    val outputRdd: RDD[Row] = spark.sparkContext.parallelize(
      Seq(
        Row("123", "14.0", "12.5")
      )
    )

    val outputDf: DataFrame = spark.createDataFrame(outputRdd, outputSchema)

    assertDataFrameEquals(outputDf, removeNullColumns(List("a", "b"))(inputDf))
  }

}
