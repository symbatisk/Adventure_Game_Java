package com.symbat.adventure;

public class Score {
    private final Account currentAccount;
    private int sessionSeconds;
    private boolean isActive;
    private float timer;

    public Score(Account account) {
        this.currentAccount = account;
        this.sessionSeconds = 0;
        this.isActive = false;
        this.timer = 0;
    }

    public void update(float delta) {
        if (isActive) {
            timer += delta;
            if (timer >= 1.0f) {
                timer = 0;
                sessionSeconds++;
                currentAccount.addScore(1);
            }
        }
    }

    public void start() {
        isActive = true;
    }

    public void pause() {
        isActive = false;
    }

    public void resume() {
        isActive = true;
    }

    public void end() {
        isActive = false;
        currentAccount.addScore(sessionSeconds);
    }

    public int getSessionSeconds() {
        return sessionSeconds;
    }

    public boolean isActive() {
        return isActive;
    }
}
