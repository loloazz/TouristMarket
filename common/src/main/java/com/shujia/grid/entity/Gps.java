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

import java.text.DecimalFormat;

public class Gps {
	private double wgLat;
	private double wgLon;

	public Gps(double wgLat, double wgLon) {
		DecimalFormat df = new DecimalFormat("0.000000");
		setWgLat(Double.parseDouble(df.format(wgLat)));
		setWgLon(Double.parseDouble(df.format(wgLon)));
	}

	public double getWgLat() {
		return this.wgLat;
	}

	public void setWgLat(double wgLat) {
		this.wgLat = wgLat;
	}

	public double getWgLon() {
		return this.wgLon;
	}

	public void setWgLon(double wgLon) {
		this.wgLon = wgLon;
	}

	public String toString() {
		return this.wgLat + "," + this.wgLon;
	}
}
