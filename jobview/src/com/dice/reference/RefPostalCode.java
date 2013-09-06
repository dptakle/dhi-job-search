package com.dice.reference;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * User: duket
 * Date: 4/7/12
 * Time: 10:30 AM
 */
public abstract class RefPostalCode {
	private static final String POSTAL_CODE_DATA = "/etc/dice/conf/postalcode_geocode.dat";
	private static Hashtable<String, String> postalCodeMap = null;

	private static boolean populatePostalCodeMap() {
		BufferedReader reader = null;
		String wrk;
		StringTokenizer toker;
		String key, val;

		try {
			postalCodeMap = new Hashtable<String, String>();
			reader = new BufferedReader(new FileReader(POSTAL_CODE_DATA));
			while ((wrk = reader.readLine()) != null) {
				toker = new StringTokenizer(wrk, "|");
				key = toker.nextToken();
				val = toker.nextToken();
				postalCodeMap.put(key, val);
			}
			return true;
		} catch (Exception x) {
			x.printStackTrace();
			return false;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception x) { /****/ }
			}
		}
	}

	public static Hashtable<String, String> getPostalCodeMap() {
		if (postalCodeMap == null) {
			populatePostalCodeMap();
		}

		return postalCodeMap;
	}
}
