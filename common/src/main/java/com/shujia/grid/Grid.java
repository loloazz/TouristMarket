/*********************************************************************
 * 
 * CHINA TELECOM CORPORATION CONFIDENTIAL
 * ______________________________________________________________
 * 
 *  [2015] - [2020] China Telecom Corporation Limited, 
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of China Telecom Corporation and its suppliers,
 * if any. The intellectual and technical concepts contained 
 * herein are proprietary to China Telecom Corporation and its 
 * suppliers and may be covered by China and Foreign Patents,
 * patents in process, and are protected by trade secret  or 
 * copyright law. Dissemination of this information or 
 * reproduction of this material is strictly forbidden unless prior 
 * written permission is obtained from China Telecom Corporation.
 **********************************************************************/
package com.shujia.grid;

import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.security.InvalidParameterException;

/**
 * 用于经纬度网格编号处理。采用全局统一编号方式，格式为：<br>
 * <b>xxxxxxxyyyyyyzr</b><br>
 * <b>xxxxxxx</b>:
 * 经度坐标，对应于xxx.xxxx度，例如114.1234度为1141234。共7位，整数部分可以少于3位，小数部分必须占4位，必要时用0填充 <br>
 * <b>yyyyyy</b>:
 * 纬度坐标，对应于yy.yyyy度，例如20.123度为201230。共6位，整数部分必须占2位，小数部分必须占4位，缺位用0填充 <br>
 * <b>Z</b>: 网格级别代码，1位，用于区分网格级别，可使用{@link GridLevel}的{@code levelOfZ}方法转换为
 * {@link GridLevel}枚举类型。列表见下。<br>
 * <table border="1">
 * <tr>
 * <th>编号</th>
 * <th>级别</th>
 * <th>右上角偏移量（度）</th>
 * <th>备注</th>
 * </tr>
 * <tr>
 * <td>0</td>
 * <td>预留</td>
 * <td></td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>1</td>
 * <td>100m</td>
 * <td>（+0.001，+0.001</td>
 * <td>基本单元</td>
 * </tr>
 * <tr>
 * <td>2</td>
 * <td>200m</td>
 * <td>（+0.002，+0.002）</td>
 * <td>可由100m聚合</td>
 * </tr>
 * <tr>
 * <td>3</td>
 * <td>预留</td>
 * <td></td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>4</td>
 * <td>500m</td>
 * <td>（+0.005，+0.005）</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>5</td>
 * <td>1000m</td>
 * <td>（+0.01，+0.01）</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>6</td>
 * <td>2000m</td>
 * <td>（+0.02，+0.02）</td>
 * <td>可由1000m聚合</td>
 * </tr>
 * <tr>
 * <td>7</td>
 * <td>预留</td>
 * <td></td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>8</td>
 * <td>5000m</td>
 * <td>（+0.05，+0.05）</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>9</td>
 * <td>10000m</td>
 * <td>（+0.1，+0.1）</td>
 * <td></td>
 * </tr>
 * </table>
 * <b>R</b>: 预留，1位
 * 
 * @author Lin Dong
 * 
 * 20150921
 * bug fix: 已修正getX getY颠倒问题
 * 
 * 20151117
 * bug fix: 修正getGridLevel未考虑保留位错误
 */
public class Grid {

	/**
	 * 100M/0.001度网格的z码
	 */
	final public static int Z_100M = 1;

	/**
	 * 200M/0.002度网格的z码
	 */
	final public static int Z_200M = 2;

	/**
	 * 500M/0.005度网格的z码
	 */
	final public static int Z_500M = 4;

	/**
	 * 1KM/0.01度网格的z码
	 */
	final public static int Z_1KM = 5;

	/**
	 * 2KM/0.02度网格的z码
	 */
	final public static int Z_2KM = 6;

	/**
	 * 5KM/0.01度网格的z码
	 */
	final public static int Z_5KM = 8;

	/**
	 * 10KM/0.1度网格的z码
	 */
	final public static int Z_10KM = 9;

	/**
	 * 获取ID对应的网格级别
	 * 
	 * @param gridID
	 *            网格ID
	 * @return 级别。如果无效则返回{@code null}
	 */
	public static GridLevel getLevel(long gridID) {
		int z = ((int) (gridID % 100))/10;
		return GridLevel.levelOfZ(z);
	}

