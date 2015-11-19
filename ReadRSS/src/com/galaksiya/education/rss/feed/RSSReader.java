package com.galaksiya.education.rss.feed;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import java.io.IOException;

import java.net.HttpURLConnection;

/**
 * this class read xml file from website.
 * 
 * @author galaksiya
 *
 */

public class RSSReader {
	

	public Iterator<?> readRSSFeed(String sourceUrl) throws IllegalArgumentException, FeedException, IOException {
		Iterator<?> itEntries = null;
		try {
			URL url = new URL(sourceUrl);
			HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
			SyndFeedInput input = new SyndFeedInput();
			XmlReader reader = new XmlReader(httpcon);
			SyndFeed feed = input.build(reader);
			List<?> entries = feed.getEntries();
			itEntries = entries.iterator();

			// connect the url adress and read data.

		} catch (MalformedURLException ue) {
			System.out.println("warning :  URL does not work!!");
		}
		catch(IOException e){
			
		}
		return itEntries;
	}
}