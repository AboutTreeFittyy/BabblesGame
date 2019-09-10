/*
Author: Mathew Boland
Course: COMP486
FileName: Fish.java
Code Citation: "Android Game Programming By Example", John Horton, 2015
 */
package com.example.babblesgame;

import android.content.Context;
import android.graphics.PointF;
public class Fish extends GameObject {
    long lastWaypointSetTime;
    PointF currentWaypoint;
    final float MAX_X_VELOCITY = 3;
    final float MAX_Y_VELOCITY = 3;
    Fish(Context context, float worldStartX, float worldStartY, char type, int pixelsPerMetre) {
        final int ANIMATION_FPS = 8;
        final int ANIMATION_FRAME_COUNT = 3;
        final String BITMAP_NAME = "fish_swim";
        final float HEIGHT = 1;
        final float WIDTH = 1;
        setHeight(HEIGHT); // 0.5 metre tall
        setWidth(WIDTH); // 1 metres wide
        setType(type);
        // Now for the player's other attributes
        // Our game engine will use these
        setMoves(true);
        setActive(true);
        setVisible(true);
        currentWaypoint = new PointF();
        // Set this object up to be animated
        setAnimFps(ANIMATION_FPS);
        setAnimFrameCount(ANIMATION_FRAME_COUNT);
        setBitmapName(BITMAP_NAME);
        setAnimated(context, pixelsPerMetre, true);
        setWorldLocation(worldStartX, worldStartY, 0);
        setFacing(LEFT);
    }

    public void update(long fps, float gravity) {
        if (currentWaypoint.x > getWorldLocation().x) {
            setxVelocity(MAX_X_VELOCITY);
            setFacing(RIGHT);
        } else if (currentWaypoint.x < getWorldLocation().x) {
            setxVelocity(-MAX_X_VELOCITY);
            setFacing(LEFT);
        } else {
            setxVelocity(0);
        }
        if (currentWaypoint.y >= getWorldLocation().y) {
            setyVelocity(MAX_Y_VELOCITY);
        } else if (currentWaypoint.y < getWorldLocation().y) {
            setyVelocity(-MAX_Y_VELOCITY);
        } else {
            setyVelocity(0);
        }
        move(fps);
        // update the fish hitbox
        setRectHitbox();
    }
    public void setWaypoint(Vector2Point5D playerLocation) {
        if (System.currentTimeMillis() > lastWaypointSetTime + 2000)
        {//Has 2 seconds passed
            lastWaypointSetTime = System.currentTimeMillis();
            currentWaypoint.x = playerLocation.x;
            currentWaypoint.y = playerLocation.y;
        }
    }
}