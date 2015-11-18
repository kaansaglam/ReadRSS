package com.galaksiya.education.rss;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Date;

import com.galaksiya.education.rss.feed.FeedWriter;
import com.galaksiya.education.rss.feed.RSSReader;
import com.sun.syndication.io.FeedException;

public class RSSNonStop {

	public void getFeeds(Date date, Map<String, Date> feedTimeMap)
			throws IOException, IllegalArgumentException, FeedException {
		String name = "Reuters";
		String line = "";
		String cvsSplitBy = ",";
		BufferedReader br = new BufferedReader(new FileReader("/home/galaksiya/Desktop/RSSfile.csv"));
		try {
			// loop until end of the file
			while ((line = br.readLine()) != null) {
				String[] CSVdata = line.split(cvsSplitBy);
				FeedWriter writer = new FeedWriter();
				RSSReader reader = new RSSReader();
				Iterator<?> itEntries;
				itEntries = reader.readRSSFeed(CSVdata[1]);
				// check publish times. it there is fresh feed execute if block.

				if (new LastPublishDateChecker().compareTimes(feedTimeMap, name)) {
					// write first feed
					writer.writeRSSFeed(itEntries, CSVdata[2]);
					// get time int format. (example : 17.28 -->1728).
					Date lastPublishDate = writer.lastPublishDate;
					name = CSVdata[0];
					// keep source name and last feed timei in map.
					feedTimeMap.put(name, lastPublishDate);
					int count = new LastPublishDateChecker().feedCount;
					// start to show 2nd and other feeds
					while (count < 0) {

						writer.writeRSSFeed(itEntries, CSVdata[2]);
						count--;
					}
				}

			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("arrayIndexOutoBoundException");

		}
		br.close();
	}

}
