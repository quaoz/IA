package com.github.quaoz;

import com.github.quaoz.gui.GUI;
import org.tinylog.provider.ProviderRegistry;

public class Main {
	public static void main(String[] args) {
		GUI gui = new GUI().init();

		/*MothManager.addMoth("Moth", "Moth moth", "10:15", "6:9", "Bog", "Bog plant");
		MothManager.addMoth("Moth2", "Moth mothus", "9:11", "7:9", "Wood", "Grass");
		MothManager.addMoth("Moth3", "Moth mothu", "3:5", "8:10", "Hills", "Tree");
		MothManager.addMoth("Moth4", "Moth mothi", "12:16", "4:7", "City", "Flowers");
		MothManager.addMoth("Moth5", "Moth motho", "5:7", "3:8", "House", "Plant");*/

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
