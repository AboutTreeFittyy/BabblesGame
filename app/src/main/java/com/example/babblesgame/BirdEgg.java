package com.example.babblesgame;

import android.content.Context;

public class BirdEgg extends GameObject {
    private float yVelocity = 5;

    BirdEgg(Context context, float initialX, float initialY, char type, int pixelsPerMetre) {
        final String BITMAP_NAME = "bird_egg";
        final float HEIGHT = 2f;
        final float WIDTH = 1;
        setHeight(HEIGHT); // 2 metre tall
        setWidth(WIDTH); // 1 metres wide
        setType(type);
        setMoves(true);
        setActive(true);
        setVisible(true);
        setBitmapName(BITMAP_NAME);
        //setAnimated(context, pixelsPerMetre, true);
        setWorldLocation(initialX, initialY, 0);
        setyVelocity(yVelocity);
    }

    public void update(long fps, float gravity) {
        move(fps);
        // update the birdeggs hitbox
        setRectHitbox();
    }
}

