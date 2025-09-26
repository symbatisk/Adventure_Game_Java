package com.symbat.adventure;

import com.badlogic.gdx.InputProcessor;

class PlayerController implements InputProcessor {
    private Player player;

    public PlayerController(Player player) {
        this.player = player;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == com.badlogic.gdx.Input.Keys.RIGHT) {
            player.move(4);
        } else if (keycode == com.badlogic.gdx.Input.Keys.LEFT) {
            player.move(-4);
        } else if (keycode == com.badlogic.gdx.Input.Keys.SPACE) {
            player.jump(10);
        } else if (keycode == com.badlogic.gdx.Input.Keys.D) {
            player.startAttack();
        } else if (keycode == com.badlogic.gdx.Input.Keys.A) {
            player.activateShield();
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == com.badlogic.gdx.Input.Keys.RIGHT || keycode == com.badlogic.gdx.Input.Keys.LEFT) {
            player.stop();
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    public void updatePlayer(Player player) {
        this.player = player;
    }
}
