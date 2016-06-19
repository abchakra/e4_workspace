
package codesnippetapp.views;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import codesnippetapp.CodeSnippetAppConstants;
import codesnippetapp.data.SnippetData;

public class SearchResultsView {
	private static int searchViewCount = 0;
	private List<SnippetData> searchResultList = null;
	TableViewer searchResultViewer = null;

	@Inject
	public SearchResultsView() {
	}

	@PostConstruct
	public void postConstruct(Composite parent, final IEventBroker eventBroker,
			@Named("search_result") List<SnippetData> searchResult,
			@Named("code_search_criteria") String codeSearchCriteria,
			@Named("desc_search_criteria") String descSearchCriteria) {
		this.searchResultList = searchResult;

		parent.setLayout(new GridLayout(2, false));

		// Label to display search criteria
		Label lbl = new Label(parent, SWT.None);
		lbl.setText("Search Criteria : Code = " + codeSearchCriteria + ", Description = " + descSearchCriteria);

		// Table viewer to display search results
		searchResultViewer = new TableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE);
		GridData gridData = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		searchResultViewer.getTable().setLayoutData(gridData);

		// Set content provider for the TableViewer
		searchResultViewer.setContentProvider(new IStructuredContentProvider() {

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}

			@Override
			public void dispose() {
			}

			@Override
			public Object[] getElements(Object inputElement) {
				return searchResultList.toArray();
			}
		});

		// Set initial input, which is list of search results
		searchResultViewer.setInput(searchResultList);

		// Subscribe to custom event beforeSnippetDelete so that we could
		// remove the deleted snippet from the search result, if it exists in
		// the search results
		// Note that you need to add dependency of org.eclipse.osgi.services in
		// the plugin.xml
		eventBroker.subscribe(CodeSnippetAppConstants.BEFORE_SNIPPET_DELETE_EVENT, new EventHandler() {

			@Override
			public void handleEvent(Event event) {
				Object data = event.getProperty(IEventBroker.DATA);
				if (data == null || data instanceof SnippetData == false)
					return;
				searchResultList.remove(data);
				searchResultViewer.refresh();
			}
		});

		// Add selection change listener for the TableViewer so that when
		// selection
		// changes in the search results view, we could set selection in the
		// SnippetsListView
		searchResultViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				StructuredSelection selection = (StructuredSelection) event.getSelection();
				SnippetData snippetData = (SnippetData) selection.getFirstElement();
				// Publish an event for selection change in results view
				if (snippetData != null)
					eventBroker.post(CodeSnippetAppConstants.SEARCH_RESULT_SELECTION_CHANGE_EVENT, snippetData);
			}
		});

		// Increment search view count. This variable keeps track of number
		// of search result views we have opened. When the view is closed,
		// we will decrement this counter. When the counter reaches 0, we will
		// hide the PartStack that contains search result views.
		++searchViewCount;
	}

	@PreDestroy
	public void preDestroy(MApplication application, EModelService modelService, EPartService partService) {
		// Get the PartStack that contains search result views
		List<MPartStack> partStacks = modelService.findElements(application, "codesnippetapp.partstack.0",
				MPartStack.class, null);
		MPartStack partStack = partStacks.get(0); // There is only one PartStack

		// decrement search view count
		--searchViewCount;

		// Hide the PartStack (container) if all views are closed
		if (searchViewCount == 0)
			partStack.setVisible(false);
	}

}