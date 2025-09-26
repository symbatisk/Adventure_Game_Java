package com.symbat.adventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Star {

    private final World world;
    private Body body;
    private final TextureRegion region;
    private boolean collected;

    public Star(World world, float x, float y) {
        this.world = world;

        this.region = Assets.starTexture;
        if (this.region == null) {
            Gdx.app.error("Star", "Assets.starTexture == null — звезда не будет отображаться!");
        }

        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(x, y);
        body = world.createBody(bdef);

        CircleShape shape = new CircleShape();
        shape.setRadius(0.3f);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.isSensor = true;

        body.createFixture(fdef).setUserData(this);
        shape.dispose();
    }

    public void render(SpriteBatch batch) {
        if (collected) return;
        if (region == null || body == null) return;

        Vector2 pos = body.getPosition();
        batch.draw(region, pos.x - 0.5f, pos.y - 0.5f, 1f, 1f);
    }

    public void update(float delta) {

    }

    public void collect(Player player) {
        if (collected) return;
        collected = true;

        if (player != null) {
            player.collectStar();
        }

        if (body != null && world != null) {
            world.destroyBody(body);
            body = null;
        }
    }

    public boolean isCollected() {
        return collected;
    }

    public void dispose() {
        if (body != null && world != null) {
            world.destroyBody(body);
            body = null;
        }
    }
}
