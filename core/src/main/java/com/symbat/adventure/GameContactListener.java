package com.symbat.adventure;

import com.badlogic.gdx.physics.box2d.*;

public class GameContactListener implements ContactListener {
    private AdventureGame game;

    public GameContactListener(AdventureGame game) {
        this.game = game;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Object userDataA = fixtureA.getUserData();
        Object userDataB = fixtureB.getUserData();

        if (userDataA instanceof Player) {
            handlePlayerCollision((Player) userDataA, userDataB);
        } else if (userDataB instanceof Player) {
            handlePlayerCollision((Player) userDataB, userDataA);
        }

        if (userDataA instanceof Enemy) {
            handleEnemyCollision((Enemy) userDataA, userDataB);
        } else if (userDataB instanceof Enemy) {
            handleEnemyCollision((Enemy) userDataB, userDataA);
        }

        if ((userDataA instanceof Player && userDataB instanceof Platform) ||
            (userDataB instanceof Player && userDataA instanceof Platform)) {
            ((Player) (userDataA instanceof Player ? userDataA : userDataB)).setGrounded(true);
        }
    }

    private void handlePlayerCollision(Player player, Object other) {
        if (other instanceof Coin) {
            player.collectCoin();
            ((Coin) other).collect(player);
        } else if (other instanceof Star) {
            player.collectStar();
            ((Star) other).collect(player);
        } else if (other instanceof Shield) {
            player.collectShieldPower();
            ((Shield) other).collect();
        } else if (other instanceof Enemy) {
            player.takeDamage(1);
        }
    }

    private void handleEnemyCollision(Enemy enemy, Object other) {
        if (other instanceof Player) {

        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Object userDataA = fixtureA.getUserData();
        Object userDataB = fixtureB.getUserData();


        if ((userDataA instanceof Player && userDataB instanceof Platform) ||
            (userDataB instanceof Player && userDataA instanceof Platform)) {
            ((Player) (userDataA instanceof Player ? userDataA : userDataB)).setGrounded(false);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}
