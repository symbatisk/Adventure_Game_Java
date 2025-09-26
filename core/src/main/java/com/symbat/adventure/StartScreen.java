package com.symbat.adventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;



public class StartScreen implements Screen {
    private final AdventureGame game;
    private Stage stage;
    private Texture background;

    public StartScreen(AdventureGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        background = new Texture("data/img/start2_bg.png");

        Table table = new Table();
        table.setFillParent(true);
        table.defaults().pad(10);

        ImageButton playButton = createIconButton("play", 250);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("StartScreen", "Play button clicked");
                showLoginDialog();
            }
        });

        ImageButton settingsButton = createIconButton("setting", 80);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("StartScreen", "Settings button clicked");
                game.showSettingsScreen("start");
            }
        });
        ImageButton infoButton = createIconButton("about", 80);
        infoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.showTutorialScreen();
            }
        });

        Table topRightTable = new Table();
        topRightTable.add(infoButton).size(80, 80).pad(10);
        topRightTable.add(settingsButton).size(80, 80).pad(10);

        table.add(topRightTable).expand().top().right().padTop(0).padRight(10).row();

        table.add(playButton).size(250, 250).center().padBottom(200).expand().row();

        stage.addActor(table);

        Gdx.app.log("StartScreen", "Screen shown with " + stage.getActors().size + " actors");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0, stage.getWidth(), stage.getHeight());
        stage.getBatch().end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
    }

    private ImageButton createIconButton(String iconName, int size) {
        try {
            Texture iconTexture = new Texture("data/Panel/" + iconName + ".png");

            ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
            style.imageUp = new TextureRegionDrawable(new TextureRegion(iconTexture));

            ImageButton button = new ImageButton(style);
            button.setSize(size, size);

            button.setVisible(true);
            button.setTouchable(Touchable.enabled);

            Gdx.app.log("StartScreen", "Created button: " + iconName + " with size: " + size);
            return button;
        } catch (Exception e) {
            Gdx.app.error("StartScreen", "Error loading texture: " + iconName, e);

            Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.RED);
            pixmap.fill();
            Texture texture = new Texture(pixmap);
            pixmap.dispose();

            ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
            style.imageUp = new TextureRegionDrawable(new TextureRegion(texture));

            ImageButton button = new ImageButton(style);
            button.setSize(size, size);

            Gdx.app.log("StartScreen", "Created placeholder button for: " + iconName);
            return button;
        }
    }

    private void showLoginDialog() {
        try {
            LoginDialog loginDialog = new LoginDialog(game);

            loginDialog.show(stage);

            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    if (loginDialog.getStage() != null) {
                        loginDialog.setPosition(
                            (stage.getWidth() - loginDialog.getWidth()) / 2,
                            (stage.getHeight() - loginDialog.getHeight()) / 2
                        );
                    }
                }
            });

            Gdx.app.log("StartScreen", "Login dialog shown");
        } catch (Exception e) {
            Gdx.app.error("StartScreen", "Error showing login dialog", e);
            game.showMenuScreen();
        }
    }
}
