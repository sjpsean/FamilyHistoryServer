package Service;

import Requests.FillRequest;
import Response.MessageResponse;

/**
 * <pre>
 *   Populates the data with generated data for a user.
 *   If there is any data associated with the username, delete all of them.
 *   Generations parameter must be a non-negative integer (default is 4)
 * </pre>
 */
public class FillService {
  FillRequest fillRequest;

  /**
   * if there are two parameters with userName and generations, fill in that number of generation.
   * @param fillRequest unique userName and number of generations to fill
   */
  public FillService(FillRequest fillRequest) {
    this.fillRequest = fillRequest;
  }

  /**
   * Call all the functions needed to execute fill request.
   * Use UsersDAO to check if the user is registered.
   * Use PersonsDAO, EventsDAO, AuthTokensDAO to delete and create new data.
   * use fillRequest that contains String userName and int generations (optional)
   * @return MessageResponse with a message indicating how many persons and events were added
   */
  public MessageResponse fill() {
    return null;
  }

}
