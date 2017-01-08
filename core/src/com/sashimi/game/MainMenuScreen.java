package com.sashimi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;


public class MainMenuScreen implements Screen {
    Music BGM;
    //Sound button;
    final Sashimi game;
    OrthographicCamera camera;
    SpriteBatch batch;
    //Texture buttonTex;
    EasyButton playButton;
    EasyButton infoButton;

    public MainMenuScreen(final Sashimi game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.screenWidth, game.screenHeight);
        batch = new SpriteBatch();
        BGM = Gdx.audio.newMusic(Gdx.files.internal("Music/MainMenu.ogg"));
        //button = Gdx.audio.newSound(Gdx.files.internal("Music/button.wav"));

        //Sets up a play button
        playButton = new EasyButton("Play Button.png");
        playButton.setX((game.screenWidth/2)-(playButton.getWidth()/2));
        playButton.setY((game.screenHeight/2));

        //Sets up info screen button
        infoButton = new EasyButton("Instructions.png");
        infoButton.setX((game.screenWidth/2)-(infoButton.getWidth()/2));
        infoButton.setY((game.screenHeight/2) - (infoButton.getHeight()*3/2));

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        //Render Play Button
        game.batch.begin();
        game.batch.draw(playButton.getButtonTexture(), playButton.getX(), playButton.getY());
        game.batch.draw(infoButton.getButtonTexture(), infoButton.getX(), infoButton.getY());
        game.batch.end();

        //In the event that screen is touched
        if(Gdx.input.justTouched()){
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            System.out.println("Screen touched! X: " + touchPos.x + " Y: " + touchPos.y);

            //Checks if the play button is touched

            System.out.println("Infobutton coords - X: " + infoButton.getX() + " Y: " + infoButton.getY());

            if(playButton.contains((int)touchPos.x,(int)touchPos.y,game.screenHeight)){
                game.level1();
                //button.play();
            }
            //Check if the info button is touched
            else if(infoButton.contains((int)touchPos.x,(int)touchPos.y,game.screenHeight)){
                //button.play();
                game.infoScreen();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        BGM.setLooping(true);
        BGM.setVolume((float)0.6);
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
        playButton.dispose();
        infoButton.dispose();
        BGM.dispose();
        //button.dispose();
    }
}
