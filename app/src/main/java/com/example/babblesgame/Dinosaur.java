/*
Author: Mathew Boland
Course: COMP486
FileName: Dinosaur.java
Code Citation: "Android Game Programming By Example", John Horton, 2015
 */
package com.example.babblesgame;

import android.content.Context;
public class Dinosaur extends GameObject {
    final float MAX_X_VELOCITY = 3;

    Dinosaur(Context context, float worldStartX, float worldStartY, char type, int pixelsPerMetre) {
        final int ANIMATION_FPS = 8;
        final int ANIMATION_FRAME_COUNT = 5;
        final String BITMAP_NAME = "dinosaur";
        final float HEIGHT = 26f;
        final float WIDTH = 8;
        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);
        setMoves(true);
        setActive(true);
        setVisible(true);
        // Set this object up to be animated
        setAnimFps(ANIMATION_FPS);
        setAnimFrameCount(ANIMATION_FRAME_COUNT);
        setBitmapName(BITMAP_NAME);
        setAnimated(context, pixelsPerMetre, true);
        setWorldLocation(worldStartX, worldStartY, 0);
        setxVelocity(MAX_X_VELOCITY);
    }

    public void update(long fps, float gravity) {
        // Let's go!
        this.move(fps);
        setRectHitbox();
    }
    public int checkCollisions(RectHitbox rectHitbox) {
        int collided = 0;// No collision
        // The left
        if (this.rectHitbox.intersects(rectHitbox)) {
            collided = 1;
        }
        return collided;
    }
    public void restorePreviousVelocity() {
        setxVelocity(MAX_X_VELOCITY);
    }
}

