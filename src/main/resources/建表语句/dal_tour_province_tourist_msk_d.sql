CREATE EXTERNAL TABLE IF NOT EXISTS dal_tour.dal_tour_province_tourist_msk_d (
    mdn string comment '手机号大写MD5加密'  
    ,source_county_id string comment '游客来源区县'  
    ,d_province_id string comment '旅游目的地省代码'  
    ,d_stay_time double comment '游客在该省停留的时间长度（小时）'  
    ,d_max_distance double comment '游客本次出游距离'  
) 
comment  '旅游应用专题数据省级别-天'
PARTITIONED BY (
    day_id string comment '日分区'  
) 
ROW FORMAT DELIMITED 
    FIELDS TERMINATED BY '\t' 
STORED AS INPUTFORMAT 'org.apache.hadoop.mapred.TextInputFormat' 
OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
location '/daas/motl/dal_tour/dal_tour_province_tourist_msk_d'; 


alter table dal_tour.dal_tour_province_tourist_msk_d  add if not exists partition(day_id='20180503') location '/daas/motl/dal/tour/dal_tour_province_tourist_msk_d/day_id=20180503';


一次出游一条数据


游客定义
    出行距离大于10km
        常住地在用户画像表中

    在省内停留时间大于3个小时


需求矩阵  需要计算的指标

根据省游客表计算如下指标

客流量按天 [省id,客流量]
性别按天 [省id,性别,客流量]
年龄按天 [省id,年龄,客流量]
常住地按天 [省id,常住地市,客流量]
归属地按天 [省id,归属地市,客流量]
终端型号按天 [省id,终端型号,客流量]
消费等级按天 [省id,消费等级,客流量]
停留时长按天 [省id,停留时长,客流量]
停留时长按天 [省id,出游距离,客流量]
