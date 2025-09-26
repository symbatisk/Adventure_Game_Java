package com.symbat.adventure;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public abstract class GameLevel {

    protected final AdventureGame game;
    protected final World world;

    protected Player player;

    protected Stage uiStage;
    protected Viewport uiViewport;
    protected Skin skin;
    protected boolean paused = false;

    protected final Array<Platform> platforms = new Array<>();
    protected final Array<MovingPlatform> movingPlatforms = new Array<>();
    protected final Array<Coin> coins = new Array<>();
    protected final Array<Star> stars = new Array<>();
    protected final Array<Apple> apples = new Array<>();
    protected Shield shield;
    protected Key key;


    protected static final float DEATH_Y = -20f;
    protected float spawnInvuln = 1.0f;
    protected float invulnTimer = 0f;
    protected Texture background;

    public GameLevel(World world, AdventureGame game) {
        this.world = world;
        this.game = game;


        createPlayer();
        buildLayout();
        if (player != null) {
            game.setPlayer(player);
        }
    }


    protected abstract void createPlayer();
    protected abstract void buildLayout();
    protected abstract String backgroundPath();

    public void update(float delta) {
        if (player != null) player.update(delta);
        for (MovingPlatform mp : movingPlatforms) mp.update();
        for (Coin c : coins) c.update(delta);
        for (Star s : stars) s.update(delta);

        if (player != null && player.getBody() != null) {
            Vector2 p = player.getBody().getPosition();
            if (p.y < DEATH_Y) {
                onPlayerDied();
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (Platform p : platforms) if (p != null) p.render(batch);
        for (MovingPlatform mp : movingPlatforms) if (mp != null) mp.render(batch);

        for (Coin c : coins) if (c != null) c.render(batch);
        for (Star s : stars) if (s != null) s.render(batch);

        if (shield != null) shield.render(batch);
        if (key != null)    key.render(batch);

        if (player != null) player.render(batch);
    }

    protected void onPlayerDied() {
        game.showGameOverScreen();
    }


    public Player getPlayer() { return player; }

    public void dispose() {
        for (Platform p : platforms) p.dispose();
        for (MovingPlatform mp : movingPlatforms) mp.dispose();
        for (Coin c : coins) c.dispose();
        for (Star s : stars) s.dispose();
        if (shield != null) shield.dispose();
        if (key != null)    key.dispose();
        if (player != null) player.dispose();
    }
}
