package Response;

import Model.Event;

public class EventResponse extends MessageResponse {
  Event[] data = null;
  Event event = null;

  public EventResponse(Event[] eventsList) {
    super(null, true);
    this.data = eventsList;
  }

  public EventResponse(Event event) {
    super(null, true);
    this.event = event;
  }

  public EventResponse(String message, boolean success) {
    super(message, success);
  }

  public Event[] getEventsList() {
    return data;
  }

  public Event getEvent() {
    return event;
  }

}
