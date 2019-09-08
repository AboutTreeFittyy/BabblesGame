package com.example.babblesgame;

import java.util.ArrayList;

public class LevelCave extends LevelData{
    LevelCave() {
        tiles = new ArrayList<>();
        //remember at least one c, u and e must be in each level
        this.tiles.add("...............p..........................................................................................");
        this.tiles.add("..........................................................................................................");
        this.tiles.add("........................................................................................b.................");
        this.tiles.add("..........................................................................................................");
        this.tiles.add("..........................................................................................................");
        this.tiles.add("..........................................................................................................");
        this.tiles.add("..........................................................................................................");
        this.tiles.add(".........................l..............c.................................................................");
        this.tiles.add("2.........................................................................................................");
        this.tiles.add("2.........................................................................................................");
        this.tiles.add("2............................................e.................................g..........................");
        this.tiles.add("2222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222");
        backgroundDataList = new ArrayList<>();
        // note that speeds less than 2 cause problems
        //startY and endY determine where on the coordinate map above the background is placed
        this.backgroundDataList.add(new BackgroundData("forest_background", true, -1, -8, 18, 10, 15 ));
        this.backgroundDataList.add(new BackgroundData("grass", true, 1, 11, 18, 24, 4 ));
    }
}