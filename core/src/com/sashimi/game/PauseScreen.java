package com.sashimi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

//import javafx.scene.paint.Color;

public class PauseScreen implements Screen{

    final Sashimi game;

    OrthographicCamera camera;
    private String pause;
    public BitmapFont pauseFont;
    public EasyButton resumeButton;
    public EasyButton menuButton;

    public PauseScreen(final Sashimi game){
            this.game = game;
            camera = new OrthographicCamera();
            camera.setToOrtho(false, game.screenWidth, game.screenHeight);
    }


    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        //Set up play (resume game) button
        resumeButton = new EasyButton("Resume.png");
        resumeButton.setX((game.screenWidth / 2) - (resumeButton.getWidth() / 2));
        resumeButton.setY((game.screenHeight / 6));

        //Set up menu button
        menuButton = new EasyButton("Main Menu.png");
        menuButton.setX((game.screenWidth / 2) - (menuButton.getWidth() / 2));
        menuButton.setY((game.screenHeight / 6) - (resumeButton.getHeight()*3/2));

        game.batch.begin();

        //Adds text that says "PAUSE"
        pause = "PAUSE";
        pauseFont = new BitmapFont();
        pauseFont.setColor(Color.TEAL);
        pauseFont.getData().scale(3);
        pauseFont.draw(game.batch, pause, 300, 700);

        //Add resume button
        game.batch.draw(resumeButton.getButtonTexture(), resumeButton.getX(), resumeButton.getY());

        //Adds main menu button
        game.batch.draw(menuButton.getButtonTexture(), menuButton.getX(), menuButton.getY());

        game.batch.end();

        //Check if user touches the screen
        if(Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            //Checks if the menu button is touched
            if(resumeButton.contains((int)touchPos.x,(int)touchPos.y,game.screenHeight)){
                game.level1();
            }
            else if(menuButton.contains((int)touchPos.x,(int)touchPos.y,game.screenHeight)){
                game.mainMenu();
            }

        }
    }


@Override
    public void show() {

    }
@Override
    public void hide() {

    }
@Override
    public void resume() {

    }
@Override
    public void pause() {

    }
@Override
    public void resize(int width, int height) {

    }
@Override
    public void dispose() {
        pauseFont.dispose();
        resumeButton.dispose();
        menuButton.dispose();
    }
}
