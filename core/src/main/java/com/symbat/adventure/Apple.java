package com.symbat.adventure;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Apple {
    private Body body;
    private World world;
    private Texture texture;
    private boolean collected = false;

    public Apple(World world, float x, float y) {
        this.world = world;
        this.texture = new Texture("data/img/apple.png");
        createBody(x, y);
    }

    private void createBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(1.2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef);
        shape.dispose();

        body.setUserData(this);
    }

    public void render(SpriteBatch batch) {
        if (!collected) {
            Vector2 pos = body.getPosition();
            batch.draw(texture,
                pos.x - 1.2f,
                pos.y - 1.2f,
                2.4f, 2.4f);
        }
    }

    public void collect(Player player) {
        collected = true;
        world.destroyBody(body);
    }

    public boolean isCollected() {
        return collected;
    }

    public void dispose() {
        texture.dispose();
    }
}
