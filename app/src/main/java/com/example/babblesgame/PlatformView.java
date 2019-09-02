package com.example.babblesgame;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.io.PrintStream;

public class PlatformView extends SurfaceView implements Runnable {
    private boolean debugging = true;
    private volatile boolean running;
    private Thread gameThread = null;
    private int factor;
    // For drawing
    private Paint paint;
    // Canvas could initially be local.
    // But later we will use it outside of draw()
    private Canvas canvas;
    private SurfaceHolder ourHolder;
    Context context;
    long startFrameTime;
    long timeThisFrame;
    long fps;
    // Our new engine classes
    private LevelManager lm;
    private Viewport vp;
    InputController ic;
    SoundManager sm;
    private PlayerState ps;

    PlatformView(Context context, int screenWidth, int screenHeight) {
        super(context);
        this.context = context;
        //This determines the scale the player grows with powerups
        factor = 2;
        // Initialize our drawing objects
        ourHolder = getHolder();
        paint = new Paint();
        // Initialize the viewport
        vp = new Viewport(screenWidth, screenHeight);
        //initialize the sound manager
        sm = new SoundManager();
        sm.loadSound(context);
        ps = new PlayerState();
        // Load the first level
        //this is used to select the starting level
        //a potential title screen could start on a
        //world select level instead
        loadLevel("LevelCave", 15, 2);
        //loadLevel("LevelCave", 1, 16);
    }

