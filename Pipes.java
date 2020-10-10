import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;
import ddf.minim.signals.*;
import ddf.minim.spi.*;
import ddf.minim.ugens.*;

import processing.core.*;

public class Pipes {
  private PApplet p;

  private Bird bird;
  private Minim minim;
  private AudioPlayer score;

  private PImage pipe1; // Pipe facing downwards
  private PImage pipe2; // Pipe facing upwards 

  private float[] pipeX; // Stores the x-values of the pipes
  private float[] pipeY; // Stores the y-values of the pipes
  
  private float[] lowerYBound; // Stores the y-value of the top of the lower pipe
  private float[] upperYBound; // Stores the y-value of the base of the uppers pipe
  
  private boolean[] scoreCheck; // Whether or not the score has been checked and increased for every distinct pair of pipes

  private int numberOfPipes = 0;
  private int count = 0;

  private float minPipeH; // The minimum height of the pipe at the top to avoid it being too small and not visible
  private float pipeGap; // Sets a constant gap between each pair of pipes
  private float groundY; // Stores the y-value of the ground
  private float pipeW; // Width of the pipes
  private float speed; // Speed at which the pipes move across the screen

  private boolean gameLost = false; // Sets as false in the start so that game can be played
  private boolean gameWon = false; // Sets as false in the start so that game can be played
  private boolean playScore = false; // Boolean to check if the score should be played

  private int maxPipes = 50;  // Change this to change the score needed to win

  public Pipes(PApplet p, Bird bird, Minim minim, AudioPlayer score) {
    this.minim = minim;
    this.score = score;

    this.p = p;

    this.bird = bird;

    pipe1 = p.loadImage("pipe.png");
    pipe2 = p.loadImage("pipe.png");

    pipeX = new float[maxPipes];
    pipeY = new float[maxPipes];

    lowerYBound = new float[maxPipes];
    upperYBound = new float[maxPipes];

    scoreCheck = new boolean[maxPipes];

    pipeGap = (float) (p.height/3.5);
    pipeW = (float) (p.width/6.4);
    groundY = (float) (p.height * 0.885);
    minPipeH = (float) (p.height/32);

    speed = p.width/130; // Set such that game gets increasing difficult as window size is reduced and easier as window size increased

    for (int i = 0; i < maxPipes; i++) {
      pipeX[i] = p.width + ((p.width/2) * i);
      pipeY[i] = p.random(minPipeH, groundY - (p.height/16) - pipeGap); // Assigns the pipes a random y-value that is at least the minimum height but less than halfway through the screen

      upperYBound[i] = pipeY[i]; // The y-value of the base of the top pipe is the same as the pipe's y-value
      lowerYBound[i] = upperYBound[i] + pipeGap; // The y-value of the top of the lower pipe is the y-value of the upper pipe + gap bw the pipes

      scoreCheck[i] = false; // Set to false to let the game know no pipe has been passed yet
    }
  }
  
  public void display() {
    for (int i = 0; i < maxPipes; i++) {
      p.image(pipe1, pipeX[i], 0, pipeW, pipeY[i]);
      p.image(pipe2, pipeX[i], pipeY[i] + pipeGap, pipeW, groundY - (pipeGap + pipeY[i]));
    }
  }

  public void move() {
    for (int i = 0; i < maxPipes; i++) {
      pipeX[i] = pipeX[i] - speed; // Moves the pipes across the screen
    }
  }

  public void detectHit() {
    for (int i = 0; i < maxPipes; i++) {

      // Checks if the bird's left boundary and right boundary are inside the left and right boundary of the pipes
      if ((bird.getRightX() <= pipeX[i] + pipeW) && (bird.getLeftX() >= pipeX[i])) { // Checks if the bird's left boundary and right boundary are inside the left and right boundary of the pipes

        // Game is lost if top of bird touches the top pipe
        if ((bird.getTopY() <= upperYBound[i])) {
          gameLost = true;
        }
        
        // Game is lost if bottom of bird touches the bottom pipe
        if ((bird.getBottomY() >= lowerYBound[i])) {
          gameLost = true;
        }
      }

      if (i > 0) { // Checks if the bird has crossed the first pipe and has hit the base of the inside of the upper pipe
        if ((bird.getLeftX() > (pipeX[i-1] + pipeW)) && (bird.getRightX() < (pipeX[i] + pipeW))) {
          if (bird.getTopY() <= upperYBound[i] && (bird.getRightX() - p.width/45 >= pipeX[i])) {
            gameLost = true;
          }

          // Checks if the bird has crossed the first pipe and has hit the top of the inside of the lower pipe
          if (bird.getBottomY() >= lowerYBound[i] && (bird.getRightX() - p.width/45 >= pipeX[i])) {
            gameLost = true;
          }
        }
      } else if (i == 0) { // Checks if bird has hit the first pipe
        if (bird.getLeftX() > (pipeX[0]) && (bird.getRightX() < (pipeX[0] + pipeW))) {
          if (bird.getTopY() <= upperYBound[0] && (bird.getRightX() - p.width/45 >= pipeX[0])) {
            gameLost = true;
          }

          if (bird.getBottomY() >= lowerYBound[0] && (bird.getRightX() - p.width/45 >= pipeX[0])) {
            gameLost = true;
          }
        }

        if ((bird.getBottomY() >= lowerYBound[0]) && bird.getRightX() < (pipeX[0] + pipeW)) {
          if (bird.getRightX() - p.width/45 >= pipeX[0]) {
            gameLost = true;
          }
        }

        if (bird.getTopY() <= upperYBound[0] && bird.getRightX() < (pipeX[0] + pipeW)) {
          if (bird.getRightX() - p.width/45 >= pipeX[0]) {
            gameLost = true;
          }
        }
      }

      if (bird.getBottomY() >= groundY) { // Checks if bird has hit the ground
        gameLost = true;
      }

      if (i < maxPipes) { // Plays the score sound if the bird has crossed the incoming pipe but has not reached the next pipe
        if (i < maxPipes -1) {
          if ((bird.getRightX() > ((pipeX[i] + pipeW))) && (bird.getRightX() < pipeX[i+1])) {
            if (!scoreCheck[i]) { // Prevents the bird from scoring more than once between two pipes
              playScore = true; // Triggers the method that will play the sound
              scoreSound();
              scoreCheck[i] = true; // Flags the pipe as passed and increases the score
            }
          }

          if ((bird.getLeftX() > ((pipeX[i] + pipeW))) && (bird.getRightX() < pipeX[i+1])) {
            count = i + 1; // Increases the score the left side of the bird passes through the right side of the pipe
            // This is used here separately to compensate for a delay in the score sound so that score is updated at the same time as the sound is played
          }
        } else {
          
          // Game is won if score = number of pipes
          if (bird.getLeftX() > pipeX[maxPipes-1] + pipeW) {
            count = maxPipes;
            gameWon = true;
          }
        }
      }
    }
    
    // Score is only displayed after all the above calculations
    score();
  }

  public void scoreSound() {
    if (playScore) { // Checks if the boolean has been flagged as true
      if (!score.isPlaying()) {
        score.rewind();
        score.play(); 
      }
      playScore = false; // Sets it back to false so that the sound doesn't keep playing
    }
  }

  public void score() {
    p.text(count, p.width/2 - p.width/50, groundY + p.height/10); // Displays score
  }

  public int getCount() {
    return count;
  }

  public boolean getGameLost() {
    return gameLost;
  }

  public boolean getGameWon() {
    return gameWon;
  }

  public boolean getPlayScore() {
    return playScore;
  }

  public void setPlayScore(boolean a) {
    this.playScore = a;
  }
}
