package Server;

import java.io.*;
import java.net.*;
import java.util.logging.*;

import Handlers.*;
import com.sun.net.httpserver.*;

import static Server.FMSLogger.*;

/*
	This example demonstrates the basic structure of the Family Map Server
	(although it is for a fictitious "Ticket to Ride" game, not Family Map).
	The example is greatly simplfied to help you more easily understand the
	basic elements of the server.

	The Server class is the "main" class for the server (i.e., it contains the
		"main" method for the server program).
	When the server runs, all command-line arguments are passed in to Server.main.
	For this server, the only command-line argument is the port number on which
		the server should accept incoming client connections.
*/
public class Server {
  private static final int MAX_WAITING_CONNECTIONS = 12;
//  private static Level level = Level.parse("FINEST");
//  private static Logger logger;
//
//  static {
//    try {
//      initLog();
//    }
//    catch (IOException e) {
//      System.out.println("Could not initialize log: " + e.getMessage());
//      e.printStackTrace();
//    }
//  }
//
//
//  private static void initLog() throws IOException {
//    Level logLevel = level;
//    // making a log
//    logger = Logger.getLogger("FMSlog");
//    logger.setLevel(logLevel);
//    logger.setUseParentHandlers(false);
//    // making handlers to use.
//    Handler consoleHandler = new ConsoleHandler();
//    consoleHandler.setLevel(logLevel);
//    consoleHandler.setFormatter(new SimpleFormatter());
//    logger.addHandler(consoleHandler); // set console handler
//    java.util.logging.FileHandler fileHandler = new java.util.logging.FileHandler("FMSlog.txt", false);
//    fileHandler.setLevel(logLevel);
//    fileHandler.setFormatter(new SimpleFormatter());
//    logger.addHandler(fileHandler); // set file handler
//  }

  /**
   * Initializes and runs the server.
   * @param portNumber specifies the port number on which the server should accept incoming connections.
   */
  private void run(String portNumber) {

    // Since the server has no "user interface", it should display "log"
    // messages containing information about its internal activities.
    // This allows a system administrator (or you) to know what is happening
    // inside the server, which can be useful for diagnosing problems
    // that may occur.
    //System.out.println("Initializing HTTP Server");
    logger.info("Initializing HTTP Server");
    HttpServer server;
    try {
      // Create a new HttpServer object.
      // Rather than calling "new" directly, we instead create
      // the object by calling the HttpServer.create static factory method.
      // Just like "new", this method returns a reference to the new object.
      server = HttpServer.create(
              new InetSocketAddress(Integer.parseInt(portNumber)),
              MAX_WAITING_CONNECTIONS);
    }
    catch (IOException e) {
      logger.log(Level.SEVERE, e.getMessage(), e);
      return;
    }

    server.setExecutor(null);

    // Log message indicating that the server is creating and installing its HTTP handlers.
    // The HttpServer class listens for incoming HTTP requests.  When one
    // is received, it looks at the URL path inside the HTTP request, and
    // forwards the request to the handler for that URL path.
    // System.out.println("Creating contexts");
    logger.info("Creating contexts");

    /**
     * Create and install the HTTP handler for the URL paths
     * When the HttpServer receives an HTTP request containing path, it will forward the request to that handler.
     */
    server.createContext("/user/register", new RegisterHandler());
    server.createContext("/user/login");
    server.createContext("/clear", new ClearHandler());
    server.createContext("/fill");  // what kind of path I need to use for username and generation?
    server.createContext("/load", new LoadHandler());
    server.createContext("/person");
    server.createContext("/", new Handlers.FileHandler());

    // Log message indicating that the HttpServer is about the start accepting
    // incoming client connections.
//    System.out.println("Starting server");
    logger.info("Starting HTTP server");
    // Tells the HttpServer to start accepting incoming client connections.
    // This method call will return immediately, and the "main" method
    // for the program will also complete.
    // Even though the "main" method has completed, the program will continue
    // running because the HttpServer object we created is still running
    // in the background.
    server.start();

    // Log message indicating that the server has successfully started.
    System.out.println("Server started");
  }

  // "main" method for the server program
  // "args" should contain one command-line argument, which is the port number
  // on which the server should accept incoming client connections.
  public static void main(String[] args) {
    String portNumber = args[0];


    new Server().run(portNumber);
  }
}
