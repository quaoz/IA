package com.github.quaoz.gui;

import com.github.quaoz.Main;
import com.github.quaoz.Moth;
import com.github.quaoz.MothManager;
import com.github.quaoz.RecordManager;
import com.github.quaoz.structures.Pair;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;
import javax.swing.*;

public class AdvancedSearchForm {
	private JTextField nameField;
	private JTextField locationField;
	private JTextField habitatField;
	private JTextField foodField;
	private JButton searchButton;
	private JLabel nameLabel;
	private JLabel locationLabel;
	private JLabel sizeLabel;
	private JLabel flightTimeLabel;
	private JLabel habitatLabel;
	private JLabel foodLabel;
	private JLabel titleLabel;
	private JButton cancelButton;
	private JPanel panel;
	private JSpinner sizeLowerSpinner;
	private JSpinner sizeUpperSpinner;
	private JSpinner flightStartSpinner;
	private JSpinner flightEndSpinner;

	public AdvancedSearchForm() {
		cancelButton.addActionListener(e -> Main.getGui().render(GUI.Content.PAST_CONTENT));

		sizeLowerSpinner.setModel(new SpinnerNumberModel(0, 0, 30, 0.1));
		sizeUpperSpinner.setModel(new SpinnerNumberModel(1, 0, 30, 0.1));
		flightStartSpinner.setModel(
				new SpinnerNumberModel(Calendar.getInstance().get(Calendar.MONTH) + 1, 1, 12, 1));
		flightEndSpinner.setModel(
				new SpinnerNumberModel(Calendar.getInstance().get(Calendar.MONTH) + 1, 1, 12, 1));

		// FIXME
		searchButton.addActionListener(
				e -> {
					String name = nameField.getText().strip();
					String habitat = habitatField.getText().strip();
					String food = foodField.getText().strip();
					String location = locationField.getText().strip();

					Double sizeLower = null;
					Double sizeUpper = null;

					try {
						sizeLower = (Double) sizeLowerSpinner.getValue();
					} catch (ClassCastException ignored) {
					}

					try {
						sizeUpper = (Double) sizeUpperSpinner.getValue();
					} catch (ClassCastException ignored) {
					}

					if (sizeLower == null) {
						sizeLower = sizeUpper;
					} else if (sizeUpper == null) {
						sizeUpper = sizeLower;
					} else {
						if (sizeLower > sizeUpper) {
							Double tmp = sizeLower;
							sizeLower = sizeUpper;
							sizeUpper = tmp;
						}
					}

					Integer flightStart = null;
					Integer flightEnd = null;

					try {
						flightStart = (Integer) flightStartSpinner.getValue();
					} catch (NumberFormatException | ClassCastException ignored) {
					}

					try {
						flightEnd = (Integer) flightEndSpinner.getValue();
					} catch (NumberFormatException | ClassCastException ignored) {
					}

					if (flightStart == null) {
						flightStart = flightEnd;
					} else if (flightEnd == null) {
						flightEnd = flightStart;
					} else {
						if (flightStart > flightEnd) {
							Integer tmp = flightStart;
							flightStart = flightEnd;
							flightEnd = tmp;
						}
					}

					ArrayList<Pair<Moth, Double>> records = new ArrayList<>();

					if (sizeLower != null) {
						records =
								MothManager.collectMoths(
										String.format("%s:%s", sizeLower, sizeUpper),
										2,
										100,
										(s1, s2) -> {
											Double sizeLower1 =
													Double.parseDouble(s1.split(":")[0]);
											Double sizeUpper1 =
													Double.parseDouble(s1.split(":")[1]);
											Double sizeLower2 =
													Double.parseDouble(s2.split(":")[0]);
											Double sizeUpper2 =
													Double.parseDouble(s2.split(":")[1]);

											Double difference1 = Math.abs(sizeLower1 - sizeLower2);
											Double difference2 = Math.abs(sizeUpper1 - sizeUpper2);

											return -(difference1 + difference2);
										});
					} else if (!location.isBlank()) {
						records = RecordManager.searchLocation(location);
					} else if (flightEnd != null) {
						records =
								MothManager.collectMoths(
										String.format("%s:%s", flightStart, flightEnd),
										3,
										100,
										(s1, s2) -> {
											Integer flightStart1 =
													Integer.parseInt(s1.split(":")[0]);
											Integer flightEnd1 = Integer.parseInt(s1.split(":")[1]);
											Integer flightStart2 =
													Integer.parseInt(s2.split(":")[0]);
											Integer flightEnd2 = Integer.parseInt(s2.split(":")[1]);

											Integer difference1 =
													Math.abs(flightStart1 - flightStart2);
											Integer difference2 = Math.abs(flightEnd1 - flightEnd2);

											return -(difference1 + difference2);
										});
					} else if (!habitat.isBlank()) {
						records = MothManager.collectMoths(habitat, 4, 100);
					} else if (!food.isBlank()) {
						records = MothManager.collectMoths(food, 5, 100);
					} else if (!name.isBlank()) {
						records = MothManager.collectMoths(name, 0, 100);
					}

					Main.getGui().setSearchResults(records);
					Main.getGui().render(GUI.Content.SEARCH_RESULTS);
				});
	}

