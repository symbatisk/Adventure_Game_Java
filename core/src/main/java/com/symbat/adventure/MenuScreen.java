package com.symbat.adventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class MenuScreen implements Screen {

    private final AdventureGame game;
    private final AccountManager accountManager;
    private final Account currentAccount;

    private Stage stage;
    private Table root;
    private Table contentRoot;
    private Image bgImage;


    private Texture texBack;
    private Texture bgTexture;
    private Texture levelsPanelTex;
    private Texture accountPanelTex;
    private Texture ratingPanelTex;
    private Texture texSettings;


    private Texture texNavLevels;
    private Texture texNavLevelsActive;
    private Texture texNavAccount;
    private Texture texNavAccountActive;
    private Texture texNavRating;
    private Texture texNavRatingActive;


    private ImageButton btnLevels;
    private ImageButton btnAccount;
    private ImageButton btnRating;

    private enum Tab { LEVELS, ACCOUNT, RATING }

    public MenuScreen(AdventureGame game, AccountManager accountManager, Account currentAccount) {
        this.game = game;
        this.accountManager = accountManager;
        this.currentAccount = currentAccount;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);


        bgTexture       = new Texture(Gdx.files.internal("data/img/start2_bg.png"));
        levelsPanelTex  = new Texture(Gdx.files.internal("data/Panel/table_2.png"));
        accountPanelTex = new Texture(Gdx.files.internal("data/Panel/accountbg.png"));
        ratingPanelTex  = new Texture(Gdx.files.internal("data/Panel/rating_bg.png"));

        texBack             = new Texture(Gdx.files.internal("data/Panel/close.png"));
        texNavLevels        = new Texture(Gdx.files.internal("data/Panel/menu.png"));
        texNavLevelsActive  = new Texture(Gdx.files.internal("data/Panel/menu_hover.png"));
        texNavAccount       = new Texture(Gdx.files.internal("data/Panel/face.png"));
        texNavAccountActive = new Texture(Gdx.files.internal("data/Panel/face_hover.png"));
        texNavRating        = new Texture(Gdx.files.internal("data/Panel/rating.png"));
        texNavRatingActive  = new Texture(Gdx.files.internal("data/Panel/rating_hover.png"));
        texSettings         = new Texture(Gdx.files.internal("data/Panel/setting.png"));



        bgImage = new Image(bgTexture);
        bgImage.setFillParent(true);
        stage.addActor(bgImage);

        root = new Table();
        root.setFillParent(true);
        stage.addActor(root);


        contentRoot = new Table();

        root.add(contentRoot).expand().fill().center().padTop(0); ;
        root.row();

        root.add(createNavBar()).growX().pad(-200, 30, 20, 30);


        setPanel(createLevelsPanel());
        setActiveTab(Tab.LEVELS);


    }


    private Table createNavBar() {
        Table nav = new Table();

        btnLevels = makeNavButton(texNavLevels, texNavLevelsActive, () -> {
            setPanel(createLevelsPanel());
            setActiveTab(Tab.LEVELS);
        });

        btnAccount = makeNavButton(texNavAccount, texNavAccountActive, () -> {
            setPanel(createAccountPanel());
            setActiveTab(Tab.ACCOUNT);
        });

        btnRating = makeNavButton(texNavRating, texNavRatingActive, () -> {
            setPanel(createRatingPanel());
            setActiveTab(Tab.RATING);
        });

        nav.add(btnLevels).size(100, 100).pad(8);
        nav.add(btnAccount).size(100, 100).pad(8);
        nav.add(btnRating).size(100, 100).pad(8);

        return nav;
    }

    private ImageButton makeNavButton(Texture normal, Texture active, Runnable onClick) {
        ImageButton.ImageButtonStyle styleNormal = new ImageButton.ImageButtonStyle();
        styleNormal.imageUp = new TextureRegionDrawable(new TextureRegion(normal));
        styleNormal.imageDown = new TextureRegionDrawable(new TextureRegion(active));

        ImageButton.ImageButtonStyle styleActive = new ImageButton.ImageButtonStyle();
        styleActive.imageUp = new TextureRegionDrawable(new TextureRegion(active));

        ImageButton btn = new ImageButton(styleNormal);
        btn.setUserObject(new NavStyles(styleNormal, styleActive));

        btn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                onClick.run();
            }
        });
        return btn;
    }

    private static class NavStyles {
        final ImageButton.ImageButtonStyle normal;
        final ImageButton.ImageButtonStyle active;
        NavStyles(ImageButton.ImageButtonStyle normal, ImageButton.ImageButtonStyle active) {
            this.normal = normal; this.active = active;
        }
    }

    private void setActiveTab(Tab tab) {
        setButtonActive(btnLevels, tab == Tab.LEVELS);
        setButtonActive(btnAccount, tab == Tab.ACCOUNT);
        setButtonActive(btnRating, tab == Tab.RATING);
    }

    private void setButtonActive(ImageButton button, boolean active) {
        if (button == null) return;
        Object o = button.getUserObject();
        if (!(o instanceof NavStyles)) return;
        NavStyles ns = (NavStyles) o;
        button.setStyle(active ? ns.active : ns.normal);
    }

    private void setPanel(Table panel) {
        contentRoot.clearChildren();

        Stack mainStack = new Stack();
        Stack panelStack = new Stack();

        Image panelBgImg = new Image(new TextureRegionDrawable(new TextureRegion(getPanelTextureFor(panel))));
        panelBgImg.setScaling(Scaling.stretch);

        Table bgWrap = new Table();
        bgWrap.add(panelBgImg).grow();


        Table contentWrap = new Table();
        contentWrap.add(panel).expand().fill().pad(0, 30, 150, 30);

        panelStack.add(bgWrap);
        panelStack.add(contentWrap);

        Table overlayButtons = new Table();
        overlayButtons.setFillParent(true);
        overlayButtons.top().left().right();
        ImageButton btnBack = makeNavButton(texBack,texBack, () -> {
            game.showStartScreen();
        });

        ImageButton btnSettings = makeNavButton(texSettings, texSettings, () -> {
            game.showSettingsScreen("menu");
        });

        overlayButtons.add(btnBack).size(100, 100).pad(-20, 0, 0, 750).left().expandX();
        overlayButtons.add().expandX();

        overlayButtons.add(btnSettings).size(100, 100).pad(-20, 0, 0, -300).right().expandX();


        mainStack.add(panelStack);
        mainStack.add(overlayButtons);
        contentRoot.add(mainStack).size(550, 650).center();
    }

    private Texture getPanelTextureFor(Table panel) {
        Object tag = panel.getUserObject();
        if ("account".equals(tag)) return accountPanelTex;
        if ("rating".equals(tag))  return ratingPanelTex;
        return levelsPanelTex;
    }

    private Table createLevelsPanel() {
        Table table = new Table();
        table.setUserObject("levels");
        table.defaults().pad(10);

        for (int i = 1; i <= 3; i++) {
            final int levelNum = i;
            final String path = "data/Panel/level" + i + ".png";

            Texture tex = new Texture(Gdx.files.internal(path));
            ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
            style.imageUp = new TextureRegionDrawable(new TextureRegion(tex));

            ImageButton levelBtn = new ImageButton(style);
            levelBtn.addListener(new ClickListener() {
                @Override public void clicked(InputEvent event, float x, float y) {
                    game.startLevel(levelNum);
                }
            });

            table.add(levelBtn).size(100, 100).pad(10);
        }


        return table;
    }

    /** Account panel */
    private Table createAccountPanel() {
        Table table = new Table();
        table.setUserObject("account");
        table.defaults().pad(6).left();


        String name   = safeUsername(currentAccount);
        int stars     = safeStars(currentAccount);
        int coins     = safeCoins(currentAccount);
        int total     = safeScore(currentAccount);
        int highestLv = safeHighestUnlocked(currentAccount);

        table.add(new Label("Name:",           game.getSkin(), "dialogLabel")).left();
        table.add(new Label(name,             game.getSkin(), "dialogLabel")).left().row();

        table.add(new Label("Stars:",          game.getSkin(), "dialogLabel")).left();
        table.add(new Label(String.valueOf(stars), game.getSkin(), "dialogLabel")).left().row();

        table.add(new Label("Coins:",          game.getSkin(), "dialogLabel")).left();
        table.add(new Label(String.valueOf(coins), game.getSkin(), "dialogLabel")).left().row();

        table.add(new Label("Total Score:",    game.getSkin(), "dialogLabel")).left();
        table.add(new Label(String.valueOf(total), game.getSkin(), "dialogLabel")).left().row();

        table.add(new Label("Highest Level:",  game.getSkin(), "dialogLabel")).left();
        table.add(new Label(String.valueOf(highestLv), game.getSkin(), "dialogLabel")).left().row();

        return table;
    }

    private Table createRatingPanel() {
        Table table = new Table();
        table.setUserObject("rating");
        table.defaults().pad(4).center();

        Texture defaultIcon = texNavAccount;

        java.util.List<Account> list = new java.util.ArrayList<>(accountManager.getAccounts().values());
        list.sort((a, b) -> Integer.compare(safeScore(b), safeScore(a)));

        int position = 1;
        for (Account acc : list) {
            Table row = new Table();
            row.defaults().pad(2);

            Label placeLabel;
            if (position <= 3) {
                String medal;
                switch (position) {
                    case 1:
                        medal = "🥇";
                        break;
                    case 2:
                        medal = "🥈";
                        break;
                    case 3:
                        medal = "🥉";
                        break;
                    default:
                        medal = "";
                        break;
                }
                placeLabel = new Label(medal, game.getSkin(), "dialogLabel");
            } else {
                placeLabel = new Label(position + ".", game.getSkin(), "dialogLabel");
            }
            row.add(placeLabel).width(30).padRight(10);


            Image icon = new Image(defaultIcon);
            row.add(icon).size(40, 40).padRight(15);


            Table infoTable = new Table();
            infoTable.defaults().left().padBottom(2);


            infoTable.add(new Label(safeUsername(acc), game.getSkin(), "dialogLabel"))
                .left().row();


            Table starsTable = new Table();
            starsTable.defaults().padRight(3);
            int starCount = safeStars(acc);
            for (int i = 0; i < starCount; i++) {
                starsTable.add(new Label("★", game.getSkin(), "dialogLabel"));
            }
            infoTable.add(starsTable).left().row();

            Table statsTable = new Table();
            statsTable.defaults().padRight(10);

            statsTable.add(new Label(String.valueOf(safeScore(acc)), game.getSkin(), "dialogLabel"));
            statsTable.add(new Label("●", game.getSkin(), "dialogLabel")).padRight(5);
            statsTable.add(new Label(String.valueOf(safeCoins(acc)), game.getSkin(), "dialogLabel"));
            statsTable.add(new Label("●", game.getSkin(), "dialogLabel")).padRight(5);
            statsTable.add(new Label("Lv." + safeHighestUnlocked(acc), game.getSkin(), "dialogLabel"));

            infoTable.add(statsTable).left();

            row.add(infoTable).expandX().left();

            table.add(row).expandX().fillX().padTop(8).padBottom(8).colspan(3);
            table.row();

            if (position < list.size()) {
                Table separator = new Table();
                separator.setBackground(new TextureRegionDrawable(new TextureRegion(levelsPanelTex))); // или другая текстура
                separator.setColor(Color.GRAY);
                table.add(separator).height(2).colspan(3)
                    .growX().padTop(5).padBottom(5).padLeft(20).padRight(20);
                table.row();
            }
        }


        if (list.isEmpty()) {
            table.add(new Label("No players yet.", game.getSkin(), "dialogLabel"))
                .colspan(3).pad(20);
        }

        return table;
    }


    private int computeAvailableLevels() {
        int max = 0;
        for (int i = 1; i <= 50; i++) {
            String cls = "com.symbat.adventure.Level" + i;
            try {
                Class.forName(cls);
                max = i;
            } catch (Throwable ignore) {
                break;
            }
        }
        return Math.max(1, max);
    }

    private Texture safeLoadTexture(String path) {
        try {
            if (Gdx.files.internal(path).exists()) {
                return new Texture(Gdx.files.internal(path));
            }
            return null;
        } catch (Throwable t) {
            return null;
        }
    }

    private String safeUsername(Account a) {
        try { return a != null ? String.valueOf(a.getUsername()) : "-"; }
        catch (Throwable t) { return "-"; }
    }
    private int safeHighestUnlocked(Account a) {
        try { return a != null ? a.getHighestUnlockedLevel() : 0; }
        catch (Throwable t) { return 0; }
    }
    private int safeCoins(Account a) {
        try { return a != null ? a.getCoinCount() : 0; }
        catch (Throwable t) { return 0; }
    }
    private int safeStars(Account a) {
        try { return a != null ? a.getStarCount() : 0; }
        catch (Throwable t) { return 0; }
    }
    private int safeScore(Account a) {
        try { return a != null ? a.getTotalScore() : 0; }
        catch (Throwable t) { return 0; }
    }

    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }
    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }

    @Override
    public void dispose() {
        if (stage != null) stage.dispose();
        if (bgTexture != null) bgTexture.dispose();
        if (levelsPanelTex != null) levelsPanelTex.dispose();
        if (accountPanelTex != null) accountPanelTex.dispose();
        if (ratingPanelTex != null) ratingPanelTex.dispose();
        if (texNavLevels != null) texNavLevels.dispose();
        if (texNavLevelsActive != null) texNavLevelsActive.dispose();
        if (texNavAccount != null) texNavAccount.dispose();
        if (texNavAccountActive != null) texNavAccountActive.dispose();
        if (texNavRating != null) texNavRating.dispose();
        if (texNavRatingActive != null) texNavRatingActive.dispose();
        if (texBack != null) texBack.dispose();
        if (texSettings != null) texSettings.dispose();
    }
}
