CREATE EXTERNAL TABLE IF NOT EXISTS dal_tour.dal_tour_scenic_tourist_msk_m (
    mdn string comment '游客手机号码'  
    ,source_city_id string comment '游客来源城市'  
    ,d_scenic_id string comment '旅游目的地景区代码'  
    ,d_arrive_time string comment '游客进入景区的时间'  
    ,d_leave_time string comment '游客离开/未离开旅游景区的更新时间'  
    ,d_stay_time double comment '游客在该景区停留的时间长度（小时）'  
    ,status int comment '游客离开/未离开旅游景区的时间标识,0已离开，1未离开'  
) 
comment  '旅游应用专题数据景区级别-月'
PARTITIONED BY (
    month_id string comment '月分区'  
) 
ROW FORMAT DELIMITED 
    FIELDS TERMINATED BY '\t' 
STORED AS INPUTFORMAT 'org.apache.hadoop.mapred.TextInputFormat' 
    OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'  
location '/daas/subtl/dal/tour/dal_tour_scenic_tourist_msk_m'; 



