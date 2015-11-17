package com.galaksiya.education.rss.feed;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;

import org.junit.Test;
import com.galaksiya.education.rss.metadata.FeedMetaDataMenager;
import com.sun.syndication.io.FeedException;

public class RSSReaderTest {

	@Test
	public void readRSS() throws IllegalArgumentException, FeedException, IOException {
		String testUrl = new FeedMetaDataMenager().readSourceURL(2);

		URL url = new URL(testUrl);
		String httpconTest = url.openConnection().toString();
		String httpcon = new RSSReader().readRSSFeed(testUrl).toString();

		assertEquals(httpconTest, httpcon);
	}

	@Test
	public void readEmptyURL() throws IOException, IllegalArgumentException, FeedException {
		String testUrl = "http://";
		URL url = new URL("http://");
		String httpconTest = url.openConnection().toString();
		String httpcon = new RSSReader().readRSSFeed(testUrl).toString();
		assertEquals(httpconTest, httpcon);
	}

	@Test(expected = NullPointerException.class)
	public void readUnavailableURL() throws Exception {

		new RSSReader().readRSSFeed("abc").toString();

	}

	@Test(expected = NullPointerException.class)
	public void readNullURL() throws Exception {
		new RSSReader().readRSSFeed("").toString();
	}
}
