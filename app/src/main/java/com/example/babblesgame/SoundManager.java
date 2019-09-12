/*
Author: Mathew Boland
Course: COMP486
Overview: Creates a SoundPool to store the sound effects for the game. Also has a method to
make sure sounds are only played after they are loaded safely and a method to play specified sounds.
Date: September 12th, 2019
FileName: SoundManager.java
Code Citation: "Android Game Programming By Example", John Horton, 2015
 */
package com.example.babblesgame;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.SystemClock;

public class SoundManager {
    private SoundPool soundPool;
    int jump = -1;
    int level_finish = -1;
    int explode = -1;
    int power_up = -1;
    int fly = -1;
    int egg_squish = -1;
    int egg_crack = -1;
    int bird_bite = -1;
    int turtle_bite = -1;
    int fish_bite = -1;
    int enemy_hit = -1;
    int player_burn = -1;
    int dinosaur_growl = -1;


    public void loadSound(Context context) {
        soundPool = new SoundPool(15, AudioManager.STREAM_MUSIC, 0);
        jump = soundPool.load(context, R.raw.jump,0);
        level_finish = soundPool.load(context, R.raw.level_finish,0);
        explode = soundPool.load(context, R.raw.explode,0);
        power_up = soundPool.load(context, R.raw.power_up,0);
        fly = soundPool.load(context, R.raw.crunch,0);
        egg_squish = soundPool.load(context, R.raw.egg_squish,0);
        egg_crack = soundPool.load(context, R.raw.egg_crack,0);
        bird_bite = soundPool.load(context, R.raw.bird_bite,0);
        turtle_bite = soundPool.load(context, R.raw.turtle_bite,0);
        fish_bite = soundPool.load(context, R.raw.fish_bite,0);
        enemy_hit = soundPool.load(context, R.raw.enemy_hit,0);
        player_burn = soundPool.load(context, R.raw.death_noise,0);
        dinosaur_growl = soundPool.load(context, R.raw.dinosaur_growl,0);

    }
    public void playSound(String sound){
        switch (sound){
            case "jump":
                play(jump);
                break;
            case "level_finish":
                play(level_finish);
                break;
            case "explode":
                play(explode);
                break;
            case "power_up":
                play(power_up);
                break;
            case "fly":
                play(fly);
                break;
            case "egg_squish":
                play(egg_squish);
                break;
            case "egg_crack":
                play(egg_crack);
                break;
            case "bird_bite":
                play(bird_bite);
                break;
            case "turtle_bite":
                play(turtle_bite);
                break;
            case "fish_bite":
                play(fish_bite);
                break;
            case "enemy_hit":
                play(enemy_hit);
                break;
            case "player_burn":
                play(player_burn);
                break;
            case "dinosaur_growl":
                play(dinosaur_growl);
                break;
        }
    }

    public void play(int id){
        int timeLimit = 1000;
        int time = 0;
        int choke = 10;
        while(soundPool.play(id, 1.f, 1.f, 0, 0, 1.f) == 0 && time < timeLimit) {
            time++;
            SystemClock.sleep(choke);
        }
    }
}

