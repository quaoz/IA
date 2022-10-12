package com.github.quaoz;

import com.github.quaoz.gui.GUI;
import java.nio.file.Path;
import net.harawata.appdirs.AppDirsFactory;
import org.tinylog.Logger;
import org.tinylog.provider.ProviderRegistry;

public class Main {
	private static Main main;

  public static synchronized Main getInstance() {
    return main;
  }

  private Main() {
	  installDir = Path.of(AppDirsFactory.getInstance().getUserDataDir("Moth-db", "1.0.0", "quaoz", false));
  }

	private final Path installDir;

	public Path getInstallDir() {
		return installDir;
	}

	public static void main(String[] args) {
		main = new Main();

		UserManager.getInstance();
		RecordManager.getInstance();
		MothManager.getInstance();
		GUI.getInstance();

		Logger.info("Installation dir: " + main.installDir.toAbsolutePath());

		Runtime
			.getRuntime()
			.addShutdownHook(
				new Thread(() -> {
					MothManager.getInstance().close();
					RecordManager.getInstance().close();
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
