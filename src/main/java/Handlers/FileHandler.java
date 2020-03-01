package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    try {
      String url = exchange.getRequestURI().toString();
      if (url == null || url.equals("/")) {
        url = "/index.html";
      }
      String filePath = "web" + url;
      File file = new File(filePath);
      if (file.exists() && file.canRead()) {
        OutputStream resBody = exchange.getResponseBody();
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        Files.copy(file.toPath(), resBody);
        resBody.close();
      } else {
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
        Path path = Paths.get("web/HTML/404.html");
        Files.copy(path, exchange.getResponseBody());
      }
    } catch (IOException e) {
      e.printStackTrace();
      throw new IOException("File not found");
    }
  }
}
