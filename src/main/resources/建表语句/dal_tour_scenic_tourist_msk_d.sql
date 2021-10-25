CREATE EXTERNAL TABLE IF NOT EXISTS dal_tour.dal_tour_scenic_tourist_msk_d (
    mdn string comment '游客手机号码'  
    ,source_city_id string comment '游客来源城市'  
    ,d_scenic_id string comment '旅游目的地景区代码'  
    ,d_scenic_name string comment '旅游目的地景区名'
    ,d_arrive_time string comment '游客进入景区的时间'  
    ,d_stay_time double comment '游客在该景区停留的时间长度（小时）'  
) 
comment  '旅游应用专题数据景区级别-天'
PARTITIONED BY (
    day_id string comment '日分区'  
) 
ROW FORMAT DELIMITED 
    FIELDS TERMINATED BY '\t' 
STORED AS INPUTFORMAT 'org.apache.hadoop.mapred.TextInputFormat' 
    OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'  
location '/daas/motl/dal_tour/dal_tour_scenic_tourist_msk_d'; 


alter table  dal_tour.dal_tour_scenic_tourist_msk_d add if not exists partition(day_id='20180503') 
