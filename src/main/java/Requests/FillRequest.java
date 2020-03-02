package Requests;

/**
 * Request for FillService
 */
public class FillRequest {
  private String userName;
  private int generations;

  /**
   * if there is only one parameter with userName, generations will be default value.
   * @param userName unique userName
   */
  public FillRequest(String userName) {
    this.userName = userName;
    generations = 4;
  }

  /**
   * if there are userName and generation number as an input, get both of them.
   * @param userName unique userName
   * @param generations number of generations to create
   */
  public FillRequest(String userName, int generations) {
    this.userName = userName;
    this.generations = generations;
  }

  public int getGenerations() {
    return generations;
  }

  public void setGenerations(int generations) {
    this.generations = generations;
  }
}
