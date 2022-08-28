package com.github.quaoz.gui;

import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;

public class SearchForm {
	private JButton searchButton;
	private JLabel titleLabel;
	private JTextField searchBox;
	private JPanel panel;

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
		panel.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:d:grow", "center:d:grow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
		panel.setMinimumSize(new Dimension(240, 180));
		panel.setPreferredSize(new Dimension(240, 180));
		titleLabel = new JLabel();
		titleLabel.setHorizontalAlignment(10);
		titleLabel.setText("Search");
		CellConstraints cc = new CellConstraints();
		panel.add(titleLabel, cc.xy(3, 3, CellConstraints.LEFT, CellConstraints.DEFAULT));
		final Spacer spacer1 = new Spacer();
		panel.add(spacer1, cc.xywh(5, 3, 1, 11, CellConstraints.RIGHT, CellConstraints.DEFAULT));
		final Spacer spacer2 = new Spacer();
		panel.add(spacer2, cc.xywh(1, 3, 1, 11, CellConstraints.LEFT, CellConstraints.DEFAULT));
		searchBox = new JTextField();
		panel.add(searchBox, cc.xy(3, 5, CellConstraints.FILL, CellConstraints.CENTER));
		searchButton = new JButton();
		searchButton.setText("Search");
		panel.add(searchButton, cc.xy(3, 7));
		final Spacer spacer3 = new Spacer();
		panel.add(spacer3, cc.xy(3, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
		final Spacer spacer4 = new Spacer();
		panel.add(spacer4, cc.xy(3, 9, CellConstraints.DEFAULT, CellConstraints.FILL));
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return panel;
	}

}
