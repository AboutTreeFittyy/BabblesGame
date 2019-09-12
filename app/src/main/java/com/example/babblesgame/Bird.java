/*
Author: Mathew Boland
Course: COMP486
Overview: Creates an object to store the Bird in the game
Date: September 12th, 2019
FileName: Bird.java
Code Citation: "Android Game Programming By Example", John Horton, 2015
 */
package com.example.babblesgame;

import android.content.Context;

public class Bird extends GameObject {
    // Birds move on x-axis from right side to left
    private int currentBlock;
    private int units;

    Bird(Context context, float worldStartX, float worldStartY, char type, int pixelsPerMetre) {
        final int ANIMATION_FPS = 8;
        final int ANIMATION_FRAME_COUNT = 5;
        final String BITMAP_NAME = "bird_fly_sprite";
        final float HEIGHT = 2f;
        final float WIDTH = 4;
        final float X_VELOCITY = 4;
        units = 100; //units moved before dropping egg
        currentBlock = 0;
        setHeight(HEIGHT); // 1 metre tall
        setWidth(WIDTH); // 2 metres wide
        setType(type);
        setMoves(true);
        setActive(true);
        setVisible(true);
        // Set this object up to be animated
        setAnimFps(ANIMATION_FPS);
        setAnimFrameCount(ANIMATION_FRAME_COUNT);
        setBitmapName(BITMAP_NAME);
        setAnimated(context, pixelsPerMetre, true);
        // Where does the tile start
        // X and y locations from constructor parameters
        setWorldLocation(worldStartX, worldStartY, 0);
        setxVelocity(-X_VELOCITY);
    }

    public void update(long fps, float gravity) {
        currentBlock++;
        // drop an egg every x units moved
        if(currentBlock % units == 0){
            setObject(true);
        }
        move(fps);
        // update the birds hitbox
        setRectHitbox();
    }
}
