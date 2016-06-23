package com.packtpub.e4.advanced.feeds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;

import com.packtpub.e4.advanced.feeds.internal.FeedParserConfigurationComparator;

public class FeedParserFactory {
	private static FeedParserFactory DEFAULT;

	public static FeedParserFactory getDefault() {
		if (DEFAULT == null) {
			DEFAULT = new FeedParserFactory();
		}
		return DEFAULT;
	}

	public List<IFeedParser> getFeedParsers() {
		List<IFeedParser> parsers = new ArrayList<IFeedParser>();
		IExtensionRegistry registry = RegistryFactory.getRegistry();
		IExtensionPoint extensionPoint = registry.getExtensionPoint("com.packtpub.e4.advanced.feeds", "feedParser");
		if (extensionPoint != null) {
			IConfigurationElement[] elements = extensionPoint.getConfigurationElements();
			Arrays.sort(elements, new FeedParserConfigurationComparator());

			for (int i = 0; i < elements.length; i++) {
				IConfigurationElement element = elements[i];
				try {
					Object parser = element.createExecutableExtension("class");
					if (parser instanceof IFeedParser) {
						parsers.add((IFeedParser) parser);
					}
				} catch (CoreException e) {
					// ignore or log as appropriate
				}
			}
		}

		return parsers;
	}
}