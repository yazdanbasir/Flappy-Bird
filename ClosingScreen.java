import processing.core.*;

public class ClosingScreen {
  private PApplet p;

  public ClosingScreen(PApplet p) {
    this.p = p;
  }

  public void winDisplay() { // Gets called in Flappy Bird when gameWon = true
    p.noStroke();
    p.fill(255, 255, 255, 3);
    p.rect(0, p.height/3, p.width, p.height/3);

    p.textAlign(p.CENTER, p.CENTER);
    p.fill(0);
    p.textSize(p.width/10);
    p.text("You Win! Play Again? (Y/N)", p.width/2, p.height/2); // Displays the winning text
  }

  public void lossDisplay() { // Gets called in Flappy Bird when gameLost = true
    p.noStroke();
    p.fill(255, 255, 255, 3);
    p.rect(0, p.height/3, p.width, p.height/3);

    p.textAlign(p.CENTER, p.CENTER);
    p.fill(0);
    p.textSize(p.width/10);
    p.text("You Lose! Play Again? (Y/N)", p.width/2, p.height/2); // Displays the losing text
  }
}
