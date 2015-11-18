package com.galaksiya.education.rss;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.galaksiya.education.rss.feed.RSSReader;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.io.FeedException;

public class LastPublishDateChecker {
	public int feedCount = 0;

	public boolean compareTimes(Map<String, Date> feedTimeMap, String name) throws FeedException, IOException {

		boolean isBefore = false;
		String line = "";
		String cvsSplitBy = ",";
		BufferedReader br = new BufferedReader(new FileReader("/home/galaksiya/Desktop/RSSfile.csv"));
		String[] CSVdata = line.split(cvsSplitBy);
		RSSReader reader = new RSSReader();
		Iterator<?> itEntries;
		itEntries = reader.readRSSFeed(CSVdata[1]);
		try {
			while (!isBefore) {
				SyndEntry entry = (SyndEntry) itEntries.next();
				Date publishDate = entry.getPublishedDate();
				if (publishDate.after(feedTimeMap.get(name))) {
					isBefore = true;
					feedCount++;
				}
			}
		} catch (NullPointerException e) {
			System.out.println("warning : can not read any feed !!");
		} catch (IllegalArgumentException e) {
			System.out.println("warning : can not read feed content !!");
		}
		br.close();
		return isBefore;

	}

}
