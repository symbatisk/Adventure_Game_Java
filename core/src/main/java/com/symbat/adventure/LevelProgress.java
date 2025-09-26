package com.symbat.adventure;
import java.io.Serializable;

public class LevelProgress implements Serializable{
    private static final long serialVersionUID = 1L;
    private int stars;
    private int coins;
    private int score;

    public LevelProgress() {
        this(0, 0, 0);
    }

    public LevelProgress(int stars, int coins, int score) {
        this.stars = stars;
        this.coins = coins;
        this.score = score;
    }

    public int getStars() {
        return stars;
    }

    public int getCoins() {
        return coins;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void updateIfBetter(int stars, int coins) {
        if (stars > this.stars) {
            this.stars = stars;
        }
        if (coins > this.coins) {
            this.coins = coins;
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
