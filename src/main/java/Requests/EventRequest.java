package Requests;

public class EventRequest {
  String eventID;
  String token;

  public EventRequest(String token) {
    this.token = token;
    eventID = null;
  }

  public EventRequest(String eventID, String token) {
    this.eventID = eventID;
    this.token =  token;
  }

  public String getEventID() {
    return eventID;
  }

  public String getToken() {
    return token;
  }
}
