/*
Author: Mathew Boland
Course: COMP486
Overview: Creates an object to store the menu. Since a level must be loaded to run, the menu is
run as if it were a level. Necessary objects are placed but not visible as the menu image is placed
in the foreground in front of the others.
Date: September 12th, 2019
FileName: Menu.java
Code Citation: "Android Game Programming By Example", John Horton, 2015
 */
package com.example.babblesgame;

import java.util.ArrayList;

public class Menu extends LevelData{
    Menu() {
        tiles = new ArrayList<>();
        //remember at least one u and e must be in the menu level
        this.tiles.add("..........................................................................................................");
        this.tiles.add("..........................................................................................................");
        this.tiles.add("..........................................................................................................");
        this.tiles.add("..........................................................................................................");
        this.tiles.add("....................................e.....................................................................");
        this.tiles.add("..........................................................................................................");
        this.tiles.add("..........................................................................................................");
        this.tiles.add("..........................................................................................................");
        this.tiles.add("...............p..........................................................................................");
        this.tiles.add("..........................................................................................................");
        this.tiles.add("..........................................................................................................");
        this.tiles.add("..........................................................................................................");
        backgroundDataList = new ArrayList<>();
        // note that speeds less than 2 cause problems
        //startY and endY determine where on the coordinate map above the background is placed
        this.backgroundDataList.add(new BackgroundData("frog_jump", true, 1, -10, 11, 10, 10 ));
    }
}