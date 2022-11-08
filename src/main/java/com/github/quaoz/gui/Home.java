package com.github.quaoz.gui;

import com.github.quaoz.managers.UserManager;
import java.awt.*;
import javax.swing.*;

public class Home {

	private JPanel panel;
	private LoggedInBar loggedInBar;
	private LoggedOutBar loggedOutBar;

	public JPanel resolve() {
		boolean loggedIn = UserManager.getInstance().getUser().length() > 1;

		loggedInBar.setVisible(loggedIn);
		loggedOutBar.setVisible(!loggedIn);

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
		createUIComponents();
		panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 0));
		panel.setMinimumSize(new Dimension(768, 768));
		panel.setPreferredSize(new Dimension(768, 768));
		panel.add(loggedOutBar, BorderLayout.NORTH);
		panel.add(loggedInBar, BorderLayout.CENTER);
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return panel;
	}

	private void createUIComponents() {
		// TODO: place custom component creation code here
		loggedInBar = new LoggedInBar();
		loggedOutBar = new LoggedOutBar();
	}
}