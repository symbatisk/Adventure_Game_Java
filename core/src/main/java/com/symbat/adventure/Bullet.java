package com.symbat.adventure;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Bullet {
    private Body body;
    private World world;
    private boolean active = true;
    private boolean goRight;
    private static final float SPEED = 10f;
    private static final float RADIUS = 0.4f;

    public Bullet(World world, Vector2 startPos, boolean goRight) {
        this.world = world;
        this.goRight = goRight;
        createBody(startPos);
        applyInitialVelocity();
    }

    private void createBody(Vector2 startPos) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(startPos);

        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(RADIUS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;

        body.createFixture(fixtureDef);
        shape.dispose();

        body.setGravityScale(0);
        body.setUserData(this);
    }

    private void applyInitialVelocity() {
        body.setLinearVelocity(goRight ? SPEED : -SPEED, 0);
    }

    public void update() {
        if (!active) return;

        if (body.getPosition().x > 50 || body.getPosition().x < -50) {
            destroy();
        }
    }

    public void render(SpriteBatch batch) {
        if (!active) return;

        Vector2 pos = body.getPosition();
        batch.draw(Assets.bulletTexture,
            pos.x - RADIUS,
            pos.y - RADIUS,
            RADIUS * 2,
            RADIUS * 2);
    }

    public void destroy() {
        active = false;
        world.destroyBody(body);
    }

    public boolean isActive() {
        return active;
    }

    public Body getBody() {
        return body;
    }
}
