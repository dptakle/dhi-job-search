package com.dice.model;

/**
 * Created by IntelliJ IDEA.
 * User: duket
 * Date: 4/6/12
 * Time: 1:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class DisplaySeeker {
	private String name;
	private String email;
	private String desiredPosition;
	private String location;
	private String skills;
	private String detailLink;

	public String getDetailLink() {
		return detailLink;
	}

	public void setDetailLink(String detailLink) {
		this.detailLink = detailLink;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDesiredPosition() {
		return desiredPosition;
	}

	public void setDesiredPosition(String desiredPosition) {
		this.desiredPosition = desiredPosition;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}
}
