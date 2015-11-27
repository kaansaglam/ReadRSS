
package com.galaksiya.education.rss.feed;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * this class read xml file from website.
 * 
 * @author galaksiya
 *
 */

public class RSSReader {
	private Logger log = Logger.getLogger(RSSReader.class);

	public Iterator<?> readRSSFeed(String sourceUrl)
			throws IllegalArgumentException, MalformedURLException, FeedException, IOException {

		BasicConfigurator.configure();
		Iterator<?> itEntries = null;
		try {
			// connect the url adress and read data.
			String doc = readURI(sourceUrl);
			itEntries = parseFeed(doc);

		} catch (NullPointerException e) {
			log.error(e);
		} catch (MalformedURLException e) {
			log.error(e);
		}
		return itEntries;
	}

	public String readURI(String sourceUrl) throws IOException, MalformedURLException {
		URL url = null;
		BufferedReader reader = null;
		StringBuilder stringBuilder = null;
		String doc = null;

		try {
			// create the HttpURLConnection
			url = new URL(sourceUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			// want to do an HTTP GET here
			connection.setRequestMethod("GET");

			// give it 15 seconds to respond
			connection.setReadTimeout(15 * 1000);
			connection.connect();

			// read the output from the server
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			stringBuilder = new StringBuilder();

			String line = null;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
			doc = stringBuilder.toString();

		} catch (NullPointerException e) {
			log.error("could not read " + sourceUrl, e);
		} catch (MalformedURLException e) {
			log.error("could not read " + sourceUrl, e);
		}
		return doc;
	}

	public Iterator<?> parseFeed(String doc) throws FeedException, IOException, UnsupportedEncodingException {

		InputStream inputStream = new ByteArrayInputStream(doc.getBytes("UTF-8"));
		XmlReader reader = new XmlReader(inputStream);
		SyndFeed feed = new SyndFeedInput().build(reader);
		List<?> entries = feed.getEntries();
		return entries.iterator();
	}
}