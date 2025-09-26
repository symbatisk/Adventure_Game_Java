package com.symbat.adventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SettingsScreen implements Screen {
    private final AdventureGame game;
    private Stage stage;
    private Texture background;
    private Texture centerBackground;
    private String previousPanel;
    private Texture onTexture, offTexture;

    public SettingsScreen(AdventureGame game, String previousPanel) {
        this.game = game;
        this.previousPanel = previousPanel;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        background = new Texture("data/img/start2_bg.png");
        centerBackground = new Texture("data/Panel/settings_bg.png");
        onTexture = new Texture("data/Panel/96.png");
        offTexture = new Texture("data/Panel/95.png");

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.setBackground(new TextureRegionDrawable(background));

        Table contentTable = new Table();
        contentTable.defaults().pad(10);

        ImageButton soundToggle = createToggleButton(SoundManager.isSoundEnabled());
        soundToggle.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                SoundManager.toggleSound();
                updateToggleButton(soundToggle, SoundManager.isSoundEnabled());
            }
        });

        ImageButton musicToggle = createToggleButton(SoundManager.isMusicEnabled());
        musicToggle.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                SoundManager.toggleMusic();
                updateToggleButton(musicToggle, SoundManager.isMusicEnabled());
            }
        });

        Slider volumeSlider = new Slider(0, 1, 0.1f, false, new Slider.SliderStyle());
        volumeSlider.setValue(SoundManager.getVolume());
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                SoundManager.setVolume(volumeSlider.getValue());
            }
        });

        contentTable.add(createLabel("Sound")).left();
        contentTable.add(soundToggle).right();
        contentTable.row();
        contentTable.add(createLabel("Music")).left();
        contentTable.add(musicToggle).right();
        contentTable.row();
        contentTable.add(createLabel("Volume")).left();
        contentTable.add(volumeSlider).width(200).right();

        ImageButton backButton = createIconButton("close_2", 100);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                returnToPreviousPanel();
            }
        });

        Table panelTable = new Table();
        panelTable.setBackground(new TextureRegionDrawable(centerBackground));
        panelTable.setSize(550, 650);

        panelTable.add(backButton).top().right().row();
        panelTable.add(contentTable).center().expand().padBottom(200);

        mainTable.add(panelTable).center().expand();


        stage.addActor(mainTable);
    }

    private void updateToggleButton(ImageButton button, boolean isOn) {
        button.getStyle().imageUp = new TextureRegionDrawable(isOn ? onTexture : offTexture);
    }

    private ImageButton createToggleButton(boolean initialState) {
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(initialState ? onTexture : offTexture);
        return new ImageButton(style);
    }

    private ImageButton createIconButton(String iconName, int size) {
        Texture iconTexture = new Texture("data/Panel/" + iconName + ".png");
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(iconTexture);
        ImageButton button = new ImageButton(style);
        button.setSize(size, size);
        return button;
    }

    private com.badlogic.gdx.scenes.scene2d.ui.Label createLabel(String text) {
        com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle style =
            new com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle();
        style.font = game.getFont();
        style.fontColor = com.badlogic.gdx.graphics.Color.BLACK;
        return new com.badlogic.gdx.scenes.scene2d.ui.Label(text, style);
    }

    private void returnToPreviousPanel() {
        if ("menu".equals(previousPanel)) {
            game.showMenuScreen();
        } else {
            game.showStartScreen();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
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
        centerBackground.dispose();
        onTexture.dispose();
        offTexture.dispose();
    }

    public void setPreviousPanel(String panel) {
        this.previousPanel = panel;
    }
}
