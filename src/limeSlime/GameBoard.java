package limeSlime;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Elise Haram Vannes on 14.06.2017.
 */
public class GameBoard {

    private Canvas canvas;
    private GraphicsContext gc;
    private AnimationTimer animation;

    // Rows come first in a Java array, 25 rows and 35 columns
    private int boardHeight; // height == rows
    private int boardWidth; // width == columns
    private int[][] boardGrid;
    private long currentTime;
    private boolean gamePlaying = false;
    private SnakePlayer player;
    private Apple apple;
    private HighScore highScore = new HighScore(gc,canvas);
    private Image appleImage;
    private Image snakeFaceUp;
    private Image snakeFaceRight;
    private Image snakeFaceDown;
    private Image snakeFaceLeft;
    private Image snakeTailUp;
    private Image snakeTailRight;
    private Image snakeTailDown;
    private Image snakeTailLeft;
    private Label scoreText;
    private int score = 0;
    private Label nameLabel;
    private Button scoreButton;
    private TextField scoreNameField;
    private int tileSize = 24;

    public GameBoard(Canvas canvas, GraphicsContext gc, SnakePlayer player, Label scoreText, Label nameLabel, Button scoreButton, TextField scoreNameField,
                     int boardWidth,int boardHeight){
        this.canvas = canvas;
        this.gc = gc;
        this.player = player;
        this.apple = new Apple();
        this.scoreText = scoreText;
        this.nameLabel = nameLabel;
        this.scoreButton = scoreButton;
        this.scoreNameField = scoreNameField;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        boardGrid = new int[boardHeight][boardWidth];

        placeApple();
        placeStartSnake();
        initPictures();

        drawBoard();
        animation = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if((now - currentTime) > 90000000){
                    drawBoard();
                    updateSnakePosition();
                    currentTime = System.nanoTime();
                }
            }
        };
    }

    /**
     * Starts a new game.
     */
    void startGame(){
        if(gamePlaying){
            resetGame();
        }
        gamePlaying = true;
        score = 0;
        player.setDirection(4);
        scoreText.setText(String.valueOf(score));
        animation.start();
    }

    /**
     * Draws the game to the canvas.
     */
    private void drawBoard(){
        // 0 means the spot is empty, 1 means a part of the LimeSlime, 2 means an apple
        int x = 0;
        int y = 0;

        gc.setFill(Color.web("#000000"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for(int i = 0; i < boardGrid.length; i++){
            for(int j = 0; j < boardGrid[0].length; j++) {
                if(boardGrid[i][j] == 1 && player.getSnakeHeadX() == i && player.getSnakeHeadY() == j){
                    int direction = player.getDirection();
                    if(direction == 1){
                        // draw image facing upwards
                        gc.drawImage(snakeFaceUp,x,y,tileSize,tileSize);
                    } else if(direction == 2){
                        gc.drawImage(snakeFaceRight,x,y,tileSize,tileSize);
                    } else if(direction == 3){
                        gc.drawImage(snakeFaceDown,x,y,tileSize,tileSize);
                    } else if(direction == 4){
                        gc.drawImage(snakeFaceLeft,x,y,tileSize,tileSize);
                    }
                }
                else if(boardGrid[i][j] == 1 && player.getSnakeEndX() == i && player.getSnakeEndY() == j) {
                    int snakeAlmostEndX = player.getSnakeAlmostEndX();
                    int snakeAlmostEndY = player.getSnakeAlmostEndY();

                    if(snakeAlmostEndX < player.getSnakeEndX()){
                        // draw image facing upwards
                        gc.drawImage(snakeTailUp,x,y,tileSize,tileSize);
                    } else if(snakeAlmostEndX > player.getSnakeEndX()){
                        gc.drawImage(snakeTailDown,x,y,tileSize,tileSize);
                    } else if(snakeAlmostEndY < player.getSnakeEndY()){
                        gc.drawImage(snakeTailLeft,x,y,tileSize,tileSize);
                    } else if(snakeAlmostEndY > player.getSnakeEndY()){
                        gc.drawImage(snakeTailRight,x,y,tileSize,tileSize);
                    }
                }
                else if(boardGrid[i][j] == 1){
                    gc.setFill(Color.web(player.getSnakeColor()));
                    gc.fillRect(x,y,tileSize,tileSize);
                }
                else if(boardGrid[i][j] == 2){
                    gc.drawImage(appleImage,x,y,tileSize,tileSize);
                }
                x += tileSize+1;
            }
            y += tileSize+1;
            x = 0;
        }
    }

    /**
     * Updates the position of the snake.
     */
    private void updateSnakePosition(){
        gamePlaying = player.moveSnake(boardGrid);

        if(gamePlaying) {
            int[][] snakeGrid = player.getSnakeGrid();

            for (int i = 0; i < boardGrid.length; i++) {
                for (int j = 0; j < boardGrid[0].length; j++) {
                    if (snakeGrid[i][j] == 1) {
                        if(boardGrid[i][j] == 2){
                            player.addSnakeLength();
                            score += 10;
                            scoreText.setText(Integer.toString(score));
                            placeApple();
                        }
                        boardGrid[i][j] = 1;
                    } else if(snakeGrid[i][j] == 0 && boardGrid[i][j] != 2){
                        boardGrid[i][j] = 0;
                    }
                }
            }
        } else {
            animation.stop();
            gameOverHappenings();
        }
    }

    /**
     * Places the snake on the board in its default state.
     */
    private void placeStartSnake(){
        int[][] snakeGrid = player.getSnakeGrid();

        for(int i = 0; i < boardGrid.length; i++){
            for(int j = 0; j < boardGrid[0].length; j++) {

                // Places new LimeSlime on board
                if(snakeGrid[i][j] == 1){
                    boardGrid[i][j] = 1;
                }
            }
        }
    }

    /**
     * Places a new apple on the board, at a random position that doesn't intersect with the snake.
     */
    private void placeApple(){

        boolean positioned = false;
        int x = 0;
        int y = 0;

        while(!positioned) {
            x = (int) Math.floor(Math.random() * boardGrid.length);
            y = (int) Math.floor(Math.random() * boardGrid[0].length);

            if (boardGrid[x][y] == 0){
                positioned = true;
            }
        }

        boardGrid[x][y] = 2;
    }

    /**
     * Initializes all the needed pictures.
     */
    private void initPictures(){
        appleImage = initPicture("C:\\Users\\Bruker\\IdeaProjects\\Snake\\src\\limeSlime\\img\\apple.png");

        snakeFaceUp = initPicture("C:\\Users\\Bruker\\IdeaProjects\\Snake\\src\\limeSlime\\img\\snakefaceup.png");
        snakeFaceRight = initPicture("C:\\Users\\Bruker\\IdeaProjects\\Snake\\src\\limeSlime\\img\\snakefaceright.png");
        snakeFaceDown = initPicture("C:\\Users\\Bruker\\IdeaProjects\\Snake\\src\\limeSlime\\img\\snakefacedown.png");
        snakeFaceLeft = initPicture("C:\\Users\\Bruker\\IdeaProjects\\Snake\\src\\limeSlime\\img\\snakefaceleft.png");

        snakeTailUp = initPicture("C:\\Users\\Bruker\\IdeaProjects\\Snake\\src\\limeSlime\\img\\tailmovesup.png");
        snakeTailRight = initPicture("C:\\Users\\Bruker\\IdeaProjects\\Snake\\src\\limeSlime\\img\\tailmovesright.png");
        snakeTailDown = initPicture("C:\\Users\\Bruker\\IdeaProjects\\Snake\\src\\limeSlime\\img\\tailmovesdown.png");
        snakeTailLeft = initPicture("C:\\Users\\Bruker\\IdeaProjects\\Snake\\src\\limeSlime\\img\\tailmovesleft.png");
    }

    /**
     * Method for initializing a picture.
     * @param location  the location of the image file
     * @return          the Image object that was created from the location
     */
    private Image initPicture(String location){
        try {
            return new Image(new FileInputStream(location));
        } catch(FileNotFoundException e){
            System.out.println("Could not find picture.");
        }
        return null;
    }

    /**
     * Saves the high score from the last played game, with the name that the player has entered.
     * If the player has not entered a name, the score will be saved with the name "anonymous"
     */
    void saveScore(){
        CharSequence name = scoreNameField.getCharacters();
        if(name.length() == 0){
            name = "anonymous";
        }
        System.out.println("Name: " + name);
        System.out.println("Score: " + score);

        highScore.saveHighScore(name,score);
        nameLabel.setVisible(false);
        scoreButton.setVisible(false);
        scoreNameField.setVisible(false);
        scoreNameField.setText("");
    }

    /**
     * Executes a number of actions when a game is over.
     */
    private void gameOverHappenings(){
        nameLabel.setVisible(true);
        scoreButton.setVisible(true);
        scoreNameField.setVisible(true);

        gc.setFill(Color.web(player.getSnakeColor()));
        gc.setFont(new Font("Bitwise", 60));
        gc.fillText("GAME OVER",250,300);

        resetGame();
    }

    /**
     * Resets the game for a new round.
     */
    private void resetGame(){
        clearBoard();
        player.resetSnake();
        placeApple();
        placeStartSnake();
    }

    /**
     * Clears the array that contains the elements of the board.
     */
    private void clearBoard(){
        for(int i = 0; i < boardGrid.length; i++){
            for(int j = 0; j < boardGrid[0].length; j++){
                boardGrid[i][j] = 0;
            }
        }
    }

    /**
     * Pauses and resumes the game
     * @param paused    boolean to define if the game should be paused or resumed
     */
    void pauseGame(boolean paused){
        if(!paused) {
            animation.stop();
        } else {
            animation.start();
        }
    }
}