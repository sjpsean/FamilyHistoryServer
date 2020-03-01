package Response;

public class LoadResponse extends MessageResponse {
  /**
   * This response only need a message and a success variables.
   * if success is true,
   * Success message is a description of what service was successfully executed.
   * if success if false,
   * Error message is a description of the error.
   *
   * @param message description of the execution or the error
   * @param success true if service went through false if not
   */
  public LoadResponse(String message, boolean success) {
    super(message, success);
  }
}
