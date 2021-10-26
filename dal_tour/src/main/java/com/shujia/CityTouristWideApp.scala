package com.shujia
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object CityTouristWideApp extends SparkTool {
  override def run(spark: SparkSession): Unit = {

    /**
     * 统计游客指标
     * 客流量按天 [市,客流量]
     * 性别按天 [市,性别,客流量]
     * 年龄按天 [市,年龄,客流量]
     * 常住地按天 [市,常住地市,客流量]
     * 归属地按天 [市,归属地市,客流量]
     * 终端型号按天 [市,终端型号,客流量]
     * 消费等级按天 [市,消费等级,客流量]
     * 停留时长按天 [市,停留时长,客流量]
     * 出游距离按天 [市,出游距离,客流量]
     */

//    stayPoint
//    mdn string comment '手机号大写MD5加密'
//    ,source_county_id string comment '游客来源区县'
//    ,d_province_id string comment '旅游目的地市代码'
//    ,d_stay_time double comment '游客在该市停留的时间长度（小时）'
//    ,d_max_distance double comment '游客本次出游距离'
//





//    dim.dim_usertag_msk_m
//
//    mdn string comment '手机号大写MD5加密'
//    ,name string comment '姓名'
//    ,gender string comment '性别，1男2女'
//    ,age string comment '年龄'
//    ,id_number string comment '证件号码'
//    ,number_attr string comment '号码归属地'
//    ,trmnl_brand string comment '终端品牌'
//    ,trmnl_price string comment '终端价格'
//    ,packg string comment '套餐'
//    ,conpot string comment '消费潜力'
//    ,resi_grid_id string comment '常住地网格'
//    ,resi_county_id string comment '常住地区县'

//    dim_admincode
//
//    prov_id string comment '市id'
//    ,prov_name string comment '市名称'
//    ,city_id string comment '市id'
//    ,city_name string comment '市名称'
//    ,county_id string comment '区县id'
//    ,county_name string comment '区县名称'
//    ,city_level string comment '城市级别，一级为1；二级为2...依此类推'
//    ,economic_belt string comment 'BJ为首都经济带、ZSJ为珠三角经济带、CSJ为长三角经济带、DB为东北经济带、HZ为华中经济带、HB为华北经济带、HD为华东经济带、HN为华南经济带、XB为西北经济带、XN为西南经济带'
//    ,city_feature1 string comment 'NL代表内陆、YH代表沿海'
//

    import spark.implicits._
    import org.apache.spark.sql.functions._


    val stayPoint: Dataset[Row] = spark.table("dwd.dwd_staypoint_msk_d  ").where($"day_id"===20180503)

    val usetag: Dataset[Row] = spark.table("ods.ods_usertag_m").where($"month_id" === 201805)

    val admincode: DataFrame = spark.table("ods.ods_admincode").select($"city_name",$"county_name").distinct()



    //市名称   性别   年龄   消费等级 ，停留时间  出游距离 常住地市，归属地
    stayPoint.join(usetag,"mdn")

      .join(admincode,$"d_province_id"===$"city_id").selectExpr("*")





  }
}
