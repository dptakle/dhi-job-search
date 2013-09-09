package com.dice.service;

import com.dice.model.*;
import org.springframework.stereotype.Service;

/**
 * User: duket
 */
@Service
public class DetailViewServiceImpl implements DetailViewService {
	public boolean getDetails(Search theData, JobDetails details) {
		try {
			System.out.println("getDetails() called with k=" + theData.getK());
			details.setCompanyName("Howard Foundation");
			details.setDatePosted("09-01-2013");
			details.setJobDescription("Job text block");
			details.setJobTitle("Genetics Tech Level IV");
			details.setSkills("Use instruments");
			details.setFormattedLocation("Ankeny, IA");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
