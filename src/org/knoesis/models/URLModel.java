package org.knoesis.models;

import java.io.Serializable;
import java.net.URL;
import java.util.Set;

/**
 * This class models a URL found in a tweet. 
 * Mainly comprises of a shortURL and longURLs with the entities found in the content of the html
 * The objective of this class is to find the entities that helps to get the context of the tweet
 * 
 * @author pavan
 *
 */
public class URLModel implements Serializable{
	private URL shortURL;
	private URL originalURL;
	private String htmlContent; 
	private Set<String> entities;
	private String htmlTitle;
	
	public URLModel(URL shortURL) {
		this.shortURL = shortURL;
	}
	public URL getOriginalURL() {
		return originalURL;
	}
	public void setOriginalURL(URL originalURL) {
		this.originalURL = originalURL;
	}
	public String getHtmlContent() {
		return htmlContent;
	}
	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}
	public Set<String> getEntities() {
		return entities;
	}
	public void setEntities(Set<String> entities) {
		this.entities = entities;
	}
	public URL getShortURL() {
		return shortURL;
	}
	public void setShortURL(URL shortURL) {
		this.shortURL = shortURL;
	}
	
	/**
	 * toString() method overriden with returning the originalURL as a string
	 */
	@Override
	public String toString() {
		String result = "";
		if (originalURL == null)
			originalURL = shortURL;
		result+="original URL: "+originalURL.toString() + "\n";
		result+= "Title: " + htmlTitle + "\n";
		result+= "Entities: "+entities+"\n";
		return result;
		
	}
	public String getHtmlTitle() {
		return htmlTitle;
	}
	public void setHtmlTitle(String htmlTitle) {
		this.htmlTitle = htmlTitle;
	}
	
}
