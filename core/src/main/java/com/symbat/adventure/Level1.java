package com.symbat.adventure;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;


public class Level1 extends GameLevel {

    private Body body;

    public Level1(World world, AdventureGame game) {
        super(world, game);
        Skeleton skeleton2 = new Skeleton(world, 17, 3, -10);
        skeleton2.setPlayer(player);

    }


    @Override
    protected void createPlayer() {
        this.player = new Player(world, game);

        this.player.getBody().setTransform(-27, -1, 0f);
    }

    @Override
    protected void buildLayout() {

        platforms.add(new Platform(world, 4, 0.5f, -25, -13, PlatformImage.LEVEL1));
        platforms.add(new Platform(world, 2, 0.5f, -14, -10, PlatformImage.LEVEL1));
        platforms.add(new Platform(world, 5, 0.5f, 7, -1.5f, PlatformImage.LEVEL1));
        platforms.add(new Platform(world, 4, 0.5f, 4, -10, PlatformImage.LEVEL1));
        platforms.add(new Platform(world, 2, 0.5f, 17, -5, PlatformImage.LEVEL1));
        platforms.add(new Platform(world, 2, 0.5f, 27, -2, PlatformImage.LEVEL1));
        platforms.add(new Platform(world, 9, 0.5f, 21, -13, PlatformImage.LEVEL1));

        movingPlatforms.add(new MovingPlatform(world, 2, 0.5f, -5, -6, 1.5f, PlatformImage.LEVEL1));


        Coin[] coins = {
            new Coin(world, -14, -7),
            new Coin(world, 2, -7),
            new Coin(world, 4, -7),
            new Coin(world, 6, -7),
            new Coin(world, 17, -2),
            new Coin(world, -5, -3),
        };


        stars.add(new Star(world, 27, 1));
        stars.add(new Star(world, 17, -10));
        stars.add(new Star(world, 7, 1));


        key = new Key(world, 26, -10, game);
    }

    @Override
    protected String backgroundPath() {


        return "data/img/Background.png";
    }

    public Body getBody() {
        return body;
    }
}
