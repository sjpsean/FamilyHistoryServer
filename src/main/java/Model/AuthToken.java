package Model;

import java.util.Objects;

public class AuthToken {
  private String authToken;
  private String associatedUsername;
  private String password;

  /**
   * <pre>
   *   When user successfully login, unique authorization token is generated.
   *   This token will be used in server to determine which user is making the request.
   * </pre>
   *
   * @param authToken Unique token for each user's each login (not null string)
   * @param associatedUsername User (username) to which this authToken belongs (not null string)
   * @param password User's password
   */
  public AuthToken(String authToken, String associatedUsername, String password) {
    this.authToken = authToken;
    this.associatedUsername = associatedUsername;
    this.password = password;
  }

  public String getAuthToken() {
    return authToken;
  }
  public void setAuthToken(String authToken) {
    this.authToken = authToken;
  }

  public String getAssociatedUsername() {
    return associatedUsername;
  }
  public void setAssociatedUsername(String associatedUsername) {
    this.associatedUsername = associatedUsername;
  }

  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password=password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AuthToken authToken1=(AuthToken) o;
    return authToken.equals(authToken1.authToken) &&
            associatedUsername.equals(authToken1.associatedUsername) &&
            password.equals(authToken1.password);
  }
}
