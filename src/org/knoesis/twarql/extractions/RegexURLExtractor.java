package org.knoesis.twarql.extractions;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.knoesis.models.AnnotatedTweet;
import org.knoesis.models.URLModel;
/**
 * This Class extracts the URL using REGEX
 * @author pavan
 *
 */

public class RegexURLExtractor implements Extractor, Serializable{

	private static final Log LOG = LogFactory.getLog(RegexURLExtractor.class);

	/**
	 *  List of tlds downloaded from
	 *  http://data.iana.org/TLD/tlds-alpha-by-domain.txt
	 *  Version 2009070700, Last Updated Tue Jul  7 07:07:01 2009 UTC
	 */
	private static String[] tlds = {"AC", "AD", "AE", "AERO", "AF", "AG", "AI", "AL", "AM", "AN", "AO", "AQ", "AR", "ARPA", "AS", "ASIA", "AT", "AU", "AW", "AX", "AZ", "BA", "BB", "BD", "BE", "BF", "BG", "BH", "BI", "BIZ", "BJ", "BM", "BN", "BO", "BR", "BS", "BT", "BV", "BW", "BY", "BZ", "CA", "CAT", "CC", "CD", "CF", "CG", "CH", "CI", "CK", "CL", "CM", "CN", "CO", "COM", "COOP", "CR", "CU", "CV", "CX", "CY", "CZ", "DE", "DJ", "DK", "DM", "DO", "DZ", "EC", "EDU", "EE", "EG", "ER", "ES", "ET", "EU", "FI", "FJ", "FK", "FM", "FO", "FR", "GA", "GB", "GD", "GE", "GF", "GG", "GH", "GI", "GL", "GM", "GN", "GOV", "GP", "GQ", "GR", "GS", "GT", "GU", "GW", "GY", "HK", "HM", "HN", "HR", "HT", "HU", "ID", "IE", "IL", "IM", "IN", "INFO", "INT", "IO", "IQ", "IR", "IS", "IT", "JE", "JM", "JO", "JOBS", "JP", "KE", "KG", "KH", "KI", "KM", "KN", "KP", "KR", "KW", "KY", "KZ", "LA", "LB", "LC", "LI", "LK", "LR", "LS", "LT", "LU", "LV", "LY", "MA", "MC", "MD", "ME", "MG", "MH", "MIL", "MK", "ML", "MM", "MN", "MO", "MOBI", "MP", "MQ", "MR", "MS", "MT", "MU", "MUSEUM", "MV", "MW", "MX", "MY", "MZ", "NA", "NAME", "NC", "NE", "NET", "NF", "NG", "NI", "NL", "NO", "NP", "NR", "NU", "NZ", "OM", "ORG", "PA", "PE", "PF", "PG", "PH", "PK", "PL", "PM", "PN", "PR", "PRO", "PS", "PT", "PW", "PY", "QA", "RE", "RO", "RS", "RU", "RW", "SA", "SB", "SC", "SD", "SE", "SG", "SH", "SI", "SJ", "SK", "SL", "SM", "SN", "SO", "SR", "ST", "SU", "SV", "SY", "SZ", "TC", "TD", "TEL", "TF", "TG", "TH", "TJ", "TK", "TL", "TM", "TN", "TO", "TP", "TR", "TRAVEL", "TT", "TV", "TW", "TZ", "UA", "UG", "UK", "US", "UY", "UZ", "VA", "VC", "VE", "VG", "VI", "VN", "VU", "WF", "WS", "XN--0ZWM56D", "XN--11B5BS3A9AJ6G", "XN--80AKHBYKNJ4F", "XN--9T4B11YI5A", "XN--DEBA0AD", "XN--G6W251D", "XN--HGBK6AJ7F53BBA", "XN--HLCJ6AYA9ESC7A", "XN--JXALPDLP", "XN--KGBECHTV", "XN--ZCKZAH", "YE", "YT", "YU", "ZA", "ZM", "ZW"};
	public static Pattern urlRegex = Pattern.compile("((http|https|ftp)://)?[a-zA-Z_0-9\\-]+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[#&\\n\\-=?\\+\\%/\\.\\w]+)?");  

	public RegexURLExtractor()  {
		Arrays.sort(tlds);
	}

	public Set<URL> execute(String initialText) {  
		Set<URL> urls = extract(initialText);
		
		if (urls == null)
			return null;
		
//		Set<URL> urls = new HashSet<URL>(); 
//		for (String u: strUrls) {
//			try {
//				urls.add(new URL(u));
//			} catch (MalformedURLException ignored) {} // can ignore because it is tested in extract(String)
//		}
		return urls;
	}  

	@Override
	public Set<URL> extract(Object initialText) {
		Set<URL> urls = new HashSet<URL>();

		// Catches anything with periods in it. 
		// e.g. 192.168.0.1, or 25.12.2009 or www.yahoo.com or http://www.google.com
		Pattern p = Pattern.compile("((http|https|ftp)://)?[a-zA-Z_0-9\\-]+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[#&\\n\\-=?\\+\\%/\\.\\w]+)?");  

		Matcher m = p.matcher((String)initialText);  
		while (m.find()) {
			String href = m.group();
			
			// If domain part ends with a TLD, we also trust it may be a URL. 
			// Otherwise discard.
			String tld = m.group(3).substring(1).toUpperCase();
			if (Arrays.binarySearch(tlds, tld)<0) {
				//System.out.println(tld);
				continue;
			}
			
			// If it contains a TLD we trust it's a URL,
			//    so if it doesn't have http:// we add it.
			if (!href.startsWith("http://")) {
				href = "http://" + href;
			}
			
			try{
				URL newurl = new URL(href);
				urls.add(newurl);
			}
			catch (MalformedURLException e) {
				//LOG.debug("URL malformed: "+href);
				continue;
			}
			//LOG.debug("URL OK: "+href);
		}
		return urls;
	}

	@Override
	public void process(AnnotatedTweet tweet) {
		Set<URLModel> urlModels = new HashSet<URLModel>();
		Set<URL> urls = this.execute(tweet.getStatusTweet().getText());
		for(URL url: urls)
			urlModels.add(new URLModel(url));
		tweet.setUrls(urlModels);
	}

	
	public static void main(String[] args) throws MalformedURLException {
		RegexURLExtractor e = new RegexURLExtractor();
		System.out.println(e.execute("http://www.cnn.com"));
		System.out.println(e.execute("www.worldcantwait.org"));
		System.out.println(e.execute("yahoo.com"));
		System.out.println(e.execute("http://www.cnn.com/video/#/video/world/2009/06/13/shubert.uk.iran.election.protests.cnn?"));
		System.out.println(e.execute("electionhttp://www.cnn.com/video/#/video/world/2009/06/13/shubert.uk.iran.election.protests.cnn?"));
		System.out.println(e.execute("http://test.com/myservlet?param=12234."));		
		
	}

}
