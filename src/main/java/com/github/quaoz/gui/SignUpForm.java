package com.github.quaoz.gui;

import com.github.quaoz.UserManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;

public class SignUpForm {
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

	{
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
		$$$setupUI$$$();
	}

	public SignUpForm(GUI gui) {
		registeredButton.addActionListener(e -> {
			//TODO: register
			if (!UserManager.userExists(usernameField.getText().strip())) {
				UserManager.addUser(usernameField.getText().strip(), emailField.getText().strip(), passwordField.getPassword());
				gui.render(GUI.Content.HOME_LOGGED_IN);
			}
		});

		cancelButton.addActionListener(e -> {
			gui.render(GUI.Content.PAST_CONTENT);
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
		panel.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:d:grow", "center:d:grow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:d:grow"));
		usernameField = new JTextField();
		CellConstraints cc = new CellConstraints();
		panel.add(usernameField, cc.xy(3, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
		final Spacer spacer1 = new Spacer();
		panel.add(spacer1, cc.xy(5, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
		final Spacer spacer2 = new Spacer();
		panel.add(spacer2, cc.xy(1, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
		final Spacer spacer3 = new Spacer();
		panel.add(spacer3, cc.xy(3, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
		final Spacer spacer4 = new Spacer();
		panel.add(spacer4, cc.xy(3, 23, CellConstraints.DEFAULT, CellConstraints.FILL));
		usernameLabel = new JLabel();
		usernameLabel.setText("Username");
		panel.add(usernameLabel, cc.xy(3, 3));
		emailLabel = new JLabel();
		emailLabel.setText("Email");
		panel.add(emailLabel, cc.xy(3, 7));
		emailField = new JTextField();
		panel.add(emailField, cc.xy(3, 9, CellConstraints.FILL, CellConstraints.DEFAULT));
		passwordLabel = new JLabel();
		passwordLabel.setText("Password");
		panel.add(passwordLabel, cc.xy(3, 11));
		repeatPassword = new JLabel();
		repeatPassword.setText("Repeat Password");
		panel.add(repeatPassword, cc.xy(3, 15));
		passwordField = new JPasswordField();
		panel.add(passwordField, cc.xy(3, 13, CellConstraints.FILL, CellConstraints.DEFAULT));
		repeatPasswordField = new JPasswordField();
		panel.add(repeatPasswordField, cc.xy(3, 17, CellConstraints.FILL, CellConstraints.DEFAULT));
		registeredButton = new JButton();
		registeredButton.setText("Register");
		panel.add(registeredButton, cc.xy(3, 19));
		cancelButton = new JButton();
		cancelButton.setText("Cancel");
		panel.add(cancelButton, cc.xy(3, 21));
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return panel;
	}

}
