package com.symbat.adventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.Preferences;

public class SoundManager {
    private static Preferences prefs = Gdx.app.getPreferences("SoundSettings");
    private static Map<String, Sound> sounds = new HashMap<>();
    private static Music backgroundMusic;

    public static void load() {
        sounds.put("coin", Gdx.audio.newSound(Gdx.files.internal("data/sounds/collect.mp3")));
        sounds.put("star", Gdx.audio.newSound(Gdx.files.internal("data/sounds/collect.mp3")));
        sounds.put("shield", Gdx.audio.newSound(Gdx.files.internal("data/sounds/collect.mp3")));
        sounds.put("slash", Gdx.audio.newSound(Gdx.files.internal("data/sounds/slash.mp3")));
        sounds.put("death", Gdx.audio.newSound(Gdx.files.internal("data/sounds/death.mp3")));

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("data/sounds/menu1.mp3"));
        backgroundMusic.setLooping(true);

        backgroundMusic.setVolume(getVolume());
        if (isMusicEnabled()) {
            backgroundMusic.play();
        }
    }

    public static void playSound(String soundName) {
        if (isSoundEnabled()) {
            Sound sound = sounds.get(soundName);
            if (sound != null) {
                sound.play(getVolume());
            }
        }
    }

    public static boolean isSoundEnabled() {
        return prefs.getBoolean("soundEnabled", true);
    }

    public static void setSoundEnabled(boolean enabled) {
        prefs.putBoolean("soundEnabled", enabled);
        prefs.flush();
    }

    public static boolean isMusicEnabled() {
        return prefs.getBoolean("musicEnabled", true);
    }

    public static void setMusicEnabled(boolean enabled) {
        prefs.putBoolean("musicEnabled", enabled);
        prefs.flush();

        if (backgroundMusic != null) {
            if (enabled) {
                backgroundMusic.play();
            } else {
                backgroundMusic.pause();
            }
        }
    }

    public static float getVolume() {
        return prefs.getFloat("volume", 0.5f);
    }

    public static void setVolume(float volume) {
        prefs.putFloat("volume", volume);
        prefs.flush();

        if (backgroundMusic != null) {
            backgroundMusic.setVolume(volume);
        }
    }

    public static void toggleSound() {
        setSoundEnabled(!isSoundEnabled());
    }

    public static void toggleMusic() {
        setMusicEnabled(!isMusicEnabled());
    }

    public static void dispose() {
        for (Sound sound : sounds.values()) {
            sound.dispose();
        }
        if (backgroundMusic != null) {
            backgroundMusic.dispose();
        }
    }
}
