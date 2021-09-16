package com.ewus.ba.energielenkerEneffcoService;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {

  // static Logger log = Logger.getLogger(Utils.class.getName());
  public static final Logger LOGGER = Logger.getLogger(Utils.class.getName());

  public static void writeToFile(String s, String logFile, boolean printSuccess) {
    if (logFile == null || logFile.equals("")) {
      logFile = "app.log";
    }
    try {
      FileWriter myWriter = new FileWriter(logFile, true);
      myWriter.write(s);
      myWriter.close();
      if (printSuccess) System.out.println("Successfully wrote to the file: " + logFile);
    } catch (IOException e) {
      System.err.println("An error occurred.");
      Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
    }
  }

  public static void updateLoggerConfig(String filePath) {
    // LOGGER.log(Level.FINE, "processing {0} entries in loop", list.size());

    Handler consoleHandler = null;
    Handler fileHandler = null;
    try {
      // Creating consoleHandler and fileHandler
      consoleHandler = new ConsoleHandler();
      fileHandler = new FileHandler("./logs/" + getCurrentDayTimeNoDots() + ".log");

      // Assigning handlers to LOGGER object
      LOGGER.addHandler(consoleHandler);
      LOGGER.addHandler(fileHandler);

      // Setting levels to handlers and LOGGER
      consoleHandler.setLevel(Level.WARNING);
      fileHandler.setLevel(Level.ALL);
      LOGGER.setLevel(Level.ALL);

      LOGGER.config("Configuration done.");

      // Console handler removed
      // LOGGER.removeHandler(consoleHandler);

    } catch (IOException exception) {
      LOGGER.log(Level.SEVERE, "Error occur in FileHandler.", exception);
    }

    LOGGER.finer("Finest example on LOGGER handler completed.");
  }

  public static String getCurrentDayTimeNoDots() {
    String formattedDate = "";
    SimpleDateFormat sdf =
        new SimpleDateFormat("yyyy_MM_dd_'T'_HH_mm_ss", Locale.US); /* w ww . j av a 2s. co m */
    formattedDate = sdf.format(new Date());
    return formattedDate;
  }

  public static String getCurrentDayTime() {
    String formattedDate = "";
    SimpleDateFormat sdf =
        new SimpleDateFormat("yyyy:MM:dd:'T'HH:mm:ss", Locale.US); /* w ww . j av a 2s. co m */
    formattedDate = sdf.format(new Date());
    return formattedDate;
  }

  public static long daysToSeconds(int days) {
    return days * 24 * 60 * 60;
  }
}
