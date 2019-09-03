package com.example.babblesgame;

import android.graphics.Rect;
import android.view.MotionEvent;
import java.util.ArrayList;
public class MenuController {
    Rect forest;
    Rect water;
    Rect cave;

    MenuController(int screenWidth, int screenHeight) {
        //Configure the player buttons
        int buttonWidth = screenWidth / 8;
        int buttonHeight = screenHeight / 7;
        int buttonPadding = screenWidth / 80;
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

    public void handleInput(MotionEvent motionEvent,LevelManager l, SoundManager sound, Viewport vp){
        int pointerCount = motionEvent.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            int x = (int) motionEvent.getX(i);
            int y = (int) motionEvent.getY(i);
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    /*case MotionEvent.ACTION_DOWN:
                        if (right.contains(x, y)) {
                            l.player.setPressingRight(true);
                            l.player.setPressingLeft(false);
                        } else if (left.contains(x, y)) {
                            l.player.setPressingLeft(true);
                            l.player.setPressingRight(false);
                        } else if (jump.contains(x, y)) {
                            l.player.startJump(sound);
                        } else if (pause.contains(x, y)) {
                            l.switchPlayingStatus();
                        }
                        break;*/
                    case MotionEvent.ACTION_UP:
                        if (forest.contains(x, y)) {
                            //start forest level

                        } else if (water.contains(x, y)) {
                            //start water level

                        } else if (cave.contains(x, y)) {
                            //start cave level

                        }
                        break;
                    /*case MotionEvent.ACTION_POINTER_DOWN:
                        if (right.contains(x, y)) {
                            l.player.setPressingRight(true);
                            l.player.setPressingLeft(false);
                        } else if (left.contains(x, y)) {
                            l.player.setPressingLeft(true);
                            l.player.setPressingRight(false);
                        } else if (jump.contains(x, y)) {
                            l.player.startJump(sound);
                        } else if (pause.contains(x, y)) {
                            l.switchPlayingStatus();
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        if (right.contains(x, y)) {
                            l.player.setPressingRight(false);
                            //Log.w("rightP:", "up" );
                        } else if (left.contains(x, y)) {
                            l.player.setPressingLeft(false);
                            //Log.w("leftP:", "up" );
                        } else if (jump.contains(x, y)) {
                            //Handle more jumping stuff here later
                        }
                        break;*/
            }
        }
    }
}