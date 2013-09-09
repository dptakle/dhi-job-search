package com.dice.service;

import com.dice.model.*;
import org.springframework.stereotype.Service;

/**
 * User: duket
 */
@Service
public class DetailViewServiceImpl implements DetailViewService {
	private Search theData;

	public boolean getDetails(Search theData, JobDetails details) {
		try {
			details.setCompanyName("Howard Foundation");
			details.setDatePosted("09-01-2013");
			details.setJobDescription("Job text block");
			details.setJobTitle("Genetics Tech Level IV");
			details.setSkills("Use instruments");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
