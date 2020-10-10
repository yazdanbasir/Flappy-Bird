import processing.core.*;
 
public class OpeningScreen {
  private PApplet p;
  private PImage bg;
  private PFont fb;
  
  private String name; // Stores the username that has been entered into the popup text box
  
  public OpeningScreen(PApplet p) {
    this.p = p;
    bg = p.loadImage("background.png");
    fb = p.createFont("FlappyBird.ttf", 32); // The original Flappy Bird font
  }
  
  public void display() {
    p.image(bg, 0, 0, (float) (p.width + p.width/3.5), p.height);
    
    p.textFont(fb);
      
    p.fill(255);
    p.textAlign(p.CENTER);
    p.textSize((p.width/5));
    p.text("Flappy Bird", p.width/2, p.height/3-p.height/20);
      
    p.textSize(p.width/18);
    p.textAlign(p.CENTER);
    p.text("Try to beat the high score", p.width/2, p.height/2 - p.height/8);
      
    p.textAlign(p.CENTER);
    p.textSize(p.width/10);
    p.text("PRESS ANY KEY TO START", p.width/2, p.height/2 - p.height/50); // Displays the intro screen
  }
}
