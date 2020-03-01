package Handlers;

import Dao.DataAccessException;
import Handlers.Convert.ObjectToJson;
import Handlers.ReadWrite.WriteString;
import Response.ClearResponse;
import Service.ClearAllService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class ClearHandler implements HttpHandler {

  private ClearAllService clearService = new ClearAllService();

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    boolean success = false;
    try {
      if (exchange.getRequestMethod().toLowerCase().equals("post")) {

        // Call clearService's function deleteAll()
        ClearResponse clearResponse = clearService.deleteAll();
        // convert clearResponse object to json and store it in resData as a String value.
        String resData = ObjectToJson.objectToJson(clearResponse);

        // Set header for response and write resData to the response Body before closing.
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        OutputStream resBody = exchange.getResponseBody();
        WriteString.ws(resData, resBody);

        resBody.close();
        success = true;
      }
      if (!success) {
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        // open and close the response body since we have nothing as a response.
        exchange.getResponseBody().close();
      }
    } catch (DataAccessException e) {
      exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR,0);
      exchange.getResponseBody().close();
      e.printStackTrace();
    }

  }
}
