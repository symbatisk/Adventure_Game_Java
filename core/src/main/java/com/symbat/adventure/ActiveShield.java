package com.symbat.adventure;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class ActiveShield {
    private static final float OFFSET = 3f;
    private final Player player;
    private final long startTime;
    private Body body;
    private World world;
    private boolean active = true;

    public ActiveShield(World world, Player player) {
        this.world = world;
        this.player = player;
        this.startTime = System.currentTimeMillis();

        createBody();
    }

    private void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(player.getPosition().x, player.getPosition().y);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1f, 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef);
        shape.dispose();

        body.setUserData(this);
        body.setGravityScale(0);
    }

    public void update() {
        if (!active) return;


        float offsetX = player.isMovingRight() ? OFFSET : -OFFSET;
        body.setTransform(
            player.getPosition().x + offsetX,
            player.getPosition().y,
            0
        );


        if (System.currentTimeMillis() - startTime > 10000) {
            destroy();
        }
    }

    public void render(SpriteBatch batch) {
        if (!active) return;

        batch.draw(Assets.shieldTexture,
            body.getPosition().x - 1f,
            body.getPosition().y - 2f,
            2f, 4f);
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
