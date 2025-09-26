package com.symbat.adventure;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import java.util.ArrayList;
import java.util.List;

public class Shield {
    private Body body;
    private World world;
    private List<Texture> shieldFrames;
    private int currentFrame = 0;
    private float animationTimer = 0;
    private static final float FRAME_DURATION = 0.2f;

    public Shield(World world, float x, float y) {
        this.world = world;
        loadTextures();
        createBody(x, y);
    }

    private void loadTextures() {
        shieldFrames = new ArrayList<>();
        shieldFrames.add(new Texture("data/shield/idle_1.png"));
        shieldFrames.add(new Texture("data/shield/idle_3.png"));
        shieldFrames.add(new Texture("data/shield/idle_4.png"));
        shieldFrames.add(new Texture("data/shield/idle_2.png"));
    }

    private void createBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1f, 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef);
        shape.dispose();

        body.setGravityScale(0);
        body.setUserData(this);
    }

    public void update(float delta) {
        animationTimer += delta;
        if (animationTimer >= FRAME_DURATION) {
            animationTimer = 0;
            currentFrame = (currentFrame + 1) % shieldFrames.size();
        }
    }

    public void render(SpriteBatch batch) {
        Vector2 pos = body.getPosition();
        batch.draw(shieldFrames.get(currentFrame), pos.x - 1f, pos.y - 2f, 2f, 4f);
    }

    public void dispose() {
        for (Texture texture : shieldFrames) {
            texture.dispose();
        }
        world.destroyBody(body);
    }

    public Body getBody() {
        return body;
    }


    public void collect() {

    }
}
