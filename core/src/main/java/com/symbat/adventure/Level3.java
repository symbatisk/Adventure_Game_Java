package com.symbat.adventure;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Level3 extends GameLevel {

    public Level3(World world, AdventureGame game) {
        super(world, game);
        Shield shield = new Shield(world,8, -1);

        Skeleton skeleton = new Skeleton(world, -14, 3, -8);
        Warrior warrior = new Warrior(world, 6, 3, -10);
        Wraith wraith = new Wraith(world, -13, 3, 7);


        skeleton.setPlayer(player);
        warrior.setPlayer(player);
        wraith.setPlayer(player);
    }

    @Override
    protected void createPlayer() {
        this.player = new Player(world, game);

        this.player.getBody().setTransform(-27, -1, 0f);
    }

    @Override
    protected void buildLayout() {

        platforms.add(new Platform(world, 4, 0.5f, -26, -13, PlatformImage.LEVEL3));
        platforms.add(new Platform(world, 5, 0.5f, -14, -10, PlatformImage.LEVEL3));
        platforms.add(new Platform(world, 6, 0.5f, 6, -12, PlatformImage.LEVEL3));
        platforms.add(new Platform(world, 4, 0.5f, 26, -12, PlatformImage.LEVEL3));
        platforms.add(new Platform(world, 2, 0.5f, 8, -3, PlatformImage.LEVEL3));
        platforms.add(new Platform(world, 8, 0.5f, 23, 1, PlatformImage.LEVEL3));
        platforms.add(new Platform(world, 11, 0.5f, -19, 4, PlatformImage.LEVEL3));
        platforms.add(new Platform(world, 4, 0.5f, -1, 7, PlatformImage.LEVEL3));

        movingPlatforms.add(new MovingPlatform(world, 2, 0.5f, -3, -6, 1, PlatformImage.LEVEL3));
        movingPlatforms.add(new MovingPlatform(world, 2, 0.5f, 17, -7.5f, 1, PlatformImage.LEVEL3));

        Coin[] coins = {
            new Coin(world, -3, -3),
            new Coin(world, 24, -9),
            new Coin(world, 26, -9),
            new Coin(world, 28, -9),

            new Coin(world, 18, 4),
            new Coin(world, 20, 4),
            new Coin(world, 22, 4),

        };



        stars.add(new Star(world, -14, -7));
        stars.add(new Star(world, 6, -9));
        stars.add(new Star(world, -13, 7));



        key = new Key(world, 0, 10, game);
    }

    @Override
    protected String backgroundPath() {
        return "data/img/BackgroundLevel3.png";
    }

}
