package com.github.quaoz.gui;

import com.github.quaoz.managers.MothManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.*;
import javax.swing.*;

public class ProfileBar extends UIPanel {

	private final JTextField searchField;

	public ProfileBar() {
		super();
		this.setLayout(
				new FormLayout(
					"fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:127px:noGrow,left:4dlu:noGrow,fill:d:grow",
					"center:d:noGrow"
				)
			);

		searchField = new JTextField();
		searchField.setMinimumSize(new Dimension(200, 30));
		searchField.setPreferredSize(new Dimension(200, 30));
		searchField.setText("");
		searchField.setToolTipText(getMessageFromBundle("ia", "search"));

		CellConstraints cc = new CellConstraints();
		this.add(
				searchField,
				cc.xy(3, 1, CellConstraints.FILL, CellConstraints.DEFAULT)
			);

		JButton submitRecordButton = new JButton();
		loadButtonText(
			submitRecordButton,
			getMessageFromBundle("ia", "submit.record")
		);
		this.add(submitRecordButton, cc.xy(7, 1));

		JButton homeButton = new JButton();
		loadButtonText(homeButton, getMessageFromBundle("ia", "home"));
		this.add(homeButton, cc.xy(9, 1));

		JButton advancedSearchButton = new JButton();
		loadButtonText(
			advancedSearchButton,
			getMessageFromBundle("ia", "advanced.search")
		);
		this.add(advancedSearchButton, cc.xy(5, 1));

		final Spacer spacer1 = new Spacer();
		this.add(
				spacer1,
				cc.xy(11, 1, CellConstraints.FILL, CellConstraints.DEFAULT)
			);

		final Spacer spacer2 = new Spacer();
		this.add(
				spacer2,
				cc.xy(1, 1, CellConstraints.FILL, CellConstraints.DEFAULT)
			);

		submitRecordButton.addActionListener(e ->
			GUI.getInstance().render(GUI.Content.SUBMIT_RECORD)
		);
		advancedSearchButton.addActionListener(e ->
			GUI.getInstance().render(GUI.Content.ADVANCED_SEARCH)
		);
		homeButton.addActionListener(e -> GUI.getInstance().render(GUI.Content.HOME)
		);

		searchField.addActionListener(e -> {
			SearchResultsForm.setSearchResults(
				MothManager
					.getInstance()
					.collectMoths(searchField.getText().strip(), 0, 100)
			);
			GUI.getInstance().render(GUI.Content.SEARCH_RESULTS);
		});
	}
}
