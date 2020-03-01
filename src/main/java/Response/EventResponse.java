package Response;

import Model.Event;

public class EventResponse extends MessageResponse {
  Event[] eventsList = null;
  Event event = null;

  EventResponse(Event[] eventsList) {
    super("", true);
    this.eventsList = eventsList;
  }

  EventResponse(Event event) {
    super("", true);
    this.event = event;
  }

  EventResponse(String message, boolean success) {
    super(message, success);
  }

  public Event[] getEventsList() {
    return eventsList;
  }

  public Event getEvent() {
    return event;
  }

}
