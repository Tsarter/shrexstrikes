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
    private String username;
    private Player.Character character;
    private int currentAdId;
    public GamePreferences() {
        prefs = Gdx.app.getPreferences("GamePreferences");
        String soundVolume = prefs.getString("soundVolume", "0.6f");
        String musicVolume = prefs.getString("musicVolume", "0.6f");
        String isSoundEnabled = prefs.getString("isSoundEnabled", "true");
        String isMusicEnabled = prefs.getString("isMusicEnabled", "true");
        String mouseSensitivity = prefs.getString("mouseSensitivity", "10f");
        String randomNum = String.valueOf((int) (Math.random() * 1000));
        String username = prefs.getString("username", "Player " + randomNum);
        String character = prefs.getString("character", Player.Character.Shrex.toString());
        String currentAdId = prefs.getString("currentAdId", "0");
        this.soundVolume = Float.parseFloat(soundVolume);
        this.musicVolume = Float.parseFloat(musicVolume);
        this.isSoundEnabled = Boolean.parseBoolean(isSoundEnabled);
        this.isMusicEnabled = Boolean.parseBoolean(isMusicEnabled);
        this.mouseSensitivity = Float.parseFloat(mouseSensitivity);
        this.username = username;
        this.character = Player.Character.valueOf(character);
        this.currentAdId = Integer.parseInt(currentAdId);
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
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
        prefs.putString("username", username);
        save();
    }
    public Player.Character getCharacter() {
        return character;
    }
    public void setCharacter(Player.Character character) {
        this.character = character;
        prefs.putString("character", character.toString());
        save();
    }
    public int getCurrentAdId() {
        return currentAdId;
    }
    public void setCurrentAdId(int currentAdId) {
        this.currentAdId = currentAdId;
        prefs.putString("currentAdId", String.valueOf(currentAdId));
        save();
    }
    public void save() {
        prefs.flush();
    }
}
