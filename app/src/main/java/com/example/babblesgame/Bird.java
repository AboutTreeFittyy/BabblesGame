package com.example.babblesgame;

import android.content.Context;

import java.util.concurrent.CopyOnWriteArrayList;

public class Bird extends GameObject {
    // Birds move on x-axis from right side to left
    private int currentBlock;
    private int numEggs;
    final float MAX_X_VELOCITY = 3;
    private CopyOnWriteArrayList<BirdEgg> eggs;

    Bird(Context context, float worldStartX, float worldStartY, char type, int pixelsPerMetre) {
        final int ANIMATION_FPS = 8;
        final int ANIMATION_FRAME_COUNT = 5;
        final String BITMAP_NAME = "bird_fly_sprite";
        final float HEIGHT = 2f;
        final float WIDTH = 4;
        currentBlock = 0;
        numEggs = 0;
        eggs = new CopyOnWriteArrayList<BirdEgg>();
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
    }

    public void update(long fps, float gravity) {
        currentBlock++;
        // drop an egg every fifth unit moved
        if(currentBlock % 200 == 0){
            /*eggs.add(numEggs, new BirdEgg(getWorldLocation().x, getWorldLocation().y, -10, 1));
            numEggs++;*/
            setObject(true);
        }
        /*for(BirdEgg bEggs: eggs){
            bEggs.update(fps, gravity);
        }*/
        move(fps);
        // update the birds hitbox
        setRectHitbox();
    }
}
