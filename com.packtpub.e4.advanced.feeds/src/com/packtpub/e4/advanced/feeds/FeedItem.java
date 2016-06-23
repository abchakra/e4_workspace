package com.packtpub.e4.advanced.feeds;

import java.util.Date;

public class FeedItem {
	// FeedItem fields
	private Date date;
	private Feed feed;
	private String title;
	public String url;

	private FeedItem(Feed feed) {
		this.feed = feed;
	}

	public Date getDate() {
		return date;
	}

	public Feed getFeed() {
		return feed;
	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}

	// FeedItem.Builder class
	public static class Builder {
		private FeedItem item;

		public Builder(Feed feed) {
			item = new FeedItem(feed);
		}

		public FeedItem build() {
			if (item.date == null) {
				item.date = new Date();
			}
			return item;
		}

		public Builder setDate(Date date) {
			item.date = date;
			return this;
		}

		public Builder setTitle(String title) {
			item.title = title;
			return this;
		}

		public Builder setUrl(String url) {
			item.url = url;
			return this;
		}
	}
}