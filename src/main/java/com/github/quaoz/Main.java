package com.github.quaoz;

import com.github.quaoz.gui.GUI;
import org.tinylog.provider.ProviderRegistry;

public class Main {
	public static void main(String[] args) {
		GUI gui = new GUI().init();

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
