package com.symbat.adventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Assets {

    public static Texture background1;
    public static Texture background2;
    public static Texture background3;


    public static Array<TextureRegion> playerWalkFrames;
    public static Array<TextureRegion> playerAttackFrames;
    public static Array<TextureRegion> playerDieFrames;
    public static TextureRegion playerIdleFrame;


    public static Array<TextureRegion> skeletonWalkFrames;
    public static Array<TextureRegion> skeletonAttackFrames;
    public static Array<TextureRegion> skeletonDieFrames;


    public static TextureRegion coinTexture;
    public static TextureRegion starTexture;
    public static TextureRegion shieldTexture;
    public static TextureRegion keyTexture;
    public static TextureRegion heartTexture;
    public static TextureRegion star;
    public static Texture pixel;


    public static Sound coinSound;
    public static Sound starSound;
    public static Sound shieldSound;
    public static Sound slashSound;
    public static Sound hurtSound;
    public static Sound deathSound;
    public static Sound jumpSound;
    public static Texture bulletTexture;

    public static void load() {

        background1 = new Texture(Gdx.files.internal("data/img/Background.png"));
        background2 = new Texture(Gdx.files.internal("data/img/bg.png"));
        background3 = new Texture(Gdx.files.internal("data/img/level3_bg.png"));

        loadPlayerAnimations();


        loadSkeletonAnimations();


        coinTexture = new TextureRegion(new Texture(Gdx.files.internal("data/Coin/Coin_01.png")));
        starTexture = new TextureRegion(new Texture(Gdx.files.internal("data/img/star.png")));
        shieldTexture = new TextureRegion(new Texture(Gdx.files.internal("data/shield/idle_1.png")));
        keyTexture = new TextureRegion(new Texture(Gdx.files.internal("data/img/key.png")));
        heartTexture = new TextureRegion(new Texture(Gdx.files.internal("data/img/heart.png")));

        coinSound = Gdx.audio.newSound(Gdx.files.internal("data/sounds/collect.mp3"));
        starSound = Gdx.audio.newSound(Gdx.files.internal("data/sounds/collect.mp3"));
        shieldSound = Gdx.audio.newSound(Gdx.files.internal("data/sounds/collect.mp3"));
        slashSound = Gdx.audio.newSound(Gdx.files.internal("data/sounds/slash.mp3"));
        deathSound = Gdx.audio.newSound(Gdx.files.internal("data/sounds/death.mp3"));

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        pixel = new Texture(pixmap);
        pixmap.dispose();

    }

    private static void loadPlayerAnimations() {
        playerWalkFrames = new Array<>();
        for (int i = 0; i < 7; i++) {
            String path = String.format("data/Player/Walking/walk_%03d.png", i);
            playerWalkFrames.add(new TextureRegion(new Texture(Gdx.files.internal(path))));
        }

        playerAttackFrames = new Array<>();
        for (int i = 0; i < 6; i++) {
            String path = String.format("data/Player/Slash/slash_%03d.png", i);
            playerAttackFrames.add(new TextureRegion(new Texture(Gdx.files.internal(path))));
        }

        playerDieFrames = new Array<>();
        for (int i = 0; i < 6; i++) {
            String path = String.format("data/Player/Dying/dying_%03d.png", i);
            playerDieFrames.add(new TextureRegion(new Texture(Gdx.files.internal(path))));
        }

        playerIdleFrame = new TextureRegion(new Texture(Gdx.files.internal("data/Player/Walking/walk_000.png")));
    }

    private static void loadSkeletonAnimations() {
        skeletonWalkFrames = new Array<>();
        for (int i = 0; i < 9; i++) {
            String path = String.format("data/Skeleton/Walk/walk_%03d.png", i);
            skeletonWalkFrames.add(new TextureRegion(new Texture(Gdx.files.internal(path))));
        }

        skeletonAttackFrames = new Array<>();
        for (int i = 0; i < 7; i++) {
            String path = String.format("data/Skeleton/Slash/slash_%03d.png", i);
            skeletonAttackFrames.add(new TextureRegion(new Texture(Gdx.files.internal(path))));
        }

        skeletonDieFrames = new Array<>();
        for (int i = 0; i < 6; i++) {
            String path = String.format("data/Skeleton/Dying/dying_%03d.png", i);
            skeletonDieFrames.add(new TextureRegion(new Texture(Gdx.files.internal(path))));
        }
    }

    public static void playSound(Sound sound) {
        sound.play(0.5f);
    }


    public static void dispose() {

        background1.dispose();
        background2.dispose();
        background3.dispose();

        coinSound.dispose();
        starSound.dispose();
        shieldSound.dispose();
        slashSound.dispose();
        hurtSound.dispose();
        deathSound.dispose();
        jumpSound.dispose();

        if (pixel != null) pixel.dispose();

    }
}
