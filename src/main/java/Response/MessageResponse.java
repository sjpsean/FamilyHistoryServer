package Response;

public class MessageResponse {
  String message;
  boolean success;

  /**
   * This response only need a message and a success variables.
   * if success is true,
   * Success message is a description of what service was successfully executed.
   * if success if false,
   * Error message is a description of the error.
   * @param message description of the execution or the error
   * @param success true if service went through false if not
   */
  public MessageResponse(String message, boolean success) {
    this.message=message;
    this.success=success;
  }

  public String getMessage() {
    return message;
  }

  public boolean isSuccess() {
    return success;
  }
}
