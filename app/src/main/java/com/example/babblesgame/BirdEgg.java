package com.example.babblesgame;

public class BirdEgg extends GameObject {

    BirdEgg(float initialX, float initialY, char type) {
        final float yVelocity = 5;
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
        setWorldLocation(initialX, initialY, 0);
        setyVelocity(yVelocity);
    }

    public void update(long fps, float gravity) {
        move(fps);
        // update the birdeggs hitbox
        setRectHitbox();
    }
}

