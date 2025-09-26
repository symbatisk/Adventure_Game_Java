package com.symbat.adventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import java.util.HashMap;
import java.util.Map;

public class AccountManager {
    private static final String ACCOUNTS_FILE = "saves/accounts.json";
    private Map<String, Account> accounts;
    private Json json;

    public AccountManager() {
        json = new Json();
        accounts = new HashMap<>();
        loadAccounts();
    }

    public void loadAccounts() {
        try {
            FileHandle file = Gdx.files.local(ACCOUNTS_FILE);
            if (file.exists()) {
                String data = file.readString();


                JsonReader reader = new JsonReader();
                JsonValue root = reader.parse(data);

                accounts = new HashMap<>();
                for (JsonValue accountValue : root) {
                    Account account = new Account();
                    account.setUsername(accountValue.getString("username"));
                    account.setPassword(accountValue.getString("password"));
                    account.setScore(accountValue.getInt("totalScore", 0));


                    JsonValue progressValue = accountValue.get("progressMap");
                    if (progressValue != null) {
                        Map<Integer, LevelProgress> progressMap = new HashMap<>();
                        for (JsonValue levelValue : progressValue) {
                            int level = levelValue.getInt("level");
                            LevelProgress progress = new LevelProgress();
                            progress.setStars(levelValue.getInt("stars", 0));
                            progress.setCoins(levelValue.getInt("coins", 0));
                            progress.setScore(levelValue.getInt("score", 0));
                            progressMap.put(level, progress);
                        }
                        account.setProgressMap(progressMap);
                    }

                    accounts.put(account.getUsername(), account);
                }
            }
        } catch (Exception e) {
            Gdx.app.error("AccountManager", "Error loading accounts", e);
            accounts = new HashMap<>();
        }
    }

    public void saveAccounts() {
        try {
            FileHandle file = Gdx.files.local(ACCOUNTS_FILE);

            FileHandle dir = file.parent();
            if (!dir.exists()) {
                dir.mkdirs();
            }


            JsonValue root = new JsonValue(JsonValue.ValueType.array);

            for (Account account : accounts.values()) {
                JsonValue accountValue = new JsonValue(JsonValue.ValueType.object);

                accountValue.addChild("username", new JsonValue(account.getUsername()));
                accountValue.addChild("password", new JsonValue(account.getPassword()));
                accountValue.addChild("totalScore", new JsonValue(account.getTotalScore()));


                JsonValue progressValue = new JsonValue(JsonValue.ValueType.array);
                for (Map.Entry<Integer, LevelProgress> entry : account.getProgressMap().entrySet()) {
                    JsonValue levelValue = new JsonValue(JsonValue.ValueType.object);
                    levelValue.addChild("level", new JsonValue(entry.getKey()));
                    levelValue.addChild("stars", new JsonValue(entry.getValue().getStars()));
                    levelValue.addChild("coins", new JsonValue(entry.getValue().getCoins()));
                    levelValue.addChild("score", new JsonValue(entry.getValue().getScore()));
                    progressValue.addChild(levelValue);
                }
                accountValue.addChild("progressMap", progressValue);

                root.addChild(accountValue);
            }

            file.writeString(root.toJson(JsonWriter.OutputType.json), false);
        } catch (Exception e) {
            Gdx.app.error("AccountManager", "Error saving accounts", e);
        }
    }

    public boolean register(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            Gdx.app.error("AccountManager", "Username is empty");
            return false;
        }

        if (password == null || password.trim().isEmpty()) {
            Gdx.app.error("AccountManager", "Password is empty");
            return false;
        }

        if (accounts.containsKey(username)) {
            Gdx.app.log("AccountManager", "Username already exists: " + username);
            return false;
        }

        Account newAccount = new Account(username, password);
        accounts.put(username, newAccount);
        saveAccounts();

        Gdx.app.log("AccountManager", "Registered new account: " + username);
        return true;
    }

    public Account login(String username, String password) {
        if (username == null || password == null) {
            Gdx.app.error("AccountManager", "Username or password is null");
            return null;
        }

        Account account = accounts.get(username);
        if (account != null && account.getPassword().equals(password)) {
            Gdx.app.log("AccountManager", "Login successful: " + username);
            return account;
        }

        Gdx.app.log("AccountManager", "Login failed: " + username);
        return null;
    }

    public Account getAccount(String username) {
        return accounts.get(username);
    }

    public void addAccount(Account account) {
        accounts.put(account.getUsername(), account);
        saveAccounts();
    }

    public Map<String, Account> getAccounts() {
        return accounts;
    }

    public void updateProgress(String username, int level, int stars, int coins, int score) {
        Account account = accounts.get(username);
        if (account != null) {
            account.updateProgress(level, stars, coins, score);
            saveAccounts();
        }
    }

    public int getAllAccounts() {
        return 0;
    }
}
