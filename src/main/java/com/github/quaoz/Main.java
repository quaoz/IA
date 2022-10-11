package com.github.quaoz;

import com.github.quaoz.gui.GUI;
import java.nio.file.Path;
import net.harawata.appdirs.AppDirsFactory;
import org.tinylog.Logger;
import org.tinylog.provider.ProviderRegistry;

public class Main {

	private static final Path installDir = Path.of(
		AppDirsFactory
			.getInstance()
			.getUserDataDir("Moth-db", "1.0.0", "quaoz", false)
	);
	private static GUI gui;
	private static UserManager userManager;

	public static UserManager getUserManager() {
		return userManager;
	}

	public static GUI getGui() {
		return gui;
	}

	public static Path getInstallDir() {
		return installDir;
	}

	public static void main(String[] args) {
		UserManager.getInstance();
		gui = new GUI();
		Logger.info("Installation dir: " + installDir.toAbsolutePath());

		Runtime
			.getRuntime()
			.addShutdownHook(
				new Thread(() -> {
					MothManager.close();
					RecordManager.close();
					UserManager.getInstance().close();

					try {
						ProviderRegistry.getLoggingProvider().shutdown();
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				})
			);
	}
}
