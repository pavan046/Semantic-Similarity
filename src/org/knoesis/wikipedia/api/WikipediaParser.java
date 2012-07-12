package org.knoesis.wikipedia.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	private static final boolean POST = true;
	
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
	
	public WikipediaParser(){
		// TODO Nothing but the class using this constructor has to call 
		// the setWikipediaPage.
		this.format = Format.JSON;
		conn = new HttpConnector(WikipediaConstants.API_URL);
	}
	
	
	public WikipediaParser(String wikipediaPage) {
		WikipediaParser.wikipediaPage = wikipediaPage;
		this.format = Format.JSON;
		conn = new HttpConnector(WikipediaConstants.API_URL);
	}

	public void setWikipediaPage(String wikipediaPage){
		this.wikipediaPage = wikipediaPage;
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
		return conn.response(params, POST);
	}
	
	/**
	 * This method takes time (format YYYYMMDDHHMMSS) and gives you the revisionID of the first page
	 * edited at that time.
	 * Once we get this revision ID we can get the links in that Page.
	 * @param time
	 * @return
	 */
	public String getRevisionID(String time){
		
		//String timeString = String.valueOf(time);
		
		params = new HashMap<String, String>();
		params.put("action","query");
		params.put("titles", wikipediaPage);
		params.put("format", Format.JSON.toString());
		params.put("prop", "revisions");
		params.put("rvlimit", "1");
		params.put("rvprop", "ids");
		params.put("rvdir", "newer");
		params.put("rvstart", time);		
		return parseRevisionJson(conn.response(params, POST));
		
	}

	public List<String> getLinks(){
		initializeParams();
		params.put("prop", "links");
		//String json =conn.response(params);
		//System.out.println(json);
		return parseJson("links", conn.response(params, POST));

	}
	
	/**
	 * This method takes revision ID and gives you all the links in that ID'ed page.
	 * @param revisionID
	 * @return
	 */
	public List<String> getRevisionIDLinks(String time){
		
		String revisionID = getRevisionID(time);
		params = new HashMap<String, String>();
		params.put("action", "parse");
		params.put("format", Format.JSON.toString());
		params.put("oldid", revisionID);
		params.put("prop", "links");
		return parseJson("links", conn.response(params, POST));
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
	
	/**
	 * This method is used to parse JSON to get revision ID
	 * TODO Need to find a way to make this and the above methods more generic
	 * @param response
	 * @return
	 */
	public static String parseRevisionJson(String response){
		String revisionID = null;
		
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject queryObject = jsonObject.getJSONObject("query");
			JSONObject pagesObject = queryObject.getJSONObject("pages");
			
			String id = pagesObject.names().get(0).toString();			
			JSONObject pageID = pagesObject.getJSONObject(id);			
			JSONArray revisionsArray = pageID.getJSONArray("revisions");	
			revisionID = revisionsArray.getJSONObject(0).getString("revid");
			
		} catch (JSONException e) {
			// TODO: handle exception
		}			
		return revisionID;
	}
	
	/**
	 * This method gets all the new links that were added to a current wikipage from that of the 
	 * same wikipage at a given past date
	 * @param time (format yyyymmddhhMMss)
	 * @return
	 */
	public List<String> getNewLinksAdded(String time){
		List<String> currentLinks = getLinks();
		List<String> linksFromPastPage = getRevisionIDLinks(time);
		
		Set<String> currentLinksSet = new HashSet<String>(currentLinks);
		Set<String> linksFromPastPageSet = new HashSet<String>(linksFromPastPage);
		
		currentLinksSet.removeAll(linksFromPastPageSet);
	
		List<String> newLinks = new ArrayList<String>(currentLinksSet);
		
		return newLinks;
	}
	
	public List<String> getLinksDeleted(String time){
		List<String> currentLinks = getLinks();
		List<String> linksFromPastPage = getRevisionIDLinks(time);
		
		Set<String> currentLinksSet = new HashSet<String>(currentLinks);
		Set<String> linksFromPastPageSet = new HashSet<String>(linksFromPastPage);
		
		linksFromPastPageSet.removeAll(currentLinksSet);
	
		List<String> deletedLinks = new ArrayList<String>(linksFromPastPageSet);
		
		return deletedLinks;
	}
	
	
	
	public static void main(String[] args) {
		WikipediaParser wikiParser = new WikipediaParser("United_States_presidential_election,_2012");
		System.out.println("Links that are added are:");
		for(String link: wikiParser.getNewLinksAdded("20120501000000"))
			System.out.println(link);
		System.out.println("\n--------------------------------------------------\n");
		System.out.println("Links that are deleted are:");
		for(String link: wikiParser.getLinksDeleted("20120501000000"))
			System.out.println(link);
	}

}
