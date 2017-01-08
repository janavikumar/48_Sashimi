package com.sashimi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;

import static com.badlogic.gdx.math.MathUtils.random;

public class Level1 extends GameScreen {
    int spawnedBoss = 0;
    Music boss;

    public Level1 (final Sashimi game) {
        super(game);
        boss = Gdx.audio.newMusic(Gdx.files.internal("Music/boss.ogg"));
    }

    @Override
    public void spawnEnemies(float deltaTime) {
        if (secondsElapsed > 2 && secondsElapsed < 6) {spawnJellyRow(deltaTime, 1200, 1);}
        if (secondsElapsed > 3 && secondsElapsed < 7) {spawnJellyRow(deltaTime, 1100, 2);}
        if (secondsElapsed > 6 && secondsElapsed < 10) {spawnJellySideColumns(deltaTime, 100);}
        if (secondsElapsed > 10 && secondsElapsed < 20) {spawnRandomEnemies(deltaTime);}
        if (secondsElapsed > 17 && secondsElapsed < 22) {spawnJellySideColumns(deltaTime, 100);}
        if (secondsElapsed > 22 && secondsElapsed < 24) {spawnJellySideColumns(deltaTime, 200);}
        if (secondsElapsed > 24 && secondsElapsed < 27) {spawnJellyRow(deltaTime, 900, 1);}
        if (secondsElapsed > 27 && secondsElapsed < 31) {spawnJellyRow(deltaTime, 800, 1);}
        if (secondsElapsed > 33 && justOnce == 0) {
            BGM.stop();
            boss.play();
            enemies.add(new Level1Boss(this, game.screenWidth/2, 1100));
            numEnemies++;
            justOnce++;
            spawnedBoss = 1;
        }
        if (spawnedBoss == 1 && enemies.size() == 0) {
            game.victory(you.getScore(), secondsElapsed);
        }
    }

    @Override
    public void render(float delta) {
        //TODO remove
        //game.level1Boss();
        super.render(delta);
    }

    @Override
    public void show() {
        BGM.setLooping(true);
        BGM.setVolume((float) 0.6);
        boss.setLooping(true);
        boss.setVolume((float) 0.6);
        BGM.play();
    }

    @Override
    public void hide() {
        boss.stop();
    }

    @Override
    public void dispose() {
//        pauseButton.dispose();
        you.dispose();
        BGtexture.dispose();
        for(Enemy e: enemies){
            e.dispose();
        }
        BGM.dispose();
        boss.dispose();
        hurt.dispose();
        enemyDestroyed.dispose();
    }
}
