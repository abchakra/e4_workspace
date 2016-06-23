package com.packtpub.e4.advanced.feeds.internal;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.packtpub.e4.advanced.feeds.Feed;
import com.packtpub.e4.advanced.feeds.FeedItem;
import com.packtpub.e4.advanced.feeds.FeedItem.Builder;
import com.packtpub.e4.advanced.feeds.IFeedParser;

public class AtomFeedParser implements IFeedParser, IExecutableExtension {
	private static final String ATOM = "http://www.w3.org/2005/Atom";

	public List<FeedItem> parseFeed(Feed feed) {
		try {
			List<FeedItem> feedItems = new ArrayList<FeedItem>();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document doc = builder.parse(new URL(feed.getUrl()).openStream());
			NodeList items = doc.getElementsByTagNameNS(ATOM, "entry");
			for (int i = 0; i < items.getLength(); i++) {
				Node item = items.item(i);
				Builder feedItem = new FeedItem.Builder(feed);
				feedItem.setTitle(getTextValueOf(item, "title"));
				feedItem.setUrl(getTextValueOfAttribute(item, "link", "href"));
				feedItem.setDate(parseDate(getTextValueOf(item, "updated")));
				feedItems.add(feedItem.build());
			}
			return feedItems;
		} catch (Exception e) {
			return null;
		}
	}

	private String getTextValueOf(Node item, String string) {
		// TODO Auto-generated method stub
		return null;
	}

	private Date parseDate(String date) {
		try {
			if (date.length() > 22 && date.charAt(22) == ':') {
				date = date.substring(0, 22) + date.substring(23);
			}
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(date);
		} catch (Exception e) {
			return null;
		}
	}

	private String getTextValueOfAttribute(Node item, String element, String attribute) {
		try {
			return ((Element) item).getElementsByTagNameNS(ATOM, element).item(0).getAttributes()
					.getNamedItem(attribute).getNodeValue();
		} catch (Exception e) {
			return null;
		}
	}

	private int max = Integer.MAX_VALUE;

	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws CoreException {
		if (data instanceof String) {
			try {
				max = Integer.parseInt((String) data);
			} catch (Exception e) {
				// Ignore
			}
		}
	}
}