package Handlers;

import java.io.*;
import java.net.*;

import com.sun.net.httpserver.*;

public class RegisterHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {

    boolean rightToken = false;
    try {
      if (exchange.getRequestMethod().toLowerCase().equals("get")) {
        Headers reqHeaders = exchange.getRequestHeaders();
        if (reqHeaders.containsKey("Authorization")) {
          String authToken = reqHeaders.getFirst("Authorization");
          //if ()
        }
      }
    } catch (Exception e) {};
  }
}