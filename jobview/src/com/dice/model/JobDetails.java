package com.dice.model;

import java.util.List;

import com.dice.service.DetailViewServiceImpl.Recommendation;

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
	
	public List<Recommendation> getRecommendations() {
		return recommendations;
	}
	public void setRecommendations(List<Recommendation> recommendations) {
		this.recommendations = recommendations;
	}

	public List<ResultItem> getMltItems() {
		return mltItems;
	}
	public void setMltItems(List<ResultItem> mltItems) {
		this.mltItems = mltItems;
	}

	private String jobTitle;
	private String jobDescription;
	private String companyName;
	private String skills;
	private String datePosted;
	private String formattedLocation;
	private List<Recommendation> recommendations;
	private List<ResultItem> mltItems;
}
