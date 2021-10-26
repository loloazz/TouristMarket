insert OVERWRITE table   dal_tour.dal_tour_city_wide_msk_d   partition (day_id='20180503')
select 
a.mdn as mdn ,  
c.city_name as d_city_name,
d.city_name as o_city_name,
c.prov_name    as o_province_name,
b.number_attr as number_attr ,
case 
    when a.d_max_distance >=10 and a.d_max_distance<50 then "[10-50)" 
    when  a.d_max_distance >=50 and a.d_max_distance<80 then "[50-80)"
    when a.d_max_distance >=80 and a.d_max_distance<120 then "[80-120)"
    when a.d_max_distance >=120 and a.d_max_distance<200 then "[120-200)"
    when a.d_max_distance >=200 and a.d_max_distance<400 then "[200-400)"
    when a.d_max_distance >=400 and a.d_max_distance<800 then "[400-800)"
    else "800~" end as d_max_distance,
case  
    when a.d_stay_time>=3 and a.d_stay_time<6 then "[3-6)"
    when a.d_stay_time>=6 and a.d_stay_time<12 then "[6-12)"
    else "[12-24)" end as d_stay_time,

 b.gender as gender,
 b.trmnl_brand as trmnl_brand,
 b.packg as pckg_price,
 b.conpot as conpot,
 b.age as age

from 
(select * from  dal_tour.dal_tour_city_tourist_msk_d where day_id=20180503 ) as a 

join  
(select * from  ods.ods_usertag_m  where month_id = 201805) as b
on  a.mdn= b.mdn


join  (select distinct  prov_name,city_name,city_id from ods.ods_admincode) as c on 
a.d_city_id=c.city_id


join (select city_name,county_id , county_name  from ods.ods_admincode ) as d 
on  a.source_county_id =  d.county_id