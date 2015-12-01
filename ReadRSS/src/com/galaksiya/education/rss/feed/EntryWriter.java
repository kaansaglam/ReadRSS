package com.galaksiya.education.rss.feed;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

import org.apache.log4j.Logger;
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
public class EntryWriter {

	private Date lastPublishDate;
	private static final Logger log = Logger.getLogger(RSSReader.class);

	public void writeFeedEntry(SyndEntry entry, String method)
			throws IllegalArgumentException, FeedException, IOException {

		try (Writer output = new BufferedWriter(new FileWriter("/home/galaksiya/Desktop/RSSfeeds.txt", true));) {
			// parse xml data.
			// when parse xml data, use ROME library.

			System.out.println("Title        : " + entry.getTitle());
			output.append("\nTitle        : " + entry.getTitle());

			String link = entry.getLink();
			System.out.println("Link         : " + link);
			output.append("\nLink         : " + link);

			setLastPublishDate(entry.getPublishedDate());
			System.out.println("Publish Date : " + getLastPublishDate());
			output.append("\nPublish Date : " + getLastPublishDate());
			// get news page source
			Document doc = Jsoup.connect(link).get();
			Elements archived;
			// get a method for each page.
			archived = doc.select(method);
			System.out.println("Content      : " + archived.text() + "\n\n\n");
			output.append("\nContent      : " + archived.text() + "\n\n\n");
		} catch (IllegalArgumentException e) {
			log.info("warning : can not read feed content !!");
		} catch (IOException e) {
			log.error("General I/O exception: ", e);
		}

	}

	public Date getLastPublishDate() {
		return lastPublishDate;
	}

	public void setLastPublishDate(Date lastPublishDate) {
		this.lastPublishDate = lastPublishDate;
	}

}
