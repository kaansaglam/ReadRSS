package com.galaksiya.education.rss;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom.JDOMException;
import org.xml.sax.SAXException;

import com.galaksiya.education.rss.feed.FeedWriter;
import com.galaksiya.education.rss.feed.RSSReader;
import com.galaksiya.education.rss.interaction.MenuPrinter;
import com.galaksiya.education.rss.interaction.UserInteraction;
import com.galaksiya.education.rss.metadata.FeedMetaDataMenager;
import com.sun.syndication.io.FeedException;

public class RSSDemo {

	public static void main(String[] args) throws JDOMException, ParserConfigurationException, SAXException,
			IllegalArgumentException, FeedException, IOException {

		FeedMetaDataMenager menageMetaData = new FeedMetaDataMenager();
		UserInteraction interaction = UserInteraction.getInstance();
		interaction.getUserPreferences();

		while (interaction.getAddOrRead() == 2) {
			// if user choosed to add a news source.
			new FeedWriter().writeRSSFeed(new RSSReader().readRSSFeed(interaction.getLink()), interaction.getMethod());

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
		new FeedWriter().writeRSSFeed(new RSSReader().readRSSFeed(URL), method);
	}
}
