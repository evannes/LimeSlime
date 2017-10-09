package limeSlime;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable{

    @FXML
    private Canvas canvas;

    @FXML
    Label scoreText;

    @FXML
    Label nameLabel;

    @FXML
    Button scoreButton;

    @FXML
    TextField scoreNameField;

    @FXML
    Button pauseButton;

    private GraphicsContext gc;
    private GameBoard gameBoard;
    private SnakePlayer player;
    private boolean paused = false;

    public void startGame(){
        // playMusic();
        if(paused){
            pauseButton.setText("PAUSE");
            paused = false;
        }
        gameBoard.startGame();
        if(nameLabel.isVisible()){
            nameLabel.setVisible(false);
        }
        if(scoreButton.isVisible()){
            scoreButton.setVisible(false);
        }
        if(scoreNameField.isVisible()){
            scoreNameField.setVisible(false);
        }
    }

    public void saveScore(){
        gameBoard.saveScore();
        showHighScores();
    }

    public void showHighScores(){
        HighScore highScore = new HighScore(gc,canvas);
        highScore.showHighScores();
    }

    public void pauseGame(){
        if(!paused) {
            gameBoard.pauseGame(paused);
            pauseButton.setText("RESUME");
            paused = true;
        } else {
            gameBoard.pauseGame(paused);
            pauseButton.setText("PAUSE");
            paused = false;
        }
    }

    public void quitGame(){
        System.exit(0);
    }

    public void movePlayer(KeyEvent event){
        if(event.getCode() == KeyCode.W || event.getCode() == KeyCode.UP){
            player.moveUp();
        }
        if(event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT){
            player.moveRight();
        }
        if(event.getCode() == KeyCode.S || event.getCode() == KeyCode.DOWN){
            player.moveDown();
        }
        if(event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT){
            player.moveLeft();
        }
    }

    public void playMusic(){
        /*AudioClip plonkSound = new AudioClip("file:/C:/Users/Bruker/IdeaProjects/Snake/sound/stretchedEating2.mp3");
        plonkSound.setCycleCount(10);//AudioClip.INDEFINITE);
        plonkSound.play();
        System.out.println("cycle count: " + plonkSound.getCycleCount());*/
        Media media = new Media("file:/C:/Users/Bruker/IdeaProjects/Snake/sound/stretchedEating2.mp3");
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int boardHeight = 28; // height == rows
        int boardWidth = 32; // width == columns

        gc = canvas.getGraphicsContext2D();
        player = new SnakePlayer(boardWidth,boardHeight);
        gameBoard = new GameBoard(canvas,gc,player,scoreText,nameLabel,scoreButton,scoreNameField,boardWidth,boardHeight);

        nameLabel.setVisible(false);
        scoreButton.setVisible(false);
        scoreNameField.setVisible(false);
    }
}
