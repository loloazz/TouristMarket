package com.shujia

import org.apache.spark.sql.SparkSession

abstract class SparkTool {

  var day_id: String = _
  var month_id: String = _


  def main(args: Array[String]): Unit = {

    if (args.length == 0) {
      println("请传入时间分区：day_id")
      return
    }

    //获取当天的分区
    day_id = args(0)

    //月分区
    month_id = day_id.substring(0, 6)

    val spark: SparkSession = SparkSession
      .builder()
      .appName(this.getClass.getSimpleName)
      .enableHiveSupport()
      .getOrCreate()

    run(spark)



  }


def run(sparkSession: SparkSession):Unit
}
