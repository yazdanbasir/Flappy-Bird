// Background image from tynker.com
// Pipe image from ya-webdesign.com
// Bird image from toppng.com
// Sounds from sounds-resource.com

/*

Yazdan Basir
Shirley Liu

CS104-02 - Final Project

Flappy Bird: Press space to make the bird fly up.

If you hit the pipes or the ground, you lose. If you reach 50 points, you win.

Prof. Joann Ordille

*/

import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;
import ddf.minim.signals.*;
import ddf.minim.spi.*;
import ddf.minim.ugens.*;

Minim minim;
AudioPlayer bg;
AudioPlayer move;
AudioPlayer loss;
AudioPlayer score;
AudioPlayer win;

Background background;
Bird bird;
ClosingScreen endScreen;
Leaderboard leaderboard;
OpeningScreen screen;
Pipes pipes;

boolean gameStart = false; // Originally set to false so that the user presses some random key for the game to start

void setup() {
  minim = new Minim(this);
  
  bg = minim.loadFile("bg.mp3");
  move = minim.loadFile("move.wav");
  loss = minim.loadFile("loss.wav");
  score = minim.loadFile("score.wav");
  win = minim.loadFile("win.mp3");
  
  size(600, 600);
  
  background = new Background(this);
  bird = new Bird(this);
  pipes = new Pipes(this, bird, minim, score);
  screen = new OpeningScreen(this);
  endScreen = new ClosingScreen(this);
  leaderboard = new Leaderboard(this);

  screen.display();  // Displays the introductory screen
  leaderboard.name(); // Displays the text box that requests the player to enter their name
}

void draw() {
  
  if (gameStart) { // Checks if the user has pressed any random key to start the game
    
    if (!bg.isPlaying()) { // Ensures the background music restarts if it finishes in the middle of a game
      bg.rewind();
      bg.play();
    }
    
    background.display(); // Displays the ground, buildings, clouds
    pipes.display();      // Displays the green pipes on the screen
    pipes.move();         // Moves the pipes from right to left on the screen
    pipes.detectHit();    // Checks if edges of the bird have hit any edges of the pipe
    bird.display();       // Draws the image of the bird
    bird.moveDown();      // Acts as gravity and pulls the bird down
  } 
  
  if (pipes.getGameLost()) { // Checks if game lost
    bg.pause();              // Pauses the background music
    loss.play();             // Plays the loss sound
    gameStart = false;       // Ensures the game stops and cannot be played in the background
    
    if (!leaderboard.isDisplayed()) { // Displays the losing screen if the leaderboard hasn't been displayed
      endScreen.lossDisplay();
    }
    
    restart(); // Method that allows the user to press 'y' to restart the game or 'n' to close the game and view the leaderboard
  }
  
  if (pipes.getGameWon()) { // Checks if the game has been won (Score of 50 reached)
    bg.pause();
    win.play();             // Plays the winning sound when you win the game
    gameStart = false;      // Stops the game
    restart();
    
    if (!leaderboard.isDisplayed()) {
      endScreen.winDisplay();
    }
  }
}

void keyPressed() {
  if (!gameStart && !pipes.getGameLost() && !pipes.getGameWon()) { // Checks if the game hasn't been started or lost yet and starts if condition met
    gameStart = true;
  }
  
  if (gameStart && key == ' ') { // ' ' checks if spacebar was the key pressed
    move.pause(); 
    move.rewind();
    move.play();   // Plays the sound for the flapping of bird's wings
    
    bird.moveUp(); // Method for moving the bird up the screen when the spacebar is pressed
  }
}

void restart() { // Function for allowing the game to be restarted
  if (pipes.getGameLost() || pipes.getGameWon()) { // First checks if the game has been lost or won
    
    if (keyPressed && key == 'y') {
      setup(); // Restarts the game
    }
    
    if (keyPressed && key == 'n') {
      leaderboard.record(pipes.getCount()); // Updates the leaderboard
      leaderboard.saveBoard();              // Saves the updated leaderboard to scores.txt
      leaderboard.display();                // Displays the leaderboard
    }
  }
}
