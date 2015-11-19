package com.galaksiya.education.rss.metadata;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

import com.galaksiya.education.rss.feed.Comparator;
import com.galaksiya.education.rss.feed.RSSReader;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.io.FeedException;

public class ComparatorTest {
	@Test
	public void compareNull() throws IllegalArgumentException, FeedException, IOException {
		Map<String, Date> feedTimeMap = new HashMap<String, Date>();
		Iterator<?> itEntries = new RSSReader().readRSSFeed("http://feeds.reuters.com/reuters/businessNews");
		feedTimeMap.put("Reuters", null);

		int result = new Comparator().compareDates(itEntries, feedTimeMap, "Retuers");

		assertEquals(0, result);
	}

	@Test
	public void compareExpected() throws IllegalArgumentException, FeedException, IOException {

		Map<String, Date> feedTimeMap = new HashMap<String, Date>();
		Iterator<?> itEntries = new RSSReader().readRSSFeed("http://feeds.reuters.com/reuters/businessNews");
		Iterator<?> itEntries2 = itEntries;
		SyndEntry entry = (SyndEntry) itEntries.next();
		feedTimeMap.put("Reuters", entry.getPublishedDate());

		int result = new Comparator().compareDates(itEntries2, feedTimeMap, "Retuers");

		assertEquals(1, result);
	}

	@Test
	public void compareDifferentDates() throws IllegalArgumentException, FeedException, IOException {
		Map<String, Date> feedTimeMap = new HashMap<String, Date>();
		Iterator<?> itEntries = new RSSReader().readRSSFeed("http://feeds.reuters.com/reuters/businessNews");
		Iterator<?> itEntries2 = new RSSReader().readRSSFeed("http://www.teknolog.com/feed/");

		SyndEntry entry = (SyndEntry) itEntries.next();
		feedTimeMap.put("Reuters", entry.getPublishedDate());

		int result = new Comparator().compareDates(itEntries2, feedTimeMap, "Retuers");

		assertEquals(0, result);
	}
}
