/*
Author: Mathew Boland
Course: COMP486
Overview: Creates an object to hold the levels data and backgrounds/foregrounds. Details
for each character and its object are written in the notes.
Date: September 12th, 2019
FileName: LevelData.java
Code Citation: "Android Game Programming By Example", John Horton, 2015
 */
package com.example.babblesgame;

import java.util.ArrayList;

public class LevelData {
    // Tile types
    // . = no tile
    // 1 = grass
    // 2 = Snow
    // 3 = Scorched
    // 4 = Stone
    //Active objects
    // a = fish
    // b = bird
    // c = fly
    // d = dinosaur
    // e = power up
    // f = fish_tile
    // g = turtle
    // l = level_finish
    // p = player
    // q = bird egg
    // u = under water turtle
    ArrayList<String> tiles;
    ArrayList<BackgroundData> backgroundDataList;
}