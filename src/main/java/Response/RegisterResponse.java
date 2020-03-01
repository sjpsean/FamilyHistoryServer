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
    super("", true);
    this.authToken=authToken;
    this.userName=userName;
    this.personID=personID;
  }

  public RegisterResponse(String message, boolean success) {
    super(message, success);
  }



  public String getAuthToken() {
    return authToken;
  }

  public void setAuthToken(String authToken) {
    this.authToken=authToken;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName=userName;
  }

  public String getPersonID() {
    return personID;
  }

  public void setPersonID(String personID) {
    this.personID=personID;
  }
}
