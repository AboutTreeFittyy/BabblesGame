/*
Author: Mathew Boland
Course: COMP486
Overview: Creates an object to Manage a Level creation and maintenance. Holds an array that contain
all the bitmaps so they can be accessed. Creates an array list of all the games GameObjects to be
processed. Also has some functions for pausing the game such as switchPlayingStatus() and adding new
objects to the level like the BirdEgg.
Date: September 12th, 2019
FileName: LevelManager
Code Citation: "Android Game Programming By Example", John Horton, 2015
 */
package com.example.babblesgame;

import android.content.Context;
import android.graphics.Bitmap;
import java.util.ArrayList;

public class LevelManager {
    String level;
    int mapWidth;
    int mapHeight;
    Player player;
    Dinosaur dinosaur;
    int playerIndex, dinosaurIndex;
    private boolean playing, levelFinished, justStarted;
    private int currentIndex;
    float gravity;
    LevelData levelData;
    ArrayList<GameObject> gameObjects;
    ArrayList<Background> backgrounds;
    Bitmap[] bitmapsArray;

    public LevelManager(Context context, int pixelsPerMetre, int screenWidth, InputController ic, String level, float px, float py) {
        this.level = level;
        currentIndex = -1;
        switch (level) {
            case "Menu":
                levelData = new Menu();
                break;
            case "LevelForest":
                levelData = new LevelForest();
                break;
            case "LevelWater":
                levelData = new LevelWater();
                break;
            case "LevelCave":
                levelData = new LevelCave();
                break;
        }
        levelFinished = false;
        justStarted = true;
        // To hold all our GameObjects
        gameObjects = new ArrayList<>();
        // To hold 1 of every Bitmap
        bitmapsArray = new Bitmap[25];
        // Load all the GameObjects and Bitmaps
        loadMapData(context, pixelsPerMetre, px, py);
        loadBackgrounds(context, pixelsPerMetre, screenWidth);
        // Set waypoints for our guards
        setWaypoints();
    }

    public boolean isPlaying() {
        return playing;
    }
    public boolean isFinished() {
        return levelFinished;
    }
    public boolean justStarted(){return justStarted;}
    public void switchStarted(){justStarted = !justStarted;}

    public Bitmap getBitmap(char blockType) {
        int index;
        switch (blockType) {
            case '.':
                index = 0;
                break;
            case '1':
                index = 1;
                break;
            case '2':
                index = 2;
                break;
            case '3':
                index = 3;
                break;
            case '4':
                index = 4;
                break;
            case 'a':
                index = 5;
                break;
            case 'b':
                index = 6;
                break;
            case 'c':
                index = 7;
                break;
            case 'd':
                index = 8;
                break;
            case 'e':
                index = 9;
                break;
            case 'f':
                index = 10;
                break;
            case 'g':
                index = 11;
                break;
            case 'l':
                index = 12; //Lowercase L
                break;
            case 'p':
                index = 13;
                break;
            case 'q':
                index = 14;
                break;
            case 'u':
                index = 15;
                break;
            default:
                index = 0;
                break;
        }// End switch
        return bitmapsArray[index];
    }// End getBitmap

    public int getBitmapIndex(char blockType) {
        int index;
        switch (blockType) {
            case '.':
                index = 0;
                break;
            case '1':
                index = 1;
                break;
            case '2':
                index = 2;
                break;
            case '3':
                index = 3;
                break;
            case '4':
                index = 4;
                break;
            case 'a':
                index = 5;
                break;
            case 'b':
                index = 6;
                break;
            case 'c':
                index = 7;
                break;
            case 'd':
                index = 8;
                break;
            case 'e':
                index = 9;
                break;
            case 'f':
                index = 10;
                break;
            case 'g':
                index = 11;
                break;
            case 'l':
                index = 12; //Lowercase L
                break;
            case 'p':
                index = 13;
                break;
            case 'q':
                index = 14;
                break;
            case 'u':
                index = 15;
                break;
            default:
                index = 0;
                break;
        }// End switch
        return index;
    }

    /* Used to add a new game object to the game that spawns after
     the game has already begun */
    public void addNewGameObject(GameObject obj, Context context, int pixelsPerMetre){
        char c = obj.getType();
        gameObjects.add(obj);
        // If the bitmap isn't prepared yet
        if (bitmapsArray[getBitmapIndex(c)] == null) {
            currentIndex++;
            bitmapsArray[getBitmapIndex(c)] = gameObjects.get(currentIndex).prepareBitmap(context, gameObjects.get(currentIndex).getBitmapName(), pixelsPerMetre);
        }
    }

    //Chang ethe size of the player to be bigger or smaller
    public void changePlayerSize(Context context, int pixelsPerMetre, int factor, boolean scale){
        player.changeSizeByFactor(factor, scale);
        bitmapsArray[getBitmapIndex('p')] = player.prepareBitmap(context, player.getBitmapName(), pixelsPerMetre);
    }