	public JPanel resolve() {
		return panel;
	}

	//spotless:off
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
		panel.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:d:grow", "center:d:grow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,center:d:grow"));
		panel.setMinimumSize(new Dimension(768, 768));
		panel.setPreferredSize(new Dimension(768, 768));
		nameField = new JTextField();
		CellConstraints cc = new CellConstraints();
		panel.add(nameField, cc.xyw(5, 5, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
		locationField = new JTextField();
		panel.add(locationField, cc.xyw(5, 7, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
		habitatField = new JTextField();
		panel.add(habitatField, cc.xyw(5, 13, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
		foodField = new JTextField();
		panel.add(foodField, cc.xyw(5, 15, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
		final JLabel label1 = new JLabel();
		this.$$$loadLabelText$$$(label1, this.$$$getMessageFromBundle$$$("ia", "to"));
		panel.add(label1, cc.xyw(7, 9, 2, CellConstraints.CENTER, CellConstraints.DEFAULT));
		final JLabel label2 = new JLabel();
		this.$$$loadLabelText$$$(label2, this.$$$getMessageFromBundle$$$("ia", "to"));
		panel.add(label2, cc.xy(7, 11, CellConstraints.CENTER, CellConstraints.DEFAULT));
		nameLabel = new JLabel();
		this.$$$loadLabelText$$$(nameLabel, this.$$$getMessageFromBundle$$$("ia", "name"));
		panel.add(nameLabel, cc.xy(3, 5));
		locationLabel = new JLabel();
		this.$$$loadLabelText$$$(locationLabel, this.$$$getMessageFromBundle$$$("ia", "location"));
		panel.add(locationLabel, cc.xy(3, 7));
		sizeLabel = new JLabel();
		this.$$$loadLabelText$$$(sizeLabel, this.$$$getMessageFromBundle$$$("ia", "size"));
		panel.add(sizeLabel, cc.xy(3, 9));
		flightTimeLabel = new JLabel();
		this.$$$loadLabelText$$$(flightTimeLabel, this.$$$getMessageFromBundle$$$("ia", "flies.from"));
		panel.add(flightTimeLabel, cc.xy(3, 11));
		habitatLabel = new JLabel();
		this.$$$loadLabelText$$$(habitatLabel, this.$$$getMessageFromBundle$$$("ia", "habitat"));
		panel.add(habitatLabel, cc.xy(3, 13));
		foodLabel = new JLabel();
		this.$$$loadLabelText$$$(foodLabel, this.$$$getMessageFromBundle$$$("ia", "food.sources"));
		panel.add(foodLabel, cc.xy(3, 15));
		searchButton = new JButton();
		this.$$$loadButtonText$$$(searchButton, this.$$$getMessageFromBundle$$$("ia", "search"));
		panel.add(searchButton, cc.xy(9, 17));
		titleLabel = new JLabel();
		this.$$$loadLabelText$$$(titleLabel, this.$$$getMessageFromBundle$$$("ia", "advanced.search"));
		panel.add(titleLabel, cc.xyw(5, 3, 5));
		cancelButton = new JButton();
		this.$$$loadButtonText$$$(cancelButton, this.$$$getMessageFromBundle$$$("ia", "cancel"));
		panel.add(cancelButton, cc.xy(5, 17));
		sizeLowerSpinner = new JSpinner();
		panel.add(sizeLowerSpinner, cc.xy(5, 9, CellConstraints.FILL, CellConstraints.DEFAULT));
		sizeUpperSpinner = new JSpinner();
		panel.add(sizeUpperSpinner, cc.xy(9, 9, CellConstraints.FILL, CellConstraints.DEFAULT));
		flightStartSpinner = new JSpinner();
		panel.add(flightStartSpinner, cc.xy(5, 11, CellConstraints.FILL, CellConstraints.DEFAULT));
		flightEndSpinner = new JSpinner();
		panel.add(flightEndSpinner, cc.xy(9, 11, CellConstraints.FILL, CellConstraints.DEFAULT));
		final Spacer spacer1 = new Spacer();
		panel.add(spacer1, cc.xy(7, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
		final Spacer spacer2 = new Spacer();
		panel.add(spacer2, cc.xy(7, 18, CellConstraints.DEFAULT, CellConstraints.FILL));
		final Spacer spacer3 = new Spacer();
		panel.add(spacer3, cc.xy(11, 10, CellConstraints.FILL, CellConstraints.DEFAULT));
		final Spacer spacer4 = new Spacer();
		panel.add(spacer4, cc.xy(1, 10, CellConstraints.FILL, CellConstraints.DEFAULT));
		locationLabel.setLabelFor(locationField);
		habitatLabel.setLabelFor(habitatField);
		foodLabel.setLabelFor(foodField);
	}

	private static Method $$$cachedGetBundleMethod$$$ = null;

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
	private void $$$loadLabelText$$$(JLabel component, String text) {
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
			component.setDisplayedMnemonic(mnemonic);
			component.setDisplayedMnemonicIndex(mnemonicIndex);
		}
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
	//spotless:on
}
