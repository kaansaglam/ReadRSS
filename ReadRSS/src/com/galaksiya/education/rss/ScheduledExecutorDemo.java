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
import com.galaksiya.education.rss.feed.Comparator;
import com.galaksiya.education.rss.feed.FeedWriter;
import com.galaksiya.education.rss.feed.RSSReader;
import com.galaksiya.education.rss.interaction.MenuPrinter;
import com.galaksiya.education.rss.metadata.FeedMetaDataMenager;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.io.FeedException;

public class ScheduledExecutorDemo {
	public static void main(String[] args) throws ParseException, IllegalArgumentException, IOException, FeedException {
		Map<String, Date> feedTimeMap = new HashMap<String, Date>();
		feedTimeMap.put("start", null);
		int sourceCount = new MenuPrinter().getConsoleText() - 1;
		// get how many news source record in file
		FeedWriter writer = new FeedWriter();
		FeedMetaDataMenager menageData = new FeedMetaDataMenager();
		// maps for keep last publish date for each source.
		Comparator compareDates = new Comparator();
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

						while (itEntries.hasNext()) {

							SyndEntry entry = (SyndEntry) itEntries.next();

							SyndEntry freshEntry = compareDates.compareDates(entry, feedTimeMap, name);

							if (feedTimeMap.get(name) == null) {

								writer.writeRSSFeed(entry, method);
								feedTimeMap.put(name, writer.getLastPublishDate());
								while (itEntries.hasNext()) {
									writer.writeRSSFeed(entry, method);
								}
							} else {

								writer.writeRSSFeed(freshEntry, method);
								feedTimeMap.put(name, writer.getLastPublishDate());
							}
						}
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