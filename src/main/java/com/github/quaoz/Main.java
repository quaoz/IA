package com.github.quaoz;

import com.github.quaoz.gui.GUI;
import com.github.quaoz.gui.SignUpForm;
import org.tinylog.provider.ProviderRegistry;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		Thread passwordListInit = new Thread(SignUpForm::init);
		passwordListInit.start();

		GUI gui = new GUI().init();

		passwordListInit.join();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			MothManager.close();
			RecordManager.close();
			UserManager.close();

			try {
				ProviderRegistry.getLoggingProvider().shutdown();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}));
	}
}
