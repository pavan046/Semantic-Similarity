package org.knoesis.twarql.extractions;

import org.knoesis.models.AnnotatedTweet;
import java.util.ArrayList;
import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.cybozu.labs.langdetect.Language;
/**
 * This class detects the language of text provided. 
 * The code uses implementation from the following URL 
 * 
 * http://code.google.com/p/language-detection/
 * 
 * Requires: A folder with profiles that has to be included to detect the language.
 * 
 * TODO: Need to pick this up from the configuration file
 * 
 * @author pavan
 *
 */
public class LanguageDetector implements Extractor<String> {

	private final static String DEFAULT_PROFILE_DIRECTORY = "./langprofiles";
	/**
	 * Default profile directory is used "./langprofiles"
	 */
	public LanguageDetector() {
		try {
			DetectorFactory.loadProfile(DEFAULT_PROFILE_DIRECTORY);
		} catch (LangDetectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Initializes the detector with the profiles in the folder (profileString)
	 * @param profileString
	 */
	public LanguageDetector(String profileString){
		try {
			DetectorFactory.loadProfile(profileString);
		} catch (LangDetectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Returns the language of the text
	 */
	@Override
	public String extract(Object text) {
		Detector detector;
		String resultLang = null;
		try {
			detector = DetectorFactory.create();
			detector.append(text.toString());
			resultLang = detector.detect();
		} catch (LangDetectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resultLang;
	}

	@Override
	public void process(AnnotatedTweet tweet) {
		tweet.setLanguage(this.extract(tweet.getStatusTweet().getText()));
	}

	public static void main(String[] args) {
		LanguageDetector langDetector = new LanguageDetector();
		//langDetector.init("./langprofiles");
		System.out.println(langDetector.extract("I am missing something here.. not sure what"));
		System.out.println(langDetector.extract("La pol’tica es dar multas a los infractores"));
	}
}
