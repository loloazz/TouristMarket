package com.shujia

/**
 * 该类是用来配置  一些常量的，通过传常量到  Config的getConfig方法得到配置信息
 *
 */
object Constants {

  // 库名


  val ODS_DATABASE_NAME: String = Config.getConfig("ods.database.name")
  val DWD_DATABASE_NAME: String = Config.getConfig("dwd.database.name")
  val DIM_DATABASE_NAME: String = Config.getConfig("dim.database.name")
  val DAL_TOUR_DATABASE_NAME: String = Config.getConfig("dal_tour.database.name")


  //  表名
  val OIDD_TABLE_NAME: String = Config.getConfig("oidd.table.name")
  val DDR_TABLE_NAME: String = Config.getConfig("ddr.table.name")
  val DPI_TABLE_NAME: String = Config.getConfig("dpi.table.name")
  val WCDR_TABLE_NAME: String = Config.getConfig("wcdr.table.name")

  // 一次加密的用户画像表
  val INIT_USERTAG_TABLE_NAME: String = Config.getConfig("usertag.table.name")
  val ADMIN_CODE_TABLE_NAME: String = Config.getConfig("adminCode.table.name")


  val SCENIC_BOUNDARY_TABLE_NAME: String = Config.getConfig("scenicBoundary.table.name")

  //  #dal_tour 表
  val CITY_TOURIST_TABLE_NAME: String = Config.getConfig("cityTourist.table.name")
//  cityTourist.save.path=/daas/motl/dal_tour/dal_tour_city_tourist_msk_d/
  val CITY_TOURIST_SAVE_PATH: String = Config.getConfig("cityTourist.save.path")

  //  #融合表
  val MERGE_LOCATION_TABLE_NAME: String = Config.getConfig("mergeLocation.table.name")
  val MERGE_LOCATION_SAVE_PATH: String = Config.getConfig("mergeLocation.save.path")



  //   停留表 dwd_staypoint_msk_d

  val STAY_POINT_TABLE_NAME: String = Config.getConfig("stayPoint.table.name")
  val STAY_POINT_SAVE_PATH: String = Config.getConfig("stayPoint.table.path")
  //  dim 用户画像表的表名
  val LAST_USERTAG_TABLE_NAME: String = Config.getConfig("dim.usertag.table.name")
  //  dim 用户画像表的路径
  val LAST_USERTAG_SAVE_PATH: String = Config.getConfig("dim.usertag.path.name")









}
