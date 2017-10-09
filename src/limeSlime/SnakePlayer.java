package limeSlime;

import java.util.*;

/**
 * Created by Elise Haram Vannes on 14.06.2017.
 */
public class SnakePlayer {

    private int length = 7;
    private int life = 3;
    private int direction = 4; // 1 2 3 4 clockwise, represents direction limeSlime is facing, 4 is left
    private int boardWidth;
    private int boardHeight;

    private String snakeColor = "#2ADE16";
    private int[][] snakeGrid;
    private List<List<Integer>> snakePositions = new ArrayList<List<Integer>>(2);

    public SnakePlayer(int boardWidth,int boardHeight){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        snakeGrid = new int[boardHeight][boardWidth];
        initSnakePositions();
        changeSnakeGrid();
    }

    /**
     * Initializes the 2D-List containing the snakes defualt position in the board.
     */
    void initSnakePositions(){
        snakePositions.add(new ArrayList<Integer>());
        snakePositions.add(new ArrayList<Integer>());

        for(int i = 17; i < 24; i++){
            snakePositions.get(0).add(12); // x-positions of limeSlime - rows
            snakePositions.get(1).add(i); // y-positions of limeSlime - columns
        }
    }

    /**
     * Changes the gameBoard to contain the current snakes position from the snakePositions-List.
     */
    private void changeSnakeGrid(){
        for(int j = 0; j < snakePositions.get(0).size(); j++){
            int x = snakePositions.get(0).get(j);
            int y = snakePositions.get(1).get(j);
            snakeGrid[x][y] = 1; // the number 1 defines the limeSlime
        }
    }

    /**
     * Moves the snake by updating its values in the snakePositions-List. Checks if it's possible
     * for the snake to move in the direction it's trying to, or if it dies. Updates the size of the snake, and
     * removes the last position while adding the newest position in the grid.
     * @param boardGrid
     * @return      true if the game is still playing
     */
    boolean moveSnake(int[][] boardGrid){
        int x = snakePositions.get(0).get(0);
        int y = snakePositions.get(1).get(0);

        switch (direction) {
            case 1:
                if (x-1 >= 0 && isInBounds(x - 1, y) && boardGrid[x-1][y] != 1) {
                    // Getting the tails ends position:
                    int endX = snakePositions.get(0).get(snakePositions.get(0).size() - 1);
                    int endY = snakePositions.get(1).get(snakePositions.get(1).size() - 1);

                    snakeGrid[x - 1][y] = 1;
                    snakePositions.get(0).add(0, x - 1);
                    snakePositions.get(1).add(0, y);

                    if(endX < 0 || endY < 0 || endX >= snakeGrid.length || endY >= snakeGrid[0].length){
                        for(int i = 0; i < snakePositions.size(); i++){
                            for(int j = 0; j < snakePositions.get(0).size(); j++){
                                System.out.print("Snakepositions: " + snakePositions.get(i).get(j) + " ");
                            }
                            System.out.println();
                            System.out.println("Case 1, endX: " + endX);
                            System.out.println("Case 1, endY: " + endY);
                        }
                    } else {
                        snakeGrid[endX][endY] = 0;
                    }
                    // Getting last item in lists: list.get(list.size() - 1)
                    snakePositions.get(0).remove(snakePositions.get(0).size() - 1);
                    snakePositions.get(1).remove(snakePositions.get(1).size() - 1);

                    return true;
                } else {
                    System.out.println("Game is over :(");
                    return false;
                }
            case 2:
                if (y+1 < snakeGrid[0].length && isInBounds(x, y + 1) && boardGrid[x][y+1] != 1) {
                    // Getting the tails ends position:
                    int endX = snakePositions.get(0).get(snakePositions.get(0).size() - 1);
                    int endY = snakePositions.get(1).get(snakePositions.get(1).size() - 1);

                    snakeGrid[x][y + 1] = 1;
                    snakePositions.get(0).add(0, x);
                    snakePositions.get(1).add(0, y + 1);

                    if(endX < 0 || endY < 0 || endX >= snakeGrid.length || endY >= snakeGrid[0].length){
                        for(int i = 0; i < snakePositions.size(); i++){
                            for(int j = 0; j < snakePositions.get(0).size(); j++){
                                System.out.print("Snakepositions: " + snakePositions.get(i).get(j) + " ");
                            }
                            System.out.println();
                        }
                        System.out.println("Case 2, endX: " + endX);
                        System.out.println("Case 2, endY: " + endY);
                    } else {
                        snakeGrid[endX][endY] = 0;
                    }
                    // Removes last item in the arraylist, the end of the limeSlime
                    snakePositions.get(0).remove(snakePositions.get(0).size() - 1);
                    snakePositions.get(1).remove(snakePositions.get(1).size() - 1);

                    return true;
                } else {
                    System.out.println("Game is over :(");
                    return false;
                }
            case 3:
                if (x+1 < snakeGrid.length && isInBounds(x + 1, y) && boardGrid[x+1][y] != 1) {
                    // Getting the tails ends position:
                    int endX = snakePositions.get(0).get(snakePositions.get(0).size() - 1);
                    int endY = snakePositions.get(1).get(snakePositions.get(1).size() - 1);

                    snakeGrid[x + 1][y] = 1;
                    snakePositions.get(0).add(0, x + 1);
                    snakePositions.get(1).add(0, y);


                    if(endX < 0 || endY < 0 || endX >= snakeGrid.length || endY >= snakeGrid[0].length){
                        for(int i = 0; i < snakePositions.size(); i++){
                            for(int j = 0; j < snakePositions.get(0).size(); j++){
                                System.out.print("Snakepositions: " + snakePositions.get(i).get(j) + " ");
                            }
                            System.out.println();
                            System.out.println("Case 3, endX: " + endX);
                            System.out.println("Case 3, endY: " + endY);
                        }
                    } else {
                        snakeGrid[endX][endY] = 0;
                    }

                    snakePositions.get(0).remove(snakePositions.get(0).size() - 1);
                    snakePositions.get(1).remove(snakePositions.get(1).size() - 1);

                    return true;
                } else {
                    System.out.println("Game is over :(");
                    return false;
                }
            case 4:
                if (y-1 >= 0 && isInBounds(x, y - 1) && boardGrid[x][y-1] != 1) {
                    // Getting the tails ends position:
                    int endX = snakePositions.get(0).get(snakePositions.get(0).size() - 1);
                    int endY = snakePositions.get(1).get(snakePositions.get(1).size() - 1);

                    snakeGrid[x][y - 1] = 1;
                    snakePositions.get(0).add(0, x);
                    snakePositions.get(1).add(0, y - 1);

                    if(endX < 0 || endY < 0 || endX >= snakeGrid.length || endY >= snakeGrid[0].length){
                        for(int i = 0; i < snakePositions.size(); i++){
                            for(int j = 0; j < snakePositions.get(0).size(); j++){
                                System.out.print("Snakepositions: " + snakePositions.get(i).get(j) + " ");
                            }
                            System.out.println();
                            System.out.println("Case 4, endX: " + endX);
                            System.out.println("Case 4, endY: " + endY);
                        }
                    } else {
                        snakeGrid[endX][endY] = 0;
                    }

                    snakePositions.get(0).remove(snakePositions.get(0).size() - 1);
                    snakePositions.get(1).remove(snakePositions.get(1).size() - 1);

                    return true;
                } else {
                    System.out.println("Game is over :(");
                    return false;
                }
            default:
                break;
        }
        return true;
    }

