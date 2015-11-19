package com.galaksiya.education.rss.feed;

import java.io.IOException;
import java.util.Date;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.io.FeedException;

/**
 * parse the feed data and show it into console. ask continue or finish.
 * 
 * @author galaksiya
 *
 */
public class FeedWriter {
	private Date lastPublishDate;

	public void writeRSSFeed(SyndEntry entry, String method)
			throws IllegalArgumentException, FeedException, IOException {

		try {

			// parse xml data.
			// when parse xml data, use ROME library.

			System.out.println("Title        : " + entry.getTitle());
			String link = entry.getLink();
			System.out.println("Link         : " + link);

			setLastPublishDate(entry.getPublishedDate());

			System.out.println("Publish Date : " + getLastPublishDate());
			// get news page source
			Document doc = Jsoup.connect(link).get();
			Elements archived;
			// get a method for each page.
			archived = doc.select(method);
			System.out.println("Content      : " + archived.text() + "\n\n\n");

		} catch (NullPointerException e) {
			System.out.println("warning : can not read any feed !!");
		} catch (IllegalArgumentException e) {
			System.out.println("warning : can not read feed content !!");
		} catch (IOException e) {
		}
	}

	public Date getLastPublishDate() {
		return lastPublishDate;
	}

	public void setLastPublishDate(Date lastPublishDate) {
		this.lastPublishDate = lastPublishDate;
	}

}
