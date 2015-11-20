package com.galaksiya.education.rss.metadata;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

import com.galaksiya.education.rss.feed.FreshEntryFinder;
import com.galaksiya.education.rss.feed.RSSReader;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.io.FeedException;

public class FreshEntryFinderTest {
	
	private static final String REUTERS_BUSINESS_FEED = "http://feeds.reuters.com/reuters/businessNews";
	
	private static final String REUTERS = "Reuters";

	@Test
	public void compareNull() throws IllegalArgumentException, FeedException, IOException {
		Map<String, Date> feedTimeMap = new HashMap<String, Date>();
		Iterator<?> itEntries = new RSSReader().readRSSFeed(REUTERS_BUSINESS_FEED);
		SyndEntry entry = (SyndEntry) itEntries.next();
		feedTimeMap.put(REUTERS, null);

		SyndEntry freshEntry = new FreshEntryFinder().compareDates(entry, feedTimeMap, "Retuers");

		assertEquals(null, freshEntry);
	}

	@Test
	public void compareExpected() throws IllegalArgumentException, FeedException, IOException {

		Map<String, Date> feedTimeMap = new HashMap<String, Date>();
		Iterator<?> itEntries = new RSSReader().readRSSFeed(REUTERS_BUSINESS_FEED);
		SyndEntry entry = (SyndEntry) itEntries.next();
		String name = REUTERS;
		feedTimeMap.put(name, entry.getPublishedDate());

		SyndEntry freshEntry = new FreshEntryFinder().compareDates(entry, feedTimeMap, name);

		assertEquals(entry.getTitle(), freshEntry.getTitle());

	}

	@Test
	public void compareDifferentDates() throws IllegalArgumentException, FeedException, IOException {
		Map<String, Date> feedTimeMap = new HashMap<String, Date>();
		Iterator<?> itEntries = new RSSReader().readRSSFeed(REUTERS_BUSINESS_FEED);
		Iterator<?> itEntriesTest = new RSSReader().readRSSFeed("http://www.teknolog.com/feed/");

		SyndEntry entry = (SyndEntry) itEntries.next();
		feedTimeMap.put(REUTERS, entry.getPublishedDate());
		SyndEntry entryTest = (SyndEntry) itEntriesTest.next();
		SyndEntry freshEntry = new FreshEntryFinder().compareDates(entryTest, feedTimeMap, "Retuers");
		assertEquals(null, freshEntry);
	}
}
