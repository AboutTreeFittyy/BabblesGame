/*
Author: Mathew Boland
Course: COMP486
Overview: Creates an object to store the FinishLine in the game
Date: September 12th, 2019
FileName: FinishLine.java
Code Citation: "Android Game Programming By Example", John Horton, 2015
 */
package com.example.babblesgame;

public class FinishLine extends GameObject{
    FinishLine(float worldStartX, float worldStartY, char type) {
        final float HEIGHT = 5f;
        final float WIDTH = 5f;
        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);
        // Choose a Bitmap
        setBitmapName("no_dinosaur_sign");
        // Where does the tile start
        // X and y locations from constructor parameters
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitline();
    }
    public void update(long fps, float gravity){}
}
