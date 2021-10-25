CREATE EXTERNAL TABLE IF NOT EXISTS dwd.dwd_staypoint_msk_d (
    mdn string comment '用户手机号码'  
    ,longi string comment '网格中心点经度'  
    ,lati string comment '网格中心点纬度'  
    ,grid_id string comment '停留点所在电信内部网格号'  
    ,county_id string comment '停留点区县'  
    ,duration string comment '机主在停留点停留的时间长度（分钟）,lTime-eTime'  
    ,grid_first_time string comment '网格第一个记录位置点时间（秒级）'  
    ,grid_last_time string comment '网格最后一个记录位置点时间（秒级）'  
) 
comment  '停留点表'
PARTITIONED BY (
    day_id string comment '天分区'  
) 
ROW FORMAT DELIMITED 
    FIELDS TERMINATED BY '\t' 
STORED AS INPUTFORMAT 'org.apache.hadoop.mapred.TextInputFormat' 
    OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
location '/daas/motl/dwd/dwd_staypoint_msk_d'; 




alter table dwi.dwi_staypoint_msk_d add if not exists partition(day_id='20180503') 