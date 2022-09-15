package com.github.quaoz.gui;

import com.github.quaoz.Main;
import com.github.quaoz.UserManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javax.swing.*;

public class SignUpForm {
private final Pattern alphanumericChecker = Pattern.compile("[^a-zA-Z0-9-]");
// https://stackoverflow.com/questions/201323/how-can-i-validate-an-email-address-using-a-regular-expression
private final Pattern emailPattern =
	Pattern.compile(
		"(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
private JPanel panel;
private JTextField usernameField;
private JTextField emailField;
private JPasswordField passwordField;
private JPasswordField repeatPasswordField;
private JButton registeredButton;
private JButton cancelButton;
private JLabel usernameLabel;
private JLabel emailLabel;
private JLabel passwordLabel;
private JLabel repeatPassword;
private JLabel usernameValidLabel;
private JLabel emailValidLabel;
private JLabel passwordValidLabel;
private JLabel repeatValidLabel;
private JLabel repeatMessage;
private JLabel usernameMessage;
private JLabel emailMessage;
private JLabel passwordMessage;

public SignUpForm() {
	registeredButton.addActionListener(
		e -> {
		// TODO: register
		if (checkUsername() && checkEmail() && checkPassword() && checkPasswordRepeat()) {
			UserManager.addUser(
				usernameField.getText().strip(),
				emailField.getText().strip(),
				passwordField.getPassword());
			Main.setUser(usernameField.getText().strip());
			Main.getGui().render(GUI.Content.HOME_LOGGED_IN);
		}
		});

	cancelButton.addActionListener(e -> Main.getGui().render(GUI.Content.PAST_CONTENT));

	usernameField.addFocusListener(
		new FocusAdapter() {
		@Override
		public void focusGained(FocusEvent e) {
			super.focusGained(e);

			if (!e.isTemporary()) {
			usernameValidLabel.setText(null);
			usernameMessage.setText(null);
			}
		}

		@Override
		public void focusLost(FocusEvent e) {
			super.focusLost(e);
			checkUsername();
		}
		});
	emailField.addFocusListener(
		new FocusAdapter() {
		@Override
		public void focusGained(FocusEvent e) {
			super.focusGained(e);

			if (!e.isTemporary()) {
			emailValidLabel.setText(null);
			emailMessage.setText(null);
			}
		}

		@Override
		public void focusLost(FocusEvent e) {
			super.focusLost(e);
			checkEmail();
		}
		});
	passwordField.addFocusListener(
		new FocusAdapter() {
		@Override
		public void focusGained(FocusEvent e) {
			super.focusGained(e);

			if (!e.isTemporary()) {
			passwordValidLabel.setText(null);
			passwordMessage.setText(null);
			}
		}

		@Override
		public void focusLost(FocusEvent e) {
			super.focusLost(e);
			checkPassword();
		}
		});
	repeatPasswordField.addFocusListener(
		new FocusAdapter() {
		@Override
		public void focusGained(FocusEvent e) {
			super.focusGained(e);

			if (!e.isTemporary()) {
			repeatValidLabel.setText(null);
			repeatMessage.setText(null);
			}
		}

		@Override
		public void focusLost(FocusEvent e) {
			super.focusLost(e);
			checkPasswordRepeat();
		}
		});

	usernameField.addActionListener(e -> checkUsername());
	emailField.addActionListener(e -> checkEmail());
	passwordField.addActionListener(e -> checkPassword());
	repeatPasswordField.addActionListener(e -> checkPasswordRepeat());
}

private boolean checkUsername() {
	String username = usernameField.getText().strip();

	if (username.length() != 0) {
	if (alphanumericChecker.matcher(username).find()) {
		usernameValidLabel.setText("✗");
		usernameMessage.setText("Username can only contain alphanumeric characters and dash");
		return false;
	} else if (username.length() > 64) {
		usernameValidLabel.setText("✗");
		usernameMessage.setText("Username cannot be over 64 characters");
		return false;
	} else if (UserManager.userExists(username)) {
		usernameValidLabel.setText("✗");
		usernameMessage.setText("Username taken");
		return false;
	} else {
		usernameValidLabel.setText("✓");
		usernameMessage.setText(null);
		return true;
	}
	}

	return false;
}

private boolean checkEmail() {
	String email = emailField.getText().strip();

	if (email.length() != 0) {
	if (!emailPattern.matcher(email).find()) {
		emailValidLabel.setText("✗");
		emailMessage.setText("Invalid email address");
		return false;
	} else {
		emailValidLabel.setText("✓");
		emailMessage.setText(null);
		return true;
	}
	}

	return false;
}

private boolean checkPassword() {
	boolean valid = false;

	char[] password = passwordField.getPassword();

	if (password.length != 0) {
	if (password.length < 8) {
		passwordValidLabel.setText("✗");
		passwordMessage.setText("Password must be 8 or more characters");
	} else if (UserManager.isCommonPassword(password)) {
		passwordValidLabel.setText("✗");
		passwordMessage.setText("Common password");
	} else {
		passwordValidLabel.setText("✓");
		passwordMessage.setText(null);
		valid = true;
	}
	}

	// Wipe confidential data
	Arrays.fill(password, (char) 0);

	return valid;
}

private boolean checkPasswordRepeat() {
	boolean valid = false;

	char[] password = passwordField.getPassword();
	char[] repeatPassword = repeatPasswordField.getPassword();

	if (repeatPassword.length != 0) {
	if (!checkPassword()) {
		repeatValidLabel.setText(null);
	} else if (!Arrays.equals(password, repeatPassword)) {
		repeatValidLabel.setText("✗");
		repeatMessage.setText("Passwords do not match");
	} else {
		repeatValidLabel.setText("✓");
		repeatMessage.setText(null);
		valid = true;
	}
	}

	// Wipe confidential data
	Arrays.fill(password, (char) 0);
	Arrays.fill(repeatPassword, (char) 0);

	return valid;
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
			"fill:d:grow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow",
			"center:d:grow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:d:grow"));
	panel.setMinimumSize(new Dimension(768, 768));
	panel.setPreferredSize(new Dimension(768, 768));
	usernameField = new JTextField();
	CellConstraints cc = new CellConstraints();
	panel.add(usernameField, cc.xy(3, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
	final Spacer spacer1 = new Spacer();
	panel.add(spacer1, cc.xy(7, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
	final Spacer spacer2 = new Spacer();
	panel.add(spacer2, cc.xy(1, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
	final Spacer spacer3 = new Spacer();
	panel.add(spacer3, cc.xy(3, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
	final Spacer spacer4 = new Spacer();
	panel.add(spacer4, cc.xy(3, 31, CellConstraints.DEFAULT, CellConstraints.FILL));
	usernameLabel = new JLabel();
	this.$$$loadLabelText$$$(usernameLabel, this.$$$getMessageFromBundle$$$("ia", "username"));
	panel.add(usernameLabel, cc.xy(3, 3));
	emailLabel = new JLabel();
	this.$$$loadLabelText$$$(emailLabel, this.$$$getMessageFromBundle$$$("ia", "email"));
	panel.add(emailLabel, cc.xy(3, 9));
	emailField = new JTextField();
	panel.add(emailField, cc.xy(3, 11, CellConstraints.FILL, CellConstraints.DEFAULT));
	passwordLabel = new JLabel();
	this.$$$loadLabelText$$$(passwordLabel, this.$$$getMessageFromBundle$$$("ia", "password"));
	panel.add(passwordLabel, cc.xy(3, 15));
	repeatPassword = new JLabel();
	this.$$$loadLabelText$$$(
		repeatPassword, this.$$$getMessageFromBundle$$$("ia", "repeat.password"));
	panel.add(repeatPassword, cc.xy(3, 21));
	passwordField = new JPasswordField();
	panel.add(passwordField, cc.xy(3, 17, CellConstraints.FILL, CellConstraints.DEFAULT));
	repeatPasswordField = new JPasswordField();
	panel.add(repeatPasswordField, cc.xy(3, 23, CellConstraints.FILL, CellConstraints.DEFAULT));
	registeredButton = new JButton();
	this.$$$loadButtonText$$$(registeredButton, this.$$$getMessageFromBundle$$$("ia", "register"));
	panel.add(registeredButton, cc.xy(3, 27));
	cancelButton = new JButton();
	this.$$$loadButtonText$$$(cancelButton, this.$$$getMessageFromBundle$$$("ia", "cancel"));
	panel.add(cancelButton, cc.xy(3, 29));
	usernameValidLabel = new JLabel();
	usernameValidLabel.setText("");
	panel.add(usernameValidLabel, cc.xy(5, 5));
	emailValidLabel = new JLabel();
	emailValidLabel.setText("");
	panel.add(emailValidLabel, cc.xy(5, 11));
	passwordValidLabel = new JLabel();
	passwordValidLabel.setText("");
	panel.add(passwordValidLabel, cc.xy(5, 17));
	repeatValidLabel = new JLabel();
	repeatValidLabel.setText("");
	panel.add(repeatValidLabel, cc.xy(5, 23));
	repeatMessage = new JLabel();
	repeatMessage.setText("");
	panel.add(repeatMessage, cc.xy(3, 25));
	usernameMessage = new JLabel();
	usernameMessage.setText("");
	panel.add(usernameMessage, cc.xy(3, 7));
	emailMessage = new JLabel();
	emailMessage.setText("");
	panel.add(emailMessage, cc.xy(3, 13));
	passwordMessage = new JLabel();
	passwordMessage.setText("");
	panel.add(passwordMessage, cc.xy(3, 19));
	usernameLabel.setLabelFor(usernameField);
	emailLabel.setLabelFor(emailField);
	passwordLabel.setLabelFor(passwordField);
	repeatPassword.setLabelFor(repeatPasswordField);
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
