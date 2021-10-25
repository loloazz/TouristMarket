CREATE EXTERNAL TABLE IF NOT EXISTS dim.dim_bsid (
    bsid string comment '扇区编码'  
    ,bsc_longi string comment '基站经度'  
    ,bsc_lati string comment '基站纬度'  
    ,center_longi string comment '扇区中心点经度'  
    ,center_lati string comment '扇区中心点维度'  
    ,boundary string comment '扇区边界顶点坐标'  
    ,city_code string comment '城市区号'  
    ,country_id string comment '区县编码'  
    ,city_id string comment '城市编码'  
    ,prov_id string comment '省编码'  
) 
comment  '扇区配置表'
ROW FORMAT DELIMITED 
    FIELDS TERMINATED BY '\t' 
STORED AS INPUTFORMAT 'org.apache.hadoop.mapred.TextInputFormat' 
    OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'  
location '/daas/motl/dim/dim_bsid'; 
