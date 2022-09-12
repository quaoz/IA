package com.github.quaoz;

import com.github.quaoz.gui.GUI;
import org.tinylog.provider.ProviderRegistry;

public class Main {
  private static final GUI gui = new GUI();
  private static String user = "";

  public static String getUser() {
    return user;
  }

  public static void setUser(String user) {
    Main.user = user;
  }

  public static GUI getGui() {
    return gui;
  }

  public static void main(String[] args) {
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
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
