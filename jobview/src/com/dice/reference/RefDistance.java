package com.dice.reference;

import java.util.ArrayList;
import java.util.List;

/**
 * User: duket
 * Date: 4/6/12
 * Time: 9:06 AM
 */
public class RefDistance {
	private int distance;
	private String description;

	public RefDistance(int distance, String description) {
		this.distance = distance;
		this.description = description;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static List<RefDistance> getDistances() {
		ArrayList<RefDistance> a = new ArrayList<RefDistance>();
		a.add(new RefDistance(5, "5 miles"));
		a.add(new RefDistance(10, "10s"));
		a.add(new RefDistance(20, "20 miles"));
		a.add(new RefDistance(30, "30 miles"));
		a.add(new RefDistance(40, "40 miles"));
		a.add(new RefDistance(50, "50 miles"));
		a.add(new RefDistance(75, "75 miles"));
		a.add(new RefDistance(100, "100 miles"));
		return a;
	}
}
