/*
Author: Mathew Boland
Course: COMP486
FileName: Animation.java
Code Citation: "Android Game Programming By Example", John Horton, 2015
 */
package com.example.babblesgame;

import android.graphics.Rect;
class Animation {
    private Rect sourceRect;
    private int frameCount;
    private int currentFrame;
    private long frameTicker;
    private int framePeriod;
    private int frameWidth;
    private boolean idle;

    Animation(float frameHeight, float frameWidth, int animFps, int frameCount, int pixelsPerMetre, boolean idle){
        this.currentFrame = 0;
        this.frameCount = frameCount;
        this.frameWidth = (int)frameWidth * pixelsPerMetre;
        this.idle = idle;
        sourceRect = new Rect(0, 0, this.frameWidth, (int)frameHeight * pixelsPerMetre);
        framePeriod = 1000 / animFps;
        frameTicker = 1;
    }

    Rect getCurrentFrame(long time, float xVelocity, float yVelocity, boolean moves){
        if(xVelocity == 0 && idle){
            currentFrame=1; // If its the player stopping make sure he isnt on the hop frame while still
        }
        if(yVelocity == -2|| xVelocity!=0 || !moves) {
            // Only animate if the object is moving or it is an object which doesn't move but is still animated (like fire)
            if (time > frameTicker + framePeriod) {
                frameTicker = time;
                currentFrame++;
                if (currentFrame >= frameCount) {
                    currentFrame = 0;
                }
            }
        }
        //update the left and right values of the source of the next frame on the spritesheet
        this.sourceRect.left = currentFrame * frameWidth;
        this.sourceRect.right = this.sourceRect.left + frameWidth;
        return sourceRect;
    }
}