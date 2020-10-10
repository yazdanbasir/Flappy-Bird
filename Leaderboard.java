import processing.core.*;
import static javax.swing.JOptionPane.*; // Imports the procedure for the text box that allows a username to be entered

public class Leaderboard {
  private PApplet p;
 
  private Scores[] scores; // Array of scores that contain every player's scores
 
  private String name;
  private String[] scoreBoard; // Array of strings that updated scores are saved to
  
  private Pipes pipes;
  
  private boolean displayed = false; // Leaderboard is not displayed until 'n' is pressed after winning/losing

  public Leaderboard(PApplet p) {
    this.p = p;
    
    String[] s = p.loadStrings("scores.txt"); // File with all the names and scores
    
    scores = new Scores[s.length];
    
    // Puts every person from scores.txt into the scores array
    for (int i = 0; i < s.length; i++) {
      scores[i] = new Scores(s[i], p);
    }
    
    scoreBoard = new String[scores.length];
  }

  // Takes name from text box and sets name to it
  public void name() {
    String input = showInputDialog("Please enter username"); // Displays the box that allows the user to enter their username
    name = input;
  }

  // Records a player's score when the game ends
  public void record (int score) {
    for (int i = 0; i < scores.length; i++) {
      
      // If the player already exists on the leaderboard and her score is greater than her previous high score, set the high score to this new score
      if (name.equalsIgnoreCase(scores[i].getName())) {
        
        if (scores[i].getHighScore() < score) {
          scores[i].setHighScore(score);
        }
        
        return;
      }
    }
        // If the name does not exist yet, create a new entry for it.
        Scores user;
        user = new Scores(name, score, p);
        Scores[] tmpScores = new Scores[scores.length+1];

        for (int i = 0; i < scores.length; i++) {
          tmpScores[i] = scores[i];
        }
        tmpScores[tmpScores.length-1] = user;
        scores = tmpScores;
      }


  // Saves the strings for the leaderboard
  public void saveBoard() {
    selectionSort(); // Sorts all the players by score before saving the new leaderboard
    scoreBoard = new String[scores.length];
    
    // The scores are saved to a separate String array called "board"
    for (int i = 0; i < scoreBoard.length; i++) {
      scoreBoard[i] = "" + scores[i];
    }
    p.saveStrings("scores.txt", scoreBoard); // Saves scoreBoard to scores.txt
  }

  // Selection sort: Puts the players with the most wins on top.
  private void selectionSort() {
    for (int i = 0; i < scores.length-1; i++) {
      if (scores[i]!=null) {
        int most = scores[i].getHighScore();
        Scores mostScores = scores[i];
        int mostLocation = i;
        
        for (int j = i+1; j < scores.length; j++) {
          if (scores[j] != null && most < scores[j].getHighScore()) {
            most = scores[j].getHighScore();
            mostScores = scores[j];
            mostLocation = j;
          }
        }
        
        scores[mostLocation] = scores[i];
        scores[i] = mostScores;
      }
    }
  }

  // Displays the leaderboard
  public void display() {
    displayed = true;
    p.background(p.color(0, 0, 255));
    p.textAlign(p.CENTER);
    p.fill(255);
    p.text("Leaderboard", p.width/2, p.height/12);
    p.textAlign(p.LEFT);
    p.text("Name", 50, p.height/6);
    p.text("High Score", p.width/2+50, p.height/6);
    for (int i = 0; i < scoreBoard.length; i++) {
      String[] tmpBoard = new String[2];
      tmpBoard = p.splitTokens(scoreBoard[i], "|");
      p.text(tmpBoard[0], 50, p.height/12*(i+3));
      p.text(tmpBoard[1], p.width/2+50, p.height/12*(i+3));
    }
  }
  
  // Getter for displayed boolean
  public boolean isDisplayed() {
    return displayed;
  }
}