	/**
	 * 获取给定网格ID的x、y、z部分
	 * 
	 * @param gridID
	 *            网格ID
	 * @return 三维数组，分别为经度x、纬度y、级别编码z。经纬度精确到小数点后第四位
	 */
	public static double[] getXYZ(long gridID) {
		double[] xyz = new double[3];
		xyz[2] = (gridID % 100) / 10;
		gridID /= 100;
		xyz[1] = (gridID % 1000000) / 10000.00;
		gridID /= 1000000;
		xyz[0] = gridID / 10000.00;
		return xyz;
	}

	/**
	 * 根据ID获取网格左下角的纬度
	 * 
	 * @param gridID
	 *            网格ID
	 * @return 左下角经度，精确到小数点后第四位
	 */
	public static double getY(long gridID) {
		gridID /= 100;
		return (gridID % 1000000) / 10000.00;
	}

	/**
	 * 根据ID获取网格左下角的经度
	 * 
	 * @param gridID
	 *            网格ID
	 * @return 左下角经度，精确到小数点后第四位
	 */
	public static double getXMin(long gridID) {
		return getX(gridID);
	}

	/**
	 * 根据ID获取网格左下角的经度
	 * 
	 * @param gridID
	 *            网格ID
	 * @return 左下角纬度，精确到小数点后第四位
	 */
	public static double getX(long gridID) {
		return (gridID / 100000000) / 10000.00;
	}

	/**
	 * 根据ID获取网格左下角的纬度
	 * 
	 * @param gridID
	 *            网格ID
	 * @return 左下角纬度，精确到小数点后第四位
	 */
	public static double getYMin(long gridID) {
		return getY(gridID);
	}

	/**
	 * 根据ID获取网格级别代码z
	 * 
	 * @param gridID
	 *            网格ID
	 * @return 网格级别代码
	 */
	public static int getZ(long gridID) {
		return (int) (gridID % 100) / 10;
	}

	/**
	 * 根据ID获取网格左下角的经纬度
	 * 
	 * @param gridID
	 *            网格ID
	 * @return 左下角经纬度，精确到小数点后第四位
	 */
	public static Point2D getXY(long gridID) {
		gridID /= 100;
		double x = (gridID % 1000000) / 10000.00;
		double y = (gridID / 1000000) / 10000.00;
		return new Point2D.Double(x, y);
	}

	/**
	 * 根据ID获取网格左下角的经纬度
	 * 
	 * @param gridID
	 *            网格ID
	 * @return 左下角经纬度，精确到小数点后第四位
	 */
	public static Point2D getLowerLeft(long gridID) {
		return getXY(gridID);
	}

	/**
	 * 根据ID获取网格右上角的经度
	 * 
	 * @param gridID
	 *            网格ID
	 * @return 右上角的经度，精确到小数点后第四位
	 */
	public static double getXMax(long gridID) {
		BigDecimal x = BigDecimal.valueOf(getX(gridID));
		GridLevel zLevel = GridLevel.levelOfZ(getZ(gridID));
		BigDecimal length = BigDecimal.valueOf(zLevel.length());
		return x.add(length).doubleValue();
	}
	
	/**
	 * 根据ID获取网格右上角的经度，未使用BigDecimal
	 * 
	 * @param gridID
	 *            网格ID
	 * @return 右上角的经度，精确到小数点后第四位
	 */
	public static double getXMaxFast(long gridID) {
		double x = getX(gridID);
		GridLevel zLevel = GridLevel.levelOfZ(getZ(gridID));		
		return x + zLevel.length();
	}
	

	/**
	 * 根据ID获取网格右上角的纬度
	 * 
	 * @param gridID
	 *            网格ID
	 * @return 右上角的纬度，精确到小数点后第四位
	 */
	public static double getYMax(long gridID) {
		BigDecimal y = BigDecimal.valueOf(getY(gridID));
		GridLevel zLevel = GridLevel.levelOfZ(getZ(gridID));
		BigDecimal length = BigDecimal.valueOf(zLevel.length());
		return y.add(length).doubleValue();
	}
	
	/**
	 * 根据ID获取网格右上角的纬度，未使用BigDecimal
	 * 
	 * @param gridID
	 *            网格ID
	 * @return 右上角的纬度，精确到小数点后第四位
	 */
	public static double getYMaxFast(long gridID) {
		double y = getY(gridID);
		GridLevel zLevel = GridLevel.levelOfZ(getZ(gridID));
		return y + zLevel.length();
	}

