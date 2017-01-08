package com.sashimi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;

//import javafx.stage.Stage;

public class InfoScreen implements Screen {
    //Sound button;
    final Sashimi game;
    private Texture infoBG;
    public EasyButton menuButton;
    public BitmapFont infoFont;
    private String text;
    Music BGM;

    OrthographicCamera camera;

    public InfoScreen(final Sashimi game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.screenWidth, game.screenHeight);
        //button = Gdx.audio.newSound(Gdx.files.internal("Music/button.wav"));

        BGM = Gdx.audio.newMusic(Gdx.files.internal("Music/info.ogg"));

        //Set up water image as background
        infoBG = new Texture(Gdx.files.internal("BG/seaBG.png"));

        //Set up menu button
        menuButton = new EasyButton("Main Menu.png");
        menuButton.setX((game.screenWidth / 2) - (menuButton.getWidth() / 2));
        menuButton.setY((game.screenHeight / 2) - 150);

        //Set up font for info screen
        infoFont = new BitmapFont();
        infoFont.setColor(Color.WHITE);
        infoFont.getData().setScale(2, 2);
        text = "MOVEMENT" +
                "\n\nHold down finger (mobile), mouse (PC), or \npress arrow keys (PC) to move around \nthe screen." +
                "\n\nHold SHIFT (PC only) for slower, more focused \nmovement." +
                "\n\n\nSHOOTING MODE" +
                "\n\nTap with a second finger while moving (mobile) \nor hold SHIFT (PC) to toggle shooting mode.";
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        //Add water background
        game.batch.draw(infoBG, 0, 0, infoBG.getWidth(), infoBG.getHeight());

        infoFont.draw(game.batch, text, 45, 1230);

        //Add main menu button
        game.batch.draw(menuButton.getButtonTexture(), menuButton.getX(), menuButton.getY());

        game.batch.end();

        //Check if user touches the screen
        if(Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            //Checks if the menu button is touched
            if(menuButton.contains((int)touchPos.x,(int)touchPos.y,game.screenHeight)){
                game.mainMenu();
                BGM.stop();
                //button.play();
            }
        }
    }

    public void show() {
        BGM.setLooping(true);
        BGM.setVolume((float)0.6);
        BGM.play();
    }

    public void hide() {
    }

    public void resume() {

    }

    public void pause() {

    }

    public void resize(int width, int height) {

    }

    public void dispose() {
        menuButton.dispose();
        infoBG.dispose();
        infoFont.dispose();
        BGM.dispose();
        //button.dispose();
    }

}