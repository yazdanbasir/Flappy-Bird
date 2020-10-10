import processing.core.*;

public class Bird {
  private PApplet p;
  
  private PImage bird;
  
  private float birdW; // Bird's width
  private float birdH; // Bird's height
  
  private float x; // The bird's x-value (top left corner)
  private float y; // The bird's y-value (top left corner)
  private float r; // Variable used to add and subtract from the bird's y-value
  
  private float birdTop;
  private float birdBottom;
  
  private float birdLeft;
  private float birdRight;
  
  public Bird(PApplet p) {
    this.p = p;
    
    bird = p.loadImage("bird.png");
    
    birdW = p.height/16; 
    birdH = p.height/20;
    
    x = birdW;
    y = p.height/3;
    
    r = 0;
  }
  
  public void display() {
    p.image(bird, x, y, birdW, birdH);
    
    y =  y + r;
    
    birdTop = y;
    birdBottom = y + birdH; 
    
    birdLeft = x;
    birdRight = x + birdW;
  }
 
  public void moveUp() { // Moves the bird up whenever the spacebar is pressed
    r = -10;
  }
  
  public void moveDown() { // Acts as gravity for the bird and pull its down constantly
    if (r < 20) {
      r++;
    }
  }
  
  public float getTopY() {
    return birdTop;
  }

  public float getBottomY() {
    return birdBottom;
  }
  
  public float getRightX() {
    return birdRight;
  } 
  
  public float getLeftX() {
    return birdLeft;
  }   
}
