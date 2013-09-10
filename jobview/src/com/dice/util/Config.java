package com.dice.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

/**
 * User: duket
 * Date: 4/11/12
 * Time: 1:14 PM
 */
public class Config {
	protected static final String CONFIG = "/etc/dhi/conf/jobview.properties";
	protected static Hashtable<String, String> config = null;
	private static String jobViewServer = null;

	public static synchronized boolean initialize() {
		Properties props;
		Enumeration propEnumerator;
		String key, val;

		try {
			config = new Hashtable<String, String>();
			props = new Properties();
			props.load(new FileInputStream(new File(CONFIG)));
			propEnumerator = props.propertyNames();
			while (propEnumerator.hasMoreElements()) {
				key = propEnumerator.nextElement().toString();
				val = props.get(key).toString();
				config.put(key, val);
				System.out.println("com.dice.util.Config: " + key + " => " + val);
			}
		} catch(Exception x) {
			x.printStackTrace();
			config = new Hashtable<String, String>();
			return false;
		}

		return true;
	}

	public static String get(String key) {
		if (config == null) {
			initialize();
		}

		return config.get(key);
	}

	public static boolean containsKey(String key) {
		if (config == null) {
			initialize();
		}

		return config.containsKey(key);
	}


	private Config() {
		/*
		 * This page left blank intentionally
		 */
	}
}