	/**
	 * 根据ID获取网格右上角的经纬度。严格来讲右上角这一点不属于该网格
	 * 
	 * @param gridID
	 *            网格ID
	 * @return 右上角经纬度，精确到小数点后第四位
	 */
	public static Point2D getUpperRight(long gridID) {
		GridLevel zLevel = GridLevel.levelOfZ(getZ(gridID));
		double x = getX(gridID);
		double y = getY(gridID);
		double length = zLevel.length();
		return new Point2D.Double(x + length, y + length);
	}

	/**
	 * 根据ID获取网格覆盖范围, 使用了BigDecimal确保精度，但速度稍慢。查询经纬度对应网格编号请使用getGridID系列方法
	 * 
	 * @param gridID
	 *            网格ID
	 * @return [xmin, xmax, ymin, ymax]，精确到小数点后第四位
	 */
	public static double[] getBound(long gridID) {
		double[] bound = new double[4];
		int z = (int) (gridID % 100) / 10;
		GridLevel zLevel = GridLevel.levelOfZ(z);
		//double length = zLevel.length();
		BigDecimal length = BigDecimal.valueOf(zLevel.length());
		gridID /= 100;
		// ymin = y
		bound[2] = (gridID % 1000000) / 10000.00;
		// ymax = y+length\	
		//BIgDecimal可替换为double, 但精度有问题
		//bound[3] = bound[2]+length;
		bound[3] = length.add(BigDecimal.valueOf(bound[2])).doubleValue();
		gridID /= 1000000;
		// xmin = x
		bound[0] = gridID / 10000.00;
		// xman = x+length
		//bound[1] = bound[0]+length;
		bound[1] = length.add(BigDecimal.valueOf(bound[0])).doubleValue();
		return bound;
	}
	public static String getStringBound(long gridID){
		double[] bound = getBound(gridID);
		return bound[0]+" "+bound[3]+","+bound[1]+" "+bound[3]+","+bound[1]+" "+bound[2]+","+bound[0]+" "+bound[2];
	}
	/**
	 * 根据ID获取网格覆盖范围， 未使用BigDecimal。查询经纬度对应网格编号请使用getGridID系列方法
	 * 
	 * @param gridID
	 *            网格ID
	 * @return [xmin, xmax, ymin, ymax]，精确到小数点后第四位
	 */
	public static double[] getBoundFast(long gridID) {
		double[] bound = new double[4];
		int z = (int) (gridID % 100) / 10;
		GridLevel zLevel = GridLevel.levelOfZ(z);
		double length = zLevel.length();
		//BigDecimal length = BigDecimal.valueOf(zLevel.length());
		gridID /= 100;
		// ymin = y
		bound[2] = (gridID % 1000000) / 10000.00;
		// ymax = y+length\	
		//BIgDecimal可替换为double, 但精度有问题
		bound[3] = bound[2]+length;
		//bound[3] = length.add(BigDecimal.valueOf(bound[2])).doubleValue();
		gridID /= 1000000;
		// xmin = x
		bound[0] = gridID / 10000.00;
		// xman = x+length
		bound[1] = bound[0]+length;
		//bound[1] = length.add(BigDecimal.valueOf(bound[0])).doubleValue();
		return bound;
	}
	/**
	 * 获取网格中心点
	 * @param gridId 网格id
	 * @return 中心点坐标
	 */
	public static Point2D.Double getCenter(Long gridId){
		double[] bound = Grid.getBound(gridId);
		double x = (bound[0]+bound[1])/2;
		double y = (bound[2]+bound[3])/2;
		return new Point2D.Double(x, y);
	}
	/**
	 * 测试给定的ID是否合法（判别条件待确认，目前一律返回{@code true}）
	 * <br><b>未完成！！</b>
	 * @param gridID
	 *            网格ID
	 * @return 测试结果
	 */
	public boolean isValidID(long gridID) {
		// TODO
		return true;
	}

	/**
	 * 计算给定坐标的0.1度（10公里）网格ID
	 * 
	 * @param x
	 *            经度
	 * @param y
	 *            纬度
	 * @return 网格ID
	 */
	public static long getGridID10KM(double x, double y) {
		return getGridID(x, y, Z_10KM);
	}
	
