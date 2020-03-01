package Requests;

/**
 * Request for login service.
 */
public class LoginRequest {
  private String userName;
  private String password;

  /**
   * All variables are elements of the Json request from the web API
   * @param userName username of the user
   * @param password password of the user
   */
  public LoginRequest(String userName, String password) {
    this.userName = userName;
    this.password = password;
  }

  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName=userName;
  }

  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password=password;
  }
}
