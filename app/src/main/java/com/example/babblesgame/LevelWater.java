package com.example.babblesgame;

import java.util.ArrayList;

public class LevelWater extends LevelData{
    LevelWater() {
        tiles = new ArrayList<>();
        //remember at least one c, p and d must be in each level
        // Also starting the player off too low minimizes how high one can jump at so keep it at the top
        this.tiles.add("..............................................................................................................................7777.................................................................");
        this.tiles.add("..............................................................................................................................7777.................................................................");
        this.tiles.add("..............................................................................................................................7777.................................................................");
        this.tiles.add("d..........p.................................................................................e................................7777.................................................................");
        this.tiles.add("...........................................................................................77777.................................77.c.................................................a............");
        this.tiles.add("...............................................................................................................................c.77777777..........................................................");
        this.tiles.add(".............................................................................................................................u7777777777777777777777777............................................");
        this.tiles.add("...............................................................................................................................................77777777.......................................l....");
        this.tiles.add("..........777777...............a...........77777777777777.......................c....................................7777......................777777777777777777777...............................");
        this.tiles.add("..........7777777777.......................7.......................................................7777777...........7777..........................................................................");
        this.tiles.add("..........77777777777777.................u.7.c...............................u....u................7777777...........7777..........................................................................");
        this.tiles.add("7777777777777777777777777777777777777777777777777777777777......77777777777777777777777777777......777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777");
        this.tiles.add("...................................................................................................................................................................................................");
        this.tiles.add("...................................................................................................................................................................................................");
        this.tiles.add("...................................................................................................................................................................................................");
        this.tiles.add("...................................................................................................................................................................................................");
        this.tiles.add("...................................................................................................................................................................................................");
        backgroundDataList = new ArrayList<>();
        // note that speeds less than 2 cause problems
        //startY and endY determine where on the coordinate map above the background is placed
        this.backgroundDataList.add(new BackgroundData("water_background", true, -1, -8, 18, 10, 15 ));
        this.backgroundDataList.add(new BackgroundData("seaweed", true, 1, 11, 18, 24, 4 ));
    }
}