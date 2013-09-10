package com.dice.service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.dice.model.*;
import com.google.gson.Gson;

import org.springframework.stereotype.Service;

/**
 * User: duket
 */
@Service
public class DetailViewServiceImpl implements DetailViewService {
	private static String jobViewServer = null;
	private static final String detailUrl = "http://dev.api.dice.com/job-search/dice/detail?dockey=";
	private static final String mltUrl = "http://dev.api.dice.com/job-search/dice/mlt?id=";
	private static final String recommendationUrl = "http://dhi-services-elb-962965289.us-east-1.elb.amazonaws.com:8080/dhi.aws.data.service/recojobsbyjob/?verbose=1&dockey=";
	public static int MAX_RECOMMENDAIONS = 4;

	public boolean getDetails(Search theData, JobDetails details) {
		JobDetails currentDetails;
		MLTAnswer mltAnswer;
		try {
			System.out.println("getDetails() called with k=" + theData.getK());
			currentDetails = setJobDetails(theData.getK());
			details.setCompanyName(currentDetails.getCompanyName());
			details.setDatePosted(currentDetails.getDatePosted());
			details.setJobDescription(currentDetails.getJobDescription());
			details.setJobTitle(currentDetails.getJobTitle());
			details.setSkills(currentDetails.getSkills());
			details.setFormattedLocation(currentDetails.getFormattedLocation());

			mltAnswer = setMLT(theData.getK(), details.getFormattedLocation());
            if (mltAnswer.getResults().size() > MAX_RECOMMENDAIONS) {
            	details.setMltItems(mltAnswer.getResults().subList(0, MAX_RECOMMENDAIONS));
            } else {
            	details.setMltItems(mltAnswer.getResults());
            }
			
			getRecommendations(theData.getK());
			details.setRecommendations(getRecommendations(theData.getK()));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private JobDetails setJobDetails(String dockey) {
		Gson gson = new Gson();
		URLConnection urlConn;
		HttpURLConnection httpConn;
		URL url; 
		StringBuilder urlBuf = new StringBuilder(detailUrl);
        InputStream responseStream = null;
        int c;
        StringBuilder jsonBuf = new StringBuilder();
        JobDetails jobDetails = new JobDetails();
      
		try {
			urlBuf.append(dockey);
            url = new URL(urlBuf.toString());
            urlConn = url.openConnection();
            if (urlConn instanceof HttpURLConnection) {
                httpConn = (HttpURLConnection)urlConn;
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                responseStream = httpConn.getInputStream();
                while ((c=responseStream.read()) != -1) {
                	jsonBuf.append((char)c);
                }
                responseStream.close();
            }
 
            jobDetails = gson.fromJson(jsonBuf.toString(), JobDetails.class);
			return jobDetails;
		} catch (Exception x) {
			x.printStackTrace();
			return jobDetails;
		}
	}

	private MLTAnswer setMLT(String dockey, String city) {
		Gson gson = new Gson();
		URLConnection urlConn;
		HttpURLConnection httpConn;
		URL url; 
		StringBuilder urlBuf = new StringBuilder(mltUrl);
        InputStream responseStream = null;
        int c;
        StringBuilder jsonBuf = new StringBuilder();
        MLTAnswer mltAnswer = new MLTAnswer();
      
		try {
			urlBuf.append(dockey).append("&city=").append(URLEncoder.encode(city, "UTF-8"));
            url = new URL(urlBuf.toString());
            urlConn = url.openConnection();
            urlConn.setRequestProperty("Accept", "application/json");
            if (urlConn instanceof HttpURLConnection) {
                httpConn = (HttpURLConnection)urlConn;
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                responseStream = httpConn.getInputStream();
                while ((c=responseStream.read()) != -1) {
                	jsonBuf.append((char)c);
                }
                responseStream.close();
            }
 
            mltAnswer = gson.fromJson(jsonBuf.toString(), MLTAnswer.class);
			return mltAnswer;
		} catch (Exception x) {
			x.printStackTrace();
			return mltAnswer;
		}
	}

	private List<Recommendation> getRecommendations(String dockey) {
		Gson gson = new Gson();
		URLConnection urlConn;
		HttpURLConnection httpConn;
		URL url; 
		StringBuilder urlBuf = new StringBuilder(recommendationUrl);
        InputStream responseStream = null;
        int c;
        StringBuilder jsonBuf = new StringBuilder();
        ArrayList<Recommendation> recommendations = new ArrayList<Recommendation>();
        ApiResponse response;
      
		try {
			urlBuf.append(dockey);
            url = new URL(urlBuf.toString());
            urlConn = url.openConnection();
            if (urlConn instanceof HttpURLConnection) {
                httpConn = (HttpURLConnection)urlConn;
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                responseStream = httpConn.getInputStream();
                while ((c=responseStream.read()) != -1) {
                	jsonBuf.append((char)c);
                }
                responseStream.close();
            }
 
            response = gson.fromJson(jsonBuf.toString(), ApiResponse.class);

            Iterator<ApiResponseJob> iterator = response.getResponse().getJobs().iterator();
            Recommendation currentRecommendation;
            int count = 0;
            while (iterator.hasNext() && count++ < MAX_RECOMMENDAIONS) {
            	ApiResponseJob job = iterator.next();
            	currentRecommendation = new Recommendation();
            	currentRecommendation.setCompanyName(job.getDice_id());
            	currentRecommendation.setJobTitle(job.getJob_title());
            	currentRecommendation.setLocation(job.getCity() + ", " + job.getRegion());
            	currentRecommendation.setScore(job.getCf_score());
            	currentRecommendation.setUrl("http://" + getJobViewServer()  + "/jobview/detail.html?k=" + job.getJob_doc_key().trim());
            	recommendations.add(currentRecommendation);
            }
			return recommendations;
		} catch (Exception x) {
			x.printStackTrace();
			return recommendations;
		}
	}

	public class ApiResponse {
		private ApiResponseActiveJob response;

		@Override
		public String toString() {
			return "ApiResponse [response=" + response + "]";
		}

		public ApiResponseActiveJob getResponse() {
			return response;
		}

		public void setResponse(ApiResponseActiveJob response) {
			this.response = response;
		}
		
	}

	public class ApiResponseActiveJob {
		private ApiResponseJob active_job;
		private List<ApiResponseJob> jobs;
		@Override
		public String toString() {
			return "ApiResponseActiveJob [active_job=" + active_job + ", jobs="
					+ jobs + "]";
		}
		public ApiResponseJob getActive_job() {
			return active_job;
		}
		public void setActive_job(ApiResponseJob active_job) {
			this.active_job = active_job;
		}
		public List<ApiResponseJob> getJobs() {
			return jobs;
		}
		public void setJobs(List<ApiResponseJob> jobs) {
			this.jobs = jobs;
		}

	}

	public class ApiResponseJob {
        private String job_doc_key;
        private String dice_id;
        private String position_id;
        private String rating;
        private String distance;
        private String skill_score;
        private String title_score;
        private String cf_score;
        private String job_title;
        private String city;
        private String region;
        private String country;
        
 
		@Override
		public String toString() {
			return "ApiResponseJob [job_doc_key=" + job_doc_key + ", dice_id="
					+ dice_id + ", position_id=" + position_id + ", rating="
					+ rating + ", distance=" + distance + ", skill_score="
					+ skill_score + ", title_score=" + title_score
					+ ", cf_score=" + cf_score + ", job_title=" + job_title
					+ ", city=" + city + ", region=" + region + ", country="
					+ country + "]";
		}
		public String getJob_doc_key() {
			return job_doc_key;
		}
		public void setJob_doc_key(String job_doc_key) {
			this.job_doc_key = job_doc_key;
		}
		public String getDice_id() {
			return dice_id;
		}
		public void setDice_id(String dice_id) {
			this.dice_id = dice_id;
		}
		public String getPosition_id() {
			return position_id;
		}
		public void setPosition_id(String position_id) {
			this.position_id = position_id;
		}
		public String getRating() {
			return rating;
		}
		public void setRating(String rating) {
			this.rating = rating;
		}
		public String getDistance() {
			return distance;
		}
		public void setDistance(String distance) {
			this.distance = distance;
		}
		public String getSkill_score() {
			return skill_score;
		}
		public void setSkill_score(String skill_score) {
			this.skill_score = skill_score;
		}
		public String getTitle_score() {
			return title_score;
		}
		public void setTitle_score(String title_score) {
			this.title_score = title_score;
		}
		public String getCf_score() {
			return cf_score;
		}
		public void setCf_score(String cf_score) {
			this.cf_score = cf_score;
		}
		public String getJob_title() {
			return job_title;
		}
		public void setJob_title(String job_title) {
			this.job_title = job_title;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getRegion() {
			return region;
		}
		public void setRegion(String region) {
			this.region = region;
		}
		public String getCountry() {
			return country;
		}
		public void setCountry(String country) {
			this.country = country;
		}
        
	}

	public class Recommendation {
		private String jobTitle;
		private String location;
		private String url;
		private String score;
		private String companyName;
		public String getJobTitle() {
			return jobTitle;
		}
		public void setJobTitle(String jobTitle) {
			this.jobTitle = jobTitle;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getScore() {
			return score;
		}
		public void setScore(String score) {
			this.score = score;
		}
		public String getCompanyName() {
			return companyName;
		}
		public void setCompanyName(String companyName) {
			this.companyName = companyName;
		}
	}

	private static synchronized String getJobViewServer() {
		if (jobViewServer == null) {
			Properties props = new Properties();
			try {
				props.load(new FileReader("/etc/dhi/conf/jobview.properties"));
				jobViewServer = props.get("jobview.server").toString().trim();
			} catch (Exception e) {
				e.printStackTrace();
				jobViewServer = "www.takle.org";
			}
			
		}
		return jobViewServer;
	}
}
