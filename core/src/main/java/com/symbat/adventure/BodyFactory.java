package com.symbat.adventure;

import com.badlogic.gdx.physics.box2d.*;

public class BodyFactory {
    public static Body createStaticBoxLL(
        World world,
        float xLeft, float yBottom,
        float width, float height,
        Object userData
    ) {
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.StaticBody;

        bd.position.set(xLeft + width/2f, yBottom + height/2f);

        Body body = world.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2f, height/2f);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        Fixture fx = body.createFixture(fd);

        if (userData != null) {
            body.setUserData(userData);
            fx.setUserData(userData);
        }

        shape.dispose();
        return body;
    }
}

