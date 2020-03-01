package Server;

import java.io.IOException;
import java.util.logging.*;

public class FMSLogger {
  public static Level level = Level.parse("FINEST");
  public static Logger logger;

  static {
    try {
      initLog();
    }
    catch (IOException e) {
      System.out.println("Could not initialize log: " + e.getMessage());
      e.printStackTrace();
    }
  }


  private static void initLog() throws IOException {
    Level logLevel = level;
    // making a log
    logger = Logger.getLogger("FMSlog");
    logger.setLevel(logLevel);
    logger.setUseParentHandlers(false);
    // making handlers to use.
    Handler consoleHandler = new ConsoleHandler();
    consoleHandler.setLevel(logLevel);
    consoleHandler.setFormatter(new SimpleFormatter());
    logger.addHandler(consoleHandler); // set console handler
    java.util.logging.FileHandler fileHandler = new java.util.logging.FileHandler("FMSlog.txt", false);
    fileHandler.setLevel(logLevel);
    fileHandler.setFormatter(new SimpleFormatter());
    logger.addHandler(fileHandler); // set file handler
  }
}
