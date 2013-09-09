package com.dice.model;

public class JobDetails {
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getJobDescription() {
		return jobDescription;
	}
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getSkills() {
		return skills;
	}
	public void setSkills(String skills) {
		this.skills = skills;
	}
	public String getDatePosted() {
		return datePosted;
	}
	public void setDatePosted(String datePosted) {
		this.datePosted = datePosted;
	}
	public String getFormattedLocation() {
		return formattedLocation;
	}
	public void setFormattedLocation(String formattedLocation) {
		this.formattedLocation = formattedLocation;
	}
	private String jobTitle;
	private String jobDescription;
	private String companyName;
	private String skills;
	private String datePosted;
	private String formattedLocation;
}
