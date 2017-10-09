package limeSlime;

import java.awt.*;

/**
 * Created by Elise Haram Vannes on 08.07.2017.
 */
public class Apple {

    private Point applePosition;

    public Apple(){
        this.applePosition = new Point(0,0);
    }

    Point getApplePosition(){
        return applePosition;
    }

    void setApplePosition(int[][] boardGrid){
        int x = (int)Math.floor(Math.random()*boardGrid.length);
        int y = (int)Math.floor(Math.random()*boardGrid[0].length);

        applePosition.setLocation(x,y);
    }
}