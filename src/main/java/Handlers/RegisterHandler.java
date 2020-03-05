package Handlers;

import java.io.*;
import java.net.*;

import Dao.DataAccessException;
import Handlers.Convert.JsonToObject;
import Handlers.Convert.ObjectToJson;
import Handlers.ReadWrite.ReadString;
import Handlers.ReadWrite.WriteString;
import Requests.RegisterRequest;
import Response.RegisterResponse;
import Service.RegisterService;
import com.sun.net.httpserver.*;

public class RegisterHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    boolean success = false;
    try {
      if (exchange.getRequestMethod().toLowerCase().equals("post")) {
//        Headers reqHeaders = exchange.getRequestHeaders();
//        if (reqHeaders.containsKey("Authorization")) {
//          String authToken = reqHeaders.getFirst("Authorization");
//          //if ()
//        }
        InputStream reqBody = exchange.getRequestBody();
        String reqData = ReadString.rs(reqBody);

        // Log the reqData for request

        RegisterRequest regReq = JsonToObject.jsonToObject(reqData, RegisterRequest.class);
        RegisterResponse regRes = new RegisterService(regReq).registerUser();

        String resData = ObjectToJson.objectToJson(regRes);

        if (regRes.isSuccess()) {
          exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
    //      Headers resHeader = exchange.getResponseHeaders();
          OutputStream resBody = exchange.getResponseBody();
          WriteString.ws(resData, resBody);
   //       resHeader.set("Authorization", regRes.getAuthToken());
          resBody.close();
        } else {
          exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
          OutputStream resBody = exchange.getResponseBody();
          WriteString.ws(resData, resBody);
          resBody.close();
        }

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