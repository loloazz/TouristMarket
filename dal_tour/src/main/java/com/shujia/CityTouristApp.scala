package com.shujia

import com.shujia.util.Geography
import org.apache.spark.sql.expressions.{UserDefinedFunction, Window}
import org.apache.spark.sql._

/** *
 * 计算每个人在每个城市中的停留时间
 */
object CityTouristApp extends SparkTool {
  override def run(spark: SparkSession): Unit = {

    //导入隐式转换  以及sparksql的函数依赖
    import org.apache.spark.sql.functions._
    import spark.implicits._

    // 行政配置表 dim.dim_admincode

    /**
     * prov_id string comment '省id'
     * ,prov_name string comment '省名称'
     * ,city_id string comment '市id'
     * ,city_name string comment '市名称'
     * ,county_id string comment '区县id'
     * ,county_name string comment '区县名称'
     * ,city_level string comment '城市级别，一级为1；二级为2...依此类推'
     * ,economic_belt string comment 'BJ为首都经济带、ZSJ为珠三角经济带、CSJ为长三角经济带、DB为东北经济带、HZ为华中经济带、HB为华北经济带、HD为华东经济带、HN为华南经济带、XB为西北经济带、XN为西南经济带'
     * ,city_feature1 string comment 'NL代表内陆、YH代表沿海'
     */

    val adminCode: DataFrame = spark.table(s"${Constants.ODS_DATABASE_NAME}.${Constants.ADMIN_CODE_TABLE_NAME}")


    /**
     * mdn string comment '用户手机号码'
     * ,longi string comment '网格中心点经度'
     * ,lati string comment '网格中心点纬度'
     * ,grid_id string comment '停留点所在电信内部网格号'
     * ,county_id string comment '停留点区县'
     * ,duration string comment '机主在停留点停留的时间长度（分钟）,lTime-eTime'
     * ,grid_first_time string comment '网格第一个记录位置点时间（秒级）'
     * ,grid_last_time string comment '网格最后一个记录位置点时间（秒级）'
     */
    // stayPoint表   停留表
    val stayPoint: Dataset[Row] = spark.table(s"${Constants.DWD_DATABASE_NAME}.${Constants.STAY_POINT_TABLE_NAME}")
      .where($"day_id" === day_id)


    /** *
     * mdn string comment '手机号大写MD5加密'
     * ,name string comment '姓名'
     * ,gender string comment '性别，1男2女'
     * ,age string comment '年龄'
     * ,id_number string comment '证件号码'
     * ,number_attr string comment '号码归属地'
     * ,trmnl_brand string comment '终端品牌'
     * ,trmnl_price string comment '终端价格'
     * ,packg string comment '套餐'
     * ,conpot string comment '消费潜力'
     * ,resi_grid_id string comment '常住地网格'
     * ,resi_county_id string comment '常住地区县'
     * )
     * comment  '用户画像表'
     */
    // 用户画像表
    val userTag: Dataset[Row] = spark.table(s"${Constants.ODS_DATABASE_NAME}.${Constants.INIT_USERTAG_TABLE_NAME}").where($"month_id" === month_id)


    /** *
     * 计算每个人在每个城市中的停留时间
     */


    val calculateLength: UserDefinedFunction = udf((point1: Long, point2: Long) => {
      Geography.calculateLength(point1, point2)
    })


    stayPoint
      // 关联行政配置表，获取城市编号// 广播小表 //
      .join(adminCode ,"county_id")
      .withColumn("sumDuration", sum("duration") over Window.partitionBy("mdn", "county_id"))
      //取出停留时间大于180的用户
      .where($"sumDuration" > 180)

      /**
       * 计算出游最远距离  根据常住地  与  目的地  的距离
       *
       */
      // 关联用户表获取常驻地  通过mdn手机号进行关联
      .join(userTag, "mdn")
      // 获得距离 这里面包含多个距离 单位米
      .withColumn("distance", calculateLength($"resi_grid_id", $"grid_id"))
      //取得最大的距离
      .withColumn("d_max_distance", max("distance") over Window.partitionBy("mdn", "county_id"))
      // 筛选出最大距离大于  10KM的
      .where($"d_max_distance" > 10000)
      //取出字段
      .select($"mdn", $"resi_county_id" as "source_county_id", $"city_id" as "d_city_id", round($"sumDuration" / 60, 2) as "d_stay_time", round($"d_max_distance" / 1000, 2) as "d_max_distance")
      //去除重复数据
      .distinct()


      //保存数据

      .write
      .format("csv")
      .option("sep", "\t")
      .mode(SaveMode.Overwrite)
      .save(s"${Constants.CITY_TOURIST_SAVE_PATH}day_id=${day_id}")


    // 增加分区
    spark.sql(s"alter table ${Constants.DAL_TOUR_DATABASE_NAME}.${Constants.CITY_TOURIST_TABLE_NAME} add if not exists partition(day_id='$day_id')")


  }
}
