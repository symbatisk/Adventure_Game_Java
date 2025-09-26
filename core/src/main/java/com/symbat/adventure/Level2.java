package com.symbat.adventure;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Level2 extends GameLevel {

    public Level2(World world, AdventureGame game) {
        super(world, game);

        Warrior warrior = new Warrior(world, -3, 2, 0);
        Skeleton skeleton1 = new Skeleton(world, -6, 3, -14);
        Skeleton skeleton2 = new Skeleton(world, 21, 3, -7);


        skeleton1.setPlayer(player);
        skeleton2.setPlayer(player);
        warrior.setPlayer(player);
    }

    @Override
    protected void createPlayer() {
        this.player = new Player(world, game);

        this.player.getBody().setTransform(-27, -1, 0f);
    }

    @Override
    protected void buildLayout() {

        platforms.add(new Platform(world, 5, 0.5f, -25.5f, -8, PlatformImage.LEVEL2));
        platforms.add(new Platform(world, 2, 0.5f, -16.5f, -12, PlatformImage.LEVEL2));
        platforms.add(new Platform(world, 7, 0.5f, -5.5f, -2, PlatformImage.LEVEL2));
        platforms.add(new Platform(world, 7, 0.5f, -5.5f, -16, PlatformImage.LEVEL2));
        platforms.add(new Platform(world, 4, 0.5f, 8, -12, PlatformImage.LEVEL2));
        platforms.add(new Platform(world, 10, 0.5f, 25.5f, -8, PlatformImage.LEVEL2));
        platforms.add(new Platform(world, 6, 0.5f, 22, 4, PlatformImage.LEVEL2));



        movingPlatforms.add(new MovingPlatform(world, 2, 0.5f, 9, 1, 2, PlatformImage.LEVEL2));


        Coin[] coins = {
            new Coin(world, 6, -9),
            new Coin(world, 8, -9),
            new Coin(world, 10, -9),
            new Coin(world, 9, 4),

        };


        stars.add(new Star(world, -3, 1));
        stars.add(new Star(world, 21, -5));
        stars.add(new Star(world, -6, -13));


        key = new Key(world, 23, 7, game);
    }

    @Override
    protected String backgroundPath() {
        return "data/img/bg.png"; // твой фон
    }
}
