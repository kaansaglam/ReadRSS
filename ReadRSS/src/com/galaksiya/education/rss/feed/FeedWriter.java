package com.galaksiya.education.rss.feed;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * parse the feed data and show it into console. ask continue or finish.
 * 
 * @author galaksiya
 *
 */
public class FeedWriter {
	public void writeRSSFeed(HttpURLConnection httpcon, String method)
			throws IllegalArgumentException, FeedException, IOException {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);

		try {
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(httpcon));
			List<?> entries = feed.getEntries();
			Iterator<?> itEntries = entries.iterator();
			String link = "";
			while (itEntries.hasNext()) {
				// parse xml data.
				// when parse xml data, use ROME library.
				SyndEntry entry = (SyndEntry) itEntries.next();
				System.out.println("Title        : " + entry.getTitle());
				link = entry.getLink();
				System.out.println("Link         : " + link);
				System.out.println("Publish Date : " + entry.getPublishedDate());
				// get news page source
				Document doc = Jsoup.connect(link).get();
				Elements archived;

				// get a method for each page.

				archived = doc.select(method);
				System.out.println("Content      : " + archived.text() + "\n\n\n");

				System.out.println("Press y for next feed :\n" + "Press any button for stop  :");
				char nextFeed = scanner.next().charAt(0);
				if (!(nextFeed == 'y')) {
					System.out.println("thank you.!");
					break;

				}
			}

		} catch (NullPointerException e)

		{
			System.out.println("warning : can not read any feed !!");
		} catch (IllegalArgumentException e)

		{
			System.out.println("warning : can not read feed content !!");
		} catch (IOException e) {
		}

	}

}
