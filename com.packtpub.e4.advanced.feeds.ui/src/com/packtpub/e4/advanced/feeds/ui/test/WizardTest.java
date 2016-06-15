package com.packtpub.e4.advanced.feeds.ui.test;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.packtpub.e4.advanced.feeds.ui.NewFeedWizard;

public class WizardTest {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		new WizardDialog(shell, new NewFeedWizard()).open();
		display.dispose();
	}
}
