package com.symbat.adventure;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import java.util.List;

public class Warrior extends Enemy {
    private List<Texture> walkFrames;
    private List<Texture> attackFrames;
    private List<Texture> dieFrames;

    public Warrior(World world, float platformX, float halfWidth, float y) {
        super(world, platformX, halfWidth, y, 3);
        loadTextures();
    }

    private void loadTextures() {
        walkFrames = new ArrayList<>();
        for (int i = 0; i <= 7; i++) {
            walkFrames.add(new Texture("data/Warrior/Walk/walk_" + String.format("%03d", i) + ".png"));
        }

        attackFrames = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            attackFrames.add(new Texture("data/Warrior/Slash/slash_" + String.format("%03d", i) + ".png"));
        }

        dieFrames = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            dieFrames.add(new Texture("data/Warrior/Dying/dying_" + String.format("%03d", i) + ".png"));
        }
    }

    @Override
    protected List<Texture> getWalkFrames() {
        return walkFrames;
    }

    @Override
    protected List<Texture> getAttackFrames() {
        return attackFrames;
    }

    @Override
    protected List<Texture> getDieFrames() {
        return dieFrames;
    }

    @Override
    protected void playAttackSound() {
        SoundManager.playSound("slash");
    }

    @Override
    protected void attack() {
        if (currentFrame == 3 && !attackSoundPlayed) {
            playAttackSound();
            attackSoundPlayed = true;

            if (Math.abs(body.getPosition().x - player.getPosition().x) < 2.5f) {
                player.takeDamage(1);
            }
        }
    }

    @Override
    public void dispose() {
        for (Texture texture : walkFrames) {
            texture.dispose();
        }
        for (Texture texture : attackFrames) {
            texture.dispose();
        }
        for (Texture texture : dieFrames) {
            texture.dispose();
        }
    }
}
