
# 用户表

DROP TABLE IF EXISTS `usertag`;
CREATE TABLE `usertag` (
    mdn varchar(255) 
    ,name varchar(255) 
    ,gender varchar(255) 
    ,age int(10)
    ,id_number varchar(255) 
    ,number_attr varchar(255) 
    ,trmnl_brand varchar(255) 
    ,trmnl_price varchar(255) 
    ,packg varchar(255) 
    ,conpot varchar(255)
    ,resi_grid_id varchar(255)
    ,resi_county_id varchar(255)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8;

# 导入数据
 LOAD DATA LOCAL INFILE 'usertag.txt' INTO TABLE usertag FIELDS TERMINATED BY ',' ;




# 景区配置表

CREATE TABLE  scenic_boundary (
    scenic_id varchar(255)   ,
    scenic_name varchar(255)  ,
    boundary text 
) ;

# 导入数据
 LOAD DATA LOCAL INFILE 'scenic_boundary.txt' INTO TABLE scenic_boundary FIELDS TERMINATED BY '|' ;


# 行政区配置表

CREATE TABLE admin_code (
    prov_id varchar(255)  
    ,prov_name varchar(255)  
    ,city_id varchar(255) 
    ,city_name varchar(255)  
    ,county_id varchar(255)  
    ,county_name varchar(255)  
    ,city_level varchar(255)  
    ,economic_belt varchar(255)   
    ,city_feature1 varchar(255)  
) ;
# 导入数据

 LOAD DATA LOCAL INFILE 'ssxdx.txt' INTO TABLE admin_code FIELDS TERMINATED BY ',' ;



    
