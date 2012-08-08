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
 * The
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
	public String response(Map<String, String> params){
		String requestParams = "";
		for(String param: params.keySet())
			requestParams += param+"="+params.get(param)+"&";
		try {
			return post(requestParams);
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
	
	public static void main(String[] args) {
		HttpConnector conn = new HttpConnector("https://en.wikipedia.org/w/api.php");
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("action", "parse");
		parameters.put("prop", "links");
		parameters.put("page", "Pavan");
		parameters.put("format", "json");
		System.out.println(conn.response(parameters));
	}
	
}
