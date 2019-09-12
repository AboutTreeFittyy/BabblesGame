/*
Author: Mathew Boland
Course: COMP486
Overview: Creates an object to store the FishTile in the game
Date: September 12th, 2019
FileName: FishTile.java
Code Citation: "Android Game Programming By Example", John Horton, 2015
 */
package com.example.babblesgame;

import android.content.Context;
public class FishTile extends GameObject{
    FishTile(Context context, float worldStartX,
         float worldStartY, char type, int pixelsPerMetre) {
        final int ANIMATION_FPS = 3;
        final int ANIMATION_FRAME_COUNT = 3;
        final String BITMAP_NAME = "fish_tile";
        final float HEIGHT = 1;
        final float WIDTH = 1;
        setHeight(HEIGHT); // 1 metre tall
        setWidth(WIDTH); // 1 metre wide
        setType(type);
        // Now for the player's other attributes
        // Our game engine will use these
        setMoves(false);
        setActive(true);
        setVisible(true);
        // Choose a Bitmap
        setBitmapName(BITMAP_NAME);
        // Set this object up to be animated
        setAnimFps(ANIMATION_FPS);
        setAnimFrameCount(ANIMATION_FRAME_COUNT);
        setBitmapName(BITMAP_NAME);
        setAnimated(context, pixelsPerMetre, true);
        // Where does the tile start
        // X and y locations from constructor parameters
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
    }
    public void update(long fps, float gravity) {
    }
}