package com.galaksiya.education.rss;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.galaksiya.education.rss.feed.FeedWriter;
import com.galaksiya.education.rss.feed.RSSReader;
import com.sun.syndication.io.FeedException;

public class ScheduledExecutor {
	private static Date date;
	private static Map<String, Date> feedTimeMap = new HashMap<String, Date>();

	public static void main(String[] args) throws ParseException, IllegalArgumentException, IOException, FeedException {
		String time = "Wed Nov 18 02:19:02 EET 2015";
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
		date = sdf.parse(time);
		try {
			String name;
			String line = "";
			String cvsSplitBy = ",";
			BufferedReader br = new BufferedReader(new FileReader("/home/galaksiya/Desktop/RSSfile.csv"));
			// map for keep last feed publish time.
			Map<String, Date> feedTimeMap = new HashMap<String, Date>();
			// loop until end of the file
			while ((line = br.readLine()) != null) {
				String[] CSVdata = line.split(cvsSplitBy);
				FeedWriter writer = new FeedWriter();
				RSSReader reader = new RSSReader();
				Iterator<?> itEntries;
				itEntries = reader.readRSSFeed(CSVdata[1]);
				// write first feed
				writer.writeRSSFeed(itEntries, CSVdata[2]);
				// get time int format. (example : 17.28 -->1728).
				Date lastPublishDate = writer.lastPublishDate;
				name = CSVdata[0];
				// keep source name and last feed timei in map.
				feedTimeMap.put(name, lastPublishDate);
				// start to show 2nd and other feeds
				while (itEntries.hasNext()) {
					writer.writeRSSFeed(itEntries, CSVdata[2]);
				}

			}
			System.out.println("\n\n\n*****************************************\n\n");
			br.close();
		} catch (ArrayIndexOutOfBoundsException e) {

		}

		Runnable runnable = new Runnable() {
			public void run() {
				try {

					RSSNonStop getFeedAlways = new RSSNonStop();

					getFeedAlways.getFeeds(date, feedTimeMap);
					System.out.println("\n\n\n*****************************************\n\n");

				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FeedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(runnable, 0, 10, TimeUnit.SECONDS);

	}
}