package com.symbat.adventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PauseScreen implements Screen {
    private final AdventureGame game;
    private Stage stage;
    private Texture background;

    public PauseScreen(AdventureGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        background = new Texture("data/Panel/pause_bg.png");

        Table table = new Table();
        table.setFillParent(true);
        table.setBackground(new TextureRegionDrawable(background));

        ImageButton resumeBtn = createIconButton("close", 100);
        resumeBtn.addListener(event -> {
            game.resumeGame();
            return true;
        });

        ImageButton restartBtn = createIconButton("restart", 100);
        restartBtn.addListener(event -> {
            game.restartGame();
            return true;
        });

        ImageButton menuBtn = createIconButton("menu", 100);
        menuBtn.addListener(event -> {
            game.showMenuScreen();
            return true;
        });

        table.add(resumeBtn).pad(20);
        table.add(restartBtn).pad(20);
        table.add(menuBtn).pad(20);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
        Texture iconTexture = new Texture("data/Panel/" + iconName + ".png");

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(iconTexture);

        ImageButton button = new ImageButton(style);
        button.setSize(size, size);

        return button;
    }
}
