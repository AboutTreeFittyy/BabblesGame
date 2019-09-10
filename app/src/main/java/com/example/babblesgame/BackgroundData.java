/*
Author: Mathew Boland
Course: COMP486
FileName: BackgroundData.java
Code Citation: "Android Game Programming By Example", John Horton, 2015
 */
package com.example.babblesgame;

public class BackgroundData {
    String bitmapName;
    boolean isParallax;
    //layer 0 is the map
    int layer;
    float startY;
    float endY;
    float speed;
    int height;
    int width;
    BackgroundData(String bitmap, boolean isParallax, int layer, float startY, float endY, float speed, int height){
        this.bitmapName = bitmap;
        this.isParallax = isParallax;
        this.layer = layer;
        this.startY = startY;
        this.endY = endY;
        this.speed = speed;
        this.height = height;
    }
}
