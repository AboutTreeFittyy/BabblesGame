package com.example.babblesgame;

import java.util.ArrayList;

public class LevelCave extends LevelData{
    LevelCave() {
        tiles = new ArrayList<String>();
        //remember at least one c, u and e must be in each level
        this.tiles.add("p.............................................");
        this.tiles.add("..............................................");
        this.tiles.add("..................c...........................");
        this.tiles.add("..............................................");
        this.tiles.add(".............................u................");
        this.tiles.add("..............................................");
        this.tiles.add("......................................e.......");
        this.tiles.add("..............................................");
        this.tiles.add("..............................................");
        this.tiles.add("..............................................");
        this.tiles.add("..............................................");
        this.tiles.add("222ff22222222222222222222222222222222222222222");
        backgroundDataList = new ArrayList<BackgroundData>();
        // note that speeds less than 2 cause problems
        //startY and endY determine where on the coordinate map above the background is placed
        this.backgroundDataList.add(new BackgroundData("skyline", true, -1, -8, 18, 10, 15 ));
        this.backgroundDataList.add(new BackgroundData("grass", true, 1, 11, 18, 24, 4 ));
    }
}