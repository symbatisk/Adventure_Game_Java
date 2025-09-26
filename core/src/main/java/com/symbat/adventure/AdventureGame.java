
package com.symbat.adventure;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;



public class AdventureGame extends Game {
    private BitmapFont font;
    private World world;
    private Player player;
    private SpriteBatch batch;
    private Music menuMusic;
    private GameLevel currentLevel;
    private int currentLevelNumber = 1;
    private Skin skin;

    public static final float WORLD_WIDTH = 12f;
    public static final float WORLD_HEIGHT = 7f;
    public static final float PPM = 100f;


    private OrthographicCamera camera;
    private Viewport viewport;
    private Box2DDebugRenderer debugRenderer;

    private GameScreen gameScreen;
    private MenuScreen menuScreen;
    private StartScreen startScreen;
    private SettingsScreen settingsScreen;
    private TutorialScreen tutorialScreen;
    private VictoryScreen victoryScreen;
    private GameOverScreen gameOverScreen;
    private PauseScreen pauseScreen;

    private AccountManager accountManager;
    private Account currentAccount;

    @Override
    public void create() {
        Assets.load();
        batch = new SpriteBatch();
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply(true);


        Gdx.graphics.setWindowedMode(1200, 700);

        accountManager = new AccountManager();
        font = new BitmapFont();
        SoundManager.load();

        accountManager.loadAccounts();
        startScreen = new StartScreen(this);
        setScreen(startScreen);
    }

    public void showGameScreen(int levelNumber) {
        if (gameScreen != null) {
            gameScreen.dispose();
            gameScreen = null;
        }
        gameScreen = new GameScreen(this, levelNumber);
        setScreen(gameScreen);

    }

    public void showMenuScreen() {
        if (menuScreen == null) {
            menuScreen = new MenuScreen(this, accountManager, currentAccount);
        }
        setScreen(menuScreen);
    }

    public void showStartScreen() {
        setScreen(startScreen);
    }

    public void showSettingsScreen(String previousPanel) {
        if (settingsScreen == null) {
            settingsScreen = new SettingsScreen(this, previousPanel);
        }
        setScreen(settingsScreen);
    }
    public void showTutorialScreen() {
        if (tutorialScreen == null) {
            tutorialScreen = new TutorialScreen(this);
        }
        setScreen(tutorialScreen);
    }

    public void showVictoryScreen() {
        if (victoryScreen == null) {
            victoryScreen = new VictoryScreen(this);
        }
        setScreen(victoryScreen);
    }

    public void showGameOverScreen() {
        int stars = (player != null) ? player.getStarCount() : 0;
        setScreen(new GameOverScreen(this, stars));
    }


    public void restartGame() {
        if (currentLevel != null) {
            currentLevel.dispose();
            currentLevel = null;
        }

        if (world != null) {
            world.dispose();
        }
        world = new World(new Vector2(0, -9.8f), true);

        startLevel(currentLevelNumber);
    }

    public void startLevel(int levelNumber) {
        currentLevelNumber = levelNumber;

        showGameScreen(levelNumber);
    }

    public void showPauseScreen() {
        if (pauseScreen == null) {
            pauseScreen = new PauseScreen(this);
        }
        setScreen(pauseScreen);
    }
    public void goToNextLevel() {
        currentLevelNumber++;
        if (currentLevelNumber > 3) {
            showVictoryScreen();
        } else {
            startLevel(currentLevelNumber);
        }
    }

    public void saveProgress(GameLevel currentLevel) {
        if (currentAccount != null && this.currentLevel != null) {
            currentAccount.setScore(currentLevelNumber);
            accountManager.saveAccounts();
        }
    }
    public void resumeGame() {
        if (gameScreen != null) setScreen(gameScreen);
        else showGameScreen(currentLevelNumber);
    }


    public int getHighestUnlockedLevel() {
        if (currentAccount != null) {
            return currentAccount.getHighestUnlockedLevel();
        }
        return 1;
    }

    public Skin getSkin() {
        if (skin == null) {
            skin = new Skin();

            BitmapFont font = new BitmapFont();
            skin.add("default-font", font);

            Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pix.setColor(Color.WHITE);
            pix.fill();
            Texture whiteTex = new Texture(pix);
            pix.dispose();
            skin.add("white", whiteTex);
            TextureRegionDrawable whiteDrawable = new TextureRegionDrawable(new TextureRegion(whiteTex));

            Pixmap pixBlack = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixBlack.setColor(Color.BLACK);
            pixBlack.fill();
            Texture blackTex = new Texture(pixBlack);
            pixBlack.dispose();
            skin.add("black", blackTex);
            TextureRegionDrawable blackDrawable = new TextureRegionDrawable(new TextureRegion(blackTex));

            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            skin.add("default-label", labelStyle);
            skin.add("dialogLabel", labelStyle);

            Window.WindowStyle windowStyle = new Window.WindowStyle();
            windowStyle.titleFont = font;
            windowStyle.titleFontColor = Color.BLACK;
            windowStyle.background = whiteDrawable;
            skin.add("dialog", windowStyle);

            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            textFieldStyle.font = font;
            textFieldStyle.fontColor = Color.BLACK;
            textFieldStyle.background = whiteDrawable;
            textFieldStyle.cursor = blackDrawable;
            textFieldStyle.selection = blackDrawable.tint(Color.LIGHT_GRAY);
            skin.add("dialogTextField", textFieldStyle);

            TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
            textButtonStyle.font = font;
            textButtonStyle.fontColor = Color.BLACK;
            textButtonStyle.up = whiteDrawable;
            textButtonStyle.down = blackDrawable.tint(Color.GRAY);
            skin.add("default", textButtonStyle);
            skin.add("dialogButton", textButtonStyle);

        }
        return skin;
    }





    public World getWorld() {
        return world;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }
    public AccountManager getAccountManager() {
        return accountManager;
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public void setCurrentAccount(Account account) {
        this.currentAccount = account;
    }

    public int getCurrentLevelNumber() {
        return currentLevelNumber;
    }

    public void setCurrentLevel(GameLevel level) {
        if (currentLevel != null) {
            currentLevel.dispose();
        }
        currentLevel = level;
    }

    public LevelProgress getLevelProgress(GameLevel currentLevel) {
        return null;
    }
    public BitmapFont getFont() {
        return font;
    }


    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {

    }


    @Override
    public void dispose() {
        if (world != null) world.dispose();
        if (gameScreen != null) gameScreen.dispose();
        if (batch != null) batch.dispose();
        if (skin != null) skin.dispose();
        SoundManager.dispose();
    }

    public void setPlayer(Player p) { this.player = p; }

    public Player getPlayer() { return player; }

    public GameLevel getCurrentLevel() { return currentLevel; }

}
