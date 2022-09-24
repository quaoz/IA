package com.github.quaoz.gui;

import com.github.quaoz.Main;
import com.github.quaoz.Moth;
import com.github.quaoz.MothManager;
import com.github.quaoz.RecordManager;
import com.github.quaoz.util.Geocoder;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.*;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import javax.swing.*;
import javax.swing.event.DocumentEvent;

public class SubmitRecordForm {
	private JPanel panel;
	private JTextField speciesField;
	private JTextField locationField;
	private JLabel speciesLabel;
	private JLabel locationLabel;
	private JLabel sizeLabel;
	private JLabel titleLabel;
	private JButton submitButton;
	private JButton cancelButton;
	private JSpinner sizeSpinner;
	private JLabel speciesMatchLabel;
	private JButton registerButton;
	private JPanel datePanel;
	private JSpinner daySpinner;
	private JSpinner monthSpinner;
	private JLabel monthLabel;
	private JLabel dayLabel;
	private JCheckBox coordsCheckBox;
	private JPanel standardLocationPanel;
	private JTextField latitudeField;
	private JTextField longitudeField;
	private JLabel latitudeLabel;
	private JLabel longitudeLabel;
	private JPanel coordinateLocationPanel;
	private JPanel locationPanel;
	private JLabel locationValidLabel;

