import processing.core.*;

public class Scores {
  private String name;
  private int highScore;
  private PApplet p;

  // Constructor 1: Receives single line from .txt file and splits it
  public Scores(String s, PApplet p) {
    String [] words = p.splitTokens(s, "|");
    name = words[0];
    highScore = Integer.parseInt(words[1]);
    this.p = p;
  }

  // Constructor 2: Receives name and score to create a line in scores.txt. Used for new players
  public Scores(String name, int highScore, PApplet p) {
    this.name = name;
    this.highScore = highScore;
    this.p = p;
  }

  public String getName() {
    return name;
  }

  public String toString() {
    return name + "|" + highScore;
  }

  public int getHighScore() {
    return highScore;
  }

  public void setHighScore(int score) {
    highScore = score;
  }
}
