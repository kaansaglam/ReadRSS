package com.galaksiya.education.rss.feed;

import java.net.MalformedURLException;
import java.net.URL;

import com.sun.syndication.io.FeedException;

import java.io.IOException;

import java.net.HttpURLConnection;

/**
 * this class read xml file from website.
 * 
 * @author galaksiya
 *
 */

public class RSSReader {

	private HttpURLConnection httpcon = null;

	public HttpURLConnection readRSSFeed(String sourceUrl) throws IllegalArgumentException, FeedException, IOException {
		try {

			URL url = new URL(sourceUrl);
			// connect the url adress and read data.
			httpcon = (HttpURLConnection) url.openConnection();
		} catch (

		MalformedURLException ue)

		{
			System.out.println("warning :  URL does not work!!");
		}

		return httpcon;

	}

}