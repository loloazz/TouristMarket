CREATE EXTERNAL TABLE IF NOT EXISTS dim.dim_admincode (
    prov_id string comment '省id'  
    ,prov_name string comment '省名称'  
    ,city_id string comment '市id'  
    ,city_name string comment '市名称'  
    ,county_id string comment '区县id'  
    ,county_name string comment '区县名称'  
    ,city_level string comment '城市级别，一级为1；二级为2...依此类推'  
    ,economic_belt string comment 'BJ为首都经济带、ZSJ为珠三角经济带、CSJ为长三角经济带、DB为东北经济带、HZ为华中经济带、HB为华北经济带、HD为华东经济带、HN为华南经济带、XB为西北经济带、XN为西南经济带'  
    ,city_feature1 string comment 'NL代表内陆、YH代表沿海'  
) 
comment  '行政区配置表'
ROW FORMAT DELIMITED 
    FIELDS TERMINATED BY '\t' 
STORED AS INPUTFORMAT 'org.apache.hadoop.mapred.TextInputFormat' 
    OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'  
location '/daas/motl/dim/dim_admincode'; 



    