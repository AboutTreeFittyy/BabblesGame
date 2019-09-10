/*
Author: Mathew Boland
Course: COMP486
FileName: Location.java
Code Citation: "Android Game Programming By Example", John Horton, 2015
 */
package com.example.babblesgame;

public class Location {
    String level;
    float x;
    float y;
    Location(String level, float x, float y){
        this.level = level;
        this.x = x;
        this.y = y;
    }
}