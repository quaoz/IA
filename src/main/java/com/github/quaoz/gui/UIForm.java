package com.github.quaoz.gui;

import java.lang.reflect.Method;
import java.util.ResourceBundle;
import javax.swing.*;
import org.jetbrains.annotations.NotNull;

public class UIForm {

	private static Method $$$cachedGetBundleMethod$$$ = null;

	String getMessageFromBundle(String path, String key) {
		ResourceBundle bundle;
		try {
			Class<?> thisClass = this.getClass();
			if ($$$cachedGetBundleMethod$$$ == null) {
				Class<?> dynamicBundleClass = thisClass
					.getClassLoader()
					.loadClass("com.intellij.DynamicBundle");
				$$$cachedGetBundleMethod$$$ =
					dynamicBundleClass.getMethod("getBundle", String.class, Class.class);
			}
			bundle =
				(ResourceBundle) $$$cachedGetBundleMethod$$$.invoke(
					null,
					path,
					thisClass
				);
		} catch (Exception e) {
			bundle = ResourceBundle.getBundle(path);
		}
		return bundle.getString(key);
	}

	/**
	 * @noinspection ALL
	 */
	void loadButtonText(AbstractButton component, @NotNull String text) {
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
}
