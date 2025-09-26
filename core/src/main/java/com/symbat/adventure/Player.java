package com.symbat.adventure;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;


import java.util.ArrayList;
import java.util.List;

public class Player {
    private Body body;
    private World world;
    private AdventureGame game;
    private boolean canJump = false;



    private enum AnimationState { IDLE, WALK, ATTACK, DIE }
    private AnimationState state = AnimationState.IDLE;

    private List<Texture> walkFrames;
    private List<Texture> attackFrames;
    private List<Texture> dieFrames;

    private int currentFrame = 0;
    private float animationTimer = 0;
    private static final float FRAME_DURATION = 0.1f;

    private boolean isMovingRight = true;
    private int hp = 6;
    private boolean isDead = false;
    private boolean finishedDying = false;

    private int starCount = 0;
    private int coinCount = 0;

    private boolean shieldActive = false;
    private long shieldStartTime;
    private boolean hasShield = false;
    private static final long SHIELD_DURATION = 10000;
    private ActiveShield activeShield;

    public Player(World world, AdventureGame game) {
        this.world = world;
        this.game = game;
        createBody(0, 0);
        loadTextures();
    }

    public Player(World world, float x, float y) {
        this.world = world;
        createBody(x, y);
        loadTextures();
    }

    private void createBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1.5f, 2.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.4f;

        body.createFixture(fixtureDef);
        shape.dispose();

        body.setUserData(this);
    }

    private void loadTextures() {
        walkFrames = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            walkFrames.add(new Texture("data/Player/Walking/walk_" + String.format("%03d", i) + ".png"));
        }

        attackFrames = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            attackFrames.add(new Texture("data/Player/Slash/slash_" + String.format("%03d", i) + ".png"));
        }

        dieFrames = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            dieFrames.add(new Texture("data/Player/Dying/dying_" + String.format("%03d", i) + ".png"));
        }
    }

    public void update(float delta) {
        if (isDead && state == AnimationState.DIE) {
            animationTimer += delta;
            if (animationTimer >= FRAME_DURATION) {
                animationTimer = 0;
                if (currentFrame < dieFrames.size() - 1) {
                    currentFrame++;
                } else {
                    finishedDying = true;
                }
            }
            return;
        }

        animationTimer += delta;
        if (animationTimer >= FRAME_DURATION) {
            animationTimer = 0;

            switch (state) {
                case WALK:
                    currentFrame = (currentFrame + 1) % walkFrames.size();
                    break;
                case ATTACK:
                    if (currentFrame < attackFrames.size() - 1) {
                        currentFrame++;
                    } else {
                        state = AnimationState.IDLE;
                        checkHitEnemy();
                    }
                    break;
                case DIE:
                    if (currentFrame < dieFrames.size() - 1) {
                        currentFrame++;
                    } else {
                        finishedDying = true;
                    }
                    break;
                default:
                    break;
            }
        }

        updateShield();
    }

    public void render(SpriteBatch batch) {
        Vector2 pos = body.getPosition();
        Texture currentTexture = getCurrentTexture();

        float width = 3f;
        float height = 5f;

        if (!isMovingRight) {
            batch.draw(currentTexture,
                pos.x + width/2, pos.y - height/2,
                -width, height);
        } else {
            batch.draw(currentTexture,
                pos.x - width/2, pos.y - height/2,
                width, height);
        }
    }

    private Texture getCurrentTexture() {
        switch (state) {
            case WALK: return walkFrames.get(currentFrame);
            case ATTACK: return attackFrames.get(currentFrame);
            case DIE: return dieFrames.get(currentFrame);
            default: return walkFrames.get(6);
        }
    }

    public void move(float speed) {
        if (isDead || state == AnimationState.ATTACK) return;

        body.setLinearVelocity(speed, body.getLinearVelocity().y);
        isMovingRight = speed > 0;

        if (state != AnimationState.WALK) {
            state = AnimationState.WALK;
            currentFrame = 0;
        }
    }

    public void jump() {
        jump(10);
    }

    public void jump(int force) {
        if (canJump && !isDead) {
            body.applyLinearImpulse(new Vector2(0, force), body.getWorldCenter(), true);
            canJump = false;
            SoundManager.playSound("jump");
        }
    }

    public void stop() {
        body.setLinearVelocity(0, body.getLinearVelocity().y);
        if (state != AnimationState.ATTACK && !isDead) {
            state = AnimationState.IDLE;
        }
    }

    public void startAttack() {
        if (state != AnimationState.ATTACK && !isDead) {
            state = AnimationState.ATTACK;
            currentFrame = 0;
            SoundManager.playSound("slash");
        }
    }

    private void die() {
        isDead = true;
        state = AnimationState.DIE;
        currentFrame = 0;
        body.setLinearVelocity(0, 0);
        SoundManager.playSound("death");
    }

    public void takeDamage(int amount) {
        if (!isDead && !shieldActive) {
            hp -= amount;
            SoundManager.playSound("hurt");
            if (hp <= 0) {
                die();
            }
        }
    }

    private void checkHitEnemy() {
        if (isDead) return;
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);
        for (Body body : bodies) {
            Object userData = body.getUserData();
            if (userData instanceof Enemy) {
                Enemy enemy = (Enemy) userData;
                Vector2 enemyPos = body.getPosition();
                Vector2 playerPos = this.body.getPosition();

                float distanceX = Math.abs(playerPos.x - enemyPos.x);
                float distanceY = Math.abs(playerPos.y - enemyPos.y);
                float attackRangeX = shieldActive ? 9f : 5f;

                if (!enemy.isDead() && distanceX < attackRangeX && distanceY < 2) {
                    enemy.takeDamage(1);
                }
            }
        }
    }

    public void collectShield() {
        hasShield = true;
    }

    public void activateShield() {
        if (hasShield && !shieldActive) {
            shieldActive = true;
            hasShield = false;
            shieldStartTime = System.currentTimeMillis();
            activeShield = new ActiveShield(world, this);
        }
    }

    private void updateShield() {
        if (shieldActive) {
            long elapsedTime = System.currentTimeMillis() - shieldStartTime;
            if (elapsedTime > SHIELD_DURATION) {
                shieldActive = false;
                if (activeShield != null) {
                    activeShield.destroy();
                    activeShield = null;
                }
            }
        }
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public Body getBody() {
        return body;
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean isFinishedDying() {
        return finishedDying;
    }

    public boolean isMovingRight() {
        return isMovingRight;
    }

    public int getHp() {
        return hp;
    }

    public void increaseHp(int amount) {
        hp += amount;
    }

    public int getCoinCount() {
        return coinCount;
    }

    public void increaseCoinCount() {
        coinCount += 20;
    }

    public int getStarCount() {
        return starCount;
    }

    public void increaseStarCount() {
        starCount++;
    }

    public boolean isShieldActive() {
        return shieldActive;
    }

    public long getShieldTimeLeft() {
        if (!shieldActive) return 0;
        return Math.max(0, SHIELD_DURATION - (System.currentTimeMillis() - shieldStartTime));
    }

    public void setGrounded(boolean grounded) {
        this.canJump = grounded;
    }

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

    public void collectCoin() {
        increaseCoinCount();
        SoundManager.playSound("coin");
    }

    public void collectStar() {
        increaseStarCount();
        SoundManager.playSound("star");
    }

    public void collectShieldPower() {
        shieldActive = true;
        shieldStartTime = System.currentTimeMillis();
        SoundManager.playSound("shield");
    }
}
