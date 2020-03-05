package Handlers;

import Dao.AuthTokenDAO;
import Dao.DataAccessException;
import Dao.Database;
import Handlers.Convert.ObjectToJson;
import Handlers.ReadWrite.WriteString;
import Requests.EventRequest;
import Requests.PersonRequest;
import Response.EventResponse;
import Response.PersonResponse;
import Service.GetEventService;
import Service.GetPersonService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.sql.Connection;

public class EventHandler implements HttpHandler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    boolean success=false;
    boolean isRegistered=false;
    String authToken=null;
    try {
      if (exchange.getRequestMethod().toLowerCase().equals("get")) {
        Headers reqHeaders=exchange.getRequestHeaders();
        if (reqHeaders.containsKey("Authorization")) {
          authToken=reqHeaders.getFirst("Authorization");
          // if it is valid token, get uri and see if it wants event by eventID or username.

          EventResponse eventRes=null;
          URI uri=exchange.getRequestURI();
          String[] parseUri=uri.toString().split("/");
          if (parseUri.length == 2) {
            // all events for user
            EventRequest eventReq=new EventRequest(authToken);
            GetEventService getEventService=new GetEventService(eventReq);
            eventRes=getEventService.getAllEvents();

            if (eventRes != null) {
              String resData=ObjectToJson.objectToJson(eventRes);
              if (eventRes.isSuccess()) {
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
            // event with eventID
            String personID=parseUri[2]; //  "" , "event", "eventID"
            EventRequest eventReq=new EventRequest(personID, authToken);
            GetEventService getEventService=new GetEventService(eventReq);
            eventRes=getEventService.getEventByID();

            if (eventRes != null) {
              String resData;
              if (eventRes.isSuccess()) {
                resData = ObjectToJson.objectToJson(eventRes.getEvent());
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream resBody=exchange.getResponseBody();
                WriteString.ws(resData, resBody);
                resBody.close();
              } else {
                resData = ObjectToJson.objectToJson(eventRes);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                OutputStream resBody=exchange.getResponseBody();
                WriteString.ws(resData, resBody);
                resBody.close();
              }
            }
          } else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            exchange.getResponseBody().close();
          }


        }
      }
    } catch (DataAccessException e) {
      exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
      exchange.getResponseBody().close();
      e.printStackTrace();
    }
  }
}
