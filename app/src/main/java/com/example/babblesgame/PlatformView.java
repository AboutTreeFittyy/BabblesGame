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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PlatformView extends SurfaceView implements Runnable {
    private boolean debugging = true;
    private volatile boolean running = false;
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
    MenuController mc;
    SoundManager sm;
    private PlayerState ps;
    float px, py;
    private String data;

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
        ic = null;
        lm = null;
        px = 15;
        py = 2;
        //write("asd");//use this to delete save or reset to 0 rather
        data = read(); // Get the data from saved file if there is one
        worldSelect(); //Load the menu
    }

    //read file
    public String read() {
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream ins = context.openFileInput("BabblesData");
            InputStreamReader is = new InputStreamReader(ins);
            BufferedReader r = new BufferedReader(is);
            String t;
            while ((t = r.readLine()) != null) {
                sb.append(t);
            }
            ins.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    //write file
    public void write(String level) {
        if(data == "3"){
            return; // Game progression maxed out so no point in writing anymore
        }
        switch(level){
            case "LevelForest":
                if(data == "2"){
                    data = "3";
                }else{ data = "1";}
                break;
            case "LevelWater":
                if(data == "1"){
                    data = "3";
                }else{ data = "2";}
                break;
            default:
                data = "0";
        }
        try {
            FileOutputStream fOut = context.openFileOutput("BabblesData", context.MODE_PRIVATE);
            fOut.write(data.getBytes());
            fOut.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    //load the main menu and display it
    public void worldSelect(){
        ps = new PlayerState();
        ic = null;
        // Create a new LevelManager
        // Pass in a Context, screen details, level name and player location
        lm = new LevelManager(context, vp.getPixelsPerMetreX(), vp.getScreenWidth(), ic, "Menu", px, py);
        mc = new MenuController(vp.getScreenWidth(), vp.getScreenHeight());
        if (ourHolder.getSurface().isValid()){
            //First we lock the area of memory we will be drawing to
            canvas = ourHolder.lockCanvas();
            // Rub out the last frame with arbitrary color
            paint.setColor(Color.argb(255, 0, 0, 255));
            canvas.drawColor(Color.argb(255, 0, 0, 255));
            canvas.drawBitmap(lm.backgrounds.get(0).bitmap, null, new Rect(0, 0, vp.getScreenWidth(), vp.getScreenHeight()), paint);
            // Unlock and draw the scene
            ourHolder.unlockCanvasAndPost(canvas);
        }
        PointF location = new PointF(px, py);
        ps.saveLocation(location);
        // Set the players location as the world centre
        vp.setWorldCentre(lm.gameObjects.get(lm.playerIndex).getWorldLocation().x, lm.gameObjects.get(lm.playerIndex).getWorldLocation().y);
    }

    public void loadLevel(String level) {
        ps = new PlayerState();
        // Create a new LevelManager
        // Pass in a Context, screen details, level name and player location
        lm = new LevelManager(context, vp.getPixelsPerMetreX(), vp.getScreenWidth(), ic, level, px, py);
        ic = new InputController(vp.getScreenWidth(), vp.getScreenHeight());
        PointF location = new PointF(px, py);
        ps.saveLocation(location);
        // Set the players location as the world centre
        vp.setWorldCentre(lm.gameObjects.get(lm.playerIndex).getWorldLocation().x, lm.gameObjects.get(lm.playerIndex).getWorldLocation().y);
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
    private void deleteObject(GameObject go, int hit, String soundKill){
        sm.playSound(soundKill);
        go.setActive(false);
        go.setVisible(false);
        if (hit != 2) {lm.player.restorePreviousVelocity();}
    }

    private void enemyContact(GameObject go, int hit, String soundDead, String soundKill){
        // Check if I hit turtle while powered up
        if(ps.isPoweredUp()){
            deleteObject(go,hit,soundKill);
        }
        else{
            //no power up so kill me
            sm.playSound(soundDead);
            loadLevel(lm.level);
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
                                ps.gotCredit();
                                deleteObject(go,hit,"fly");
                                break;
                            case 'e':
                                //egg power up
                                lm.changePlayerSize(context, vp.getPixelsPerMetreX(), factor, true);
                                ps.startPowerUp();
                                deleteObject(go,hit,"power_up");
                                break;
                            case 'a':
                                enemyContact(go, hit, "fish_bite", "enemy_hit"); //hit by fish
                                break;
                            case 'd':
                                //Only do when not powered up as dinosaur can't die
                                //It just can't hurt you while powered up
                                if(!ps.isPoweredUp()) {
                                    enemyContact(go, hit, "player_burn", "enemy_hit"); //hit by dinosaur
                                }
                                break;
                            case 'q':
                                enemyContact(go, hit, "egg_squish", "egg_crack"); //hit by egg
                                break;
                            case 'b':
                                enemyContact(go, hit, "bird_bite", "enemy_hit"); //hit by bird
                                break;
                            case 'g':
                                enemyContact(go, hit, "turtle_bite", "enemy_hit"); //hit by turtle
                                break;
                            case 'f':
                                //Only do when not powered up as you can't kill fire
                                //It just can't hurt you while powered up
                                if(!ps.isPoweredUp()) {
                                    enemyContact(go, hit, "player_burn", "enemy_hit");
                                }
                                break;
                            case 'l':
                                //Finish line
                                if(lm.isPlaying()){
                                    sm.playSound("level_finish");
                                    lm.levelFinished();
                                }
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
                loadLevel(lm.level);
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
            // This code needs bitmaps: coin
            // Therefore there must be at least one of each in the level
            int topSpace = vp.getPixelsPerMetreY() / 4;
            int iconSize = vp.getPixelsPerMetreX()*2;
            int padding = vp.getPixelsPerMetreX() / 5;
            int centring = vp.getPixelsPerMetreY() / 6;
            paint.setTextSize(vp.getPixelsPerMetreY());
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setColor(Color.argb(255, 255, 255, 0));
            canvas.drawText("" + ps.getCredits()+"/4 ", vp.getScreenWidth()/2 - iconSize - (padding * 6), topSpace * 2 + centring + padding*2, paint);
            canvas.drawBitmap(lm.getBitmap('c'), vp.getScreenWidth()/2 - iconSize, topSpace, paint);
            canvas.drawText(" COLLECTED", iconSize + padding +vp.getScreenWidth()/2, topSpace * 2 + centring + padding*2, paint);
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
            //Are we drawing menu or game buttons?
            if(ic == null){
                buttonsToDraw = mc.getButtons();
            }
            else{
                buttonsToDraw = ic.getButtons();
            }

            int i = 0;
            for (Rect rect : buttonsToDraw) {
                RectF rf = new RectF(rect.left, rect.top,rect.right, rect.bottom);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(100);
                int textY = (rect.bottom - rect.top) / 4;
                String text = "";
                if(ic == null){
                    //draw menu buttons
                    paint.setColor(Color.argb(200, 75, 0, 130));
                    canvas.drawRoundRect(rf, 15f, 15f, paint);
                    switch(i){
                        case 0: text = "Forest";
                        break;
                        case 1: text = "Water";
                        break;
                        case 2: text = "Cave";
                    }
                    paint.setColor(Color.argb(255, 220, 20, 60));
                    canvas.drawText(text, (rect.left + rect.right)/2, (rect.top + rect.bottom)/2 + textY, paint);
                }
                else if(!lm.isFinished()){
                    if(this.lm.isPlaying()){
                        //draw playing buttons and then text
                        switch(i){
                            case 0: text = "Pause";
                                break;
                            case 1: text = "Left";
                                break;
                            case 2: text = "Right";
                                break;
                        }
                        if(text != "") {
                            paint.setColor(Color.argb(255, 0, 128, 0));
                            canvas.drawRoundRect(rf, 15f, 15f, paint);
                            paint.setColor(Color.argb(200, 75, 0, 130));
                            canvas.drawText(text, (rect.left + rect.right) / 2, (rect.top + rect.bottom) / 2 + textY, paint);
                        }
                    } else if(lm.justStarted()){
                        //draw start button
                        if(i == 4) {
                            paint.setColor(Color.argb(200, 75, 75, 130));
                            canvas.drawRoundRect(rf, 50f, 50f, paint);
                            paint.setColor(Color.argb(255, 255, 255, 255));
                            canvas.drawText("TAP TO START!", vp.getScreenWidth() / 2, vp.getScreenHeight() / 2, paint);
                        }
                    } else{
                        //draw paused buttons and then text
                        switch(i){
                            case 0: text = "Resume";
                                break;
                            case 3: text = "Menu";
                                break;
                        }
                        if(text != "") {
                            paint.setColor(Color.argb(200, 75, 0, 130));
                            canvas.drawRoundRect(rf, 15f, 15f, paint);
                            paint.setColor(Color.argb(255, 255, 255, 255));
                            canvas.drawText(text, (rect.left + rect.right) / 2, (rect.top + rect.bottom) / 2 + textY, paint);
                        }
                    }
                } else {
                    //draw finished level buttons and then text
                    switch (i) {
                        case 0:
                            text = "Restart";
                            break;
                        case 3:
                            text = "Menu";
                            break;
                    }
                    if (text != "") {
                        paint.setColor(Color.argb(255, 0, 128, 0));
                        canvas.drawRoundRect(rf, 15f, 15f, paint);
                        paint.setColor(Color.argb(200, 75, 0, 130));
                        canvas.drawText(text, (rect.left + rect.right) / 2, (rect.top + rect.bottom) / 2 + textY, paint);
                    }
                    //draw dinosaur with text of outcome
                    paint.setTextSize(vp.getPixelsPerMetreY());
                    paint.setTextAlign(Paint.Align.CENTER);
                    paint.setColor(Color.argb(255, 255, 0, 0));
                    // draw the raw bitmap just push it off screen far enough to only show one head properly
                    canvas.drawBitmap(lm.getBitmap('d'), -vp.getScreenWidth(), vp.getScreenHeight()/4, paint);
                    String dinoText = "";
                    int indent = vp.getScreenWidth()/10;
                    if(ps.getCredits() == 4){
                        //babbles got all the flies so dinosaur is mad
                        dinoText = "BABBLES! YOU ATE ALL MY FLIES! >:(";
                    } else{
                        //babbles missed some flies so dinosaur mocks player
                        canvas.drawText("HA HA HA BABBLES YOU LOSER", vp.getScreenWidth()/2 + indent, vp.getScreenHeight() - vp.getScreenHeight()/3, paint);
                        dinoText = "YOU DID'T EVEN EAT ALL MY FLIES";
                    }
                    canvas.drawText(dinoText, vp.getScreenWidth()/2 + indent, vp.getScreenHeight() - vp.getScreenHeight()/4, paint);
                }
                i++;
            }
            //draw paused text
            if (!this.lm.isPlaying()) {
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(120);
                if(ic != null) {
                    if(lm.isFinished()){
                        canvas.drawText("Level Complete", vp.getScreenWidth() / 2, vp.getScreenHeight() / 2, paint);
                    }else if(!lm.justStarted()){
                        canvas.drawText("Paused", vp.getScreenWidth() / 2, vp.getScreenHeight() / 2, paint);
                    }
                }
                else{
                    canvas.drawText("Select Level", vp.getScreenWidth() / 2, vp.getScreenHeight() / 4, paint);
                }
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
                    fromRect1 = new Rect(0, 0, bg.width - bg.xClip, bg.height);
                    toRect1 = new Rect(bg.xClip, startY, bg.width, endY);
                    fromRect2 = new Rect(bg.width - bg.xClip, 0, bg.width, bg.height);
                    toRect2 = new Rect(0, startY, bg.xClip, endY);
                }
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
        if (lm != null && ic != null) {
            ic.handleInput(motionEvent, lm, sm, vp);
            if(ic.menuSelected){
                if(lm.isFinished() && ps.getCredits() == 4) {
                    write(lm.level); //Save progress on completion with all flies
                }
                worldSelect();
            }else if(ic.restart){
                loadLevel(lm.level);
            }
        }
        else{
            mc.handleInput(motionEvent, data);
            if(mc.selected){
                loadLevel(mc.getLevel());
                mc.selected = false;
            }
        }
        return true;
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
