package com.github.quaoz.gui;

import com.github.quaoz.managers.MothManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.*;
import javax.swing.*;

public class LoggedOutBar extends UIPanel {

	private final JTextField searchField;

	public LoggedOutBar() {
		super();
		this.setLayout(
				new FormLayout(
					"fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):" +
					"noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow," +
					"left:4dlu:noGrow,fill:127px:noGrow,left:4dlu:noGrow,fill:d:grow",
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

		JButton advancedSearchButton = new JButton();
		loadButtonText(
			advancedSearchButton,
			getMessageFromBundle("ia", "advanced.search")
		);
		this.add(advancedSearchButton, cc.xy(5, 1));

		JButton signInButton = new JButton();
		signInButton.setMinimumSize(new Dimension(150, 25));
		loadButtonText(signInButton, getMessageFromBundle("ia", "sign.in"));
		this.add(signInButton, cc.xy(7, 1));

		JButton signUpButton = new JButton();
		loadButtonText(signUpButton, getMessageFromBundle("ia", "sign.up"));
		this.add(signUpButton, cc.xy(9, 1));

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

		advancedSearchButton.addActionListener(e ->
			GUI.getInstance().render(GUI.Content.ADVANCED_SEARCH)
		);
		signInButton.addActionListener(e ->
			GUI.getInstance().render(GUI.Content.SIGN_IN)
		);
		signUpButton.addActionListener(e ->
			GUI.getInstance().render(GUI.Content.SIGN_UP)
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
