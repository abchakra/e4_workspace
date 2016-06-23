package com.packtpub.e4.advanced.feeds;

import java.util.List;

public interface IFeedParser {
	public List<FeedItem> parseFeed(Feed feed);
}