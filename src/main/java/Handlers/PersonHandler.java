package Handlers;

import Dao.AuthTokenDAO;
import Dao.DataAccessException;
import Dao.Database;
import Handlers.Convert.ObjectToJson;
import Handlers.ReadWrite.WriteString;
import Model.AuthToken;
import Requests.PersonRequest;
import Response.PersonResponse;
import Service.GetPersonService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.sql.Connection;

public class PersonHandler implements HttpHandler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String authToken=null;
    try {
      if (exchange.getRequestMethod().toLowerCase().equals("get")) {
        Headers reqHeaders=exchange.getRequestHeaders();
        if (reqHeaders.containsKey("Authorization")) {
          authToken = reqHeaders.getFirst("Authorization");
          PersonResponse personRes = null;
          URI uri = exchange.getRequestURI();
          String[] parseUri=uri.toString().split("/");

          if (parseUri.length == 2) {
            // all person for user
            PersonRequest personReq=new PersonRequest(authToken);
            GetPersonService getPersonService=new GetPersonService(personReq);
            personRes=getPersonService.getAllPersons();

            if (personRes != null) {
              String resData=ObjectToJson.objectToJson(personRes);
              if (personRes.isSuccess()) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream resBody=exchange.getResponseBody();
                WriteString.ws(resData, resBody);
                resBody.close();
              } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                OutputStream resBody=exchange.getResponseBody();
                WriteString.ws(resData, resBody);
                resBody.close();
              }
            }


          } else if (parseUri.length == 3) {
            // person with personID
            String personID=parseUri[2]; //  "" , "person", "personID"
            PersonRequest personReq=new PersonRequest(personID, authToken);
            GetPersonService getPersonService=new GetPersonService(personReq);
            personRes = getPersonService.getPersonByID();

            String resData;
            if (personRes.isSuccess()) {
              resData = ObjectToJson.objectToJson(personRes.getPerson());
              exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
              OutputStream resBody=exchange.getResponseBody();
              WriteString.ws(resData, resBody);
              resBody.close();
            } else {
              resData = ObjectToJson.objectToJson(personRes);
              exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
              OutputStream resBody = exchange.getResponseBody();
              WriteString.ws(resData, resBody);
              resBody.close();
            }
          } else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            exchange.getResponseBody().close();
          }

        }
        else {
          exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
          exchange.getResponseBody().close();
        }
      }
    } catch (DataAccessException ex) {
      exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR,0);
      exchange.getResponseBody().close();
      ex.printStackTrace();
    }
  }
}
