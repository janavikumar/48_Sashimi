package com.sashimi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class GameOverScreen implements Screen {
    //Sound button;
    Music BGM;
    final Sashimi game;
    OrthographicCamera camera;
    SpriteBatch batch;
    public BitmapFont infoFont;
    protected String text;

    public GameOverScreen(final Sashimi game, int score, int secondsElapsed) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.screenWidth, game.screenHeight);
        batch = new SpriteBatch();
        //button = Gdx.audio.newSound(Gdx.files.internal("Music/button.wav"));
        BGM = Gdx.audio.newMusic(Gdx.files.internal("Music/GameOver.ogg"));

        infoFont = new BitmapFont();
        infoFont.setColor(Color.WHITE);
        infoFont.getData().setScale(2, 2);
        text = "          GAME OVER!\n\n" +
                "Score:               " + score + "\n" +
                "You survived:    " + secondsElapsed + " seconds";
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        infoFont.draw(game.batch, text, game.screenWidth/4, game.screenHeight/2 + 3*infoFont.getLineHeight());
        game.batch.end();

        //In the event that screen is touched
        if(Gdx.input.justTouched()){
            game.mainMenu();
            BGM.stop();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        BGM.setLooping(true);
        BGM.setVolume((float)0.5);
        BGM.play();
    }

    @Override
    public void hide() {
        BGM.stop();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose(){
        batch.dispose();
        infoFont.dispose();
        //button.dispose();
        BGM.dispose();
    }
}
