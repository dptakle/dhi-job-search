package com.dice.reference;

import java.util.Hashtable;

/**
 * User: duket
 * Date: 4/8/12
 * Time: 8:52 PM
 */
public abstract class FacetDisplayNameTool {
	private static Hashtable<String, String> t = null;
	private static void initialize() {
		t = new Hashtable<String, String>();
		t.put("skill", "Skill");
		t.put("City", "City");
		t.put("seekerContactState", "Contact state");
		t.put("seekerContactCountry", "Contact country");
		t.put("workAuthorization", "Work authorization");
		t.put("techExperienceRange", "Years experience");
		t.put("employmentType", "Employment type");
		t.put("jobClassification", "Job classification");
		t.put("highestDegree", "Highest degree");
		t.put("securityClearance", "Security clearance");
		t.put("thirdParty", "Third party");
		t.put("diceCommunication", "Dice communication");
		t.put("thirdPartyCommunication", "Third party communication");
		t.put("diceAdvisorCommunication", "Dice Adivisor communication");
		t.put("resumeStatusCommunication", "Resume status communication");
		t.put("willingtoRelocate", "Willing to relocate");
		t.put("searchableStatus", "Searchable");
	}

	public static String lookup(String ugly) {
		if (t == null) {
			initialize();
		}

		if (t.containsKey(ugly)) {
			return t.get(ugly);
		} else {
			return ugly;
		}
	}
}
