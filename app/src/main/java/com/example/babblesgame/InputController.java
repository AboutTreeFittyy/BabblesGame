package com.example.babblesgame;

import android.graphics.Rect;
import android.view.MotionEvent;
import java.util.ArrayList;
public class InputController {
    Rect left;
    Rect right;
    Rect menu;
    Rect pause;
    InputController(int screenWidth, int screenHeight) {
        //Configure the player buttons
        int buttonWidth = screenWidth / 4;
        int buttonHeight = screenHeight / 7;
        int buttonPadding = screenWidth / 80;
        pause = new Rect(screenWidth - buttonPadding - buttonWidth, buttonPadding, screenWidth - buttonPadding, buttonPadding + buttonHeight);
        left = new Rect(buttonPadding, screenHeight - buttonHeight - buttonPadding, buttonWidth, screenHeight - buttonPadding);
        //Note move rect below this to right side after done dev so it works better on phones than my pc
        right = new Rect(buttonWidth + buttonPadding, screenHeight - buttonHeight - buttonPadding, buttonWidth + buttonPadding + buttonWidth, screenHeight - buttonPadding);
        menu = new Rect(screenWidth - buttonWidth - buttonPadding, screenHeight - buttonHeight - buttonPadding - buttonHeight - buttonPadding, screenWidth - buttonPadding, screenHeight - buttonPadding - buttonHeight - buttonPadding);

    }

    public ArrayList getButtons(){
        //create an array of buttons for the draw method
        ArrayList<Rect> currentButtonList = new ArrayList<>();
        currentButtonList.add(pause);
        currentButtonList.add(left);
        currentButtonList.add(right);
        currentButtonList.add(menu);

        return currentButtonList;
    }

    public void handleInput(MotionEvent motionEvent,LevelManager l, SoundManager sound, Viewport vp){
        int pointerCount = motionEvent.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            int x = (int) motionEvent.getX(i);
            int y = (int) motionEvent.getY(i);
            if(l.isPlaying()) {
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        if (right.contains(x, y)) {
                            l.player.setPressingRight(true);
                            l.player.setPressingLeft(false);
                        } else if (left.contains(x, y)) {
                            l.player.setPressingLeft(true);
                            l.player.setPressingRight(false);
                        } /*else if (jump.contains(x, y)) {
                            l.player.startJump(sound);
                        }*/ else if (pause.contains(x, y)) {
                            l.switchPlayingStatus();
                        }
                        else{
                            l.player.startJump(sound);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (right.contains(x, y)) {
                            l.player.setPressingRight(false);
                        } else if (left.contains(x, y)) {
                            l.player.setPressingLeft(false);
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        if (right.contains(x, y)) {
                            l.player.setPressingRight(true);
                            l.player.setPressingLeft(false);
                        } else if (left.contains(x, y)) {
                            l.player.setPressingLeft(true);
                            l.player.setPressingRight(false);
                        }/* else if (jump.contains(x, y)) {
                            l.player.startJump(sound);
                        } */else if (pause.contains(x, y)) {
                            l.switchPlayingStatus();
                        }
                        else{
                            l.player.startJump(sound);
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        if (right.contains(x, y)) {
                            l.player.setPressingRight(false);
                            //Log.w("rightP:", "up" );
                        } else if (left.contains(x, y)) {
                            l.player.setPressingLeft(false);
                            //Log.w("leftP:", "up" );
                        }/* else if (jump.contains(x, y)) {
                            //Handle more jumping stuff here later
                        }*/
                        break;
                }// End if(l.playing)
            }else {// Not playing
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        if (pause.contains(x, y)) {
                            l.switchPlayingStatus();
                        }
                        break;
                }
            }
        }
    }
}