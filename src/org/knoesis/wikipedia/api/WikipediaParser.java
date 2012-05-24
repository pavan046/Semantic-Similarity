package org.knoesis.wikipedia.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.knoesis.http.connector.HttpConnector;

/**
 * This class is responsible for the operations which is related to the action parse from wikipedia API
 * 
 * API: https://en.wikipedia.org/w/api.php
 * 
 *   title               - Title of page the text belongs to
 *                       Default: API
 * text                - Wikitext to parse
 * summary             - Summary to parse
 * page                - Parse the content of this page. Cannot be used together with text and title
 * pageid              - Parse the content of this page. Overrides page
 * redirects           - If the page or the pageid parameter is set to a redirect, resolve it
 * oldid               - Parse the content of this revision. Overrides page and pageid
 * prop                - Which pieces of information to get
 *                        text           - Gives the parsed text of the wikitext
 *                        langlinks      - Gives the language links in the parsed wikitext
 *                        categories     - Gives the categories in the parsed wikitext
 *                        categorieshtml - Gives the HTML version of the categories
 *                        languageshtml  - Gives the HTML version of the language links
 *                        links          - Gives the internal links in the parsed wikitext
 *                        templates      - Gives the templates in the parsed wikitext
 *                        images         - Gives the images in the parsed wikitext
 *                        externallinks  - Gives the external links in the parsed wikitext
 *                        sections       - Gives the sections in the parsed wikitext
 *                        revid          - Adds the revision ID of the parsed page
 *                        displaytitle   - Adds the title of the parsed wikitext
 *                        headitems      - Gives items to put in the <head> of the page
 *                        headhtml       - Gives parsed <head> of the page
 *                        iwlinks        - Gives interwiki links in the parsed wikitext
 *                        wikitext       - Gives the original wikitext that was parsed
 *                       Values (separate with '|'): text, langlinks, languageshtml, categories, categorieshtml, links, templates, images, externallinks,
 *                           sections, revid, displaytitle, headitems, headhtml, iwlinks, wikitext
 *                       Default: text|langlinks|categories|links|templates|images|externallinks|sections|revid|displaytitle
 * pst                 - Do a pre-save transform on the input before parsing it
 *                       Ignored if page, pageid or oldid is used
 * onlypst             - Do a pre-save transform (PST) on the input, but don't parse it
 *                       Returns the same wikitext, after a PST has been applied. Ignored if page, pageid or oldid is used
 * uselang             - Which language to parse the request in
 * section             - Only retrieve the content of this section number
 * disablepp           - Disable the PP Report from the parser output
 * mobileformat        - Return parse output in a format suitable for mobile devices
 *                       One value: wml, html
 * noimages            - Disable images in mobile output
 * mainpage            - Apply mobile main page transformations
 * 
 * @author pavan
 * 
 * TODO: For now the class just retrieves the list of internal links from a wikipedia article using
 * 	     the json format. Later it has to be provided for different formats and for the all the existing 
 * 		 properties
 *
 */
public class WikipediaParser {
	/**
	 * Types supported can be extended if there is a necessity.
	 * @author pavan
	 *
	 */
	public enum Format{
		XML("xml"), JSON("json");
		private String value; 

		private Format(String value){
			this.value = value;
		}

		public String toString(){
			return value;
		}

	}
	private static HttpConnector conn;
	private static String wikipediaPage;
	private Format format; 
	private static Map<String, String> params;
	/**
	 * Constuctor takes in the wikipage you need to parse
	 * 
	 * @param wikpediaPage -- Wikipage
	 * @param format -- Response Expected in which format
	 * @param wikipediaPage 
	 * 
	 * TODO: Make this a singleton
	 */
	public WikipediaParser(String wikipediaPage, Format format) {
		WikipediaParser.wikipediaPage = wikipediaPage;
		this.format = format;
		conn = new HttpConnector(WikipediaConstants.API_URL);
	}
	
	public void setWikipediaPage(String wikipediaPage){
		this.wikipediaPage = wikipediaPage;
	}
	
	public WikipediaParser(String wikipediaPage) {
		WikipediaParser.wikipediaPage = wikipediaPage;
		this.format = Format.JSON;
		conn = new HttpConnector(WikipediaConstants.API_URL);
	}

	/**
	 * initializes the parameters for every call to the connect to the API
	 * 
	 * For example: 1. to get links 
	 * 				2. to get text
	 */
	private static void initializeParams(){
		params = new HashMap<String, String>();
		params.put("action", "parse");
		params.put("page", wikipediaPage);
		params.put("format", Format.JSON.toString());
	}
	
	public String getLinksJson(){
		initializeParams();
		params.put("prop", "links");
		return conn.response(params);
	}

	public List<String> getLinks(){
		initializeParams();
		params.put("prop", "links");
		String json =conn.response(params);
		//System.out.println(json);
		return parseJson("links", conn.response(params));

	}
	/**
	 * TODO: The parsing of JSON should be an Interface and should parse 
	 * 		jsons for all the properties above.
	 * @param property
	 * @param response
	 * @return
	 */
	public static List<String> parseJson(String property, String response) {
		//Not considering the properties as of now
		List<String> links = new ArrayList<String>();
		try {
			JSONObject jsonObj = new JSONObject(response);
			JSONArray linksJson = jsonObj.getJSONObject("parse").getJSONArray("links");
			for(int i=0; i<linksJson.length(); i++){
				links.add(linksJson.getJSONObject(i).getString("*"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return null;
		}
		return links;
	}
	
	public static void main(String[] args) {
		WikipediaParser wikiParser = new WikipediaParser("Independent (politician)");
		System.out.println(wikiParser.getLinks());
	}

}