    /**
     * Adds to the length of the snake.
     */
    void addSnakeLength(){
        int snakeAlmostEndX = getSnakeAlmostEndX();
        int snakeAlmostEndY = getSnakeAlmostEndY();

        if(snakeAlmostEndX < getSnakeEndX()){
            snakePositions.get(0).add(getSnakeEndX()+1);
            snakePositions.get(1).add(getSnakeEndY());
        } else if(snakeAlmostEndX > getSnakeEndX()){
            snakePositions.get(0).add(getSnakeEndX()-1);
            snakePositions.get(1).add(getSnakeEndY());
        } else if(snakeAlmostEndY < getSnakeEndY()){
            snakePositions.get(0).add(getSnakeEndX());
            snakePositions.get(1).add(getSnakeEndY()+1);
        } else if(snakeAlmostEndY > getSnakeEndY()){
            snakePositions.get(0).add(getSnakeEndX());
            snakePositions.get(1).add(getSnakeEndY()-1);
        }
    }

    /**
     * Changes the snakes direction to upwards.
     */
    void moveUp(){
        if(direction != 3 && snakePositions.get(0).get(1) >= snakePositions.get(0).get(0)){
            direction = 1;
        }
    }

    /**
     * Changes the snakes direction to right.
     */
    void moveRight(){
        if(direction != 4 && snakePositions.get(1).get(1) <= snakePositions.get(1).get(0)){
            direction = 2;
        }
    }

    /**
     * Changes the snakes direction to downwards.
     */
    void moveDown(){
        if(direction != 1 && snakePositions.get(0).get(1) <= snakePositions.get(0).get(0)){
            direction = 3;
        }
    }

    /**
     * Changes the snakes direction to left.
     */
    void moveLeft(){
        if(direction != 2 && snakePositions.get(1).get(1) >= snakePositions.get(1).get(0)){
            direction = 4;
        }
    }

    /**
     * Checks if an x and y value is within the bounds of the snakeGrid-array.
     * @param x the x value in the array
     * @param y the y value in the array
     * @return  true if the values were within the bounds of the snakeGrid-array
     */
    boolean isInBounds(int x, int y){
        return !(x < 0 || x >= snakeGrid.length || y < 0 || y >= snakeGrid[0].length);
    }

    /**
     * Clears the snakeGrid-array.
     */
    public void clearSnakeGrid(){
        for(int i = 0; i < snakeGrid.length; i++){
            for(int j = 0; j < snakeGrid[0].length; j++){
                snakeGrid[i][j] = 0;
            }
        }
    }

    /**
     * Resets the snake to its original size, direction, and position.
     */
    public void resetSnake(){
        clearSnakeGrid();
        snakePositions.clear();
        direction = 4;
        initSnakePositions();
        changeSnakeGrid();
    }

    int[][] getSnakeGrid(){
        return snakeGrid;
    }

    int getSnakeHeadX(){
        return snakePositions.get(0).get(0);
    }

    int getSnakeHeadY(){
        return snakePositions.get(1).get(0);
    }

    int getSnakeAlmostEndX(){
        return snakePositions.get(0).get(snakePositions.get(0).size()-2);
    }

    int getSnakeAlmostEndY(){
        return snakePositions.get(1).get(snakePositions.get(1).size()-2);
    }

    int getSnakeEndX(){
        return snakePositions.get(0).get(snakePositions.get(0).size()-1);
    }

    int getSnakeEndY(){
        return snakePositions.get(1).get(snakePositions.get(1).size()-1);
    }

    int getDirection(){
        return direction;
    }

    void setDirection(int direction){
        this.direction = direction;
    }

    public int getLength(){
        return length;
    }

    public void setLength(int newLength){
        this.length = newLength;
    }

    public int getLife(){
        return life;
    }

    public void setLife(int newLife){
        this.life = newLife;
    }

    String getSnakeColor(){
        return snakeColor;
    }
}