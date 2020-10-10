import processing.core.*; // Imports the Processing library

public class Background {
  private PApplet p;
  private PImage bg;
  
  public Background(PApplet p) {
    this.p = p;
    bg = p.loadImage("background.png"); // Loads up the background image
  }
  
  public void display() {
    p.image(bg, 0, 0, (float) (p.width + p.width/3.5), p.height); // Draws the background image sufficiently large enough to cover more than the screen
  }
}