	/**
	 * 计算给定坐标的0.05度（5公里）网格ID
	 * 
	 * @param x
	 *            经度
	 * @param y
	 *            纬度
	 * @return 网格ID
	 */
	public static long getGridID5KM(double x, double y) {
		return getGridID(x, y, Z_5KM);
	}
	
	/**
	 * 计算给定坐标的0.02度（2公里）网格ID
	 * 
	 * @param x
	 *            经度
	 * @param y
	 *            纬度
	 * @return 网格ID
	 */
	public static long getGridID2KM(double x, double y) {
		return getGridID(x, y, Z_2KM);
	}
	
	/**
	 * 计算给定坐标的0.01度（1公里）网格ID
	 * 
	 * @param x
	 *            经度
	 * @param y
	 *            纬度
	 * @return 网格ID
	 */
	public static long getGridID1KM(double x, double y) {
		return getGridID(x, y, Z_1KM);
	}
	
	/**
	 * 计算给定坐标的0.005度（500米）网格ID
	 * 
	 * @param x
	 *            经度
	 * @param y
	 *            纬度
	 * @return 网格ID
	 */
	public static long getGridID500M(double x, double y) {
		return getGridID(x, y, Z_500M);
	}
	
	/**
	 * 计算给定坐标的0.002度（200米）网格ID
	 * 
	 * @param x
	 *            经度
	 * @param y
	 *            纬度
	 * @return 网格ID
	 */
	public static long getGridID200M(double x, double y) {
		return getGridID(x, y, Z_200M);
	}
	
	/**
	 * 计算给定坐标的0.001度（100米）网格ID
	 * 
	 * @param x
	 *            经度
	 * @param y
	 *            纬度
	 * @return 网格ID
	 */
	public static long getGridID100M(double x, double y) {
		return getGridID(x, y, Z_100M);
	}

	/**
	 * 计算指定坐标的网格ID
	 * @param x 经度
	 * @param y 纬度
	 * @param level 网格级别
	 * @return 网格ID
	 */
	public static long getGridID(double x, double y, GridLevel level) {
		int z = level.z();
		return getGridID(x, y, z);
	}

	/**
	 * 根据经纬度和网格级别计算网格ID
	 * 
	 * @param x
	 *            经度
	 * @param y
	 *            纬度
	 * @param z
	 *            网格级别代码z，见<b>Grid.Z_近似大小</b> 系列常量
	 * @return 网格ID。如果使用了未定义的z将抛出{@code InvalidParameterException}
	 */
	public static long getGridID(double x, double y, int z) {
		// xxxxxxx 后面共8位 yyyyyy后共2位，z后共1位
		return getVVV(x, z) * 100000000 + getVVV(y, z) * 100 + z * 10 + getR();
	}

	// 按照z的粒度取整、乘以10000扩张为整数，xxxxxxx和yyyyyy都是这样计算的
	private static long getVVV(double x, int z) {
		long id = 0;
		switch (z) {
		
		case Z_100M:
			return ((long)(x*1000))*10;
		
		case Z_200M:
			id = (long)(x*10000);
			return id-id%20;
		
		case Z_500M:
			id = (long)(x*10000);
			return id-id%50;			
		
		case Z_1KM:
			return ((long)(x*100))*100;
		
		case Z_2KM:
			id = (long)(x*10000);
			return id-id%200;
		
		case Z_5KM:
			id = (long)(x*10000);
			return id-id%500;			
		
		case Z_10KM:
			return ((long)(x*10))*1000;
		
		default:
			throw new InvalidParameterException("Invalid z value.");
		}
	}

	/**
	 * 网格级别转换：根据给定网格ID的左下角坐标求取它所对应的其他级别的网格ID
	 * @param gridID 指定ID
	 * @param level 目标级别
	 * @return 目标级别的对应网格ID
	 */
	public static long changeLevel(long gridID, GridLevel level){
		double x = Grid.getX(gridID);
		double y = Grid.getY(gridID);
		return Grid.getGridID(x, y, level);
	}
	
	
	// 目前默认为0
	private static int getR() {
		return 0;
	}
	
	
 public static void main(String[] args) {
		double x = 123.4567;
		double y = 30.1234;
		long gridID500M = getGridID500M(x, y);
		String stringBound = getStringBound(gridID500M);
		System.out.println(stringBound);
		long id =((long)(x*10000)-((long)(x*10000) % 50))*100000000+((long)(y*10000)-((long)(y*10000) % 50))*100+40; 
		System.out.println(id);
	}
}
