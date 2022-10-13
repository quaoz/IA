package com.github.quaoz;

import com.github.quaoz.gui.GUI;
import com.github.quaoz.managers.MothManager;
import com.github.quaoz.managers.RecordManager;
import com.github.quaoz.managers.UserManager;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.harawata.appdirs.AppDirsFactory;
import org.tinylog.Logger;
import org.tinylog.provider.ProviderRegistry;

public class Main {

	private static Main main;

	private final Path installDir;

	private Main() {
		installDir =
			Path.of(
				AppDirsFactory
					.getInstance()
					.getUserDataDir("Moth-db", "1.0.0", "quaoz", false)
			);
	}

	public static synchronized Main getInstance() {
		return main;
	}

	public static void main(String[] args) {
		main = new Main();

		// Initialise the managers async
		List<CompletableFuture<Void>> futures = Arrays.asList(
				CompletableFuture.runAsync(UserManager::init),
				CompletableFuture.runAsync(RecordManager::init),
				CompletableFuture.runAsync(MothManager::init)
		);

		futures.forEach(CompletableFuture::join);
		GUI.init();

		Logger.info(
			"Installation dir: " + Main.getInstance().getInstallDir().toAbsolutePath()
		);

		Runtime
			.getRuntime()
			.addShutdownHook(
				new Thread(() -> {
					MothManager.getInstance().close();
					RecordManager.getInstance().close();
					UserManager.getInstance().close();
					Logger.info("Shutdown");

					try {
						ProviderRegistry.getLoggingProvider().shutdown();
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				})
			);
	}

	public Path getInstallDir() {
		return installDir;
	}
}
