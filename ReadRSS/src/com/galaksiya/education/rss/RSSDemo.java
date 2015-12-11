package com.galaksiya.education.rss;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import org.xml.sax.SAXException;

import com.galaksiya.education.rss.feed.EntryWriter;
import com.galaksiya.education.rss.feed.RSSReader;
import com.galaksiya.education.rss.interaction.MenuPrinter;
import com.galaksiya.education.rss.interaction.UserInteraction;
import com.galaksiya.education.rss.metadata.FeedMetaDataMenager;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.io.FeedException;

public class RSSDemo {
	private static Logger log = Logger.getLogger(EntryWriter.class);

	public static void main(String[] args) throws JDOMException, ParserConfigurationException, SAXException,
			IllegalArgumentException, FeedException, IOException {

		try {
			FeedMetaDataMenager menageMetaData = new FeedMetaDataMenager();
			UserInteraction interaction = UserInteraction.getInstance();
			interaction.getUserPreferences();
			RSSReader reader = new RSSReader();
			EntryWriter writer = new EntryWriter();

			while (interaction.getAddOrRead() == 2) {
				// the method is for add a new news source
				addNewSource(interaction, menageMetaData, reader, writer);
			}
			// the method is for show a source's feed
			writeFeed(interaction, writer, menageMetaData, reader);

		} catch (MalformedURLException e) {
			log.error("invalid URL type", e);
		}
	}

	public static void writeFeed(UserInteraction interaction, EntryWriter writer, FeedMetaDataMenager menageMetaData,
			RSSReader reader) throws IOException, IllegalArgumentException, FeedException {

		// get from file sources's URL
		String URL = menageMetaData.readSourceURL(new MenuPrinter().showMenu());

		// get from file source's query method
		String method = menageMetaData.getSourceQuery(URL);
		try {
			// if user choose to show RSS feed into console run FeedWriter
			Iterator<?> itEntries = reader.readRSSFeed(URL);
			if (itEntries != null) {
				while (itEntries.hasNext()) {
					SyndEntry entry = (SyndEntry) itEntries.next();
					writer.writeFeedEntry(entry.getTitle(), entry.getLink(), entry.getPublishedDate(), method);
					// char nextFeed = interaction.continueCheck();
					if (!(interaction.continueCheck() == 'y')) {
						break;
					}
				}
			}
		} catch (MalformedURLException e) {
			log.error("invalid URL type", e);
		}
	}

	public static void addNewSource(UserInteraction interaction, FeedMetaDataMenager menageMetaData, RSSReader reader,
			EntryWriter writer) throws IllegalArgumentException, FeedException, IOException {
		Iterator<?> itEntries = reader.readRSSFeed(interaction.getLink());
		try {
			if (itEntries != null) {
				// if user choosed to add a news source.
				while (itEntries.hasNext()) {
					SyndEntry entry = (SyndEntry) itEntries.next();
					if (entry != null) {
						writer.writeFeedEntry(entry.getTitle(), entry.getLink(), entry.getPublishedDate(),
								interaction.getMethod());

						// char nextFeed = interaction.continueCheck();
						if (!(interaction.continueCheck() == 'y')) {
							break;
						}
					}
				}
			}
		} catch (MalformedURLException e) {
			log.error("invalid URL type", e);
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
}
