#!/usr/bin/env bash
#***********************************************************************************
# **  文件名称: CityTourist.sh
# **  创建日期: 2021年10月23日
# **  编写人员: yaolong

# **  输出信息:
# **
# **  功能描述: 对用户画像表再次进行脱敏操作
# **  处理过程:

#***********************************************************************************

#***********************************************************************************
#==修改日期==|===修改人=====|======================================================|
#
#***********************************************************************************


#获取脚本所在目录
shell_home="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

#进入脚本目录
cd $shell_home


# 得到第一个参数
day_id=$1


#根据自己服务器的性能进行配置   num-executors 2   executor-memory  executor-cores 2



spark-submit \
--master yarn-client \
--com.touristMarket.dwd.StayPoint \
--num-executors 2 \
--executor-memory 4G \
--executor-cores 2 \
--conf spark.sql.shuffle.partitions=10 \
--jars common-1.0.jar \
dwd-1.0.jar $day_id \

