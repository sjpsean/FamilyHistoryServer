package Response;

public class RegisterResponse extends MessageResponse {
  private String authToken;
  private String userName;
  private String personID;

  /**
   * MessageResponse for Register
   * @param authToken unique string to authenticate the user
   * @param userName unique string for username
   * @param personID unique string to point where the family tree starts
   */
  public RegisterResponse(String authToken, String userName, String personID) {
    super(null, true);
    this.authToken = authToken;
    this.userName = userName;
    this.personID = personID;
  }

  public RegisterResponse(String message, boolean success) {
    super(message, success);
  }

  public String getAuthToken() {
    return authToken;
  }

  public String getUserName() {
    return userName;
  }

  public String getPersonID() {
    return personID;
  }
}
