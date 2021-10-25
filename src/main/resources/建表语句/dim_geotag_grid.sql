CREATE EXTERNAL TABLE IF NOT EXISTS dim.dim_geotag_grid (
    grid_id string comment '网格ID，500米级别'  
    ,center_longi string comment '中心点经度'  
    ,center_lati string comment '中心点纬度'  
    ,county_id string comment '区县id'   
    ,county_type string comment '区县类型，0郊区，1城区'  
    ,grid_type string comment '网格类型，详见网格类型码表'  
) 
comment  'gis网格配置表'
ROW FORMAT DELIMITED 
    FIELDS TERMINATED BY '\t' 
STORED AS PARQUET
location '/daas/motl/dim/dim_geotag_grid'; 



