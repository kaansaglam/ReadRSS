package com.galaksiya.education.rss;

import java.io.IOException;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.JDOMException;
import org.xml.sax.SAXException;

import com.galaksiya.education.rss.feed.FeedWriter;
import com.galaksiya.education.rss.feed.RSSReader;
import com.galaksiya.education.rss.interaction.MenuPrinter;
import com.galaksiya.education.rss.interaction.UserInteraction;
import com.galaksiya.education.rss.metadata.FeedMetaDataMenager;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.io.FeedException;

public class RSSDemo {

	public static void main(String[] args) throws JDOMException, ParserConfigurationException, SAXException,
			IllegalArgumentException, FeedException, IOException {

		FeedMetaDataMenager menageMetaData = new FeedMetaDataMenager();
		UserInteraction interaction = UserInteraction.getInstance();
		interaction.getUserPreferences();

		RSSReader reader = new RSSReader();
		FeedWriter writer = new FeedWriter();
		Iterator<?> itEntries;
		itEntries = reader.readRSSFeed(interaction.getLink());
		while (interaction.getAddOrRead() == 2) {
			// if user choosed to add a news source.
			while (itEntries.hasNext()) {
				SyndEntry entry = (SyndEntry) itEntries.next();
				writer.writeRSSFeed(entry, interaction.getMethod());

				// char nextFeed = interaction.continueCheck();

				if (!(interaction.continueCheck() == 'y')) {

					break;
				}
			}

			char addControl = interaction.addOrNot();
			// user decided to add soruce in file.
			if (addControl == 'a') {
				String name = interaction.getName();
				String link = interaction.getLink();
				String method = interaction.getMethod();
				// check is source already exist.
				if (menageMetaData.checkSource(name, link) == true) {
					char entryCheck = interaction.addAlreadyExistRecord();
					if (entryCheck == 'y')
						menageMetaData.addSource(name, link, method);
				} else {
					menageMetaData.addSource(name, link, method);
				}

			}
			interaction.getUserPreferences();
		}
		// user want to read which news source
		int sourceNumber = new MenuPrinter().showMenu();
		// get from file sources's URL
		String URL = menageMetaData.readSourceURL(sourceNumber);
		// get from file source's query method
		String method = menageMetaData.getSourceQuery(URL);

		// if user choose to show RSS feed into console run FeedWriter
		itEntries = reader.readRSSFeed(URL);
		while (itEntries.hasNext()) {
			SyndEntry entry = (SyndEntry) itEntries.next();
			writer.writeRSSFeed(entry, method);

			// char nextFeed = interaction.continueCheck();

			if (!(interaction.continueCheck() == 'y')) {

				break;
			}
		}
	}
}
