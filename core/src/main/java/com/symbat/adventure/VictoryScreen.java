package com.symbat.adventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class VictoryScreen implements Screen {
    private final AdventureGame game;
    private Stage stage;
    private Texture background;
    private Texture starResult;

    public VictoryScreen(AdventureGame game) {
        this.game = game;

        int stars = game.getPlayer().getStarCount();
        if (stars < 0 || stars > 3) stars = 0;

        String path = "data/Panel/star_" + stars + ".png";
        starResult = new Texture(path);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        background = new Texture("data/Panel/victory_bg.png");

        Table table = new Table();
        table.setFillParent(true);

        ImageButton restartButton = createIconButton("restart", 100);
        restartButton.addListener(event -> {
            game.restartGame();
            return true;
        });

        ImageButton nextLevelButton = createIconButton("play", 100);
        nextLevelButton.addListener(event -> {
            game.goToNextLevel();
            return true;
        });

        ImageButton exitButton = createIconButton("close", 100);
        exitButton.addListener(event -> {
            game.showMenuScreen();
            return true;
        });

        table.add(restartButton).pad(20);
        table.add(nextLevelButton).pad(20);
        table.add(exitButton).pad(20);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        stage.getBatch().begin();
        stage.getBatch().setColor(0f, 0f, 0f, 0.6f);
        stage.getBatch().draw(Assets.pixel, 0, 0, stage.getWidth(), stage.getHeight());
        stage.getBatch().setColor(1f, 1f, 1f, 1f);

        stage.getBatch().draw(background,
            (stage.getWidth() - 550) / 2,
            (stage.getHeight() - 650) / 2,
            550, 650);


        if (starResult != null) {
            stage.getBatch().draw(starResult,
                (stage.getWidth() - 370) / 2,
                (stage.getHeight() - 220) / 2 + 50,
                370, 220);
        }
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
        starResult.dispose();
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
