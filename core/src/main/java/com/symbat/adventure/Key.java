
package com.symbat.adventure;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Key {
    private Body body;
    private World world;
    private AdventureGame game;
    private Texture texture;
    private boolean collected = false;

    public Key(World world, float x, float y, AdventureGame game) {
        this.world = world;
        this.game = game;
        this.texture = new Texture("data/img/key.png");
        createBody(x, y);
    }

    private void createBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef);
        shape.dispose();

        body.setUserData(this);
    }

    public void collect(Player player) {
        if (!collected) {
            collected = true;

            GameLevel currentLevel = game.getCurrentLevel();
            LevelProgress progress = game.getLevelProgress(currentLevel);
            progress.updateIfBetter(player.getStarCount(), player.getCoinCount());

            game.saveProgress(currentLevel);
            game.showVictoryScreen();

            world.destroyBody(body);
        }
    }

    public void render(SpriteBatch batch) {
        if (!collected) {
            Vector2 pos = body.getPosition();
            batch.draw(texture, pos.x - 0.5f, pos.y - 0.5f, 1f, 1f);
        }
    }

    public boolean isCollected() {
        return collected;
    }

    public void dispose() {
        texture.dispose();
    }
    public Body getBody() {
        return body;
    }
}
