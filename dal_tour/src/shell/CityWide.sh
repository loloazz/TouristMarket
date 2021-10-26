#!/usr/bin/env bash


#获取脚本所在目录
shell_home="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

#进入脚本目录
cd $shell_home


# 得到第一个参数
day_id=$1


#根据自己服务器的性能进行配置   num-executors 2   executor-memory  executor-cores 2



spark-submit \
--master yarn-client \
--class com.shujia.CityTouristWideApp \
--num-executors 2 \
--executor-memory 2G \
--executor-cores 2 \
--conf spark.sql.shuffle.partitions=10 \
--jars common-1.0.jar \
dal_tour-1.0.jar $day_id \

