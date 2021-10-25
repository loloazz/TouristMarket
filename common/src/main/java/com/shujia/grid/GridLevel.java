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


import java.util.ArrayList;
import java.util.List;


/**
 * 基础网格级别，经纬度网格
 * 
 * @author Lin Dong
 *
 */
public enum GridLevel {

	/**
	 * 0.1度（约10公里）
	 */
	GRID_0_1,

	/**
	 * 0.05度（约5公里）
	 */
	GRID_0_05,

	/**
	 * 0.02度（约2公里）
	 */
	GRID_0_02,

	/**
	 * 0.01度（约1公里）
	 */
	GRID_0_01,

	/**
	 * 0.005度（约500米）
	 */
	GRID_0_005,

	/**
	 * 0.002度（约200米）
	 */
	GRID_0_002,

	/**
	 * 0.001度（约100米）
	 */
	GRID_0_001;

	/**
	 * 返回网格的高度/宽度（经纬度）
	 * 
	 * @param level
	 *            网格级别
	 * @return 网格高度/宽度
	 */
	private static double value(GridLevel level) {
		switch (level) {
		case GRID_0_001:
			return 0.00100;
		case GRID_0_002:
			return 0.00200;
		case GRID_0_005:
			return 0.00500;
		case GRID_0_01:
			return 0.01000;
		case GRID_0_02:
			return 0.02000;
		case GRID_0_05:
			return 0.05000;
		case GRID_0_1:
			return 0.10000;
		default:
			// 实际上不应该执行
			return Double.NaN;
		}
	}

	/**
	 * 返回网格的大小
	 * 
	 * @return 网格高度/宽度（单位：度）
	 */
	public double length() {
		return this.value();
	}

	/**
	 * 返回网格的高度/宽度（经纬度）
	 * 
	 * @return 网格高度/宽度
	 */
	public double value() {
		return GridLevel.value(this);
	}

	/**
	 * 返回网格的近似高/宽
	 * 
	 * @param level
	 *            网格级别
	 * @return 网格近似高/宽，单位：米
	 */
	private static double lengthAlias(GridLevel level) {
		// 1度约等于100km
		return level.value() * 100000;
	}

	/**
	 * 返回网格的近似高/宽
	 * 
	 * @return 网格近似高/宽，单位：米
	 */
	public double lengthAlias() {
		return lengthAlias(this);
	}

	/**
	 * 返回网格的准确宽度。网格对应的宽度随纬度变化，即便同城也有微小变化，但大多数场景下可以忽略
	 * 
	 * @param level
	 *            网格级别 * @param y 网格左下角的纬度
	 * @return 网格宽度，单位：米
	 */
	private static double lengthX(GridLevel level, double y) {
		double delta = level.value();
		return GISUtil.getDistance(0, y, 0 + delta, y);
	}

	/**
	 * 返回网格的准确宽度。网格对应的宽度随纬度变化，即便同城也有微小变化，但大多数场景下可以忽略
	 * 
	 * @param y
	 *            网格左下角的纬度
	 * @return 网格宽度，单位：米
	 */
	public double lengthX(double y) {
		return lengthX(this, y);
	}

	/**
	 * 返回网格的准确高度
	 * 
	 * @param level
	 *            网格级别
	 * @return 网格高度，单位：米
	 */
	private static double lengthY(GridLevel level) {
		// 1度约等于100km
		return level.value() * Constants.M_PER_DEGREE_Y;
	}

	/**
	 * 返回网格的准确高度
	 * 
	 * @return 网格高度，单位：米
	 */
	public double lengthY() {
		return lengthY(this);
	}

	/**
	 * 根据近似距离判断网格级别
	 * 
	 * @param length
	 *            近似距离（单位：米）。目前仅支持10000、5000、2000、1000、500、200、100这几种
	 * @return 对应网格级别，如果不存在返回{@code null}
	 */
	public static GridLevel levelOfAlias(int length) {
		switch (length) {
		case 10000:
			return GRID_0_1;
		case 5000:
			return GRID_0_05;
		case 2000:
			return GRID_0_02;
		case 1000:
			return GRID_0_01;
		case 500:
			return GRID_0_005;
		case 200:
			return GRID_0_002;
		case 100:
			return GRID_0_001;
		default:
			// 不支持的网格宽度
			return null;
		}
	}

