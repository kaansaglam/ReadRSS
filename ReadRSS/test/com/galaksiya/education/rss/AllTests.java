package com.galaksiya.education.rss;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.galaksiya.education.rss.feed.RSSReaderTest;
import com.galaksiya.education.rss.metadata.AllMetadataTests;

@RunWith(Suite.class)
@SuiteClasses({ AllMetadataTests.class, RSSReaderTest.class })
public class AllTests {
}