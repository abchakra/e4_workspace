package com.packtpub.e4.advanced.feeds.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public class NewFeedPreviewPage extends WizardPage {
	private Browser browser;

	protected NewFeedPreviewPage() {
		super("NewFeedPreviewPage");
		setTitle("Preview	of	Feed");
		setMessage("A	preview	of	the	provided	URL	is	shown	below");
		setImageDescriptor(ImageDescriptor.createFromFile(NewFeedPreviewPage.class, "/icons/feed_16.png"));
	}

	public void createControl(Composite parent) {
		Composite page = new Composite(parent, SWT.NONE);
		setControl(page);
		page.setLayout(new FillLayout());
		browser = new Browser(page, SWT.None);
		browser.setText("Loading...");
	}

	@Override
	public void setVisible(boolean visible) {
		if (visible && browser != null && !browser.isDisposed()) {
			NewFeedPage newFeedPage = (NewFeedPage) (getWizard().getPage("NewFeedPage"));
			String url = newFeedPage.getURL();
			browser.setUrl(url);
		}
		super.setVisible(visible);
	}
}