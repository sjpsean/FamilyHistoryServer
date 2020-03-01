package Response;

public class LoginResponse extends RegisterResponse {

  /**
   * MessageResponse for Login. Used all variables from RegisterResponse to avoid duplication.
   * @param authToken authentication token
   * @param userName user's username
   * @param personID peron's ID
   */
  public LoginResponse(String authToken, String userName, String personID) {
    super(authToken, userName, personID);
  }

  public LoginResponse(String message, boolean success) {
    super(message, success);
  }
}
