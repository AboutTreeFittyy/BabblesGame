/*
Author: Mathew Boland
Course: COMP486
FileName: UnderWaterTurtle.java
Code Citation: "Android Game Programming By Example", John Horton, 2015
 */
package com.example.babblesgame;

import android.content.Context;
public class UnderWaterTurtle extends GameObject {
    // Turtles just move on y axis between 2 waypoints
    private float waypointY1;// always on left
    private float waypointY2;// always on right
    private int currentWaypoint;
    final float MAX_Y_VELOCITY = 2;

    UnderWaterTurtle(Context context, float worldStartX, float worldStartY, char type, int pixelsPerMetre) {
        final int ANIMATION_FPS = 8;
        final int ANIMATION_FRAME_COUNT = 3;
        final String BITMAP_NAME = "turtle_walk_sprite";
        final float HEIGHT = 1f;
        final float WIDTH = 2;
        setHeight(HEIGHT); // 1 metre tall
        setWidth(WIDTH); // 2 metres wide
        setType(type);
        // Now for the player's other attributes
        // Our game engine will use these
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
        setyVelocity(-MAX_Y_VELOCITY);
        currentWaypoint = 2;
        setFacing(LEFT);

        waypointY1 = getWorldLocation().y;
        waypointY2 = waypointY1 - 3;
    }

    public void update(long fps, float gravity) {
        if(currentWaypoint == 1) {// Heading up
            if (getWorldLocation().y >= waypointY1) {
                // Arrived at waypoint 1
                currentWaypoint = 2;
                setyVelocity(-MAX_Y_VELOCITY);
            }
        }
        if(currentWaypoint == 2){
            if (getWorldLocation().y <= waypointY2) {
                // Arrived at waypoint 2
                currentWaypoint = 1;
                setyVelocity(MAX_Y_VELOCITY);
            }
        }
        move(fps);
        // update the turtles hitbox
        setRectHitbox();
    }
}

