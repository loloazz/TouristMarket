CREATE EXTERNAL TABLE IF NOT EXISTS ods.ods_scenic_boundary (
    scenic_id string comment '景区id'  
    ,scenic_name string comment '景区名称'  
    ,boundary string comment '景区边界'  
) 
comment  '景区配置表'
ROW FORMAT DELIMITED 
    FIELDS TERMINATED BY '\t' 
STORED AS INPUTFORMAT 'org.apache.hadoop.mapred.TextInputFormat' 
    OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'  
location '/daas/motl/ods/ods_scenic_boundary'; 