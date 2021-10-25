  /**
      * 统计游客指标
      * 客流量按天 [省,客流量]
      * 性别按天 [省,性别,客流量]
      * 年龄按天 [省,年龄,客流量]
      * 常住地按天 [省,常住地市,客流量]
      * 归属地按天 [省,归属地市,客流量]
      * 终端型号按天 [省,终端型号,客流量]
      * 消费等级按天 [省,消费等级,客流量]
      * 停留时长按天 [省,停留时长,客流量]
      * 出游距离按天 [省,出游距离,客流量]
      */

    mdn string comment '手机号大写MD5加密'  
    ,source_county_id string comment '游客来源区县'  
    ,d_province_id string comment '旅游目的地省代码'  
    ,d_stay_time double comment '游客在该省停留的时间长度（小时）'  
    ,d_max_distance double comment '游客本次出游距离'  


    
    dim.dim_usertag_msk_m

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

客流量按天 [省,客流量]

select d_province_id,day_id,count(1) as c from dal_tour.dal_tour_province_tourist_msk_d where day_id=20180503 group by d_province_id,day_id


 性别按天 [省,性别,客流量]

select a.d_province_id,b.gender,count(1) c from 
(select * from dal_tour.dal_tour_province_tourist_msk_d  where day_id=20180503) as a
join 
(select * from dim.dim_usertag_msk_m where month_id=201805) as b
on a.mdn=b.mdn
group by a.d_province_id,b.gender


年龄按天 [省,年龄,客流量]


select a.d_province_id,b.age,count(1) c from 
(select * from dal_tour.dal_tour_province_tourist_msk_d  where day_id=20180503) as a
join 
(select * from dim.dim_usertag_msk_m where month_id=201805) as b
on a.mdn=b.mdn
group by a.d_province_id,b.age


为了减少表关联，可以先做一张宽表，将所需要的所有字段关联好，
后续的指标需要什么字段直接选择即可


使用宽表统计指标
select d_province_name,count(1) from dal_tour.dal_tour_province_wide_msk_d where day_id=20180503 group by d_province_name


年龄按天 [省,年龄,客流量]

select d_province_name,gender,count(1) from dal_tour.dal_tour_province_wide_msk_d where day_id=20180503 group by d_province_name,gender


select d_province_name,gender,age,count(1) from dal_tour.dal_tour_province_wide_msk_d where day_id=20180503 group by d_province_name,gender,age

