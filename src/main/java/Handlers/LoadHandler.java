package Handlers;

import Dao.DataAccessException;
import Handlers.Convert.JsonToObject;
import Handlers.Convert.ObjectToJson;
import Handlers.ReadWrite.ReadString;
import Handlers.ReadWrite.WriteString;
import Requests.LoadRequest;
import Response.LoadResponse;
import Service.LoadService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import static Server.FMSLogger.logger;

public class LoadHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    boolean success = false;
    try {
      if (exchange.getRequestMethod().toLowerCase().equals("post")) {
        InputStream reqBody = exchange.getRequestBody();
        String reqData = ReadString.rs(reqBody);

        // ** log the request JSON data // like this?-> logger.info(reqData);

        // convert reqData to object FillRequest to call LoadService.load()
        LoadRequest loadReq = JsonToObject.jsonToObject(reqData, LoadRequest.class);
        LoadResponse loadRes = new LoadService(loadReq).load();
        // convert LoadResponse to Json.
        String resData = ObjectToJson.objectToJson(loadRes);

        if (loadRes.isSuccess()) {
          exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
          OutputStream resBody = exchange.getResponseBody();
          WriteString.ws(resData, resBody);
          resBody.close();
        }
        else {
          exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
          OutputStream resBody = exchange.getResponseBody();
          WriteString.ws(resData, resBody);
          resBody.close();
        }

        // Sending HTTP response to the client.
        // 1. send status code and any defined headers.
        // 2. get the response body output stream.
        // 3. Write json string to the output stream.
        // 4. close the output stream.


        success = true;
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