	/**
	 * 返回网格的z代码<br>
	 * <b>Z</b>: 网格级别，1位，用于区分网格级别。列表见下。<br>
	 * <table border="1">
	 * <tr>
	 * <td>编号</td>
	 * <td>级别</td>
	 * <td>右上角偏移量（度）</td>
	 * <td>备注</td>
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
	 * 
	 * @param level
	 *            网格级别
	 * @return 级别的z代号
	 */
	private static int z(GridLevel level) {
		switch (level) {
		case GRID_0_001:
			return 1;
		case GRID_0_002:
			return 2;
		case GRID_0_005:
			return 4;
		case GRID_0_01:
			return 5;
		case GRID_0_02:
			return 6;
		case GRID_0_05:
			return 8;
		case GRID_0_1:
			return 9;
		default:
			// 实际上不应该执行
			return -1;
		}
	}

	/**
	 * 返回网格的z代码<br>
	 * <b>Z</b>: 网格级别，1位，用于区分网格级别。列表见下。<br>
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
	 * 
	 * @return 级别的z代号
	 */
	public int z() {
		return z(this);
	}

	/**
	 * 返回z代码对应的网个级别<br>
	 * @param z 网格级别，1位，用于区分网格级别。列表见下。<br>
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
	 * 
	 * @return z对应的级别，不存在返回{@code null}
	 */
	public static GridLevel levelOfZ(int z) {
		switch (z) {
		case 0:// 预留
			return null;
		case 1:
			return GRID_0_001;
		case 2:
			return GRID_0_002;
		case 3:// 预留
			return null;
		case 4:
			return GRID_0_005;
		case 5:
			return GRID_0_01;
		case 6:
			return GRID_0_02;
		case 7:// 预留
			return null;
		case 8:
			return GRID_0_05;
		case 9:
			return GRID_0_1;
		default:
			return null;
		}
	}

	/**
	 * 返回相邻级别的网格（更小的）
	 * 
	 * @return 下一级网格，如果不存在则返回{@code null}
	 */
	public GridLevel nextLevel() {
		switch (this) {
		// case GRID_0_001:
		case GRID_0_002:
			return GRID_0_001;
		case GRID_0_005:
			return GRID_0_002;
		case GRID_0_01:
			return GRID_0_005;
		case GRID_0_02:
			return GRID_0_01;
		case GRID_0_05:
			return GRID_0_02;
		case GRID_0_1:
			return GRID_0_05;
		default:
			return null;
		}
	}

	/**
	 * 返回相邻级别的网格（更大的）
	 * 
	 * @return 上一级网格，如果不存在则返回{@code null}
	 */
	public GridLevel lastLevel() {
		switch (this) {
		case GRID_0_001:
			return GRID_0_002;
		case GRID_0_002:
			return GRID_0_005;
		case GRID_0_005:
			return GRID_0_01;
		case GRID_0_01:
			return GRID_0_02;
		case GRID_0_02:
			return GRID_0_05;
		case GRID_0_05:
			return GRID_0_1;
		// case GRID_0_1:
		default:
			return null;
		}
	}

	/**
	 * 相邻级别（上若干级和下若干级，含自身）
	 * @param distance 级差范围
	 * 
	 * @return 相邻的网格级别
	 */
	public List<GridLevel> neighbourLevel(int distance) {
		List<GridLevel> neighbours = new ArrayList<GridLevel>();
		if (distance <= 0) {
			return null;
		}
		neighbours.add(this);
		GridLevel last = this.lastLevel();
		GridLevel next = this.nextLevel();
		if (last != null) {
			neighbours.add(0,last);
		}
		if (next != null) {
			neighbours.add(neighbours.size(),next);
		}
	
		for (int i = 1; i < distance; i++) {				
			if (last != null) {		
				last = last.lastLevel();
				if(last != null){
					neighbours.add(0,last);		
				}
			}

			if (next != null) {
				next = next.nextLevel();
				if(next != null){
					neighbours.add(neighbours.size(),next);			
				}						
			}

		}
		
		return neighbours;
	}

	/**
	 * 相邻级别（上一级和下一级，含自身）
	 * 
	 * @return 相邻的网格级别，可能包含2个或1个元素
	 */
	public List<GridLevel> neighbourLevel() {
		return neighbourLevel(1);
	}
	/*
	public static void main(String[] args) {
		System.out.println(GridLevel.GRID_0_002.neighbourLevel(2));
	}
	 */
}
