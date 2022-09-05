package com.github.quaoz.gui;

import com.github.quaoz.Main;
import com.github.quaoz.MothManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

public class HomeLoggedOut {
	private JTextField searchField;
	private JPanel panel;
	private JButton advancedSearchButton;
	private JButton signInButton;
	private JButton signUpButton;

	public HomeLoggedOut() {
		advancedSearchButton.addActionListener(e -> Main.getGui().render(GUI.Content.ADVANCED_SEARCH));
		signInButton.addActionListener(e -> Main.getGui().render(GUI.Content.SIGN_IN));
		signUpButton.addActionListener(e -> Main.getGui().render(GUI.Content.SIGN_UP));

		searchField.addActionListener(e -> {
			Main.getGui().setSearchResults(MothManager.collectMoths(searchField.getText().strip(), 0, 100));
			Main.getGui().render(GUI.Content.SEARCH_RESULTS);
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
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		panel = new JPanel();
		panel.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow", "center:d:noGrow"));
		panel.setMinimumSize(new Dimension(600, 300));
		panel.setPreferredSize(new Dimension(550, 300));
		searchField = new JTextField();
		searchField.setMinimumSize(new Dimension(200, 30));
		searchField.setPreferredSize(new Dimension(200, 30));
		searchField.setText("Search");
		CellConstraints cc = new CellConstraints();
		panel.add(searchField, cc.xy(3, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
		advancedSearchButton = new JButton();
		this.$$$loadButtonText$$$(advancedSearchButton, this.$$$getMessageFromBundle$$$("ia", "advanced.search"));
		panel.add(advancedSearchButton, cc.xy(5, 1));
		signInButton = new JButton();
		this.$$$loadButtonText$$$(signInButton, this.$$$getMessageFromBundle$$$("ia", "sign.in"));
		panel.add(signInButton, cc.xy(7, 1));
		signUpButton = new JButton();
		this.$$$loadButtonText$$$(signUpButton, this.$$$getMessageFromBundle$$$("ia", "sign.up"));
		panel.add(signUpButton, cc.xy(9, 1));
		final Spacer spacer1 = new Spacer();
		panel.add(spacer1, cc.xy(11, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
		final Spacer spacer2 = new Spacer();
		panel.add(spacer2, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
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
