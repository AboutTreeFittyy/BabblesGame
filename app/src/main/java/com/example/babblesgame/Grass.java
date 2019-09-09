package com.example.babblesgame;

public class Grass extends GameObject {
    Grass(float worldStartX, float worldStartY, char type) {
        setTraversable();
        final float HEIGHT = 1;
        final float WIDTH = 1;
        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);
        setBitmapName("grass_block");
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
    }
    public void update(long fps, float gravity) {
    }
}