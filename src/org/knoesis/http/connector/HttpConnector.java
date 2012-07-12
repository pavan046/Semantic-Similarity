package org.knoesis.http.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * The objective of this class is given a URL (web service)
 * with appropriate parameters returns the results. POST/GET
 * 
 * @author pavan
 *
 */
public class HttpConnector {

	private String apiURL; 
	private Map<String, String> params;


	public HttpConnector(String apiURL) {
		this.apiURL = apiURL;
	}

	/**
	 * Responds with string of the appropriate format.
	 * @param params -- Parameters in Map<param name, param value>
	 * @return String 
	 */
	public String response(Map<String, String> params, boolean post){
		String requestParams = "";
		for(String param: params.keySet())
			requestParams += param+"="+params.get(param)+"&";
		try {
			if(post)
				return post(requestParams);
			else 
				return get(requestParams);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * The function opens a url connection to apiURL with the appropriate parameters and 
	 * responds with the output as a String
	 * 
	 * @param parameters
	 * @return
	 * @throws IOException
	 */
	private String post(String parameters) throws IOException {
		URL queryURL = new URL(apiURL);
		URLConnection urlConn = queryURL.openConnection();
		((HttpURLConnection) urlConn).setRequestMethod("POST");
		urlConn.setDoOutput(true);
		urlConn.setDoInput(true);
		urlConn.setUseCaches(false);

		OutputStream oStream = urlConn.getOutputStream();

		oStream.write(parameters.getBytes());
		oStream.flush();
		oStream.close();

		BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
		StringBuilder response = new StringBuilder(); String aLine;
		while ((aLine = in.readLine()) != null) {
			response.append(aLine);
		}

		return response.toString();
	}
	/**
	 * The below function does a get if there is no option of post from the websevice
	 * as in webngram by microsoft.
	 * 
	 * The GET Request cannot send parameters using an ouput stream and also the 
	 * setDoOutput should be false. Please diff the above fuction post to get to know the details 
	 * @param parameters
	 * @return
	 * @throws IOException
	 */
	private String get(String parameters) throws IOException {
		URL queryURL = new URL(apiURL+"?"+parameters);
		URLConnection urlConn = queryURL.openConnection();
		((HttpURLConnection) urlConn).setRequestMethod("GET");

		BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
		StringBuilder response = new StringBuilder(); String aLine;
		while ((aLine = in.readLine()) != null) {
			response.append(aLine);
		}

		return response.toString();

	}

	public static void main(String[] args) {
		HttpConnector conn = new HttpConnector("http://web-ngram.research.microsoft.com/ngramwordbreaker/break.svc");
		Map<String, String> parameters = new HashMap<String, String>();
		//		parameters.put("action", "parse");
		//		parameters.put("prop", "links");
		parameters.put("p", "supertuesday");
		parameters.put("format", "json");
		System.out.println(conn.response(parameters, false));
	}

}
