package com.github.quaoz.gui;

import com.github.quaoz.Main;
import com.github.quaoz.Moth;
import com.github.quaoz.MothManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.text.DateFormatSymbols;
import java.util.ResourceBundle;

public class RecordForm {
	private static Method $$$cachedGetBundleMethod$$$ = null;
	private JPanel panel;
	private JTextField searchField;
	private JButton advancedSearchButton;
	private JButton submitRecordButton;
	private JButton profileButton;
	private JLabel nameLabel;
	private JLabel sciNameLabel;
	private JLabel sizeLabel;
	private JLabel flightTimeLabel;
	private JLabel habitatLabel;
	private JLabel foodSourceLabel;
	private JButton recordsButton;

	{
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
		$$$setupUI$$$();
	}

	public RecordForm() {
		Moth moth = Main.getGui().getRecord();

		nameLabel.setText(moth.getName());
		sciNameLabel.setText(moth.getSciName());

		sizeLabel.setText("Size: " + moth.getSizeLower() + " to " + moth.getSizeUpper());
		flightTimeLabel.setText(
				"Flies from "
						+ DateFormatSymbols.getInstance().getMonths()[moth.getFlightStart()]
						+ " to "
						+ DateFormatSymbols.getInstance().getMonths()[moth.getFlightEnd()]);
		habitatLabel.setText("Habitat: " + moth.getHabitat());
		foodSourceLabel.setText("Food sources: " + moth.getFood());

		recordsButton.addActionListener(
				e -> {
					// TODO: get records
					Main.getGui().render(GUI.Content.SEARCH_RESULTS);
				});

		submitRecordButton.addActionListener(e -> Main.getGui().render(GUI.Content.SUBMIT_RECORD));

		advancedSearchButton.addActionListener(e -> Main.getGui().render(GUI.Content.ADVANCED_SEARCH));

		profileButton.addActionListener(e -> Main.getGui().render(GUI.Content.PROFILE));

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
		panel = new JPanel();
		panel.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow", "center:d:noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:18px:noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow"));
		panel.setMinimumSize(new Dimension(768, 768));
		panel.setPreferredSize(new Dimension(768, 768));
		searchField = new JTextField();
		searchField.setMinimumSize(new Dimension(200, 30));
		searchField.setPreferredSize(new Dimension(200, 30));
		searchField.setText("Search");
		CellConstraints cc = new CellConstraints();
		panel.add(searchField, cc.xy(3, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
		advancedSearchButton = new JButton();
		this.$$$loadButtonText$$$(advancedSearchButton, this.$$$getMessageFromBundle$$$("ia", "advanced.search"));
		panel.add(advancedSearchButton, cc.xy(5, 1));
		submitRecordButton = new JButton();
		this.$$$loadButtonText$$$(submitRecordButton, this.$$$getMessageFromBundle$$$("ia", "submit.record"));
		panel.add(submitRecordButton, cc.xy(7, 1));
		profileButton = new JButton();
		this.$$$loadButtonText$$$(profileButton, this.$$$getMessageFromBundle$$$("ia", "profile"));
		panel.add(profileButton, cc.xy(9, 1));
		final Spacer spacer1 = new Spacer();
		panel.add(spacer1, cc.xy(11, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
		final Spacer spacer2 = new Spacer();
		panel.add(spacer2, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
		nameLabel = new JLabel();
		nameLabel.setText("");
		panel.add(nameLabel, cc.xy(3, 3));
		sciNameLabel = new JLabel();
		sciNameLabel.setText("");
		panel.add(sciNameLabel, cc.xy(3, 5));
		sizeLabel = new JLabel();
		sizeLabel.setText("");
		panel.add(sizeLabel, cc.xy(5, 3));
		flightTimeLabel = new JLabel();
		flightTimeLabel.setText("");
		panel.add(flightTimeLabel, cc.xy(5, 5));
		habitatLabel = new JLabel();
		habitatLabel.setText("");
		panel.add(habitatLabel, cc.xy(5, 7));
		foodSourceLabel = new JLabel();
		foodSourceLabel.setText("");
		panel.add(foodSourceLabel, cc.xy(5, 9));
		recordsButton = new JButton();
		this.$$$loadButtonText$$$(recordsButton, this.$$$getMessageFromBundle$$$("ia", "view.records"));
		panel.add(recordsButton, cc.xy(3, 7));
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

}
