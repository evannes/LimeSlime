package limeSlime;

import eliseJson.JsonParser;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Elise Haram Vannes on 17.07.2017.
 */
public class HighScore {

    private String newName;
    private String newScore;
    List<List<String>> jsonOutput;
    GraphicsContext gc;
    Canvas canvas;

    public HighScore(GraphicsContext gc, Canvas canvas){
        this.gc = gc;
        this.canvas = canvas;
    }

    /**
     * Saves high score in a text file.
     * @param name  the name to be saved with the score
     * @param score the score to be saved
     */
    void saveHighScore(CharSequence name, int score){
        this.newName = name.toString();
        this.newScore = String.valueOf(score);

        String fileContent = getFileContent();
        if(fileContent != null && !fileContent.equals("")) {
            jsonOutput = JsonParser.readJsonArray(fileContent);

            jsonOutput.get(0).add(newScore);
            jsonOutput.get(1).add(newName);

            List<List<String>> finalList = rearrangeHighScores(jsonOutput);

            String finalJsonArray = JsonParser.create2DJsonArray(finalList);
            System.out.println("text: " + finalJsonArray);
            writeToFile(finalJsonArray, "C:\\Users\\Bruker\\IdeaProjects\\Snake\\src\\limeSlime\\text\\highScores.txt");
        } else {
            List<List<String>> newList = new ArrayList<>(2);
            newList.add(new ArrayList<>());
            newList.add(new ArrayList<>());

            newList.get(0).add(newScore);
            newList.get(1).add(newName);

            String finalJsonArray = JsonParser.create2DJsonArray(newList);
            writeToFile(finalJsonArray,"C:\\Users\\Bruker\\IdeaProjects\\Snake\\src\\limeSlime\\text\\highScores.txt");
        }
    }

    /**
     * Prints the high scores to the current canvas.
     */
    void showHighScores(){
        String fileContent = getFileContent();
        if(fileContent != null && !fileContent.equals("")) {
            jsonOutput = JsonParser.readJsonArray(fileContent);
            int x = 300;
            int y = 100;

            gc.setFill(Color.BLACK);
            gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());

            gc.setFont(new Font("Bitwise", 34));
            gc.setFill(Color.web("#2ADE16"));

            for(int i = 0; i < jsonOutput.get(0).size(); i++){{
                if(i == jsonOutput.get(0).size()-1){
                    gc.fillText(i+1 + ". ",235,y);
                } else {
                    gc.fillText(i + 1 + ". ", 250, y);
                }
                for(int j = 0; j < jsonOutput.size(); j++){

                    gc.fillText(jsonOutput.get(j).get(i),x,y);

                    x += 100;
                }
                x = 300;
                y += 50;
            }

            }
        } else {
            // show empty list of high scores
        }
    }

    /**
     * Arranges high scores in correct order according to the highest scores.
     * Also removes all scores that are lower than the top ten values.
     * @param listInput list of high scores
     * @return          the updated list of high scores
     */
    private List<List<String>> rearrangeHighScores(List<List<String>> listInput){

        List<List<String>> newList = new ArrayList<>(2);
        newList.add(new ArrayList<>());
        newList.add(new ArrayList<>());

        while(listInput.get(0).size()>0) {
            int firstScore = Integer.parseInt(listInput.get(0).get(0));
            int indexScore = 0;

            for (int i = 0; i < listInput.get(0).size(); i++) {
                int currentScore = Integer.parseInt(listInput.get(0).get(i));

                if (currentScore > firstScore) {
                    firstScore = currentScore;
                    indexScore = i;
                }
            }
            newList.get(0).add(listInput.get(0).get(indexScore));
            newList.get(1).add(listInput.get(1).get(indexScore));

            listInput.get(0).remove(indexScore);
            listInput.get(1).remove(indexScore);
        }

        // trim list to only include top 10 scores
        if(newList.get(0).size() > 10){
            for(int i = 10; i < newList.get(0).size(); i++){
                for(int j = 0; j < newList.size(); j++){
                    newList.get(j).remove(i);
                }
            }
        }

        return newList;
    }

    /**
     * Gets the content from a text file.
     * @return  a String with the full text content
     */
    private String getFileContent(){
        try {
            Scanner scanner = new Scanner(new File("C:\\Users\\Bruker\\IdeaProjects\\Snake\\src\\limeSlime\\text\\highScores.txt"));

            String fileContent = "";
            while(scanner.hasNext()) {
                scanner.useDelimiter("\\Z");  // \\Z indicates end of file
                fileContent = scanner.next();
            }
            scanner.close();

            return fileContent;
        } catch(FileNotFoundException e){
            System.err.println("Error, file was not found.");
        }
        return null;
    }

    /**
     * Writes text to a file.
     * @param text  text to be written
     * @param path  the file path
     */
    private void writeToFile(String text,String path){
        File textFile = new File(path);
        try {
            FileWriter fileWriter = new FileWriter(textFile);
            fileWriter.write(text);
            fileWriter.close();
        } catch (IOException ie){
            System.err.println("Error in file handling.");
        }
    }
}