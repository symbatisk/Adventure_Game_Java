package com.symbat.adventure;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import java.util.ArrayList;
import java.util.List;

public class Portal {
    private Body body;
    private World world;
    private Portal linkedPortal;
    private boolean isActive = true;
    private static final float COOLDOWN = 1.5f;
    private final Vector2 entryPoint;
    private final Vector2 exitPoint;

    private List<Texture> portalFrames;
    private int currentFrame = 0;
    private float animationTimer = 0;
    private static final float FRAME_DURATION = 0.1f;

    private boolean coolingDown = false;
    private float cooldownTimer = 0;

    public Portal(World world, Vector2 entryPoint, Vector2 exitPoint) {
        this.world = world;
        this.entryPoint = entryPoint;
        this.exitPoint = exitPoint;

        loadTextures();
        createBody();
    }

    private void loadTextures() {
        portalFrames = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            portalFrames.add(new Texture("data/portal/portal" + i + ".png"));
        }
    }

    private void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(entryPoint);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 1f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef);
        shape.dispose();

        body.setUserData(this);
    }

    public void linkPortals(Portal otherPortal) {
        this.linkedPortal = otherPortal;
        otherPortal.linkedPortal = this;

        if (this.body.getPosition().x > otherPortal.body.getPosition().x) {
            otherPortal.body.setTransform(
                otherPortal.body.getPosition().x + 1,
                otherPortal.body.getPosition().y,
                0
            );
        }
    }

    public void update(float delta) {
        animationTimer += delta;
        if (animationTimer >= FRAME_DURATION) {
            animationTimer = 0;
            currentFrame = (currentFrame + 1) % portalFrames.size();
        }

        if (coolingDown) {
            cooldownTimer += delta;
            if (cooldownTimer >= COOLDOWN) {
                coolingDown = false;
                cooldownTimer = 0;
                isActive = true;
                if (linkedPortal != null) {
                    linkedPortal.isActive = true;
                }
            }
        }
    }

    public void render(SpriteBatch batch) {
        Vector2 pos = body.getPosition();
        batch.draw(portalFrames.get(currentFrame),
            pos.x - 0.5f,
            pos.y - 1f,
            1f, 2f);
    }

    public void teleportPlayer(Player player) {
        if (linkedPortal != null && isActive && !coolingDown) {
            Vector2 exitPos = new Vector2(
                linkedPortal.body.getPosition().x + 1,
                linkedPortal.body.getPosition().y
            );
            player.getBody().setTransform(exitPos, 0);

            isActive = false;
            linkedPortal.isActive = false;
            coolingDown = true;
            linkedPortal.coolingDown = true;
        }
    }

    public void dispose() {
        for (Texture texture : portalFrames) {
            texture.dispose();
        }
        world.destroyBody(body);
    }

    public Body getBody() {
        return body;
    }
}
