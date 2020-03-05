package Handlers;

import Dao.DataAccessException;
import Handlers.Convert.ObjectToJson;
import Handlers.ReadWrite.*;
import Requests.FillRequest;
import Response.FillResponse;
import Service.FillService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;

public class FillHandler implements HttpHandler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    boolean success = false;
    try {
      if (exchange.getRequestMethod().toLowerCase().equals("post")) {

      // first, parse the uri to make fillRequest using username and generation number.
        URI uri = exchange.getRequestURI();
        // /fill/username/genNum .substring(6) = username/genNum
        StringBuilder path = new StringBuilder(uri.getPath().substring(6));
        String userName = null;
        int genNum = 0;
        // if there is no number, genNum = 4 by default. Else, get it from url
        if (path.indexOf("/") == -1) {
          userName = path.toString();
          genNum = 4;
        }
        else {
          String[] tmp = path.toString().split("/");
          userName = tmp[0];
          genNum = Integer.parseInt(tmp[1]);
        }

        // check if it's valid uri to create a request.
        if (userName != null || genNum > 0) {
          // create a fill request.
          FillRequest fillReq = new FillRequest(userName, genNum);
          FillResponse fillRes = new FillService(fillReq).fill();

          String resData = ObjectToJson.objectToJson(fillRes);

          exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
          OutputStream resBody = exchange.getResponseBody();
          WriteString.ws(resData, resBody);
          resBody.close();

          success = true;
        }
      }

      if (!success) {
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        exchange.getResponseBody().close();
      }
    } catch (IOException | DataAccessException e) {
      exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
      exchange.getResponseBody().close();
      e.printStackTrace();
    }
  }
}
