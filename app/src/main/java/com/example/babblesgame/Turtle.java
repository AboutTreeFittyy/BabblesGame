package com.example.babblesgame;

import android.content.Context;
public class Turtle extends GameObject {
    // Guards just move on x axis between 2 waypoints
    private float waypointX1;// always on left
    private float waypointX2;// always on right
    private int currentWaypoint;
    final float MAX_X_VELOCITY = 3;

    Turtle(Context context, float worldStartX, float worldStartY, char type, int pixelsPerMetre) {
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
        setxVelocity(-MAX_X_VELOCITY);
        currentWaypoint = 1;
    }

    public void setWaypoints(float x1, float x2){
        waypointX1 = x1;
        waypointX2 = x2;
    }

    public void update(long fps, float gravity) {
        if(currentWaypoint == 1) {// Heading left
            if (getWorldLocation().x <= waypointX1) {
                // Arrived at waypoint 1
                currentWaypoint = 2;
                setxVelocity(MAX_X_VELOCITY);
                setFacing(RIGHT);
            }
        }
        if(currentWaypoint == 2){
            if (getWorldLocation().x >= waypointX2) {
                // Arrived at waypoint 2
                currentWaypoint = 1;
                setxVelocity(-MAX_X_VELOCITY);
                setFacing(LEFT);
            }
        }
        move(fps);
        // update the guards hitbox
        setRectHitbox();
    }
}// End Guard class
