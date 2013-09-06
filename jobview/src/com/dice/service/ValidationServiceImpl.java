package com.dice.service;

import com.dice.model.Credentials;
import com.dice.util.Config;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.Format;

/**
 * User: duket
 * Date: 4/10/12
 * Time: 6:47 PM
 */
@Service
public class ValidationServiceImpl implements ValidationService {
	public boolean validateCredentials(Credentials credentials) {
		InputStream responseStream = null;
		StringBuffer urlBuf;
		URL url;
		HttpURLConnection httpConn = null;
		URLConnection urlConn;
		int i;
		StringBuilder responseBuffer = new StringBuilder();

		try {
			///api/rest/employer/validate?user=duket@dice.com&pass=z1pper
			urlBuf = new StringBuffer(Config.get("secure.api.url.base"));
			urlBuf.append("/api/rest/employer/validate?");
			urlBuf.append("user=");
			urlBuf.append(credentials.getU());
			urlBuf.append("&pass=");
			urlBuf.append(credentials.getP());


			url = new URL(urlBuf.toString());
			urlConn = url.openConnection();
			if (urlConn instanceof HttpURLConnection) {
				httpConn = (HttpURLConnection)urlConn;
				httpConn.setRequestMethod("GET");
				httpConn.connect();
				responseStream = httpConn.getInputStream();
				while ((i = responseStream.read()) != -1) {
					responseBuffer.append((char)i);
				}
				System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXX: " + responseBuffer.toString());
				if (responseBuffer.toString().trim().equals("1")) {
					return true;
				}
			}
		} catch(Exception x) {
			x.printStackTrace();
		} finally {
			if (responseStream != null)  try { responseStream.close(); } catch(Exception x) { /****/ }
			if (httpConn != null)  try { httpConn.disconnect(); } catch(Exception x) { /****/ }
		}
		return false;
	}
}
