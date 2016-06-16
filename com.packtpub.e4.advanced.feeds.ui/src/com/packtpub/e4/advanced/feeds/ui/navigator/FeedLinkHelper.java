package com.packtpub.e4.advanced.feeds.ui.navigator;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.navigator.ILinkHelper;

import com.packtpub.e4.advanced.feeds.ui.NewFeedWizard;

public class FeedLinkHelper implements ILinkHelper {
	public void activateEditor(IWorkbenchPage page, IStructuredSelection selection) {
		Object object = selection.getFirstElement();
		if (object instanceof Feed) {
			Feed feed = ((Feed) object);
			byte[] line = (feed.getURL().replace(":", "\\:") + "=" + feed.getName()).getBytes();
			IProject bookmarks = ResourcesPlugin.getWorkspace().getRoot().getProject(NewFeedWizard.FEEDS_PROJECT);
			if (bookmarks.exists() && bookmarks.isOpen()) {
				IFile feeds = bookmarks.getFile(NewFeedWizard.FEEDS_FILE);
				if (feeds.exists()) {
					try {
						TextSelection textSelection = findContent(line, feeds);
						if (textSelection != null) {
							setSelection(page, feeds, textSelection);
						}
					} catch (Exception e) {
						// Ignore
					}
				}
			}
		}
	}

	@Override
	public IStructuredSelection findSelection(IEditorInput anInput) {
		// TODO Auto-generated method stub
		return null;
	}

	private TextSelection findContent(byte[] content, IFile file) throws CoreException, IOException {
		int len = content.length;
		int start = -1;
		InputStream in = new BufferedInputStream(file.getContents());
		int pos = 0;
		while (start == -1) {
			int b = in.read();
			if (b == -1)
				break;
			if (b == content[0]) {
				in.mark(len);
				boolean found = true;
				for (int i = 1; i < content.length && found; i++) {
					found &= in.read() == content[i];
				}
				if (found) {
					start = pos;
				}
				in.reset();
			}
			pos++;
		}
		if (start != -1) {
			return new TextSelection(start, len);
		} else {
			return null;
		}
	}

	private void setSelection(IWorkbenchPage page, IFile feeds, TextSelection textSelection) throws PartInitException {
		IEditorPart editor = IDE.openEditor(page, feeds, false);
		editor.getEditorSite().getSelectionProvider().setSelection(textSelection);
	}
}