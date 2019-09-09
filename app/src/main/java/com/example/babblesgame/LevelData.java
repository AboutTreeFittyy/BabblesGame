package com.example.babblesgame;

import java.util.ArrayList;

public class LevelData {
    // Tile types
    // . = no tile
    // 2 = Snow
    // 6 = Scorched
    // 7 = Stone
    //Active objects
    // g = turtle
    // b = bird
    // a = fish
    // d = dinosaur
    // t = teleport
    // c = fly
    // f = fire
    // e = power up
    // q = bird egg
    //Inactive objects
    // w = tree
    // x = tree2 (snowy)
    // r = stalactite
    // s = stalagmite
    // m = mine cart
    // z = boulders
    ArrayList<String> tiles;
    ArrayList<BackgroundData> backgroundDataList;
    ArrayList<Location> locations;
}