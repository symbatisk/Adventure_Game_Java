package com.symbat.adventure;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import java.util.List;

public abstract class Enemy {
    protected enum AnimationState { PATROL, ATTACK, DIE }

    protected Body body;
    protected World world;
    protected Player player;

    protected int hp;
    protected boolean isDead = false;
    protected AnimationState state = AnimationState.PATROL;

    protected boolean movingRight = true;
    protected float leftLimit, rightLimit;

    protected float attackRangeX = 5f;
    protected float attackRangeY = 2f;

    protected int currentFrame = 0;
    protected float animationTimer = 0;
    protected static final float FRAME_DURATION = 0.1f;

    protected boolean attackSoundPlayed = false;
    protected boolean deathSoundPlayed = false;

    public Enemy(World world, float platformX, float halfWidth, float y, int hp) {
        this.world = world;
        this.hp = hp;
        this.leftLimit = platformX - halfWidth;
        this.rightLimit = platformX + halfWidth;

        createBody(platformX, y);
    }

    private void createBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1f, 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.4f;

        body.createFixture(fixtureDef);
        shape.dispose();

        body.setUserData(this);
    }

    public void update(float delta) {
        if (isDead && state != AnimationState.DIE) return;

        animationTimer += delta;
        if (animationTimer >= FRAME_DURATION) {
            animationTimer = 0;
            currentFrame = (currentFrame + 1) % getCurrentAnimation().size();
        }

        switch (state) {
            case PATROL:
                patrol();
                checkPlayerDistance();
                break;
            case ATTACK:
                attack();
                break;
            case DIE:
                dieAnimation();
                break;
        }
    }

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

    protected void patrol() {
        float currentX = body.getPosition().x;

        if (movingRight && currentX >= rightLimit) {
            movingRight = false;
        } else if (!movingRight && currentX <= leftLimit) {
            movingRight = true;
        }

        if (movingRight) {
            body.setLinearVelocity(2, body.getLinearVelocity().y);
        } else {
            body.setLinearVelocity(-2, body.getLinearVelocity().y);
        }
    }

    protected void checkPlayerDistance() {
        if (player == null || player.isDead()) return;

        Vector2 playerPos = player.getPosition();
        Vector2 enemyPos = body.getPosition();

        float distanceY = Math.abs(enemyPos.y - playerPos.y);
        float distanceX = enemyPos.x - playerPos.x;

        if (Math.abs(distanceX) < attackRangeX && distanceY < attackRangeY) {
            body.setLinearVelocity(0, body.getLinearVelocity().y);
            state = AnimationState.ATTACK;
            currentFrame = 0;
            attackSoundPlayed = false;
        }
    }

    protected abstract void attack();

    protected void dieAnimation() {
        if (currentFrame >= getDieFrames().size() - 1) {
            world.destroyBody(body);
        }
    }

    public void takeDamage(int amount) {
        if (!isDead) {
            hp -= amount;
            if (hp <= 0) {
                die();
            }
        }
    }

    protected void die() {
        isDead = true;
        state = AnimationState.DIE;
        currentFrame = 0;
        deathSoundPlayed = false;
        body.setLinearVelocity(0, 0);
    }

    public boolean isDead() {
        return isDead;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    protected abstract List<Texture> getWalkFrames();
    protected abstract List<Texture> getAttackFrames();
    protected abstract List<Texture> getDieFrames();

    protected List<Texture> getCurrentAnimation() {
        switch (state) {
            case PATROL: return getWalkFrames();
            case ATTACK: return getAttackFrames();
            case DIE: return getDieFrames();
            default: return getWalkFrames();
        }
    }

    protected abstract void playAttackSound();

    public void dispose() {
        for (Texture texture : getWalkFrames()) {
            texture.dispose();
        }
        for (Texture texture : getAttackFrames()) {
            texture.dispose();
        }
        for (Texture texture : getDieFrames()) {
            texture.dispose();
        }
    }
}
