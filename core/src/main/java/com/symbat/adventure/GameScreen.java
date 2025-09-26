package com.symbat.adventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class GameScreen implements Screen {

    private final AdventureGame game;
    private final int levelNumber;

    private World world;
    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;
    private SpriteBatch batch;

    private GameLevel level;
    private boolean debugMode = false;

    private float accumulator = 0f;
    private static final float TIME_STEP = 1f / 60f;


    private Texture background1, background2, background3;
    private Texture heartTex, coinTex, starTex, shieldTex;

    public GameScreen(AdventureGame game, int levelNumber) {
        this.game = game;
        this.levelNumber = levelNumber;

        this.world = new World(new Vector2(0, -9.8f), true);
        this.world.setContactListener(new GameContactListener(game));

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 40f, 24f);

        this.batch = game.getBatch();
        this.debugRenderer = new Box2DDebugRenderer();


        background1 = safeTexture("data/img/Background.png");
        background2 = safeTexture("data/img/bg.png");
        background3 = safeTexture("data/img/level3_bg.png");

        heartTex  = safeTexture("data/img/heart.png");
        coinTex   = safeTexture("data/Coin/Coin_01.png");
        starTex   = safeTexture("data/img/star.png");
        shieldTex = safeTexture("data/shield/idle_1.png");


        this.level = createLevel(levelNumber);
    }

    private Texture safeTexture(String path) {
        if (Gdx.files.internal(path).exists()) return new Texture(path);
        Gdx.app.error("ASSET", "Файл не найден: " + path);
        return null;
    }

    private GameLevel createLevel(int number) {
        switch (number) {
            case 1: return new Level1(world, game);
            case 2: return new Level2(world, game);
            case 3: return new Level3(world, game);
            default: return new Level1(world, game);
        }
    }

    @Override
    public void render(float delta) {
        accumulator += delta;
        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, 6, 2);
            level.update(TIME_STEP);
            accumulator -= TIME_STEP;
        }

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (level.getPlayer() != null) {
            float px = level.getPlayer().getBody().getPosition().x;
            float py = level.getPlayer().getBody().getPosition().y;
            camera.position.set(px, py, 0f);
        }
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        drawBackground();
        level.render(batch);
        drawHUD();
        batch.end();

        if (debugMode) {
            debugRenderer.render(world, camera.combined);
        }

        handleInput();
    }

    private void drawBackground() {
        Texture bg = background1;
        if (levelNumber == 2 && background2 != null) bg = background2;
        if (levelNumber == 3 && background3 != null) bg = background3;

        if (bg != null) {
            float bgWidth = 40, bgHeight = 24;
            float bgX = camera.position.x - bgWidth / 2f;
            float bgY = camera.position.y - bgHeight / 2f;
            batch.draw(bg, bgX, bgY, bgWidth, bgHeight);
        }
    }

    private void drawHUD() {
        if (level.getPlayer() == null) return;


        if (heartTex != null) {
            for (int i = 0; i < level.getPlayer().getHp(); i++) {
                batch.draw(heartTex,
                    camera.position.x - 19 + i * 1.2f,
                    camera.position.y + 10, 1, 1);
            }
        }

        if (coinTex != null) {
            batch.draw(coinTex, camera.position.x + 15, camera.position.y + 10, 1, 1);
            game.getFont().draw(batch,
                String.valueOf(level.getPlayer().getCoinCount()),
                camera.position.x + 16.2f, camera.position.y + 10.8f);
        }
        // Stars
        if (starTex != null) {
            batch.draw(starTex, camera.position.x + 15, camera.position.y + 8, 1, 1);
            game.getFont().draw(batch,
                String.valueOf(level.getPlayer().getStarCount()),
                camera.position.x + 16.2f, camera.position.y + 8.8f);
        }
        // Shield
        if (level.getPlayer().isShieldActive() && shieldTex != null) {
            batch.draw(shieldTex, camera.position.x + 13, camera.position.y + 10, 1, 1);
            float s = level.getPlayer().getShieldTimeLeft() / 1000f;
            game.getFont().draw(batch,
                String.format("%.1fs", s),
                camera.position.x + 14.2f, camera.position.y + 10.8f);
        }
    }

    private void handleInput() {
        if (level.getPlayer() == null) return;

        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            level.getPlayer().move(-5f);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            level.getPlayer().move(5f);
        } else {
            level.getPlayer().move(0f);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) ||
            Gdx.input.isKeyJustPressed(Input.Keys.W) ||
            Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            level.getPlayer().jump();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.J) ||
            Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            level.getPlayer().startAttack();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            debugMode = !debugMode;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.showPauseScreen();
        }
    }

    @Override public void show() { }
    @Override public void resize(int width, int height) { }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }

    @Override
    public void dispose() {
        level.dispose();
        debugRenderer.dispose();

        if (background1 != null) background1.dispose();
        if (background2 != null) background2.dispose();
        if (background3 != null) background3.dispose();
        if (heartTex != null) heartTex.dispose();
        if (coinTex != null) coinTex.dispose();
        if (starTex != null) starTex.dispose();
        if (shieldTex != null) shieldTex.dispose();
    }
}
