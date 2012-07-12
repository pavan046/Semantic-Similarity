package org.knoesis.microsoft.ngram;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.knoesis.http.connector.HttpConnector;
/**
 * This class connects to the word breaking service of the microsoft. 
 * @author pavan
 *
 */
public class WordBreaker {
	//TODO: This has to be moved to a constants file or a config file
	private String API_URL = "http://web-ngram.research.microsoft.com/ngramwordbreaker/break.svc/";
	private HttpConnector connection;
	private Map<String,String> params;
	
	public WordBreaker(){
		connection = new HttpConnector(API_URL);
	}
	
	/**
	 * This method gives you the correct word with all the spaces (word breaking) using
	 * micrsoft ngranword breaker
	 * @param word
	 * @return
	 */
	public String getBreakedWord(String word){
		params = new HashMap<String, String>();		
		params.put("format", "json");
		params.put("p", word);		
		
		return parseNgramJson(connection.response(params, false));
	}
	
	/**
	 * This method takes in jsonResponse from http as input, parses it and gives you the
	 * word with necessary spaces.
	 * @param jsonResponse
	 * @throws JSONException
	 */
	public String parseNgramJson(String jsonResponse) {
		String brokenWord = "";
		try {
			JSONArray wordBreaks = new JSONArray(jsonResponse);		
			JSONObject topScoredWordBreak = (JSONObject) wordBreaks.get(0);		
			JSONArray wordsArray = topScoredWordBreak.getJSONArray("Subtokens");						
			for(int i =0; i < wordsArray.length();i++){
				brokenWord = brokenWord +" "+ wordsArray.getString(i);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return brokenWord.trim();
		
	}
	
	public static void main(String[] args) throws JSONException {
		WordBreaker test = new WordBreaker();		
		System.out.println(test.getBreakedWord("BarackObama"));
	}
	
}
