/*
Author: Mathew Boland
Course: COMP486
Overview: Creates an object to store the PowerUp in the game
FileName: PowerUp.java
Code Citation: "Android Game Programming By Example", John Horton, 2015
 */
package com.example.babblesgame;

public class PowerUp extends GameObject{
    PowerUp(float worldStartX, float worldStartY, char type) {
        final float HEIGHT = .8f;
        final float WIDTH = .65f;
        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);
        // Choose a Bitmap
        setBitmapName("power_up");
        // Where does the tile start
        // X and y locations from constructor parameters
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
    }
    public void update(long fps, float gravity){}
}