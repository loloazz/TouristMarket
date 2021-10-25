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
package com.shujia.grid.entity;

public class GridEntity {
	private double ymax = 0.0D;
	private double xmax = 0.0D;
	private double ymin = 0.0D;
	private double xmin = 0.0D;
	private double cenlat = 0.0D;
	private double cenlng = 0.0D;
	private int length;
	private String adminCode;
	private int prarentGridID;
	private int objectid;

	public GridEntity(int objectid, int prarentGridID, String adminCode,
			int length, double xmin, double ymin, double xmax, double ymax) {
		this.objectid = objectid;
		this.prarentGridID = prarentGridID;
		this.adminCode = adminCode;
		this.length = length;
		this.xmin = xmin;
		this.ymin = ymin;
		this.xmax = xmax;
		this.ymax = ymax;
		this.cenlng = ((xmin + xmax) / 2.0D);
		this.cenlat = ((ymin + ymax) / 2.0D);
	}

	public int getObjectid() {
		return this.objectid;
	}

	public int getPrarentGridID() {
		return this.prarentGridID;
	}

	public String getAdminCode() {
		return this.adminCode;
	}

	public int getLength() {
		return this.length;
	}

	public double getCenlng() {
		return this.cenlng;
	}

	public double getCenlat() {
		return this.cenlat;
	}

	public double getXmin() {
		return this.xmin;
	}

	public double getYmin() {
		return this.ymin;
	}

	public double getXmax() {
		return this.xmax;
	}

	public double getYmax() {
		return this.ymax;
	}

	public int getArea() {
		return this.length * this.length;
	}

	public String getPts() {
		return "[[" + this.xmin + "," + this.ymin + "],[" + this.xmin + ","
				+ this.ymax + "],[" + this.xmax + "," + this.ymin + "],["
				+ this.xmax + "," + this.ymax + "]]";
	}

	public String toString() {
		return this.objectid + ":" + getPts();
	}
}
