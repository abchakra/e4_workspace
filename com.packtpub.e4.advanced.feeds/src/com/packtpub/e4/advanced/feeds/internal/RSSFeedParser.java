package com.packtpub.e4.advanced.feeds.internal;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.packtpub.e4.advanced.feeds.Feed;
import com.packtpub.e4.advanced.feeds.FeedItem;
import com.packtpub.e4.advanced.feeds.FeedItem.Builder;
import com.packtpub.e4.advanced.feeds.IFeedParser;

public class RSSFeedParser implements IFeedParser {
	public List<FeedItem> parseFeed(Feed feed) {
		try {
			List<FeedItem> feedItems = new ArrayList<FeedItem>();
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(new URL(feed.getUrl()).openStream());
			NodeList items = doc.getElementsByTagName("item");
			for (int i = 0; i < items.getLength(); i++) {
				Node item = items.item(i);
				Builder feedItem = new FeedItem.Builder(feed);
				feedItem.setTitle(getTextValueOf(item, "title"));
				feedItem.setUrl(getTextValueOf(item, "link"));
				feedItem.setDate(parseDate(getTextValueOf(item, "pubDate")));
				feedItems.add(feedItem.build());
			}
			return feedItems;
		} catch (Exception e) {
			return null;
		}
	}

	private Date parseDate(String date) {
		try {
			return new SimpleDateFormat("EEE,	dd	MMM	yyyy	HH:mm:ss	zzz").parse(date);
		} catch (Exception e) {
			return null;
		}
	}

	private String getTextValueOf(Node item, String element) {
		try {
			return ((Element) item).getElementsByTagName(element).item(0).getTextContent();
		} catch (Exception e) {
			return null;
		}
	}
}