package com.shujia

import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import com.shujia.util.Md5
import org.apache.spark.sql.expressions.UserDefinedFunction
object UserTagMskApp extends SparkTool {
  override def run(spark: SparkSession): Unit = {

//    ods_usertag_m
    import  spark.implicits._
    val odsUserTag: DataFrame = spark.sql(s"select * from ${Constants.ODS_DATABASE_NAME}.${Constants.INIT_USERTAG_TABLE_NAME} where month_id = ${month_id}")

    // 注册md5函数


    import org.apache.spark.sql.functions._

    // 注册md5函数
    val md5function: UserDefinedFunction = udf((str: String) => Md5.md5(str))


    //2、对铭感信息加密
    val dimUserTagMsk: DataFrame = odsUserTag
      .select(
        md5function($"mdn") as "mdn",
        md5function($"name") as "name",
        $"gender",
        $"age",
        md5function($"id_number") as "id_number",
        $"number_attr",
        $"trmnl_brand",
        $"trmnl_price",
        $"packg",
        $"conpot",
        $"resi_grid_id",
        $"resi_county_id"
      )

    //保存数据
    dimUserTagMsk
      .write
      .format("csv")
      .mode(SaveMode.Overwrite)
      .option("sep", "\t")
      .save(s"${Constants.LAST_USERTAG_SAVE_PATH}month_id=${month_id}")

    //增加分区
    spark.sql(s"alter table ${Constants.DIM_DATABASE_NAME}.${Constants.LAST_USERTAG_TABLE_NAME} add if not exists partition(month_id='$month_id')")
  }
}


