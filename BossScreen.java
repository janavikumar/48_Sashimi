package com.sashimi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class BossScreen extends GameScreen {
    final Sashimi game;

    BossScreen(final Sashimi game) {
        super(game);
        this.game = game;
    }
    @Override
    public void spawnEnemies(float deltaTime){
        if (justOnce == 0) {
            enemies.add(new Level1Boss(this, 500, 1000));
            numEnemies++;
            justOnce = 1;
        }
    }
}
