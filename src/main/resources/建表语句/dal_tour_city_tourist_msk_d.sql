CREATE EXTERNAL TABLE IF NOT EXISTS dal_tour.dal_tour_city_tourist_msk_d (
    mdn string comment '手机号大写MD5加密'  
    ,source_county_id string comment '游客来源区县'  
    ,d_city_id string comment '旅游目的地市代码'  
    ,d_stay_time double comment '游客在该省停留的时间长度（小时）'  
    ,d_max_distance double comment '游客本次出游距离'  
) 
comment  '旅游应用专题数据城市级别-天'
PARTITIONED BY (
    day_id string comment '日分区'  
) 
ROW FORMAT DELIMITED 
    FIELDS TERMINATED BY '\t' 
STORED AS INPUTFORMAT 'org.apache.hadoop.mapred.TextInputFormat' 
OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
location '/daas/motl/dal_tour/dal_tour_city_tourist_msk_d'; 


alter table dal_tour.dal_tour_city_tourist_msk_d add if not exists partition(day_id='20180503') ;


