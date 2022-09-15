package com.github.quaoz.gui;

import com.github.quaoz.Main;
import com.github.quaoz.UserManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.ResourceBundle;
import javax.swing.*;

public class SignInForm {
private JPanel panel;
private JTextField usernameField;
private JPasswordField passwordField;
private JButton signInButton;
private JLabel usernameLabel;
private JLabel passwordLabel;
private JButton cancelButton;

public SignInForm() {
	signInButton.addActionListener(
		e -> {
		// TODO: Sign in
		if (UserManager.validateUser(
			usernameField.getText().strip(), passwordField.getPassword())) {
			Main.setUser(usernameField.getText().strip());
			Main.getGui().render(GUI.Content.HOME_LOGGED_IN);
		}
		});

	cancelButton.addActionListener(
		e -> {
		Main.getGui().render(GUI.Content.PAST_CONTENT);
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
* Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR
* call it in your code!
*
* @noinspection ALL
*/
private void $$$setupUI$$$() {
	panel = new JPanel();
	panel.setLayout(
		new FormLayout(
			"fill:d:grow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:d:grow",
			"center:d:grow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:grow"));
	panel.setMinimumSize(new Dimension(768, 768));
	panel.setPreferredSize(new Dimension(768, 768));
	usernameField = new JTextField();
	CellConstraints cc = new CellConstraints();
	panel.add(usernameField, cc.xy(3, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
	final Spacer spacer1 = new Spacer();
	panel.add(spacer1, cc.xy(3, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
	final Spacer spacer2 = new Spacer();
	panel.add(spacer2, cc.xy(3, 15, CellConstraints.DEFAULT, CellConstraints.FILL));
	final Spacer spacer3 = new Spacer();
	panel.add(spacer3, cc.xy(5, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
	final Spacer spacer4 = new Spacer();
	panel.add(spacer4, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
	usernameLabel = new JLabel();
	this.$$$loadLabelText$$$(usernameLabel, this.$$$getMessageFromBundle$$$("ia", "username"));
	panel.add(usernameLabel, cc.xy(3, 3));
	passwordLabel = new JLabel();
	this.$$$loadLabelText$$$(passwordLabel, this.$$$getMessageFromBundle$$$("ia", "password"));
	panel.add(passwordLabel, cc.xy(3, 7));
	passwordField = new JPasswordField();
	panel.add(passwordField, cc.xy(3, 9, CellConstraints.FILL, CellConstraints.DEFAULT));
	signInButton = new JButton();
	this.$$$loadButtonText$$$(signInButton, this.$$$getMessageFromBundle$$$("ia", "sign.in"));
	panel.add(signInButton, cc.xy(3, 11));
	cancelButton = new JButton();
	this.$$$loadButtonText$$$(cancelButton, this.$$$getMessageFromBundle$$$("ia", "cancel"));
	panel.add(cancelButton, cc.xy(3, 13));
	usernameLabel.setLabelFor(usernameField);
	passwordLabel.setLabelFor(passwordField);
}

private static Method $$$cachedGetBundleMethod$$$ = null;

private String $$$getMessageFromBundle$$$(String path, String key) {
	ResourceBundle bundle;
	try {
	Class<?> thisClass = this.getClass();
	if ($$$cachedGetBundleMethod$$$ == null) {
		Class<?> dynamicBundleClass =
			thisClass.getClassLoader().loadClass("com.intellij.DynamicBundle");
		$$$cachedGetBundleMethod$$$ =
			dynamicBundleClass.getMethod("getBundle", String.class, Class.class);
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
