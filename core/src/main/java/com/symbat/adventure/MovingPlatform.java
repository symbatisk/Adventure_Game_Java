
package com.symbat.adventure;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class MovingPlatform extends Platform {
    private float startX;
    private float distance;
    private float speed;
    private boolean movingRight = true;

    public MovingPlatform(World world, float width, float height, float x, float y, float distance, PlatformImage skin) {
        super(world, width, height, x, y, skin);
        this.startX = x;
        this.distance = distance;
        this.speed = 2f;
    }

    public void update() {
        Body body = getBody();
        float posX = body.getPosition().x;

        if (movingRight) {
            body.setTransform(posX + speed * 0.016f, body.getPosition().y, 0);
            if (posX > startX + distance) movingRight = false;
        } else {
            body.setTransform(posX - speed * 0.016f, body.getPosition().y, 0);
            if (posX < startX - distance) movingRight = true;
        }
    }
}
