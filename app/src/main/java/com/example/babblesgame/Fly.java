package com.example.babblesgame;

public class Fly extends GameObject{
    Fly(float worldStartX, float worldStartY, char type) {
        final float HEIGHT = 1f;
        final float WIDTH = 1f;
        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);
        // Choose a Bitmap
        setBitmapName("fly");
        // Where does the tile start
        // X and y locations from constructor parameters
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
    }
    public void update(long fps, float gravity){}
}
