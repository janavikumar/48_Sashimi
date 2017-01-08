package com.sashimi.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

//import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
//import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class Sashimi extends Game {
    private TextureAtlas atlas;
    public SpriteBatch batch;

    // CURRENTLY DOES NOT WORK ON ANDROID, BUT WORKS ON DESKTOP
    // We want to use this because bitmap fonts are not as scalable
    //public FreeTypeFontGenerator generator;
    //public FreeTypeFontParameter parameter;

    public BitmapFont font;

    public int screenWidth = 720;
    public int screenHeight = 1280;

    //Called when Application is Created
	public void create () {
        atlas = new TextureAtlas();
        batch = new SpriteBatch();

        //generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Helvetica.OTF"));
        //parameter = new FreeTypeFontParameter();
        //parameter.size = 30;
        //font = generator.generateFont(parameter);
        font = new BitmapFont();
        this.setScreen(new MainMenuScreen(this));

	}

    // render method is where we draw the media elements of the game (that we created earlier)
    // method is called every 0.025 seconds by the game loop
    // Here, we can move images, update animations, and do other screen rendering tasks
    //May Include Game Logic Updates
	public void render () {
        super.render();
	}


    //Called when Application is Destroyed
    public void dispose() {
        atlas.dispose();
        batch.dispose();
        font.dispose();
    }

    public void level1Boss(){
        System.out.println("Disposing of main menu screen");
        screen.dispose();
        setScreen(new BossScreen(this));
    }

    public void level1(){
        System.out.println("Disposing of main menu screen");
        screen.dispose();
        setScreen(new Level1(this));
    }

    public void infoScreen(){
        System.out.println("Disposing of main menu screen");
        screen.dispose();
        setScreen(new InfoScreen(this));
    }

    public void mainMenu(){
        System.out.println("Disposing of info / game over screen");
        screen.dispose();
        setScreen(new MainMenuScreen(this));
    }

    public void gameOver(int score, int secondsElapsed){
        System.out.println("Disposing of game screen");
        screen.dispose();
        setScreen(new GameOverScreen(this, score, secondsElapsed));
    }

    public void victory(int score, int secondsElapsed){
        System.out.println("Disposing of game screen");
        screen.dispose();
        setScreen(new VictoryScreen(this, score, secondsElapsed));
    }

    public void pauseScreen(){
        System.out.println("Disposing of game screen");
        screen.dispose();
        setScreen(new PauseScreen(this));
    }

    //Called When Home Button is Pressed or Incoming Call Received
    //Good Place to Save Game State
    //public void pause() {}

    //Called When game is re-sized and game is not paused
    //Also Called Once after Create() Method
    //Parameters are width and height the Screen has Been resized to
    //public void resize(int width, int height){}

    //public void resume(){}
}