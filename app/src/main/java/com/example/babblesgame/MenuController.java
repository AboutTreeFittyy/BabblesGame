/*
Author: Mathew Boland
Course: COMP486
FileName: MenuController.java
Code Citation: "Android Game Programming By Example", John Horton, 2015
 */
package com.example.babblesgame;

import android.graphics.Rect;
import android.view.MotionEvent;
import java.util.ArrayList;
public class MenuController {
    Rect forest;
    Rect water;
    Rect cave;
    String level;
    public boolean selected;

    MenuController(int screenWidth, int screenHeight) {
        level = "";
        selected = false;
        //Configure the player buttons
        int buttonWidth = screenWidth / 6;
        int buttonHeight = screenHeight / 7;
        int buttonPadding = screenWidth / 60;
        int bottom = screenHeight/2;
        int top = bottom - buttonHeight;
        int centerLeft = screenWidth/2 - buttonWidth/2;
        forest = new Rect(centerLeft - buttonPadding - buttonWidth, top, centerLeft - buttonPadding, bottom);
        water = new Rect(centerLeft, top, centerLeft + buttonWidth, bottom);
        cave = new Rect(centerLeft + buttonPadding + buttonWidth, top, centerLeft + buttonPadding +buttonWidth +buttonWidth, bottom);
    }

    public ArrayList getButtons(){
        //create an array of buttons for the draw method
        ArrayList<Rect> currentButtonList = new ArrayList<>();
        currentButtonList.add(forest);
        currentButtonList.add(water);
        currentButtonList.add(cave);
        return currentButtonList;
    }

    public String getLevel(){
        return level;
    }

    public void handleInput(MotionEvent motionEvent, String data){
        int pointerCount = motionEvent.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            int x = (int) motionEvent.getX(i);
            int y = (int) motionEvent.getY(i);
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        if (forest.contains(x, y)) {
                            //start forest level
                            level = "LevelForest";
                            selected = true;
                        } else if (water.contains(x, y)) {
                            //start water level
                            level = "LevelWater";
                            selected = true;
                        } else if (cave.contains(x, y) && data.equals("3")) {
                            //start cave level only when other two are beaten
                            level = "LevelCave";
                            selected = true;
                        }
                        break;
            }
        }
    }
}