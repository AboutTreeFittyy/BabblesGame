package com.example.babblesgame;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.SystemClock;

public class SoundManager {
    private SoundPool soundPool;
    int shoot = -1;
    int jump = -1;
    int teleport = -1;
    int fly = -1;
    int gun_upgrade = -1;
    int player_burn = -1;
    int ricochet = -1;
    int hit_guard = -1;
    int explode = -1;
    int power_up = -1;

    public void loadSound(Context context) {
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        shoot = soundPool.load(context, R.raw.lick,0);
        jump = soundPool.load(context, R.raw.jump,0);
        teleport = soundPool.load(context, R.raw.teleport,0);
        fly = soundPool.load(context, R.raw.crunch,0);
        gun_upgrade = soundPool.load(context, R.raw.upgrade,0);
        player_burn = soundPool.load(context, R.raw.death_noise,0);
        ricochet = soundPool.load(context, R.raw.ricochet,0);
        hit_guard = soundPool.load(context, R.raw.dinosaur_growl,0);
        explode = soundPool.load(context, R.raw.explode,0);
        power_up = soundPool.load(context, R.raw.power_up,0);
    }
    public void playSound(String sound){
        switch (sound){
            case "shoot":
                play(shoot);
                break;
            case "jump":
                play(jump);
                break;
            case "teleport":
                play(teleport);
                break;
            case "fly":
                play(fly);
                break;
            case "gun_upgrade":
                play(gun_upgrade);
                break;
            case "player_burn":
                play(player_burn);
                break;
            case "ricochet":
                play(ricochet);
                break;
            case "hit_guard":
                play(hit_guard);
                break;
            case "explode":
                play(explode);
                break;
            case "power_up":
                play(power_up);
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

