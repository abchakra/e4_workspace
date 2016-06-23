package com.packtpub.e4.advanced.feeds;

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

	public String getUrl() {
		return URL;
	}

}
