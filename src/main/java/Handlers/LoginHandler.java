package Handlers;

import Dao.DataAccessException;
import Handlers.Convert.JsonToObject;
import Handlers.Convert.ObjectToJson;
import Handlers.ReadWrite.ReadString;
import Handlers.ReadWrite.WriteString;
import Requests.LoginRequest;
import Response.LoginResponse;
import Service.LoginService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class LoginHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    boolean success = false;
    try {
      if (exchange.getRequestMethod().toLowerCase().equals("post")) {
        InputStream reqBody = exchange.getRequestBody();
        String reqData = ReadString.rs(reqBody);

        // Log the request data

        LoginRequest loginReq = JsonToObject.jsonToObject(reqData, LoginRequest.class);
        LoginResponse loginRes = new LoginService(loginReq).loginUser();

        String resData = ObjectToJson.objectToJson(loginRes);

        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        Headers resHeader = exchange.getResponseHeaders();
        OutputStream resBody = exchange.getResponseBody();
        WriteString.ws(resData, resBody);
        resHeader.set("Authorization", loginRes.getAuthToken());
        resBody.close();

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
