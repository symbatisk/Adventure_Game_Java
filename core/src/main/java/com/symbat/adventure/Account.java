package com.symbat.adventure;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private int totalScore;
    private Map<Integer, LevelProgress> progressMap = new HashMap<>();

    public Account() {
    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.totalScore = 0;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Map<Integer, LevelProgress> getProgressMap() {
        return progressMap;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void addScore(int points) {
        this.totalScore += points;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProgressMap(Map<Integer, LevelProgress> progressMap) {
        this.progressMap = progressMap;
    }

    public int getHighestUnlockedLevel() {
        int highest = 1;
        for (Integer level : progressMap.keySet()) {
            if (level > highest) {
                highest = level;
            }
        }
        return highest;
    }

    public int getCoinCount() {
        int total = 0;
        for (LevelProgress progress : progressMap.values()) {
            total += progress.getCoins();
        }
        return total;
    }

    public int getStarCount() {
        int total = 0;
        for (LevelProgress progress : progressMap.values()) {
            total += progress.getStars();
        }
        return total;
    }

    public void updateProgress(int level, int stars, int coins, int score) {
        LevelProgress progress = progressMap.getOrDefault(level, new LevelProgress());
        progress.setStars(stars);
        progress.setCoins(coins);
        progress.setScore(score);
        progressMap.put(level, progress);


        totalScore = 0;
        for (LevelProgress p : progressMap.values()) {
            totalScore += p.getScore();
        }
    }

    public void setScore(int level) {

        totalScore = 0;
        for (LevelProgress progress : progressMap.values()) {
            totalScore += progress.getScore();
        }
    }


}
