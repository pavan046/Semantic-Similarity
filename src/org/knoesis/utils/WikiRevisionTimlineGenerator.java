package org.knoesis.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.knoesis.api.WikipediaParser;
import org.knoesis.models.WikiRevision;

public class WikiRevisionTimlineGenerator {
	public static void main(String[] args){
		String startDateString = "2011-09-29";
		String endDateString = "2012-09-20";
		WikipediaParser wikiParser = new WikipediaParser("Occupy Oakland");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date startDate = formatter.parse(startDateString);
			Date endDate = formatter.parse(endDateString);
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);

			List<WikiRevision> revisions = wikiParser.getRevisionInformation();
			Map<String, Integer> dateCount = new HashMap<String, Integer>();
			for (WikiRevision revision: revisions){
				int value = revision.getSize();
				String date = formatter.format(revision.getDate());
				
				if(dateCount.keySet().contains(date)){
					dateCount.put(date, dateCount.get(date)+value);
				}
				else 
					dateCount.put(date, value);	
			}
			System.out.println(dateCount);
			while(cal.getTime().before(endDate)){
				if(dateCount.containsKey(formatter.format(cal.getTime())))
					System.out.println(formatter.format(cal.getTime())+"\t"+dateCount.get(formatter.format(cal.getTime())));
				else
					System.out.println(formatter.format(cal.getTime())+"\t"+0);
				cal.add(Calendar.DATE, 1);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
