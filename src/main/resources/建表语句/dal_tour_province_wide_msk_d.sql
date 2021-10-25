CREATE EXTERNAL TABLE IF NOT EXISTS dal_tour.dal_tour_province_wide_msk_d(
     mdn string comment '手机号'  
    ,d_province_name string comment '旅游目的地省名'  
    ,o_city_name string comment '旅游来源地地市名'  
    ,o_province_name string comment '旅游来源地省名'  
    ,number_attr string comment '号码归属地'  
    ,d_distance_section string comment '出游距离'
    ,d_stay_time string comment '停留时间按小时'
    ,gender string comment '性别'
    ,trmnl_brand string comment '终端品牌'
    ,pckg_price int comment '套餐'
    ,conpot int comment '消费潜力'
    ,age int comment '年龄'
) 
comment  '旅游应用专题数据省级别-天-宽表'
PARTITIONED BY (
    day_id string comment '日分区'  
) 
ROW FORMAT DELIMITED 
    FIELDS TERMINATED BY '\t' 
STORED AS INPUTFORMAT 'org.apache.hadoop.mapred.TextInputFormat' 
OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
location '/daas/motl/dal_tour/dal_tour_province_wide_msk_d'; 


alter table dal_tour.dal_tour_province_wide_msk_d add if not exists partition(day_id='20180503') ;

