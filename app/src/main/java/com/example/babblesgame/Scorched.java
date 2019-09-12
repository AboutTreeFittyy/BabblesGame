/*
Author: Mathew Boland
Course: COMP486
Overview: Creates an object to store a scorched tile in the game
Date: September 12th, 2019
FileName: Scorched.java
Code Citation: "Android Game Programming By Example", John Horton, 2015
 */
package com.example.babblesgame;

public class Scorched extends GameObject {
    Scorched(float worldStartX, float worldStartY, char type) {
        setTraversable();
        final float HEIGHT = 1;
        final float WIDTH = 1;
        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);
        setBitmapName("scorched");
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
    }
    public void update(long fps, float gravity) {
    }
}