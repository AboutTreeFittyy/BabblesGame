/*
Author: Mathew Boland
Course: COMP486
FileName: PlayerState.java
Code Citation: "Android Game Programming By Example", John Horton, 2015
 */
package com.example.babblesgame;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.PointF;
public class PlayerState {
    private int numCredits;
    private float restartX;
    private float restartY;
    boolean isPoweredUp, resetSize;
    Timer pow;
    PlayerState() {
        isPoweredUp = false;
        resetSize = false;
        numCredits = 0;
        pow = new Timer();
    }

    public void saveLocation(PointF location) {
        // The location saves each time the player uses a teleport
        restartX = location.x;
        restartY = location.y;
    }

    public void gotCredit(){
        numCredits ++;
    }
    public int getCredits(){
        return numCredits;
    }


    public void startPowerUp(){
        isPoweredUp = true;
        pow.schedule(new EndPowerUp(), 10000);
    }

    public boolean isPoweredUp(){
        return isPoweredUp;
    }

    //Returns a boolean indicating true if the size
    //of babbles should be reset after a power up ended
    public boolean resetSize(){
        return resetSize;
    }

    //Tell program that the size ahs been reset and doesn't
    //need to be reset again
    public void sizeReset(){
        resetSize = false;
    }

    class EndPowerUp extends TimerTask {
        @Override
        public void run() {
            isPoweredUp = false;
            resetSize = true;
            pow.cancel();
        }
    }
}

