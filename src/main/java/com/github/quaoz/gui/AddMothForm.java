package com.github.quaoz.gui;

import com.github.quaoz.Main;
import com.github.quaoz.MothManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.ResourceBundle;

public class AddMothForm {
	private static Method $$$cachedGetBundleMethod$$$ = null;
	private JPanel panel;
	private JTextField speciesField;
	private JLabel speciesLabel;
	private JLabel speciesValidLabel;
	private JLabel sciNameLabel;
	private JTextField sciNameField;
	private JLabel sciNameValidLabel;
	private JPanel sizePanel;
	private JSpinner sizeLowerSpinner;
	private JSpinner sizeUpperSpinner;
	private JLabel sizeLabel;
	private JLabel sizeValidLabel;
	private JLabel sizeToLabel;
	private JLabel flightTimeLabel;
	private JPanel flightPanel;
	private JSpinner flightStartSpinner;
	private JLabel flightToLabel;
	private JSpinner flightEndSpinner;
	private JLabel flightValidLabel;
	private JLabel habitatLabel;
	private JTextField habitatField;
	private JLabel foodLabel;
	private JTextField foodField;
	private JButton submitButton;
	private JButton cancelButton;

	{
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
		$$$setupUI$$$();
	}

	public AddMothForm() {
		sizeLowerSpinner.setModel(new SpinnerNumberModel(0, 0, 30, 0.1));
		sizeUpperSpinner.setModel(new SpinnerNumberModel(1, 0, 30, 0.1));
		flightStartSpinner.setModel(new SpinnerNumberModel(Calendar.getInstance().get(Calendar.MONTH) + 1, 1, 12, 1));
		flightEndSpinner.setModel(new SpinnerNumberModel(Calendar.getInstance().get(Calendar.MONTH) + 1, 1, 12, 1));

		cancelButton.addActionListener(e -> Main.getGui().render(GUI.Content.PAST_CONTENT));

		speciesField.addActionListener(e -> checkSpecies());
		speciesField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (!e.isTemporary()) {
					speciesValidLabel.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				checkSpecies();
			}
		});

		sciNameField.addActionListener(e -> checkSciName());
		sciNameField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (!e.isTemporary()) {
					sciNameValidLabel.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				checkSciName();
			}
		});

		FocusListener sizeFocusListener = new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (!e.isTemporary()) {
					sizeValidLabel.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				checkSize();
			}
		};

		sizeLowerSpinner.addFocusListener(sizeFocusListener);
		sizeUpperSpinner.addFocusListener(sizeFocusListener);

		FocusListener flightFocusListener = new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (!e.isTemporary()) {
					flightValidLabel.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				checkFlight();
			}
		};

		flightEndSpinner.addFocusListener(flightFocusListener);
		flightStartSpinner.addFocusListener(flightFocusListener);

		submitButton.addActionListener(e -> {
			if (checkSpecies() && checkSciName() && checkSize() && checkFlight()) {
				MothManager.addMoth(speciesField.getText().strip(), sciNameField.getText().strip(),
						Double.parseDouble((String) sizeLowerSpinner.getValue()),
						Double.parseDouble((String) sizeUpperSpinner.getValue()),
						Integer.parseInt(((String) flightStartSpinner.getValue())),
						Integer.parseInt(((String) flightEndSpinner.getValue())),
						habitatField.getText().strip(), foodField.getText().strip());

				Main.getGui().render(GUI.Content.PAST_CONTENT);
			}
		});
	}

	private boolean checkSpecies() {
		String species = speciesField.getText().strip();

		if (species.length() > 1) {
			if (species.chars().allMatch(Character::isLetter)) {
				return true;
			} else {
				speciesValidLabel.setText("Species name may only contain letters");
			}
		}

		return false;
	}

	private boolean checkSciName() {
		String species = sciNameField.getText().strip();

		if (species.length() > 1) {
			if (species.chars().allMatch(Character::isLetter)) {
				return true;
			} else {
				speciesValidLabel.setText("Scientific name may only contain letters");
			}
		}

		return false;
	}

	private boolean checkSize() {
		try {
			Double.parseDouble((String) sizeLowerSpinner.getValue());
			Double.parseDouble((String) sizeUpperSpinner.getValue());
			return true;
		} catch (NumberFormatException e) {
			sizeValidLabel.setText("Invalid size");
			return false;
		}
	}

	private boolean checkFlight() {
		try {
			Integer.parseInt((String) flightEndSpinner.getValue());
			Integer.parseInt((String) sizeUpperSpinner.getValue());
			return true;
		} catch (NumberFormatException e) {
			flightValidLabel.setText("Invalid size");
			return false;
		}
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
		panel.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:d:grow", "center:d:grow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:d:grow"));
		speciesLabel = new JLabel();
		this.$$$loadLabelText$$$(speciesLabel, this.$$$getMessageFromBundle$$$("ia", "species.name"));
		CellConstraints cc = new CellConstraints();
		panel.add(speciesLabel, cc.xy(3, 3));
		speciesField = new JTextField();
		panel.add(speciesField, cc.xy(3, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
		final Spacer spacer1 = new Spacer();
		panel.add(spacer1, cc.xy(5, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
		final Spacer spacer2 = new Spacer();
		panel.add(spacer2, cc.xy(1, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
		final Spacer spacer3 = new Spacer();
		panel.add(spacer3, cc.xy(3, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
		final Spacer spacer4 = new Spacer();
		panel.add(spacer4, cc.xy(3, 43, CellConstraints.DEFAULT, CellConstraints.FILL));
		speciesValidLabel = new JLabel();
		speciesValidLabel.setText("");
		panel.add(speciesValidLabel, cc.xy(3, 7));
		sciNameLabel = new JLabel();
		this.$$$loadLabelText$$$(sciNameLabel, this.$$$getMessageFromBundle$$$("ia", "scientific.name"));
		panel.add(sciNameLabel, cc.xy(3, 9));
		sciNameField = new JTextField();
		panel.add(sciNameField, cc.xy(3, 11, CellConstraints.FILL, CellConstraints.DEFAULT));
		sizePanel = new JPanel();
		sizePanel.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow", "center:d:grow,top:3dlu:noGrow,center:max(d;4px):noGrow"));
		panel.add(sizePanel, cc.xy(3, 17));
		sizeLowerSpinner = new JSpinner();
		sizePanel.add(sizeLowerSpinner, cc.xy(1, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
		sizeUpperSpinner = new JSpinner();
		sizePanel.add(sizeUpperSpinner, cc.xy(5, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
		sizeToLabel = new JLabel();
		this.$$$loadLabelText$$$(sizeToLabel, this.$$$getMessageFromBundle$$$("ia", "to"));
		sizePanel.add(sizeToLabel, cc.xy(3, 3));
		sizeLabel = new JLabel();
		this.$$$loadLabelText$$$(sizeLabel, this.$$$getMessageFromBundle$$$("ia", "size"));
		panel.add(sizeLabel, cc.xy(3, 15));
		sizeValidLabel = new JLabel();
		sizeValidLabel.setText("");
		panel.add(sizeValidLabel, cc.xy(3, 19));
		flightTimeLabel = new JLabel();
		flightTimeLabel.setText("Flies from");
		panel.add(flightTimeLabel, cc.xy(3, 21));
		sciNameValidLabel = new JLabel();
		sciNameValidLabel.setText("");
		panel.add(sciNameValidLabel, cc.xy(3, 13));
		flightPanel = new JPanel();
		flightPanel.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow", "center:d:grow,top:3dlu:noGrow,center:max(d;4px):noGrow"));
		panel.add(flightPanel, cc.xy(3, 23));
		flightStartSpinner = new JSpinner();
		flightPanel.add(flightStartSpinner, cc.xy(1, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
		flightToLabel = new JLabel();
		this.$$$loadLabelText$$$(flightToLabel, this.$$$getMessageFromBundle$$$("ia", "to"));
		flightPanel.add(flightToLabel, cc.xy(3, 3));
		flightEndSpinner = new JSpinner();
		flightPanel.add(flightEndSpinner, cc.xy(5, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
		flightValidLabel = new JLabel();
		flightValidLabel.setText("");
		panel.add(flightValidLabel, cc.xy(3, 25));
		habitatLabel = new JLabel();
		this.$$$loadLabelText$$$(habitatLabel, this.$$$getMessageFromBundle$$$("ia", "habitat"));
		panel.add(habitatLabel, cc.xy(3, 27));
		habitatField = new JTextField();
		panel.add(habitatField, cc.xy(3, 29, CellConstraints.FILL, CellConstraints.DEFAULT));
		foodLabel = new JLabel();
		foodLabel.setText("Food sources");
		panel.add(foodLabel, cc.xy(3, 33));
		foodField = new JTextField();
		panel.add(foodField, cc.xy(3, 35, CellConstraints.FILL, CellConstraints.DEFAULT));
		submitButton = new JButton();
		this.$$$loadButtonText$$$(submitButton, this.$$$getMessageFromBundle$$$("ia", "submit"));
		panel.add(submitButton, cc.xy(3, 39));
		cancelButton = new JButton();
		this.$$$loadButtonText$$$(cancelButton, this.$$$getMessageFromBundle$$$("ia", "cancel"));
		panel.add(cancelButton, cc.xy(3, 41));
		speciesLabel.setLabelFor(speciesField);
		sciNameLabel.setLabelFor(sciNameField);
		habitatLabel.setLabelFor(habitatField);
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

}
