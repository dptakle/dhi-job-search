package com.dice.model;

public class ResultItem {
	private String detailUrl;
	private String jobTitle;
	private String company;
	private String location;
	private String date;
	private String id;

	protected ResultItem() {
		/****/
	}

	protected ResultItem(String detailUrl, String jobTitle, String company, String location, String date, int ffff) {
		this.detailUrl = detailUrl;
		this.jobTitle = jobTitle;
		this.company = company;
		this.location = location;
		this.date = date;
	}

	public String getDetailUrl() {
		//return detailUrl;
		return "http://localhost:8080/jobview/detail.html?k=" + id;
	}

	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "<br/>\n<br/>\n\t\tResultItem [<br/>\n&#10147;\t\t\tdetailUrl=" + detailUrl + 
				"<br/>\n&#10147;\t\t\tjobTitle=" + jobTitle + 
				"<br/>\n&#10147;\t\t\tcompany=" + company + 
				"<br/>\n&#10147;\t\t\tlocation=" + location + 
				"<br/>\n&#10147;\t\t\tdate=" + date + 
				"<br/>\n&#10147;\t\t\tid=" + id + "<br/>\n\t\t]";
	}
}
