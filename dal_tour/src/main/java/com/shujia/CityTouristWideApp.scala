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


    spark.sql(
      s"""
         |
         |cache table  ${Constants.ODS_DATABASE_NAME}.${Constants.ADMIN_CODE_TABLE_NAME};
         |insert OVERWRITE table   ${Constants.DAL_TOUR_DATABASE_NAME}.${Constants.CITY_WIDE_TABLE_NAME}   partition (day_id=${day_id})
         |
         |select
         |a.mdn as mdn ,
         |c.city_name as d_city_name,
         |d.city_name as o_city_name,
         |c.prov_name    as o_province_name,
         |b.number_attr as number_attr ,
         |case
         |    when a.d_max_distance >=10 and a.d_max_distance<50 then "[10-50)"
         |    when  a.d_max_distance >=50 and a.d_max_distance<80 then "[50-80)"
         |    when a.d_max_distance >=80 and a.d_max_distance<120 then "[80-120)"
         |    when a.d_max_distance >=120 and a.d_max_distance<200 then "[120-200)"
         |    when a.d_max_distance >=200 and a.d_max_distance<400 then "[200-400)"
         |    when a.d_max_distance >=400 and a.d_max_distance<800 then "[400-800)"
         |    else "800~" end as d_max_distance,
         |case
         |    when a.d_stay_time>=3 and a.d_stay_time<6 then "[3-6)"
         |    when a.d_stay_time>=6 and a.d_stay_time<12 then "[6-12)"
         |    else "[12-24)" end as d_stay_time,
         |
         | b.gender as gender,
         | b.trmnl_brand as trmnl_brand,
         | b.packg as pckg_price,
         | b.conpot as conpot,
         | b.age as age
         |
         |from
         |(select * from  ${Constants.DAL_TOUR_DATABASE_NAME}.${Constants.CITY_TOURIST_TABLE_NAME} where day_id=$day_id ) as a
         |
         |join
         |(select * from  ${Constants.ODS_DATABASE_NAME}.${Constants.INIT_USERTAG_TABLE_NAME}  where month_id = $month_id) as b
         |on  a.mdn= b.mdn
         |
         |
         |join  (select distinct  prov_name,city_name,city_id from ${Constants.ODS_DATABASE_NAME}.${Constants.ADMIN_CODE_TABLE_NAME}) as c on
         |a.d_city_id=c.city_id
         |
         |
         |join (select city_name,county_id , county_name  from ${Constants.ODS_DATABASE_NAME}.${Constants.ADMIN_CODE_TABLE_NAME} ) as d
         |on  a.source_county_id =  d.county_id
         |""".stripMargin)


  }
}
