
package codesnippetapp.handlers;

import java.util.List;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import codesnippetapp.data.SnippetData;
import codesnippetapp.views.SearchDialog;

public class FindCommandHandler {
	// @Execute
	// public void execute(Shell shell) {
	// MessageDialog.openInformation(shell, "Find Command", "Find command
	// executed");
	// }

	@Execute
	public void executeFind(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell, IEclipseContext ctx,
			EPartService partService, EModelService modelService, MApplication application) {
		SearchDialog dlg = ContextInjectionFactory.make(SearchDialog.class, ctx);

		if (dlg.open() != Dialog.OK)
			return;
		// Get search results from SearchDialog
		List<SnippetData> searchResult = dlg.getSearchResult();

		// If there are no results to show, display a message and return
		if (searchResult == null || searchResult.size() == 0) {
			MessageDialog.openInformation(shell, "No match found", "No match found for the search criteria");
			return;
		}

		IEclipseContext newPartContext = ctx.createChild("search_result_context");

		MPart findResultPart = MBasicFactory.INSTANCE.createPart();
		findResultPart.setContributionURI("bundleclass://CodeSnippetApp/codesnippetapp.views.SearchResultsView");
		findResultPart.setContext(newPartContext);
		findResultPart.setCloseable(true);
		findResultPart.setLabel("Search Results");

		List<MPartStack> partStackList = modelService.findElements(application, "codesnippetapp.partstack.0",
				MPartStack.class, null);

		if (partStackList != null) {
			// In our application there is only one PartStack
			MPartStack partStack = partStackList.get(0);
			// Make PartStack visible
			partStack.setVisible(true);
			// Append Part
			partStack.getChildren().add(findResultPart);
			// Activate the Part
			partService.activate(findResultPart, true);
		}

	}
}