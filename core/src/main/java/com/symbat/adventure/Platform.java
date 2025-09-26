package com.symbat.adventure;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;

public class Platform {
    private Body body;
    private float width;
    private float height;
    private PlatformImage skin;

    public Platform(World world, float width, float height, float x, float y, PlatformImage skin) {
        this.width = width;
        this.skin = skin;
        this.height = height;

        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.StaticBody;
        bd.position.set(x, y);

        body = world.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2f, height / 2f);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        body.createFixture(fd).setUserData(this);
        shape.dispose();
    }

    public void render(SpriteBatch batch) {
        TextureRegion start = skin.getStart();
        TextureRegion middle = skin.getMiddle();
        TextureRegion end = skin.getEnd();

        float tileW = 1f;
        int tiles = Math.max(2, (int) Math.floor(width / tileW));

        float baseX = body.getPosition().x - width / 2f;
        float y = body.getPosition().y - 0.5f;

        batch.draw(start, baseX, y, tileW, 1f);

        for (int i = 1; i < tiles - 1; i++) {
            batch.draw(middle, baseX + i * tileW, y, tileW, 1f);
        }

        batch.draw(end, baseX + (tiles - 1) * tileW, y, tileW, 1f);
    }

    public Body getBody() { return body; }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public PlatformImage getSkin() {
        return skin;
    }
    public void dispose() {

    }
}
