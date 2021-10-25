CREATE EXTERNAL TABLE IF NOT EXISTS ods.ods_dpi(
    mdn string comment '手机号码'  
    ,start_time string comment '业务开始时间'  
    ,county_id string comment '区县编码'  
    ,longi string comment '经度'  
    ,lati string comment '纬度'  
    ,bsid string comment '基站标识'  
    ,grid_id string comment '网格号'  
    ,biz_type string comment '业务类型'  
    ,event_type string comment '事件类型'  
    ,data_source string comment '数据源'  
) 
comment  '位置数据融合表'
PARTITIONED BY (
    day_id string comment '天分区'  
) 
ROW FORMAT DELIMITED 
    FIELDS TERMINATED BY '\t' 
STORED AS INPUTFORMAT 'org.apache.hadoop.mapred.TextInputFormat' 
    OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'  
location '/daas/motl/ods/ods_dpi'; 



// 添加分区
alter table ods.ods_dpi add if not exists partition(day_id='20180503') ;
