package com.symbat.adventure;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import java.util.ArrayList;
import java.util.List;

public class Wraith extends Enemy {
    private static final float MIN_ATTACK_DISTANCE = 6f;
    private static final float RETREAT_SPEED = 3f;
    private static final int SHOOT_INTERVAL = 120;
    private int stepCount = 0;

    private List<Texture> walkFrames;
    private List<Texture> attackFrames;
    private List<Texture> dieFrames;

    private int currentFrame = 0;
    private float animationTimer = 0;
    private static final float FRAME_DURATION = 0.1f;

    public Wraith(World world, float platformX, float halfWidth, float y) {
        super(world, platformX, halfWidth, y, 4);
        loadTextures();
        this.attackRangeX = 8f;
        this.attackRangeY = 2f;
    }

    private void loadTextures() {
        walkFrames = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            String path = String.format("data/Wraith/Walk/walk_%03d.png", i);
            walkFrames.add(new Texture(path));
        }

        attackFrames = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            String path = String.format("data/Wraith/Attack/attack_%03d.png", i);
            attackFrames.add(new Texture(path));
        }

        dieFrames = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            String path = String.format("data/Wraith/Die/dying_%03d.png", i);
            dieFrames.add(new Texture(path));
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (state == AnimationState.ATTACK) {
            stepCount++;
            if (stepCount >= SHOOT_INTERVAL) {
                shoot();
                stepCount = 0;
            }
        } else {
            stepCount = 0;
        }

        animationTimer += delta;
        if (animationTimer >= FRAME_DURATION) {
            animationTimer = 0;
            currentFrame = (currentFrame + 1) % getCurrentAnimation().size();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Vector2 pos = body.getPosition();
        Texture currentTexture = getCurrentAnimation().get(currentFrame);

        float width = 2f;
        float height = 4f;

        if (!movingRight) {
            batch.draw(currentTexture,
                pos.x + width/2, pos.y - height/2,
                -width, height);
        } else {
            batch.draw(currentTexture,
                pos.x - width/2, pos.y - height/2,
                width, height);
        }
    }

    @Override
    protected void attack() {
        float dx = body.getPosition().x - player.getPosition().x;

        if (Math.abs(dx) < MIN_ATTACK_DISTANCE) {
            float retreatDir = dx < 0 ? -1 : 1;
            body.setLinearVelocity(RETREAT_SPEED * retreatDir, body.getLinearVelocity().y);
            return;
        }

        body.setLinearVelocity(0, body.getLinearVelocity().y);
        movingRight = dx < 0;

        if (currentFrame == 2) {
            shoot();
        }
    }

    private void shoot() {
        Vector2 pos = body.getPosition();
        float offset = 1.5f;
        Vector2 spawnPos = new Vector2(
            pos.x + (movingRight ? offset : -offset),
            pos.y
        );
        new Bullet(world, spawnPos, movingRight);
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
        SoundManager.playSound("shoot");
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
        super.dispose();
    }
}
