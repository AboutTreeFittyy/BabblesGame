package com.example.babblesgame;

public class PowerUp extends GameObject{
    PowerUp(float worldStartX, float worldStartY, char type) {
        final float HEIGHT = .8f;
        final float WIDTH = .65f;
        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);
        // Choose a Bitmap
        setBitmapName("power_up");
        // Where does the tile start
        // X and y locations from constructor parameters
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
    }
    public void update(long fps, float gravity){}
}