    @Override
    public void run() {
        while (running) {

            startFrameTime = System.currentTimeMillis();
            update();
            draw();
            // Calculate the fps this frame
            // We can then use the result to
            // time animations and movement.
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }

    //Delete a GameObject and restore player velocity
    private void deleteObject(GameObject go, int hit){
        go.setActive(false);
        go.setVisible(false);
        if (hit != 2) {lm.player.restorePreviousVelocity();}
    }

    private void enemyContact(GameObject go, int hit, String sound){
        // Check if I hit turtle while powered up
        if(ps.isPoweredUp()){
            deleteObject(go,hit);
        }
        else{
            //no power up so kill me
            sm.playSound(sound);
            ps.loseLife();
            PointF location = new PointF(ps.loadLocation().x, ps.loadLocation().y);
            lm.player.setWorldLocationX(location.x);
            lm.player.setWorldLocationY(location.y);
            lm.player.setxVelocity(0);
        }
    }

    private void update() {
        for (GameObject go : lm.gameObjects) {
            if (go.isActive()) {
                // Clip anything off-screen
                if (!vp.clipObjects(go.getWorldLocation().x, go.getWorldLocation().y, go.getWidth(), go.getHeight())) {
                    // Set visible flag to true
                    go.setVisible(true);
                    // check collisions with player
                    int hit = lm.player.checkCollisions(go.getHitbox());
                    if (hit > 0) {
                        //collision! Now deal with different types

                        switch (go.getType()) {
                            case 'c':
                                sm.playSound("fly");
                                ps.gotCredit();
                                deleteObject(go,hit);
                                break;
                            case 'e':
                                //egg power up
                                sm.playSound("power_up");
                                lm.changePlayerSize(context, vp.getPixelsPerMetreX(), factor, true);
                                ps.startPowerUp();
                                deleteObject(go,hit);
                                break;
                            case 'a':
                                enemyContact(go, hit, "player_burn"); //hit by fish
                                break;
                            case 'd':
                                //Only do when not powered up as dinosaur can't die
                                //It just can't hurt you while powered up
                                if(!ps.isPoweredUp()) {
                                    enemyContact(go, hit, "player_burn"); //hit by dinosaur
                                }
                                break;
                            case 'q':
                                enemyContact(go, hit, "player_burn"); //hit by egg
                                break;
                            case 'b':
                                enemyContact(go, hit, "player_burn"); //hit by bird
                                break;
                            case 'g':
                                enemyContact(go, hit, "player_burn"); //hit by turtle
                                break;
                            case 'f':
                                //Only do when not powered up as you can't kill fire
                                //It just can't hurt you while powered up
                                if(!ps.isPoweredUp()) {
                                    enemyContact(go, hit, "player_burn"); //hit by dinosaur
                                }
                                break;
                            case 't':
                                Teleport teleport = (Teleport) go;
                                Location t = teleport.getTarget();
                                loadLevel(t.level, t.x, t.y);
                                sm.playSound("teleport");
                                break;
                            default:// Probably a regular tile
                                if (hit == 1) {// Left or right
                                    lm.player.setxVelocity(0);
                                    lm.player.setPressingRight(false);
                                }
                                if (hit == 2) {// Feet
                                    lm.player.isFalling = false;
                                }
                                break;
                        }
                    }
                }
                if (lm.isPlaying()) {
                    // Run any un-clipped updates
                    go.update(fps, lm.gravity);
                    if (go.getType() == 'a') {
                        // Let any near by drones know where the player is
                        Fish f = (Fish) go;
                        f.setWaypoint(lm.player.getWorldLocation());
                    }
                }else {
                    // Set visible flag to false
                    go.setVisible(false);
                    // Now draw() can ignore them
                }
            }
        }
        if (lm.isPlaying()) {
            //Reset the players location as the centre of the viewport
            vp.setWorldCentre(lm.gameObjects.get(lm.playerIndex).getWorldLocation().x, lm.gameObjects.get(lm.playerIndex).getWorldLocation().y);
            //Has player fallen out of the map?
            if (lm.player.getWorldLocation().x < 0 || lm.player.getWorldLocation().x > lm.mapWidth || lm.player.getWorldLocation().y > lm.mapHeight) {
                sm.playSound("player_burn");
                ps.loseLife();
                PointF location = new PointF(ps.loadLocation().x, ps.loadLocation().y);
                lm.player.setWorldLocationX(location.x);
                lm.player.setWorldLocationY(location.y);
                lm.player.setxVelocity(0);
            }
            // Check if game is over
            if (ps.getLives() == 0) {
                ps = new PlayerState();
                loadLevel("LevelCave", 1, 16);
            }
        }
    }


    private void draw() {
        if (ourHolder.getSurface().isValid()){
            //First we lock the area of memory we will be drawing to
            canvas = ourHolder.lockCanvas();
            // Rub out the last frame with arbitrary color
            paint.setColor(Color.argb(255, 0, 0, 255));
            canvas.drawColor(Color.argb(255, 0, 0, 255));
            // Draw parallax backgrounds from -1 to -3
            drawBackground(0, -3);
            // Draw all the GameObjects
            Rect toScreen2d = new Rect();
            //placeholder if need to insert new game object after
            GameObject ga = null;

            // Draw a layer at a time
            for (int layer = -1; layer <= 1; layer++) {
                for (GameObject go : lm.gameObjects) {
                    //Only draw if visible and this layer
                    if (go.isVisible() && go.getWorldLocation().z == layer) {
                        toScreen2d.set(vp.worldToScreen(go.getWorldLocation().x, go.getWorldLocation().y, go.getWidth(), go.getHeight()));
                        if (go.isAnimated()) {
                            //check if a power up has ended
                            if(ps.resetSize()){
                                lm.changePlayerSize(context, vp.getPixelsPerMetreX(), factor, false);
                                ps.sizeReset();
                            }
                            //Rotate bitmap if necessary and get frame to draw
                            if(ps.isPoweredUp() && go.getType() == 'p'){
                                //Player is powered up so need to recreate bitmap for both directions
                                Matrix flipper = new Matrix();
                                Rect r = go.getRectToDraw(System.currentTimeMillis());
                                if(go.getFacing() == 1) {
                                    //change player size going right
                                    flipper.preScale(-1, 1);
                                }else{
                                    //change player size going left
                                    flipper.preScale(1, 1);
                                }
                                //Now draw it
                                Bitmap b = Bitmap.createBitmap(lm.bitmapsArray[lm.getBitmapIndex(go.getType())], r.left * 2, r.top * 2, r.width() * 2, r.height() * 2, flipper, true);
                                canvas.drawBitmap(b, toScreen2d.left, toScreen2d.top, paint);
                            }else if (go.getFacing() == 1) {
                                // Rotate player right
                                Matrix flipper = new Matrix();
                                flipper.preScale(-1, 1);
                                Rect r = go.getRectToDraw(System.currentTimeMillis());
                                Bitmap b = Bitmap.createBitmap(lm.bitmapsArray[lm.getBitmapIndex(go.getType())], r.left, r.top, r.width(), r.height(), flipper, true);
                                canvas.drawBitmap(b, toScreen2d.left, toScreen2d.top, paint);
                            }
                            else {
                                // draw it the regular way round (turn player left)
                                canvas.drawBitmap(lm.bitmapsArray[lm.getBitmapIndex(go.getType())], go.getRectToDraw(System.currentTimeMillis()), toScreen2d, paint);
                            }
                        } else {
                            // Just draw the whole bitmap
                            canvas.drawBitmap(lm.bitmapsArray[lm.getBitmapIndex(go.getType())], toScreen2d.left, toScreen2d.top, paint);
                        }
                        //check if bird
                        if(go.getType() == 'b'){
                            //check if egg needs to be added
                            if(go.getAddObject()){
                                ga = new BirdEgg(go.getWorldLocation().x, go.getWorldLocation().y, 'q');
                                go.setObject(false);
                            }
                        }

                    }
                }
            }
            //now add the bird egg to the set of game objects
            if(ga != null){
                lm.addNewGameObject(ga, context, vp.getPixelsPerMetreX());
            }
            // Draw parallax backgrounds from layer 1 to 3
            drawBackground(4, 0);
            // Draw the HUD
            // This code needs bitmaps: extra life, upgrade and coin
            // Therefore there must be at least one of each in the level
            int topSpace = vp.getPixelsPerMetreY() / 4;
            int iconSize = vp.getPixelsPerMetreX();
            int padding = vp.getPixelsPerMetreX() / 5;
            int centring = vp.getPixelsPerMetreY() / 6;
            paint.setTextSize(vp.getPixelsPerMetreY()/2);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setColor(Color.argb(100, 0, 0, 0));
            canvas.drawRect(0,0,iconSize * 7.0f, topSpace*2 + iconSize,paint);
            paint.setColor(Color.argb(255, 255, 255, 0));
            canvas.drawBitmap(lm.getBitmap('e'), 0, topSpace, paint);
            canvas.drawText("" + ps.getLives(), (iconSize) + padding, (iconSize) - centring, paint);
            canvas.drawBitmap(lm.getBitmap('c'), (iconSize * 2.5f) + padding, topSpace, paint);
            canvas.drawText("" + ps.getCredits(), (iconSize * 3.5f) + padding * 2, (iconSize) - centring, paint);
            // Text for debugging
            if (debugging) {
                paint.setTextSize(16);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.argb(255, 255, 255, 255));
                canvas.drawText("fps:" + fps, 10, 60, paint);
                canvas.drawText("num objects:" + lm.gameObjects.size(), 10, 80, paint);
                canvas.drawText("num clipped:" + vp.getNumClipped(), 10, 100, paint);
                canvas.drawText("playerX:" + lm.gameObjects.get(lm.playerIndex).getWorldLocation().x, 10, 120, paint);
                canvas.drawText("playerY:" + lm.gameObjects.get(lm.playerIndex).getWorldLocation().y, 10, 140, paint);
                canvas.drawText("Gravity:" + lm.gravity, 10, 160, paint);
                canvas.drawText("X velocity:" + lm.gameObjects.get(lm.playerIndex).getxVelocity(), 10, 180, paint);
                canvas.drawText("Y velocity:" + lm.gameObjects.get(lm.playerIndex).getyVelocity(), 10, 200, paint);
                //for reset the number of clipped objects each frame
                vp.resetNumClipped();
            }// End if(debugging)
            //draw buttons
            paint.setColor(Color.argb(80, 255, 255, 255));
            ArrayList<Rect> buttonsToDraw;
            buttonsToDraw = ic.getButtons();
            for (Rect rect : buttonsToDraw) {
                RectF rf = new RectF(rect.left, rect.top,rect.right, rect.bottom);
                canvas.drawRoundRect(rf, 15f, 15f, paint);
            }
            //draw paused text
            if (!this.lm.isPlaying()) {
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(120);
                canvas.drawText("Paused", vp.getScreenWidth() / 2, vp.getScreenHeight() / 2, paint);
            }
            // Unlock and draw the scene
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawBackground(int start, int stop) {
        Rect fromRect1 = new Rect();
        Rect toRect1 = new Rect();
        Rect fromRect2 = new Rect();
        Rect toRect2 = new Rect();
        for (Background bg : lm.backgrounds) {
            if (bg.z < start && bg.z > stop) {
                // Is this layer in the viewport?
                // Clip anything off-screen
                if (!vp.clipObjects(-1, bg.y, 1000, bg.height)) {
                    float floatstartY = ((vp.getyCentre() - ((vp.getViewportWorldCentreY() - bg.y) * vp.getPixelsPerMetreY())));
                    int startY = (int) floatstartY;
                    float floatendY = ((vp.getyCentre() - ((vp.getViewportWorldCentreY() - bg.endY) * vp.getPixelsPerMetreY())));
                    int endY = (int) floatendY;
                    // Define what portion of bitmaps to capture
                    // and what coordinates to draw them at
                    fromRect1 = new Rect(0, 0, bg.width - bg.xClip,
                            bg.height);
                    toRect1 = new Rect(bg.xClip, startY, bg.width, endY);
                    fromRect2 = new Rect(bg.width - bg.xClip, 0,
                            bg.width, bg.height);
                    toRect2 = new Rect(0, startY, bg.xClip, endY);
                }// End if (!vp.clipObjects...
                //draw backgrounds
                if (!bg.reversedFirst) {
                    canvas.drawBitmap(bg.bitmap, fromRect1, toRect1, paint);
                    canvas.drawBitmap(bg.bitmapReversed, fromRect2, toRect2, paint);
                } else {
                    canvas.drawBitmap(bg.bitmap, fromRect2, toRect2, paint);
                    canvas.drawBitmap(bg.bitmapReversed, fromRect1, toRect1, paint);
                }
                // Calculate the next value for the background's
                // clipping position by modifying xClip
                // and switching which background is drawn first,
                // if necessary.
                bg.xClip -= lm.player.getxVelocity() / (20 / bg.speed);
                if (bg.xClip >= bg.width) {
                    bg.xClip = 0;
                    bg.reversedFirst = !bg.reversedFirst;
                }
                else if (bg.xClip <= 0) {
                    bg.xClip = bg.width;
                    bg.reversedFirst = !bg.reversedFirst;
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (lm != null) {
            ic.handleInput(motionEvent, lm, sm, vp);
        }
        //invalidate();
        return true;
    }

    public void loadLevel(String level, float px, float py) {
        lm = null;
        // Create a new LevelManager
        // Pass in a Context, screen details, level name and player location
        lm = new LevelManager(context, vp.getPixelsPerMetreX(), vp.getScreenWidth(), ic, level, px, py);
        ic = new InputController(vp.getScreenWidth(), vp.getScreenHeight());
        PointF location = new PointF(px, py);
        ps.saveLocation(location);
        // Set the players location as the world centre
        vp.setWorldCentre(lm.gameObjects.get(lm.playerIndex).getWorldLocation().x, lm.gameObjects.get(lm.playerIndex).getWorldLocation().y);
    }

    // Clean up our thread if the game is interrupted
    public void pause() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("error", "failed to pause thread");
        }
    }

    // Make a new thread and start it
    // Execution moves to our run method
    public void resume() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
}// End of PlatformView
