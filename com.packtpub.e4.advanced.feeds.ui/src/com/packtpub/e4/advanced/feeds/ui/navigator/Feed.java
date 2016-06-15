package com.packtpub.e4.advanced.feeds.ui.navigator;

public class Feed {
	private String name;
	private String URL;

	public Feed(String value, String key) {
		this.name = value;
		this.URL = key;
	}

	public String getName() {
		return name;
	}

	public String getURL() {
		return URL;
	}

}
