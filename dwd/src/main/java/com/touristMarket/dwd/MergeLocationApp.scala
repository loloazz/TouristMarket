package com.touristMarket.dwd

import com.shujia.{ Constants, SparkTool}
import org.apache.spark.sql._

object MergeLocationApp  extends SparkTool{

  /**
   *
   * 真实环境的处理逻辑
   * 提取OIDD、移动DPI和WCDR分钟粒度位置数据，对重复时间点数据进行二次清洗，完成后写入合并文件库；根据乒乓切换、超速数据等清洗原则对噪声数据进行清洗，完成后写入融合文件库；移动DDR数据在数据上传后延迟一天进行。
   * 将原始信令数据融合处理后，根据应用的需求和后期应用场景的需求，共保留12个字段。包括手机号、城市代码、业务开始时间、经纬度、BSID、对端号码、数据源和业务类型。
   *
   * 我们这里没有原始，只能简单的union
   *
   *
   */
  override def run(spark: SparkSession): Unit = {


    /**
     * 加载四个表
     */
    val oidd: DataFrame = spark.sql(s"select * from ${Constants.ODS_DATABASE_NAME}.${Constants.OIDD_TABLE_NAME} where day_id=$day_id")
    val ddr: DataFrame = spark.sql(s"select * from  ${Constants.ODS_DATABASE_NAME}.${Constants.DDR_TABLE_NAME}  where day_id=$day_id")
    val dpi: DataFrame = spark.sql(s"select * from  ${Constants.ODS_DATABASE_NAME}.${Constants.DPI_TABLE_NAME}   where day_id=$day_id")
    val wcdr: DataFrame = spark.sql(s"select * from ${Constants.ODS_DATABASE_NAME}.${Constants.WCDR_TABLE_NAME}  where day_id=$day_id")


    val unionDataFrame: Dataset[Row] = oidd.union(ddr).union(dpi).union(wcdr)
    unionDataFrame
      .write
      .format("csv")
      .option("sep", "\t")
      .mode(SaveMode.Overwrite)
      .save(s"${Constants.MERGE_LOCATION_SAVE_PATH} day_id=$day_id")


    spark.sql(s"alter table ${Constants.DWD_DATABASE_NAME}.${Constants.MERGE_LOCATION_TABLE_NAME}  add if not exists partition(day_id='$day_id')")



    /** *
     * spark-submit --master yarn-client --class com.touristMarket.dwd.MergeLocationApp  --num-executors 2 --executor-memory 4G --executor-cores 2
     *
     * com.touristMarket.dwd.MergeLocationApp
     */

  }
}
