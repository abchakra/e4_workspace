package com.packtpub.e4.advanced.feeds.ui.navigator;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.progress.UIJob;

public class FeedContentProvider implements ITreeContentProvider, IResourceChangeListener {
	private static final Object[] NO_CHILDREN = new Object[0];
	private Viewer viewer;

	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		Object[] result = NO_CHILDREN;
		if (parentElement instanceof IResource) {
			IResource resource = (IResource) parentElement;
			if (resource.getName().endsWith(".feeds")) {
				try {
					Properties properties = new Properties();
					InputStream stream = resource.getLocationURI().toURL().openStream();
					properties.load(stream);
					stream.close();
					result = new Object[properties.size()];
					int i = 0;
					Iterator it = properties.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<String, String> entry = (Entry<String, String>) it.next();
						result[i++] = new Feed(entry.getValue(), entry.getKey());
					}
				} catch (Exception e) {
					return NO_CHILDREN;
				}
			}
		}
		return result;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = viewer;
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_CHANGE);
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof IFile) {
			((File) element).getAbsoluteFile();
			return true;
		}
		return false;
	}

	@Override
	public void dispose() {
		viewer = null;
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		if (viewer != null) {
			try {
				FeedsRefresher feedsChanged = new FeedsRefresher();
				event.getDelta().accept(feedsChanged);
			} catch (CoreException e) {
			}
		}
	}

	private class FeedsRefresher implements IResourceDeltaVisitor {
		public boolean visit(IResourceDelta delta) throws CoreException {
			final IResource resource = delta.getResource();
			if (resource != null && "feeds".equals(resource.getFileExtension())) {
				new UIJob("RefreshingFeeds") {
					public IStatus runInUIThread(IProgressMonitor monitor) {
						if (viewer != null) {
							// viewer.refresh();
							((StructuredViewer) viewer).refresh(resource);
						}
						return Status.OK_STATUS;
					}
				}.schedule();
			}
			return true;
		}
	}
}
