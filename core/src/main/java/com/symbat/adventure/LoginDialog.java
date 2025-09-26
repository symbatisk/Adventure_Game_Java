package com.symbat.adventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class LoginDialog extends Dialog {
    private final AdventureGame game;

    private TextField usernameField;
    private TextField passwordField;
    private TextField confirmField;

    private boolean isRegisterMode = false;

    private static final String WINDOW_STYLE = "dialog";
    private static final String LABEL_STYLE = "dialogLabel";
    private static final String TEXTFIELD_STYLE = "dialogTextField";
    private static final String BUTTON_STYLE = "dialogButton";

    public LoginDialog(AdventureGame game) {
        super("Login", game.getSkin(), WINDOW_STYLE);
        this.game = game;
        setModal(true);
        setMovable(false);
        rebuildUI();
    }

    private void rebuildUI() {
        getTitleLabel().setText(isRegisterMode ? "Register" : "Login");

        Table content = getContentTable();
        Table buttons = getButtonTable();
        content.clear();
        buttons.clear();
        pad(20);


        content.defaults().pad(10);

        content.add(new Label("Username:", game.getSkin(), LABEL_STYLE));
        usernameField = new TextField("", game.getSkin(), TEXTFIELD_STYLE);
        content.add(usernameField).width(220).row();

        content.add(new Label("Password:", game.getSkin(), LABEL_STYLE));
        passwordField = new TextField("", game.getSkin(), TEXTFIELD_STYLE);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        content.add(passwordField).width(220).row();

        if (isRegisterMode) {
            content.add(new Label("Confirm:", game.getSkin(), LABEL_STYLE));
            confirmField = new TextField("", game.getSkin(), TEXTFIELD_STYLE);
            confirmField.setPasswordMode(true);
            confirmField.setPasswordCharacter('*');
            content.add(confirmField).width(220).row();
        } else {
            confirmField = null;
        }


        buttons.defaults().pad(10);

        if (isRegisterMode) {
            TextButton createBtn = new TextButton("Register", game.getSkin(), BUTTON_STYLE);
            TextButton backBtn   = new TextButton("Back", game.getSkin(), BUTTON_STYLE);

            createBtn.addListener(new ChangeListener() {
                @Override public void changed(ChangeEvent event, Actor actor) {
                    onRegisterConfirm();
                }
            });
            backBtn.addListener(new ChangeListener() {
                @Override public void changed(ChangeEvent event, Actor actor) {
                    isRegisterMode = false;
                    rebuildUI();
                    centerDialog();
                }
            });

            buttons.add(createBtn);
            buttons.add(backBtn);
        } else {
            TextButton loginBtn    = new TextButton("Login", game.getSkin(), BUTTON_STYLE);
            TextButton registerBtn = new TextButton("Register", game.getSkin(), BUTTON_STYLE);
            TextButton cancelBtn   = new TextButton("Cancel", game.getSkin(), BUTTON_STYLE);

            loginBtn.addListener(new ChangeListener() {
                @Override public void changed(ChangeEvent event, Actor actor) {
                    onLogin();
                }
            });
            registerBtn.addListener(new ChangeListener() {
                @Override public void changed(ChangeEvent event, Actor actor) {
                    isRegisterMode = true;
                    rebuildUI();
                    centerDialog();
                }
            });
            cancelBtn.addListener(new ChangeListener() {
                @Override public void changed(ChangeEvent event, Actor actor) {
                    hide();
                }
            });

            buttons.add(loginBtn);
            buttons.add(registerBtn);
            buttons.add(cancelBtn);
        }

        pack();
    }

    private void onLogin() {
        String u = usernameField.getText().trim();
        String p = passwordField.getText().trim();

        if (u.isEmpty() || p.isEmpty()) {
            showError("Username and password cannot be empty!");
            return;
        }

        Account acc = game.getAccountManager().login(u, p);
        if (acc != null) {
            game.setCurrentAccount(acc);
            showSuccess("Login successful!");
            game.showMenuScreen();
            hide();
        } else {
            showError("Invalid username or password!");
        }
    }

    private void onRegisterConfirm() {
        String u = usernameField.getText().trim();
        String p = passwordField.getText().trim();
        String c = (confirmField != null) ? confirmField.getText().trim() : "";

        if (u.isEmpty() || p.isEmpty()) {
            showError("Username and password cannot be empty!");
            return;
        }
        if (!p.equals(c)) {
            showError("Passwords don't match!");
            return;
        }

        if (game.getAccountManager().register(u, p)) {
            Account acc = game.getAccountManager().login(u, p);
            game.setCurrentAccount(acc);
            showSuccess("Registration successful!");
            game.showMenuScreen();
            hide();
        } else {
            showError("Username already exists!");
        }
    }

    private void showError(String message) {
        Dialog d;
        try { d = new Dialog("Error", game.getSkin(), WINDOW_STYLE); }
        catch (Exception e) { d = new Dialog("Error", game.getSkin()); }
        d.text(new Label(message, game.getSkin(), LABEL_STYLE));
        d.button("OK");
        d.show(getStage());
    }

    private void showSuccess(String message) {
        Dialog d;
        try { d = new Dialog("Success", game.getSkin(), WINDOW_STYLE); }
        catch (Exception e) { d = new Dialog("Success", game.getSkin()); }
        d.text(new Label(message, game.getSkin(), LABEL_STYLE));
        d.button("OK");
        d.show(getStage());
    }

    @Override
    public Dialog show(Stage stage) {
        Dialog dlg = super.show(stage);
        pack();
        centerDialog();

        if (getStage() != null) {
            getStage().setKeyboardFocus(this);
        }
        return dlg;
    }

    private void centerDialog() {
        Stage stage = getStage();
        if (stage != null) {
            setPosition(
                (stage.getWidth() - getWidth()) / 2f,
                (stage.getHeight() - getHeight()) / 2f
            );
        }
    }
}
