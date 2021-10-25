package com.touristMarket.dwd

import com.shujia.{Constants, SparkTool}
import com.shujia.grid.Grid
import com.shujia.util.Md5
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.{expr, udf}
import org.apache.spark.sql.{DataFrame, Dataset, SaveMode, SparkSession}

import java.awt.geom.Point2D
import java.text.SimpleDateFormat
import scala.collection.mutable.ListBuffer

object StayPoint  extends SparkTool{

  override def run(spark: SparkSession): Unit = {

    //1、读取融合表 dwd_res_regn_mergelocation_msk_d

    /**
     *
     * mdn string comment '手机号码'
     * ,start_time string comment '业务时间'
     * ,county_id string comment '区县编码'
     * ,longi string comment '经度'
     * ,lati string comment '纬度'
     * ,bsid string comment '基站标识'
     * ,grid_id string comment '网格号'
     * ,biz_type string comment '业务类型'
     * ,event_type string comment '事件类型'
     * ,data_source string comment '数据源'
     */


    val mergelocation: DataFrame = spark.sql(s"select mdn ,start_time,county_id,grid_id from ${Constants.DWD_DATABASE_NAME}.${Constants.MERGE_LOCATION_TABLE_NAME} where day_id=${day_id} ")


    /** *
     * 按照时间的顺序排序，对mdn 和   网格号进行分组得到
     */


    // 将df转成rdd
    val KVRDD: RDD[(String, (String, String, String, String))] = mergelocation.rdd.map(row => {
      val mdn: String = row.getAs[String]("mdn") // 手机号

      val start_time: String = row.getAs[String]("start_time")
      val county_id: String = row.getAs[String]("county_id")
      val grid_id: String = row.getAs[String]("grid_id")

      // 由于数据中的start_time中  有  在一个网格号中的，开始时间，和结束时间  并以逗号分割
      val Start_date: Array[String] = start_time.split(",")
      val startTime: String = Start_date(0)
      val endTime: String = Start_date(1)

      (mdn, (startTime, endTime, county_id, grid_id))

    })

    // 按照mdn分组   将同一个人的数据汇总到同一组中

    val groupRDD: RDD[(String, Iterable[(String, String, String, String)])] = KVRDD.groupByKey()
    //    groupRDD.map {
    //      case (mdn: String, points: Iterable[(String, String, String, String)) =>
    //
    //        // 将迭代器转成List方便以后的操作
    //        val pointList: List[(String, String, String, String)] = points.toList
    //
    //        // 先对集合进行按照结束时间排序
    //
    //        val sortList: List[(String, String, String, String)] = pointList.sortBy(_._2)
    //
    //
    //        // 先得到第一个点
    //
    //        val headPoint: (String, String, String, String) = sortList.head
    //
    //        //headStartTime, headEndTime, headCountyId, headGridId
    //        // 获取第一个点的关键信息
    //        var headStartTime: String = headPoint._1
    //        var headEndTime: String = headPoint._2
    //        var headCountyId: String = headPoint._3
    //        var headGridId: String = headPoint._4
    //
    //        //使用集合保存最终输出结果
    //        val resultPoints = new ListBuffer[(String, String, String, String, String)]
    //
    //        // 得到以后点的信息
    //        // 里面的数据都是按照mdn分组以及和时间进行升序排列的
    //        val tailPoints: List[(String, String, String, String)] = sortList.tail
    //
    //        // 遍历tailPoints  数据  与headpoint的headGridId进行比较
    //        tailPoints.foreach{
    //              //startTime, endTime, county_id, grid_id
    //          case (startTime:String, endTime:String, county_id : String, grid_id:String)=>
    //            if (grid_id.equals(headGridId)){
    //              // 如果grid相等时 更新  endtime即可
    //              headEndTime=endTime
    //            }else{
    //
    //              // 保持上一节点
    //              resultPoints+=(mdn,headStartTime,headEndTime,headCountyId,headGridId)
    //
    //              // 将数据进行更新，用做下次保存的数据
    //              headGridId = grid_id
    //              headCountyId = county_id
    //              headStartTime = startTime
    //              headEndTime = endTime
    //
    //            }
    //
    //        }
    //
    //        // 在使用的时候，最后一个时间无法收尾
    //        resultPoints.+=((mdn, headCountyId, headGridId, headStartTime, headEndTime))
    //        // 返回最后一条
    //        resultPoints
    //  }

    // 由于返回一个list 这里不如使用flatmap  将list打开
    val resultRDD: RDD[(String, String, String, String, String)] = groupRDD.flatMap {
      case (mdn: String, points: Iterable[(String, String, String, String)]) =>

        // 将迭代器转成List方便以后的操作
        val pointList: List[(String, String, String, String)] = points.toList

        // 先对集合进行按照结束时间排序

        val sortList: List[(String, String, String, String)] = pointList.sortBy(_._2)


        // 先得到第一个点

        val headPoint: (String, String, String, String) = sortList.head

        //headStartTime, headEndTime, headCountyId, headGridId
        // 获取第一个点的关键信息
        var headStartTime: String = headPoint._1
        var headEndTime: String = headPoint._2
        var headCountyId: String = headPoint._3
        var headGridId: String = headPoint._4

        //使用集合保存最终输出结果
        val resultPoints = new ListBuffer[(String, String, String, String, String)]

        // 得到以后点的信息
        // 里面的数据都是按照mdn分组以及和时间进行升序排列的
        val tailPoints: List[(String, String, String, String)] = sortList.tail

        // 遍历tailPoints  数据  与headpoint的headGridId进行比较
        tailPoints.foreach {
          //startTime, endTime, county_id, grid_id
          case (startTime: String, endTime: String, county_id: String, grid_id: String) =>
            if (grid_id.equals(headGridId)) {
              // 如果grid相等时 更新  endtime即可
              headEndTime = endTime
            } else {

              // 保持上一节点
              resultPoints.+=((mdn, headStartTime, headEndTime, headCountyId, headGridId))

              // 将数据进行更新，用做下次保存的数据
              headGridId = grid_id
              headCountyId = county_id
              headStartTime = startTime
              headEndTime = endTime

            }

        }

        // 在使用的时候，最后一个时间无法收尾
        resultPoints.+=((mdn, headCountyId, headGridId, headStartTime, headEndTime))
        // 返回最后一条
        resultPoints
    }

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


    //计算网格中心的的经纬度和用户在网格中的停留时间


    val result= resultRDD.filter(x=>{(x._4.length>8&& x._5.length > 8)}).map {
      case (mdn: String, county_id: String, grid_id: String, startTime: String, endTime: String) =>
        // 通过工具类获取网格中心点的经纬度
        val point: Point2D.Double = Grid.getCenter(grid_id.toLong)
        val longi: Double = point.getX
        val lati: Double = point.getY

        /** *
         * 计算停留时间
         */

        // 创建日期格式化对象

        val format = new SimpleDateFormat("yyyyMMddHHmmss")


        val startDate: Long = format.parse(startTime).getTime
        val endDate: Long = format.parse(endTime).getTime

        println()
        // 数据的输出，以及保留小数位
        (mdn, longi.formatted("%.4f"), lati.formatted("%.4f"), grid_id, county_id, ((endDate - startDate) / 60000.0).formatted("%.2f"), startDate, endDate)

    }


    // 进行数据的保存
    import  spark.implicits._

    result.toDF()
      .write
      .format("csv")
      .option("sep","\t")
      .mode(SaveMode.Overwrite)
      .save(s"${Constants.STAY_POINT_SAVE_PATH}day_id=${day_id}")


    // 注意先创建表

    /***
     ** CREATE EXTERNAL TABLE IF NOT EXISTS dwd.dwd_staypoint_msk_d (
      *        mdn string comment '用户手机号码'
      *        ,longi string comment '网格中心点经度'
      *        ,lati string comment '网格中心点纬度'
      *        ,grid_id string comment '停留点所在电信内部网格号'
      *        ,county_id string comment '停留点区县'
      *        ,duration string comment '机主在停留点停留的时间长度（分钟）,lTime-eTime'
      *        ,grid_first_time string comment '网格第一个记录位置点时间（秒级）'
      *        ,grid_last_time string comment '网格最后一个记录位置点时间（秒级）'
      *    )
      *    comment  '停留点表'
      *    PARTITIONED BY (
      *        day_id string comment '天分区'
      *    )
      *    ROW FORMAT DELIMITED
      *        FIELDS TERMINATED BY '\t'
      *    STORED AS INPUTFORMAT 'org.apache.hadoop.mapred.TextInputFormat'
      *        OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
      *    location '/daas/motl/dwd/dwd_staypoint_msk_d';
      *
      *
      *
      *
      */
    //在创建分区

    /***
     * alter table dwi.dwi_staypoint_msk_d add if not exists partition(day_id='20180503')
     */
//    spark.sql(s"alter table ${Constants.DWD_DATABASE_NAME}.${Constants.STAY_POINT_TABLE_NAME}  add  id not exist partition(day_id=$day_id)")

  }
}
