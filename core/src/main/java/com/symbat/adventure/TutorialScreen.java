package com.symbat.adventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class TutorialScreen implements Screen {
    private final AdventureGame game;
    private Stage stage;
    private Texture background;
    private Texture panelBackground;
    private List<TutorialPage> pages;
    private int currentPageIndex = 0;
    private Table contentTable;
    private Table dotsTable;

    public TutorialScreen(AdventureGame game) {
        this.game = game;
        loadPages();
    }

    private void loadPages() {
        pages = new ArrayList<>();

        pages.add(new TutorialPage(
            new DisplayItem(new Texture("data/instruction/walk.png"),
                "Move left and right using the arrow keys to navigate platforms and explore the level")
        ));

        pages.add(new TutorialPage(
            new DisplayItem(new Texture("data/instruction/jump.png"),
                "Press the SPACE to leap over obstacles, avoid enemies, or reach hidden areas")
        ));

        pages.add(new TutorialPage(
            new DisplayItem(new Texture("data/instruction/attack.png"),
                "Strike enemies with the D key. Each hit reduces their health by 1 HP")
        ));

        pages.add(new TutorialPage(
            new DisplayItem(new Texture("data/instruction/shield.png"),
                "Activate your shield by pressing A to block incoming damage temporarily")
        ));

        pages.add(new TutorialPage(
            new DisplayItem(new Texture("data/instruction/apple.png"), "Restores 2 HP when collected"),
            new DisplayItem(new Texture("data/instruction/star.png"), "Collect to unlock progress through levels"),
            new DisplayItem(new Texture("data/instruction/coin.png"), "Collect to unlock achievements through levels")
        ));

        pages.add(new TutorialPage(
            new DisplayItem(new Texture("data/instruction/portal.png"),
                "Teleport between distant platforms to uncover secrets or shortcut paths"),
            new DisplayItem(new Texture("data/instruction/key.png"),
                "Find the golden key to unlock the exit and complete the level. Victory awaits!")
        ));
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        background = new Texture("data/img/start2_bg.png");
        panelBackground = new Texture("data/Panel/info_bg.png");

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        mainTable.setBackground(new TextureRegionDrawable(new TextureRegion(background)));

        Table panelTable = new Table();
        panelTable.setBackground(new TextureRegionDrawable(new TextureRegion(panelBackground)));
        panelTable.setSize(550, 650);

        contentTable = new Table();
        contentTable.defaults().pad(10);

        ImageButton prevButton = createNavigationButton("prew", 30);
        prevButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showPreviousPage();
            }
        });

        ImageButton nextButton = createNavigationButton("next", 30);
        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showNextPage();
            }
        });

        ImageButton backButton = createNavigationButton("close_2", 30);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.showStartScreen();
            }
        });

        dotsTable = new Table();
        updateDots();

        Table navigationTable = new Table();
        navigationTable.add(prevButton).size(80, 80).pad(12);
        navigationTable.add(dotsTable);
        navigationTable.add(nextButton).size(80, 80).pad(12);

        panelTable.add(backButton).size(100,100).expand().top().right().pad(0);
        panelTable.row();
        panelTable.add(contentTable).expand().center().pad(0);
        panelTable.row();
        panelTable.add(navigationTable).expand().bottom().center().pad(0);

        mainTable.add(panelTable).center();

        stage.addActor(mainTable);
        showCurrentPage();
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
        if (stage != null) stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        if (stage != null) stage.dispose();
        if (background != null) background.dispose();
        if (panelBackground != null) panelBackground.dispose();
        for (TutorialPage page : pages) {
            for (DisplayItem item : page.getItems()) {
                item.getImage().dispose();
            }
        }
    }

    private ImageButton createNavigationButton(String iconName, int size) {
        Texture iconTexture = new Texture("data/Panel/" + iconName + ".png");
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(new TextureRegion(iconTexture));
        ImageButton btn = new ImageButton(style);
        btn.setSize(size, size);
        return btn;
    }

    private void showCurrentPage() {
        if (contentTable == null) return;
        contentTable.clear();

        TutorialPage page = pages.get(currentPageIndex);
        List<DisplayItem> items = page.getItems();

        switch (items.size()) {
            case 1:
                renderSingleItem(items.get(0));
                break;
            case 2:
                renderTwoItems(items);
                break;
            case 3:
                renderThreeItems(items);
                break;
            default:

                renderSingleItem(items.get(0));
                break;
        }

        updateDots();
    }

    private void renderSingleItem(DisplayItem item) {
        Image image = new Image(item.getImage());
        Label label = createLabel(item.getDescription(), 22);
        label.setAlignment(Align.center);

        contentTable.add(image).size(230, 120).row();
        contentTable.add(label).width(400).row();
    }

    private void renderTwoItems(List<DisplayItem> items) {
        Table rowTable = new Table();
        rowTable.defaults().pad(20);

        for (DisplayItem item : items) {
            Table itemTable = new Table();
            Image image = new Image(item.getImage());
            Label label = createLabel(item.getDescription(), 18);
            label.setAlignment(Align.center);

            itemTable.add(image).size(150, 100).row();
            itemTable.add(label).width(200).row();
            rowTable.add(itemTable);
        }

        contentTable.add(rowTable);
    }

    private void renderThreeItems(List<DisplayItem> items) {
        Table rowTable = new Table();
        rowTable.defaults().pad(15);

        for (DisplayItem item : items) {
            Table itemTable = new Table();
            Image image = new Image(item.getImage());
            Label label = createLabel(item.getDescription(), 16);
            label.setAlignment(Align.center);

            itemTable.add(image).size(110, 80).row();
            itemTable.add(label).width(150).row();
            rowTable.add(itemTable);
        }

        contentTable.add(rowTable);
    }

    private Label createLabel(String text, int fontSize) {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = game.getFont();
        style.fontColor = com.badlogic.gdx.graphics.Color.BLACK;
        Label label = new Label(text, style);
        label.setFontScale(fontSize / 24f);
        label.setWrap(true);

        return label;
    }

    private void showPreviousPage() {
        if (currentPageIndex > 0) {
            currentPageIndex--;
            showCurrentPage();
        }
    }

    private void showNextPage() {
        if (currentPageIndex < pages.size() - 1) {
            currentPageIndex++;
            showCurrentPage();
        }
    }

    private void updateDots() {
        if (dotsTable == null) return;
        dotsTable.clear();

        for (int i = 0; i < pages.size(); i++) {

            Label dot = new Label("•", game.getSkin(), "dialogLabel");
            dot.setFontScale(1.6f);
            if (i == currentPageIndex) {
                dot.setColor(Color.GREEN);
            } else {
                dot.setColor(0.6f, 0.6f, 0.6f, 1f);
            }
            dotsTable.add(dot).pad(6);
        }
    }
}
