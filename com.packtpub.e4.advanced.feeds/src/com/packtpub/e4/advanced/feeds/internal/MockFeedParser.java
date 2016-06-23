package com.packtpub.e4.advanced.feeds.internal;

import java.util.ArrayList;
import java.util.List;

import com.packtpub.e4.advanced.feeds.Feed;
import com.packtpub.e4.advanced.feeds.FeedItem;
import com.packtpub.e4.advanced.feeds.IFeedParser;

public class MockFeedParser implements IFeedParser {
	public List<FeedItem> parseFeed(Feed feed) {
		List<FeedItem> items = new ArrayList<FeedItem>(3);
		items.add(new FeedItem.Builder(feed).setTitle("AlBlue's	Blog").setUrl("http://alblue.bandlem.com").build());
		items.add(new FeedItem.Builder(feed).setTitle("Packt	Publishing").setUrl("http://www.packtpub.com").build());
		items.add(new FeedItem.Builder(feed).setTitle("Source	Code")
				.setUrl("https://github.com/alblue/com.packtpub.e4.advanced").build());
		return items;
	}
}