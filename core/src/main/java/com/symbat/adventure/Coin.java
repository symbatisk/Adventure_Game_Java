package com.symbat.adventure;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import java.util.ArrayList;
import java.util.List;

public class Coin {
    private Body body;
    private World world;

    private List<Texture> frames;
    private int currentFrame = 0;
    private float animationTimer = 0;
    private static final float FRAME_DURATION = 0.08f;

    private boolean collected = false;

    public Coin(World world, float x, float y) {
        this.world = world;
        createBody(x, y);
        loadTextures();
    }

    private void createBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(0.7f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef);
        shape.dispose();

        body.setUserData(this);
    }

    private void loadTextures() {
        frames = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            frames.add(new Texture("data/Coin/Coin_" + String.format("%02d", i) + ".png"));
        }
    }

    public void update(float delta) {
        if (collected) return;

        animationTimer += delta;
        if (animationTimer >= FRAME_DURATION) {
            animationTimer = 0;
            currentFrame = (currentFrame + 1) % frames.size();
        }
    }

    public void render(SpriteBatch batch) {
        if (collected) return;

        Vector2 pos = body.getPosition();
        Texture currentTexture = frames.get(currentFrame);

        float width = 1.4f;
        float height = 1.4f;

        batch.draw(currentTexture,
            pos.x - width/2, pos.y - height/2,
            width, height);
    }

    public void collect(Player player) {
        collected = true;
        world.destroyBody(body);
    }

    public boolean isCollected() {
        return collected;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public void dispose() {
        for (Texture texture : frames) {
            texture.dispose();
        }
    }

    public Body getBody() {
        return body;
    }
}
