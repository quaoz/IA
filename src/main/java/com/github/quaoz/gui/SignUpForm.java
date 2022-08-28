package com.github.quaoz.gui;

import com.github.quaoz.UserManager;
import com.github.quaoz.structures.BinarySearchTree;
import com.github.quaoz.util.DualPivotIntroSort;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class SignUpForm {
	private static final BinarySearchTree<String> commonPasswords = new BinarySearchTree<>();
	private static final String COMMON_PASSWORD_URL = "https://raw.githubusercontent.com/danielmiessler/SecLists/master/Passwords/Common-Credentials/10-million-password-list-top-10000.txt";
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
	private JLabel validLabel;

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

		usernameField.addFocusListener(new FocusAdapter() {
			private final Pattern alphanumericChecker = Pattern.compile("[^a-zA-Z0-9 -]");

			@Override
			public void focusGained(FocusEvent e) {
				super.focusGained(e);

				if (!e.isTemporary()) {
					usernameValidLabel.setText("?");
					validLabel.setText(null);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				super.focusLost(e);

				String username = usernameField.getText().strip();

				if (username.length() != 0) {
					if (alphanumericChecker.matcher(username).find()) {
						usernameValidLabel.setText("✗");
						validLabel.setText("Username can only contain alphanumeric characters and dash");
					} else if (username.length() > 64) {
						usernameValidLabel.setText("✗");
						validLabel.setText("Username cannot be over 64 characters");
					} else if (UserManager.userExists(username)) {
						usernameValidLabel.setText("✗");
						validLabel.setText("Username taken");
					} else {
						usernameValidLabel.setText("✓");
					}
				}
			}
		});
		emailField.addFocusListener(new FocusAdapter() {
			// https://stackoverflow.com/questions/201323/how-can-i-validate-an-email-address-using-a-regular-expression
			private final Pattern emailPattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");

			@Override
			public void focusGained(FocusEvent e) {
				super.focusGained(e);

				if (!e.isTemporary()) {
					emailValidLabel.setText("?");
					validLabel.setText(null);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				super.focusLost(e);

				String email = emailField.getText().strip();

				if (email.length() != 0) {
					if (!emailPattern.matcher(email).find()) {
						emailValidLabel.setText("✗");
						validLabel.setText("Invalid email address");
					} else {
						emailValidLabel.setText("✓");
					}
				}
			}
		});
		passwordField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				super.focusGained(e);

				if (!e.isTemporary()) {
					passwordValidLabel.setText("?");
					validLabel.setText(null);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				super.focusLost(e);

				char[] password = passwordField.getPassword();

				if (password.length < 8) {
					passwordValidLabel.setText("✗");
					validLabel.setText("Password must be 8 or more characters");
				} else if (commonPasswords.contains(String.valueOf(password))) {
					passwordValidLabel.setText("✗");
					validLabel.setText("Common password");
				} else {
					passwordValidLabel.setText("✓");
				}

				Arrays.fill(password, (char) 0);
			}
		});
	}

	public static void init() {
		File common_passwords = new File("src/main/resources/common_passwords.txt");

		if (!common_passwords.exists()) {
			try {
				generate(new URL(COMMON_PASSWORD_URL));
			} catch (MalformedURLException e) {
				Logger.error(e, "Broken password url");
				throw new RuntimeException(e);
			}
		}

		try (
				BufferedReader br = new BufferedReader(new FileReader(common_passwords))
		) {
			// Import the lines into a binary search tree
			Logger.info("Constructing common passwords tree...");
			long start = System.currentTimeMillis();

			for (String line = br.readLine(); line != null; line = br.readLine()) {
				commonPasswords.add(line);
			}

			commonPasswords.balance();
			Logger.info("Finished tree construction, took: " + (System.currentTimeMillis() - start) + "ms");
		} catch (IOException e) {
			Logger.error(e, "Unable to read common password file");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws MalformedURLException {
		generate(new URL(COMMON_PASSWORD_URL));
	}

	private static void generate(@NotNull URL url) {
		File destination = new File("src/main/resources/common_passwords.txt");
		File source;

		try {
			source = File.createTempFile("temp_password_list", ".txt");
		} catch (IOException e) {
			Logger.error(e, "Unable to create temporary file");
			throw new RuntimeException(e);
		}

		try (
				FileOutputStream fileOutputStream = new FileOutputStream(source);
				BufferedReader br = new BufferedReader(new FileReader(source));
				PrintWriter printWriter = new PrintWriter(new FileWriter(destination, true))
		) {
			// Download the password list
			Logger.info("Downloading common passwords...");
			long start = System.currentTimeMillis();

			ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
			fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
			Logger.info("Finished download, took: " + (System.currentTimeMillis() - start) + "ms");

			Logger.info("Sorting passwords...");
			start = System.currentTimeMillis();

			ArrayList<String> lines = new ArrayList<>();
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				if (line.length() >= 8) {
					lines.add(line);
				}
			}

			DualPivotIntroSort.sort(lines);
			Logger.info("Finished sorting, took: " + (System.currentTimeMillis() - start) + "ms");

			Logger.info("Writing passwords to file...");
			start = System.currentTimeMillis();
			lines.forEach(printWriter::println);
			Logger.info("Finished writing, took: " + (System.currentTimeMillis() - start) + "ms");
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		panel = new JPanel();
		panel.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow", "center:d:grow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:d:grow"));
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
		panel.add(spacer4, cc.xy(3, 25, CellConstraints.DEFAULT, CellConstraints.FILL));
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
		panel.add(registeredButton, cc.xy(3, 21));
		cancelButton = new JButton();
		cancelButton.setText("Cancel");
		panel.add(cancelButton, cc.xy(3, 23));
		usernameValidLabel = new JLabel();
		usernameValidLabel.setText("");
		panel.add(usernameValidLabel, cc.xy(5, 5));
		emailValidLabel = new JLabel();
		emailValidLabel.setText("");
		panel.add(emailValidLabel, cc.xy(5, 9));
		passwordValidLabel = new JLabel();
		passwordValidLabel.setText("");
		panel.add(passwordValidLabel, cc.xy(5, 13));
		repeatValidLabel = new JLabel();
		repeatValidLabel.setText("");
		panel.add(repeatValidLabel, cc.xy(5, 17));
		validLabel = new JLabel();
		validLabel.setText("");
		panel.add(validLabel, cc.xy(3, 19));
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return panel;
	}

}
