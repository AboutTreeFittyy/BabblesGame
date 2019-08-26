package com.example.babblesgame;

import android.graphics.PointF;
public class PlayerState {
    private int numCredits;
    private int lives;
    private float restartX;
    private float restartY;
    PlayerState() {
        lives = 3;
        numCredits = 0;
    }

    public void saveLocation(PointF location) {
        // The location saves each time the player uses a teleport
        restartX = location.x;
        restartY = location.y;
    }
    public PointF loadLocation() {
        // Used every time the player loses a life
        return new PointF(restartX, restartY);
    }

    public int getLives(){
        return lives;
    }
    public void gotCredit(){
        numCredits ++;
    }
    public int getCredits(){
        return numCredits;
    }
    public void loseLife(){
        lives--;
    }
    public void addLife(){
        lives++;
    }
}