	public SubmitRecordForm() {
		$$$setupUI$$$();

		coordinateLocationPanel.setVisible(false);
		daySpinner.setModel(
				new SpinnerNumberModel(
						Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 1, 31, 1));
		monthSpinner.setModel(
				new SpinnerNumberModel(Calendar.getInstance().get(Calendar.MONTH) + 1, 1, 12, 1));
		sizeSpinner.setModel(new SpinnerNumberModel(0, 0, 30, 0.1));

		submitButton.addActionListener(
				e -> {
					boolean valid = true;
					String location = "";

					if (coordsCheckBox.isSelected()) {
						try {
							double latitude = Double.parseDouble(latitudeField.getText().strip());
							double longitude = Double.parseDouble(longitudeField.getText().strip());

							location = Geocoder.standardise(latitude, longitude);
						} catch (NumberFormatException exception) {
							locationValidLabel.setText("Invalid coordinates");
							valid = false;
						}
					} else if (locationField.getText().strip().length() <= 1) {
						locationValidLabel.setText("Invalid location");
						valid = false;
					} else {
						location = Geocoder.standardise(locationField.getText().strip());
					}

					if (speciesField.getText().strip().length() <= 1) {
						speciesMatchLabel.setText("Invalid species");
						valid = false;
					}

					// TODO: Submit record
					if (valid) {
						Moth species =
								MothManager.collectMoths(speciesField.getText().strip(), 0, 1)
										.get(0)
										.getKey();

						DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						Date date =
								new GregorianCalendar(
										Calendar.getInstance().get(Calendar.YEAR),
										((Integer) monthSpinner.getValue()),
										((Integer) daySpinner.getValue()))
										.getTime();

						RecordManager.addRecord(
								species.getName(),
								location,
								dateFormat.format(date),
								((Double) sizeSpinner.getValue()),
								Main.getUser());
						Main.getGui().render(GUI.Content.PAST_CONTENT);
					}
				});

		cancelButton.addActionListener(e -> Main.getGui().render(GUI.Content.PAST_CONTENT));

		speciesField
				.getDocument()
				.addDocumentListener(
						new SimpleDocumentListener() {
							private CompletableFuture<Void> completableFuture;

							@Override
							public void update(DocumentEvent e) {
								if (completableFuture != null) {
									completableFuture.cancel(true);
								}

								completableFuture =
										CompletableFuture.supplyAsync(
														() ->
																MothManager.collectMoths(
																		speciesField
																				.getText()
																				.strip(),
																		0,
																		1))
												.thenAccept(
														s ->
																speciesMatchLabel.setText(
																		"Closest match: "
																				+ s.get(0)
																				.getKey()
																				.getName()
																				+ ", if this is not"
																				+ " your moth then"
																				+ " please register"
																				+ " a new moth"
																				+ " bellow."));
							}
						});

		coordsCheckBox.addActionListener(
				e -> {
					if (coordsCheckBox.isSelected()) {
						standardLocationPanel.setVisible(false);
						coordinateLocationPanel.setVisible(true);
					} else {
						standardLocationPanel.setVisible(true);
						coordinateLocationPanel.setVisible(false);
					}
				});

		registerButton.addActionListener(e -> Main.getGui().render(GUI.Content.ADD_MOTH));
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
		panel.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:d:grow", "center:d:grow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:5dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:grow"));
		panel.setMinimumSize(new Dimension(768, 768));
		panel.setPreferredSize(new Dimension(768, 768));
		speciesField = new JTextField();
		CellConstraints cc = new CellConstraints();
		panel.add(speciesField, cc.xy(5, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
		final Spacer spacer1 = new Spacer();
		panel.add(spacer1, cc.xy(5, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
		final Spacer spacer2 = new Spacer();
		panel.add(spacer2, cc.xy(5, 25, CellConstraints.DEFAULT, CellConstraints.FILL));
		final Spacer spacer3 = new Spacer();
		panel.add(spacer3, cc.xy(7, 9, CellConstraints.FILL, CellConstraints.DEFAULT));
		final Spacer spacer4 = new Spacer();
		panel.add(spacer4, cc.xy(1, 9, CellConstraints.FILL, CellConstraints.DEFAULT));
		speciesLabel = new JLabel();
		this.$$$loadLabelText$$$(speciesLabel, this.$$$getMessageFromBundle$$$("ia", "species"));
		panel.add(speciesLabel, cc.xy(3, 5));
		locationLabel = new JLabel();
		this.$$$loadLabelText$$$(locationLabel, this.$$$getMessageFromBundle$$$("ia", "location"));
		panel.add(locationLabel, cc.xy(3, 9));
		sizeLabel = new JLabel();
		this.$$$loadLabelText$$$(sizeLabel, this.$$$getMessageFromBundle$$$("ia", "size.cm"));
		panel.add(sizeLabel, cc.xy(3, 17));
		titleLabel = new JLabel();
		this.$$$loadLabelText$$$(titleLabel, this.$$$getMessageFromBundle$$$("ia", "add.record"));
		panel.add(titleLabel, cc.xy(5, 3));
		submitButton = new JButton();
		this.$$$loadButtonText$$$(submitButton, this.$$$getMessageFromBundle$$$("ia", "submit"));
		panel.add(submitButton, cc.xy(5, 19));
		cancelButton = new JButton();
		this.$$$loadButtonText$$$(cancelButton, this.$$$getMessageFromBundle$$$("ia", "cancel"));
		panel.add(cancelButton, cc.xy(5, 23));
		sizeSpinner = new JSpinner();
		panel.add(sizeSpinner, cc.xy(5, 17, CellConstraints.FILL, CellConstraints.DEFAULT));
		speciesMatchLabel = new JLabel();
		speciesMatchLabel.setText("");
		panel.add(speciesMatchLabel, cc.xy(5, 7));
		registerButton = new JButton();
		this.$$$loadButtonText$$$(registerButton, this.$$$getMessageFromBundle$$$("ia", "register.moth"));
		panel.add(registerButton, cc.xy(5, 21));
		datePanel = new JPanel();
		datePanel.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow", "center:d:grow"));
		panel.add(datePanel, cc.xy(5, 15));
		daySpinner = new JSpinner();
		daySpinner.setToolTipText(this.$$$getMessageFromBundle$$$("ia", "day"));
		daySpinner.putClientProperty("html.disable", Boolean.FALSE);
		datePanel.add(daySpinner, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
		monthSpinner = new JSpinner();
		monthSpinner.setRequestFocusEnabled(false);
		monthSpinner.setToolTipText("Month");
		datePanel.add(monthSpinner, cc.xy(5, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
		monthLabel = new JLabel();
		this.$$$loadLabelText$$$(monthLabel, this.$$$getMessageFromBundle$$$("ia", "month-colon"));
		datePanel.add(monthLabel, cc.xy(3, 1));
		dayLabel = new JLabel();
		this.$$$loadLabelText$$$(dayLabel, this.$$$getMessageFromBundle$$$("ia", "day-colon"));
		panel.add(dayLabel, cc.xy(3, 15));
		coordsCheckBox = new JCheckBox();
		coordsCheckBox.setEnabled(true);
		coordsCheckBox.setSelected(false);
		this.$$$loadButtonText$$$(coordsCheckBox, this.$$$getMessageFromBundle$$$("ia", "use.coordinates"));
		panel.add(coordsCheckBox, cc.xy(5, 13));
		locationPanel = new JPanel();
		locationPanel.setLayout(new FormLayout("fill:d:grow", "center:d:grow,top:3dlu:noGrow,center:max(d;4px):noGrow"));
		panel.add(locationPanel, cc.xy(5, 9));
		standardLocationPanel = new JPanel();
		standardLocationPanel.setLayout(new FormLayout("fill:d:grow", "center:d:noGrow"));
		locationPanel.add(standardLocationPanel, cc.xy(1, 3));
		locationField = new JTextField();
		standardLocationPanel.add(locationField, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
		coordinateLocationPanel = new JPanel();
		coordinateLocationPanel.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow", "center:d:noGrow"));
		locationPanel.add(coordinateLocationPanel, cc.xy(1, 1));
		latitudeField = new JTextField();
		latitudeField.setToolTipText(this.$$$getMessageFromBundle$$$("ia", "latitude"));
		coordinateLocationPanel.add(latitudeField, cc.xy(3, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
		longitudeField = new JTextField();
		longitudeField.setToolTipText(this.$$$getMessageFromBundle$$$("ia", "longitude"));
		coordinateLocationPanel.add(longitudeField, cc.xy(7, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
		latitudeLabel = new JLabel();
		this.$$$loadLabelText$$$(latitudeLabel, this.$$$getMessageFromBundle$$$("ia", "latitude"));
		coordinateLocationPanel.add(latitudeLabel, cc.xy(1, 1));
		longitudeLabel = new JLabel();
		this.$$$loadLabelText$$$(longitudeLabel, this.$$$getMessageFromBundle$$$("ia", "longitude"));
		coordinateLocationPanel.add(longitudeLabel, cc.xy(5, 1));
		locationValidLabel = new JLabel();
		locationValidLabel.setText("");
		panel.add(locationValidLabel, cc.xy(5, 11));
		sizeLabel.setLabelFor(sizeSpinner);
		titleLabel.setLabelFor(speciesField);
		latitudeLabel.setLabelFor(latitudeField);
		longitudeLabel.setLabelFor(longitudeField);
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

}
