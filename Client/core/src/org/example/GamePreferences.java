package org.example;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GamePreferences {
    private float soundVolume;
    private float musicVolume;
    private boolean isSoundEnabled;
    private boolean isMusicEnabled;
    private float mouseSensitivity;
    private Preferences prefs;
    public GamePreferences() {
        prefs = Gdx.app.getPreferences("GamePreferences");
        String soundVolume = prefs.getString("soundVolume", "0.6f");
        String musicVolume = prefs.getString("musicVolume", "0.6f");
        String isSoundEnabled = prefs.getString("isSoundEnabled", "true");
        String isMusicEnabled = prefs.getString("isMusicEnabled", "true");
        String mouseSensitivity = prefs.getString("mouseSensitivity", "10f");
        this.soundVolume = Float.parseFloat(soundVolume);
        this.musicVolume = Float.parseFloat(musicVolume);
        this.isSoundEnabled = Boolean.parseBoolean(isSoundEnabled);
        this.isMusicEnabled = Boolean.parseBoolean(isMusicEnabled);
        this.mouseSensitivity = Float.parseFloat(mouseSensitivity);
    }
    public float getSoundVolume() {
        return soundVolume;
    }
    public void setSoundVolume(float soundVolume) {
        this.soundVolume = soundVolume;
        prefs.putString("soundVolume", String.valueOf(soundVolume));
        save();
    }
    public float getMusicVolume() {
        return musicVolume;
    }
    public void setMusicVolume(float musicVolume) {
        this.musicVolume = musicVolume;
        prefs.putString("musicVolume", String.valueOf(musicVolume));
        save();
    }
    public boolean isSoundEnabled() {
        return isSoundEnabled;
    }
    public void setSoundEnabled(boolean soundEnabled) {
        isSoundEnabled = soundEnabled;
        prefs.putString("isSoundEnabled", String.valueOf(soundEnabled));
        save();
    }
    public boolean isMusicEnabled() {
        return isMusicEnabled;
    }
    public void setMusicEnabled(boolean musicEnabled) {
        isMusicEnabled = musicEnabled;
        prefs.putString("isMusicEnabled", String.valueOf(musicEnabled));
        save();
    }
    public float getMouseSensitivity() {
        return mouseSensitivity;
    }
    public void setMouseSensitivity(float mouseSensitivity) {
        this.mouseSensitivity = mouseSensitivity;
        prefs.putString("mouseSensitivity", String.valueOf(mouseSensitivity));
        save();
    }
    public void save() {
        prefs.flush();
    }
}
