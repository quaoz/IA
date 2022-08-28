package com.github.quaoz.gui;

import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;

public class HomeLoggedIn {
	private JTextField searchField;
	private JButton advancedSearchButton;
	private JButton submitRecordButton;
	private JButton profileButton;
	private JPanel panel;

	public HomeLoggedIn(GUI gui) {
		submitRecordButton.addActionListener(e -> {
			gui.render(GUI.Content.SUBMIT_RECORD);
		});

		advancedSearchButton.addActionListener(e -> {
			gui.render(GUI.Content.ADVANCED_SEARCH);
		});

		profileButton.addActionListener(e -> {
			gui.render(GUI.Content.PROFILE);
		});

		searchField.addActionListener(e -> {
			//TODO: Do search
			gui.render(GUI.Content.SEARCH_RESULTS);
		});
	}

	public JPanel resolve() {
		return panel;
	}

	{
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
		$$$setupUI$$$();
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		panel = new JPanel();
		panel.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow", "center:max(d;4px):noGrow"));
		panel.setMinimumSize(new Dimension(600, 300));
		panel.setPreferredSize(new Dimension(600, 300));
		searchField = new JTextField();
		searchField.setMinimumSize(new Dimension(200, 30));
		searchField.setPreferredSize(new Dimension(200, 30));
		searchField.setText("Search");
		CellConstraints cc = new CellConstraints();
		panel.add(searchField, cc.xy(3, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
		advancedSearchButton = new JButton();
		advancedSearchButton.setText("Advanced Search");
		panel.add(advancedSearchButton, cc.xy(5, 1));
		submitRecordButton = new JButton();
		submitRecordButton.setText("Submit Record");
		panel.add(submitRecordButton, cc.xy(7, 1));
		profileButton = new JButton();
		profileButton.setText("Profile");
		panel.add(profileButton, cc.xy(9, 1));
		final Spacer spacer1 = new Spacer();
		panel.add(spacer1, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
		final Spacer spacer2 = new Spacer();
		panel.add(spacer2, cc.xy(11, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return panel;
	}

}
