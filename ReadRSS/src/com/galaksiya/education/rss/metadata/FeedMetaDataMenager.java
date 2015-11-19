package com.galaksiya.education.rss.metadata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.sun.syndication.io.FeedException;

/**
 * open the file and get URL and query method. check a source is already exist
 * and add new source.
 * 
 * @author galaksiya
 *
 */
public class FeedMetaDataMenager {
	public BufferedReader br;

	public BufferedReader menageMetaData() throws IOException {
		br = new BufferedReader(new FileReader("/home/galaksiya/Desktop/RSSfile.csv"));
		return br;
	}

	/**
	 * get a source number and return thats number's URL.
	 * 
	 * @param newsNumber
	 * @return
	 * @throws IOException
	 */
	public String readSourceName(int newsNumber) throws IOException {

		String line = "";
		String cvsSplitBy = ",";
		String name = null;
		int counter = 0;
		BufferedReader br = menageMetaData();
		try {
			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] CSVdata = line.split(cvsSplitBy);
				if (newsNumber - 1 == counter)
					name = CSVdata[0];
				counter++;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
		} catch (IOException e) {

		}
		br.close();
		return name;
	}

	public String readSourceURL(int newsNumber) throws IOException {

		String line = "";
		String cvsSplitBy = ",";
		String sourceUrl = null;
		int counter = 0;
		BufferedReader br = menageMetaData();
		try {
			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] CSVdata = line.split(cvsSplitBy);
				if (newsNumber - 1 == counter)
					sourceUrl = CSVdata[1];
				counter++;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
		} catch (IOException e) {

		}
		br.close();
		return sourceUrl;
	}

	/**
	 * find URL's query method and return it
	 * 
	 * @param sourceUrl
	 * @return
	 * @throws IOException
	 */
	public String getSourceQuery(String sourceUrl) throws IOException {

		String line = "";
		String cvsSplitBy = ",";
		BufferedReader br = menageMetaData();
		String method = null;
		try {
			while ((line = br.readLine()) != null) {
				String[] CSVdata = line.split(cvsSplitBy);
				if (sourceUrl.equals(CSVdata[1])) {
					method = CSVdata[2];
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("");
		} catch (NullPointerException e) {
			System.out.println("");
		} catch (IOException e) {
		}
		br.close();
		return method;
	}

	/**
	 * get a name and URL and search in file are that already exist.
	 * 
	 * @param name
	 * @param sourceUrl
	 * @return
	 * @throws IOException
	 */
	public boolean checkSource(String name, String sourceUrl) throws IOException {
		boolean isExist = false;
		String line = "";
		String cvsSplitBy = ",";
		BufferedReader br = menageMetaData();
		try {
			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] CSVdata = line.split(cvsSplitBy);
				if (sourceUrl.equals(CSVdata[1]) || name.equals(CSVdata[0])) {
					isExist = true;
					break;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("warning : null query line!!");
		} catch (NullPointerException e) {
			System.out.println("");
		}
		br.close();
		return isExist;
	}

	/**
	 * add a new source. write it last line of file.
	 * 
	 * @param name
	 * @param link
	 * @param method
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws FeedException
	 */
	public void addSource(String name, String link, String method)
			throws IOException, IllegalArgumentException, FeedException {
		try {
			FileWriter fstream = new FileWriter("/home/galaksiya/Desktop/RSSfile.csv", true);
			BufferedWriter fbw = new BufferedWriter(fstream);
			fbw.write(name);
			fbw.write(",");
			fbw.write(link);
			fbw.write(",");
			fbw.write(method);
			fbw.newLine();
			fbw.close();
		} catch (NullPointerException e) {
			System.out.println("Warning : null value..");
		}
	}
}
