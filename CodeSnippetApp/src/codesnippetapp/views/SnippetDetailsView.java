
package codesnippetapp.views;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import codesnippetapp.CodeSnippetAppConstants;
import codesnippetapp.data.SnippetData;

public class SnippetDetailsView {
	private Text snippetText, codeText, descText;
	private Button saveButton;
	private SnippetData snippet; // Currently displayed snippet data

	@Inject
	public SnippetDetailsView() {

	}

	@PostConstruct
	public void postConstruct(Composite parent, final IEventBroker eventBroker) {
		Composite snippetDetailsComposite = new Composite(parent, SWT.None);
		snippetDetailsComposite.setLayout(new GridLayout(2, false));

		Label snippetLabel = new Label(snippetDetailsComposite, SWT.None);
		snippetLabel.setText("Snippet Name:");
		snippetText = new Text(snippetDetailsComposite, SWT.BORDER);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.CENTER).applyTo(snippetText);

		Label codeLabel = new Label(snippetDetailsComposite, SWT.None);
		codeLabel.setText("Code:");

		codeText = new Text(snippetDetailsComposite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(codeText);

		Label descLabel = new Label(snippetDetailsComposite, SWT.None);
		descLabel.setText("Description:");

		descText = new Text(snippetDetailsComposite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(descText);

		// Create Save button
		saveButton = new Button(snippetDetailsComposite, SWT.BORDER);
		saveButton.setText("Save");
		saveButton.setEnabled(false);
		saveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!saveSnippetData())
					return;
				eventBroker.post(CodeSnippetAppConstants.SNIPPET_SAVED_EVENT, snippet);
			}
		});
	}

	@Inject
	@Optional
	public void snippetSelectionChanged(
			@UIEventTopic(CodeSnippetAppConstants.SNIPPET_SELECTION_CHANGE_EVENT) SnippetData snippet) {
		if (snippet != null && snippet.name != null)
			snippetText.setText(snippet.name);
		else
			snippetText.setText("");
		if (snippet != null && snippet.code != null)
			codeText.setText(snippet.code);
		else
			codeText.setText("");
		if (snippet != null && snippet.description != null)
			descText.setText(snippet.description);
		else
			descText.setText("");
		this.snippet = snippet;
		if (snippet != null) {
			saveButton.setEnabled(true);
			snippetText.setFocus();
			int caretPos = snippetText.getText().length();
			snippetText.setSelection(caretPos, caretPos);
		} else
			saveButton.setEnabled(false);
	}

	private boolean saveSnippetData() {
		if (snippet == null) {
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error saving Snippet",
					"No snippet selected for saving");
			return false;
		}

		// TODO: Validate snippet data here

		snippet.name = snippetText.getText();
		snippet.code = codeText.getText();
		snippet.description = descText.getText();
		return true;
	}

}