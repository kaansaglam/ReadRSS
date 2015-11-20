package com.galaksiya.education.rss;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.galaksiya.education.rss.feed.FreshEntryFinder;
import com.galaksiya.education.rss.feed.EntryWriter;
import com.galaksiya.education.rss.feed.RSSReader;
import com.galaksiya.education.rss.interaction.MenuPrinter;
import com.galaksiya.education.rss.metadata.FeedMetaDataMenager;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.io.FeedException;

public class ScheduledExecutorDemo {
	public static void main(String[] args) throws ParseException, IllegalArgumentException, IOException, FeedException {

		Map<String, Date> feedTimeMap = new HashMap<String, Date>();
		feedTimeMap.put("Reuters", null);
		int sourceCount = new MenuPrinter().getConsoleText() - 1;
		// get how many news source record in file
		EntryWriter writer = new EntryWriter();
		FeedMetaDataMenager menageData = new FeedMetaDataMenager();
		// maps for keep last publish date for each source.
		FreshEntryFinder compareDates = new FreshEntryFinder();
		RSSReader reader = new RSSReader();
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					int i = 1;
					// loop source number times.
					for (i = 1; i <= sourceCount; i++) {

						// source name
						String name = menageData.readSourceName(i);
						// source URL
						String URL = menageData.readSourceURL(i);
						// source query method
						String method = menageData.getSourceQuery(URL);
						Iterator<?> itEntries = reader.readRSSFeed(menageData.readSourceURL(i));
						// this block for first time run the code.
						if (feedTimeMap.get(name) == null) {
							SyndEntry entry = (SyndEntry) itEntries.next();
							writer.writeRSSFeed(entry, method);
							feedTimeMap.put(name, writer.getLastPublishDate());
							while (itEntries.hasNext()) {
								entry = (SyndEntry) itEntries.next();
								writer.writeRSSFeed(entry, method);
							}
							// this block for after first run the code.
						} else {
							SyndEntry entry = (SyndEntry) itEntries.next();
							SyndEntry freshEntry = compareDates.compareDates(entry, feedTimeMap, name);
							if (freshEntry != null) {
								writer.writeRSSFeed(freshEntry, method);
								feedTimeMap.put(name, writer.getLastPublishDate());
							}
							while (itEntries.hasNext()) {
								entry = (SyndEntry) itEntries.next();
								freshEntry = compareDates.compareDates(entry, feedTimeMap, name);
								if (freshEntry != null) {
									writer.writeRSSFeed(freshEntry, method);
								}
							}
						}
						System.out.println(feedTimeMap.get(name));
					}
				} catch (ArrayIndexOutOfBoundsException e) {
				} catch (IOException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (FeedException e) {
					e.printStackTrace();
				}
			}
		};

		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(runnable, 0, 10, TimeUnit.SECONDS);
	}
}