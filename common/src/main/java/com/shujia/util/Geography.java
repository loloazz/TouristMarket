
package com.shujia.util;



import com.shujia.grid.Grid;

import java.awt.geom.Point2D;

/**
 * 经纬度坐标算法工具类
 * Copyright (C) China Telecom Corporation Limited, Cloud Computing Branch Corporation - All Rights Reserved
 * <p>
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * <p>
 * Proprietary and confidential
 * <p>
 * Contributors:
 * 83644, dingjb@chinatelecom.cn, 2015
 */
public class Geography {

    /**
     * 地球半径
     */
    private static final double EARTH_RADIUS = 6378137;
    /**
     * 每纬度所垮距离
     */
    private static final double PER_LATI_LENGTH = Math.PI*EARTH_RADIUS*2/360;
    /**
     * 私有构造方法
     */
    private Geography() {

    }



    /**
     * 计算两个网格之间的距离
     *
     * @param p1 第一个网格点
     * @param p2 第二个网格点
     * @return 返回两个点的距离
     */
    public static double calculateLength(Long p1, Long p2) {
        Point2D.Double point1 = Grid.getCenter(p1);
        Point2D.Double point2 = Grid.getCenter(p2);
        return calculateLength(point1.x, point1.y, point2.x, point2.y);
    }

    /**
     * 计算两个经纬度之间的距离
     *
     * @param longi1 经度1
     * @param lati1  纬度1
     * @param longi2 经度2
     * @param lati2  纬度2
     * @return 距离
     */
    public static double calculateLength(double longi1, double lati1, double longi2, double lati2) {
        double er = EARTH_RADIUS; // 地球半径
        double lat21 = lati1 * Math.PI / 180.0;
        double lat22 = lati2 * Math.PI / 180.0;
        double a = lat21 - lat22;
        double b = (longi1 - longi2) * Math.PI / 180.0;
        double sa2 = Math.sin(a / 2.0);
        double sb2 = Math.sin(b / 2.0);
        double d = 2 * er * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat21) * Math.cos(lat22) * sb2 * sb2));
        return Math.abs(d);
    }

    /**
     * 将经纬度点转换为直角坐标系点，单位米
     * @param basePoint 原点
     * @param point
     * @return
     */
    public static Point2D.Double longilati2Decare(Point2D.Double basePoint,Point2D.Double point){
        double x = (point.getX()-basePoint.getX())*getPerLongiLength(basePoint.getY());
        double y = (point.getY()-basePoint.getY())*PER_LATI_LENGTH;
        return new Point2D.Double(x, y);
    }

    /**
     * 获取某一个纬度下每经度所对应的距离
     * @param lati 纬度
     * @return 每经度所对应的距离
     */
    public static double getPerLongiLength(double lati){
        return Math.abs(PER_LATI_LENGTH * Math.cos(lati * Math.PI / 180));
    }

    /**
     * 将经纬度点转换为直角坐标系点，单位米
     * @param basePoint
     * @param point
     * @return
     */
    public static Point2D.Double decare2Longilati(Point2D.Double basePoint,Point2D.Double point){
        double x = basePoint.x+point.x/getPerLongiLength(basePoint.y);
        double y = basePoint.y+point.y/PER_LATI_LENGTH;
        return new Point2D.Double(x, y);
    }

    /**
     * 计算两点之间的线与正北方向的夹角
     * @param longi1  经度1
     * @param lati1 纬度1
     * @param longi2  经度2
     * @param lati2  纬度2
     * @return 与正北方向的夹角(角度),二三象限为负，一四象限为正
     */
    public static double calculateAngle(double longi1, double lati1, double longi2,
                                        double lati2) {
        //原点
        if(Double.doubleToRawLongBits(longi2)==Double.doubleToRawLongBits(longi1)&&Double.doubleToRawLongBits(lati2)==Double.doubleToRawLongBits(lati1)){
            throw new RuntimeException("same point");
        }
        //y正半轴
        if(Double.doubleToRawLongBits(longi2)==Double.doubleToRawLongBits(longi1)&&Double.doubleToRawLongBits(lati2)>Double.doubleToRawLongBits(lati1)){
            return 0;
        }
        //y负半轴
        if(Double.doubleToRawLongBits(longi2)==Double.doubleToRawLongBits(longi1)&&Double.doubleToRawLongBits(lati2)<Double.doubleToRawLongBits(lati1)){
            return 180;
        }
        //x正半轴
        if(Double.doubleToRawLongBits(longi2)>Double.doubleToRawLongBits(longi1)&&Double.doubleToRawLongBits(lati2)==Double.doubleToRawLongBits(lati1)){
            return 90;
        }
        //x负半轴
        if(Double.doubleToRawLongBits(longi2)<Double.doubleToRawLongBits(longi1)&&Double.doubleToRawLongBits(lati2)==Double.doubleToRawLongBits(lati1)){
            return 270;
        }
        double length1 = getPerLongiLength(lati2)*Math.abs(longi2-longi1);//x
        double length2 = PER_LATI_LENGTH*Math.abs(lati2-lati1);//y
        double radian = Math.atan(length2/length1);
        double angle = 90-toAngle(radian);
        //第一象限
        if(Double.doubleToRawLongBits(longi2)>Double.doubleToRawLongBits(longi1)&&Double.doubleToRawLongBits(lati2)>Double.doubleToRawLongBits(lati1)){
            return angle;
        }
        //第二象限
        if(Double.doubleToRawLongBits(longi2)<Double.doubleToRawLongBits(longi1)&&Double.doubleToRawLongBits(lati2)>Double.doubleToRawLongBits(lati1)){
            return 360-angle;
        }
        //第三象限
        if(Double.doubleToRawLongBits(longi2)<Double.doubleToRawLongBits(longi1)&&Double.doubleToRawLongBits(lati2)<Double.doubleToRawLongBits(lati1)){
            return 180+angle;
        }
        //第四象限
        if(Double.doubleToRawLongBits(longi2)>Double.doubleToRawLongBits(longi1)&&Double.doubleToRawLongBits(lati2)<Double.doubleToRawLongBits(lati1)){
            return 180-angle;
        }
        throw new RuntimeException("no such situation");
    }
    /**
     * 弧度转换为角度
     * @return 角度
     */
    public static double toAngle(double radian){
        return radian*180d/Math.PI;
    }


    /**
     * 跨指定距离对应的纬度
     * @param length 跨距离
     * @return
     */
    public static double lengthLati(double length){
        return length/PER_LATI_LENGTH;
    }

    /**
     * 某纬度下，跨指定距离经度
     * @param lati
     * @param length
     * @return
     */
    public static double lengthLongi(double lati,double length){
        return length/getPerLongiLength(lati);
    }

}
