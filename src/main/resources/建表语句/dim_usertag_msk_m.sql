CREATE EXTERNAL TABLE IF NOT EXISTS dim.dim_usertag_msk_m (
    mdn string comment '手机号大写MD5加密'  
    ,name string comment '姓名'  
    ,gender string comment '性别，1男2女'  
    ,age string comment '年龄'  
    ,id_number string comment '证件号码'  
    ,number_attr string comment '号码归属地'  
    ,trmnl_brand string comment '终端品牌'    
    ,trmnl_price string comment '终端价格'
    ,packg string comment '套餐'  
    ,conpot string comment '消费潜力'  
    ,resi_grid_id string comment '常住地网格'  
    ,resi_county_id string comment '常住地区县'  
) 
comment  '用户画像表'
PARTITIONED BY (
    month_id string comment '月分区'  
) 
ROW FORMAT DELIMITED 
    FIELDS TERMINATED BY '\t' 
STORED AS INPUTFORMAT 'org.apache.hadoop.mapred.TextInputFormat' 
    OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'  
location '/daas/motl/dim/dim_usertag_msk_m'; 


alter table dim.dim_usertag_msk_m add if not exists partition(month_id='201805') ;


