package com.packtpub.e4.advanced.feeds.ui.navigator;

import java.net.URL;
import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.statushandlers.StatusManager;

import com.packtpub.e4.advanced.feeds.ui.Activator;

public class ShowFeedInBrowserHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection sel = HandlerUtil.getCurrentSelection(event);
		if (sel instanceof IStructuredSelection) {
			Iterator<?> it = ((IStructuredSelection) sel).iterator();
			while (it.hasNext()) {
				Object object = it.next();
				if (object instanceof Feed) {
					String url = ((Feed) object).getURL();
					try {
						PlatformUI.getWorkbench().getBrowserSupport().createBrowser(url).openURL(new URL(url));
					} catch (Exception e) {
						StatusManager.getManager()
								.handle(new Status(Status.ERROR, Activator.PLUGIN_ID,
										"Could	not	open	browser	for	" + url, e),
										StatusManager.LOG | StatusManager.SHOW);
					}
				}
			}
		}
		return null;
	}
}