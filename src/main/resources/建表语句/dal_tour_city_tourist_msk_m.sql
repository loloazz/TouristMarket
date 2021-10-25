CREATE EXTERNAL TABLE IF NOT EXISTS dal_tour.dal_tour_city_tourist_msk_m (
    mdn string comment '手机号大写MD5加密'  
    ,source_city_id string comment '游客来源城市'  
    ,source_city_type string comment '游客来源取值类型'  
    ,departure_time string comment '本次出游出发时间'  
    ,d_city_id string comment '旅游目的地城市代码'  
    ,d_arrive_time string comment '游客进入旅游城市的时间'  
    ,d_leave_time string comment '游客离开/未离开旅游城市的更新时间'  
    ,status int comment '游客离开/未离开旅游城市的时间标识'  
    ,o_city_id string comment '游客上一个旅游城市代码'  
    ,d_stay_time double comment '游客在该城市停留的时间长度（小时）'  
    ,d_stay_time_day int comment '游客在该城市持续停留的时间长度（天数）'  
    ,d_max_distance double comment '游客本次出游距离'  
    ,d_arrive_means string comment '游客本次出游交通方式'  
    ,d_track_distance double comment '实际出游轨迹距离'  
) 
comment  '旅游应用专题数据城市级别-月'
PARTITIONED BY (
    month_id string comment '月分区'  
) 
ROW FORMAT DELIMITED 
    FIELDS TERMINATED BY '\t' 
STORED AS INPUTFORMAT 'org.apache.hadoop.mapred.TextInputFormat' 
    OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'  
location '/daas/subtl/dal/tour/dal_tour_city_tourist_msk_m'; 



