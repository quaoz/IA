package com.github.quaoz;

import com.github.quaoz.database.DataBase;
import com.github.quaoz.database.DataBaseConfig;
import com.github.quaoz.structures.Pair;
import com.github.quaoz.util.CustomRatio;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MothManager {
  private static final File MOTHS_DB_FILE =
      new File("src/main/java/com/github/quaoz/tests/db/moths.db");
  private static final File MOTHS_CONF_FILE =
      new File("src/main/java/com/github/quaoz/tests/db/moths.json");
  // name 64, sci name 64, size 16, flight 32, habitat 64, food 128
  private static final DataBaseConfig mothsConfig =
      new DataBaseConfig().init(369, new Integer[] {64, 128, 144, 176, 240, 368});
  private static final DataBase mothsDatabase =
      new DataBase(MOTHS_DB_FILE.toPath(), MOTHS_CONF_FILE.toPath(), mothsConfig);

  public static void addMoth(
      String name,
      String sciName,
      double sizeLower,
      double sizeUpper,
      int flightStart,
      int flightEnd,
      String habitat,
      String food) {
    String record =
        String.format(
            "%-64s%-64s%-16s%-32s%-64s%-128s\n",
            name,
            sciName,
            String.format("%s:%s", sizeLower, sizeUpper),
            String.format("%s:%s", flightStart, flightEnd),
            habitat,
            food);
    mothsDatabase.add(record);
  }

  public static Moth basicSearch(String name) {
    name = name.strip();
    String record = mothsDatabase.get(name);

    String sciName = record.substring(mothsConfig.fields[0], mothsConfig.fields[1]).strip();
    String size = record.substring(mothsConfig.fields[1], mothsConfig.fields[2]).strip();
    String flight = record.substring(mothsConfig.fields[2], mothsConfig.fields[3]).strip();
    String habitat = record.substring(mothsConfig.fields[3], mothsConfig.fields[4]).strip();
    String food = record.substring(mothsConfig.fields[4], mothsConfig.fields[5]).strip();

    return new Moth(
        name,
        sciName,
        Double.parseDouble(size.split(":")[0]),
        Double.parseDouble(size.split(":")[1]),
        Integer.parseInt(flight.split(":")[0]),
        Integer.parseInt(flight.split(":")[1]),
        habitat,
        food);
  }

  public static @NotNull ArrayList<Pair<Moth, Double>> collectMoths(
      @NotNull String field, int compField) {
    return collectMoths(field, compField, null);
  }

  public static @NotNull ArrayList<Pair<Moth, Double>> collectMoths(
      @NotNull String field, int compField, CustomRatio customRatio) {
    return collectMoths(field, compField, 10, customRatio);
  }

  public static @NotNull ArrayList<Pair<Moth, Double>> collectMoths(
      @NotNull String field, int compField, int count) {
    return collectMoths(field, compField, count, null);
  }

  public static @NotNull ArrayList<Pair<Moth, Double>> collectMoths(
      @NotNull String field, int compField, int count, CustomRatio customRatio) {
    field = field.strip();

    ArrayList<Pair<String, Double>> rawMoths = new ArrayList<>();
    mothsDatabase.collect(field, compField).forEach((s) -> rawMoths.add(new Pair<>(s, 100.0)));

    if (rawMoths.size() <= 1) {
      rawMoths.addAll(mothsDatabase.collect(field, compField, count, customRatio));
    }

    ArrayList<Pair<Moth, Double>> moths = new ArrayList<>();

    for (Pair<String, Double> pair : rawMoths) {
      String s = pair.getKey();
      if (s != null) {
        String size = s.substring(mothsConfig.fields[1], mothsConfig.fields[2]).strip();
        String flight = s.substring(mothsConfig.fields[2], mothsConfig.fields[3]).strip();

        moths.add(
            new Pair<>(
                new Moth(
                    s.substring(0, mothsConfig.fields[0]).strip(),
                    s.substring(mothsConfig.fields[0], mothsConfig.fields[1]).strip(),
                    Double.parseDouble(size.split(":")[0]),
                    Double.parseDouble(size.split(":")[1]),
                    Integer.parseInt(flight.split(":")[0]),
                    Integer.parseInt(flight.split(":")[1]),
                    s.substring(mothsConfig.fields[3], mothsConfig.fields[4]).strip(),
                    s.substring(mothsConfig.fields[4], mothsConfig.fields[5]).strip()),
                pair.getValue()));
      }
    }

    return moths;
  }

  public static void close() {
    try {
      mothsDatabase.close();
    } catch (IOException e) {
      Logger.error(e, "Unable to close database");
      throw new RuntimeException(e);
    }
  }
}
