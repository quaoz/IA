package com.github.quaoz.gui;

import com.github.quaoz.Main;
import com.github.quaoz.Moth;
import com.github.quaoz.MothManager;
import com.github.quaoz.RecordManager;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Profile {
	private static Method $$$cachedGetBundleMethod$$$ = null;
	private JTextField searchField;
	private JButton advancedSearchButton;
	private JButton submitRecordButton;
	private JButton homeButton;
	private JPanel panel;
	private JScrollPane recordScrollPane;
	private JTable table;
	private final ArrayList<Moth> records;

	public Profile() {
		$$$setupUI$$$();
		records = RecordManager.searchUser(Main.getUser());

		submitRecordButton.addActionListener(e -> Main.getGui().render(GUI.Content.SUBMIT_RECORD));
		advancedSearchButton.addActionListener(e -> Main.getGui().render(GUI.Content.ADVANCED_SEARCH));
		homeButton.addActionListener(e -> Main.getGui().render(GUI.Content.HOME_LOGGED_IN));

		searchField.addActionListener(
				e -> {
					Main.getGui()
							.setSearchResults(MothManager.collectMoths(searchField.getText().strip(), 0, 100));
					Main.getGui().render(GUI.Content.SEARCH_RESULTS);
				});
	}

	public JPanel resolve() {
		return panel;
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		createUIComponents();
		panel = new JPanel();
		panel.setLayout(new GridLayoutManager(2, 6, new Insets(0, 0, 0, 0), -1, -1));
		panel.setMinimumSize(new Dimension(768, 768));
		panel.setPreferredSize(new Dimension(768, 768));
		searchField = new JTextField();
		searchField.setMinimumSize(new Dimension(200, 30));
		searchField.setPreferredSize(new Dimension(200, 30));
		searchField.setText("Search");
		panel.add(searchField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		advancedSearchButton = new JButton();
		this.$$$loadButtonText$$$(advancedSearchButton, this.$$$getMessageFromBundle$$$("ia", "advanced.search"));
		panel.add(advancedSearchButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		submitRecordButton = new JButton();
		this.$$$loadButtonText$$$(submitRecordButton, this.$$$getMessageFromBundle$$$("ia", "submit.record"));
		panel.add(submitRecordButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		homeButton = new JButton();
		this.$$$loadButtonText$$$(homeButton, this.$$$getMessageFromBundle$$$("ia", "home"));
		panel.add(homeButton, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer1 = new Spacer();
		panel.add(spacer1, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		final Spacer spacer2 = new Spacer();
		panel.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		panel.add(recordScrollPane, new GridConstraints(1, 1, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		table = new JTable();
		recordScrollPane.setViewportView(table);
	}

	private String $$$getMessageFromBundle$$$(String path, String key) {
		ResourceBundle bundle;
		try {
			Class<?> thisClass = this.getClass();
			if ($$$cachedGetBundleMethod$$$ == null) {
				Class<?> dynamicBundleClass = thisClass.getClassLoader().loadClass("com.intellij.DynamicBundle");
				$$$cachedGetBundleMethod$$$ = dynamicBundleClass.getMethod("getBundle", String.class, Class.class);
			}
			bundle = (ResourceBundle) $$$cachedGetBundleMethod$$$.invoke(null, path, thisClass);
		} catch (Exception e) {
			bundle = ResourceBundle.getBundle(path);
		}
		return bundle.getString(key);
	}

	/**
	 * @noinspection ALL
	 */
	private void $$$loadButtonText$$$(AbstractButton component, String text) {
		StringBuffer result = new StringBuffer();
		boolean haveMnemonic = false;
		char mnemonic = '\0';
		int mnemonicIndex = -1;
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == '&') {
				i++;
				if (i == text.length()) break;
				if (!haveMnemonic && text.charAt(i) != '&') {
					haveMnemonic = true;
					mnemonic = text.charAt(i);
					mnemonicIndex = result.length();
				}
			}
			result.append(text.charAt(i));
		}
		component.setText(result.toString());
		if (haveMnemonic) {
			component.setMnemonic(mnemonic);
			component.setDisplayedMnemonicIndex(mnemonicIndex);
		}
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return panel;
	}

	private void createUIComponents() {
		String[] columnNames = {
				"Species", "Scientific Name", "Size", "Flight", "Habitat", "Food Sources"
		};
		String[][] data = new String[records.size()][6];

		int count = 0;
		for (Moth moth : records) {
			data[count][0] = moth.getName();
			data[count][1] = moth.getSciName();
			data[count][2] = moth.getSizeLower() + " - " + moth.getSizeUpper();
			data[count][3] = moth.getFlightStart() + " - " + moth.getFlightEnd();
			data[count][4] = moth.getHabitat();
			data[count][5] = moth.getFood();
			count++;
		}

		table = new JTable(data, columnNames);

		// Hacky way to prevent editing
		table.setDefaultEditor(Object.class, null);
	}
}