    private void loadMapData(Context context, int pixelsPerMetre, float px, float py) {
        char c;
        // how wide and high is the map? Viewport needs to know
        mapHeight = levelData.tiles.size();
        mapWidth = levelData.tiles.get(0).length();
        for (int i = 0; i < levelData.tiles.size(); i++) {
            for (int j = 0; j < levelData.tiles.get(i).length(); j++) {
                c = levelData.tiles.get(i).charAt(j);
                // Don't want to load the empty spaces
                if (c != '.'){
                    currentIndex++;
                    switch (c) {
                        case 'a':
                            // Add a fish to the gameObjects
                            gameObjects.add(new Fish(context, j, i, c, pixelsPerMetre));
                            break;
                        case 'b':
                            // Add a bird to the gameObjects
                            gameObjects.add(new Bird(context, j, i, c, pixelsPerMetre));
                            break;

                        case 'c':
                            // Add a fly to the gameObjects
                            gameObjects.add(new Fly(j, i, c));
                            break;
                        case 'd':
                            // Add a dinosaur to the gameObjects
                            gameObjects.add(new Dinosaur(context, j, i, c, pixelsPerMetre));
                            dinosaurIndex = currentIndex;
                            dinosaur = (Dinosaur) gameObjects.get(dinosaurIndex);
                            break;
                        case 'e':
                            // Add a power up to the gameObjects
                            gameObjects.add(new PowerUp(j, i, c));
                            break;
                        case 'f':
                            // Add a fire tile the gameObjects
                            gameObjects.add(new FishTile(context, j, i, c, pixelsPerMetre));
                            break;
                        case 'g':
                            // Add a turtle to the gameObjects
                            gameObjects.add(new Turtle(context, j, i, c, pixelsPerMetre));
                            break;
                        case 'l':
                            // Add a FinishLine to the gameObjects
                            gameObjects.add(new FinishLine(j, i, c));
                            break;
                        case 'p':
                            // Add a player to the gameObjects
                            gameObjects.add(new Player(context, px, py, pixelsPerMetre));
                            // We want the index of the player
                            playerIndex = currentIndex;
                            // We want a reference to the player
                            player = (Player) gameObjects.get(playerIndex);
                            break;
                        case 'u':
                            // Add a turtle to the gameObjects
                            gameObjects.add(new UnderWaterTurtle(context, j, i, c, pixelsPerMetre));
                            break;
                        case '1':
                            // Add a tile to the gameObjects
                            gameObjects.add(new Grass(j, i, c));
                            break;
                        case '2':
                            // Add a tile to the gameObjects
                            gameObjects.add(new Snow(j, i, c));
                            break;
                        case '3':
                            // Add a tile to the gameObjects
                            gameObjects.add(new Scorched(j, i, c));
                            break;
                        case '4':
                            // Add a tile to the gameObjects
                            gameObjects.add(new Stone(j, i, c));
                            break;
                    }// End switch
                    // If the bitmap isn't prepared yet
                    if (bitmapsArray[getBitmapIndex(c)] == null) {
                        System.out.println("This: "+c);
                        bitmapsArray[getBitmapIndex(c)] = gameObjects.get(currentIndex).prepareBitmap(context, gameObjects.get(currentIndex).getBitmapName(), pixelsPerMetre);
                    }// End if
                }// End if (c != '.'){
            }// End for j
        }// End for i
    }// End loadMapData()

    public void switchPlayingStatus() {
        playing = !playing;
        if (playing) {
            gravity = 6;
        } else {
            gravity = 0;
        }
    }

    public void levelFinished(){
        playing = !playing;
        levelFinished = !levelFinished;
        gravity = 0;
    }

    public void setWaypoints() {
        // Loop through all game objects looking for turtles
        for (GameObject turtle : this.gameObjects) {
            if (turtle.getType() == 'g') {
                int startTileIndex = -1;
                //int startTurtleIndex = 0;
                float waypointX1 = -1;
                float waypointX2 = -1;
                for (GameObject tile : this.gameObjects) {
                    startTileIndex++;
                    if (tile.getWorldLocation().y == turtle.getWorldLocation().y + 1) {
                        // Tile is two spaces below current turtle
                        // Now see if has same x coordinate
                        if (tile.getWorldLocation().x == turtle.getWorldLocation().x) {
                            // Found the tile the turtle is "standing" on
                            // Now go left as far as possible
                            // before non travers-able tile is found
                            // Either on turtles row or tile row
                            for (int i = 0; i < 5; i++) {// left for loop
                                if (!gameObjects.get(startTileIndex -i).isTraversable()) {
                                    //set the left waypoint
                                    waypointX1 = gameObjects.get(startTileIndex - (i + 1)).getWorldLocation().x;
                                    break;// Leave left for loop
                                } else {
                                    // Set to max 5 tiles as
                                    // no non traversible tile found
                                    waypointX1 = gameObjects.get(startTileIndex - 5).getWorldLocation().x;
                                }
                            }// end get left waypoint
                            for (int i = 0; i < 5; i++) {// right for loop
                                if (!gameObjects.get(startTileIndex + i).isTraversable()) {
                                    //set the right waypoint
                                    waypointX2 = gameObjects.get(startTileIndex + (i - 1)).getWorldLocation().x;
                                    break;// Leave right for loop
                                } else {
                                    //set to max 5 tiles away
                                    waypointX2 = gameObjects.get(startTileIndex + 5).getWorldLocation().x;
                                }
                            }// end get right waypoint
                            Turtle t = (Turtle) turtle;
                            t.setWaypoints(waypointX1, waypointX2);
                        }
                    }
                }
            }
        }
    }// End setWaypoints()

    private void loadBackgrounds(Context context, int pixelsPerMetre, int screenWidth) {
        backgrounds = new ArrayList<>();
        //load the background data into the Background objects and
        // place them in our GameObject arraylist
        for (BackgroundData bgData : levelData.backgroundDataList) {
            backgrounds.add(new Background(context, pixelsPerMetre, screenWidth, bgData));
        }
    }
}// End LevelManager
