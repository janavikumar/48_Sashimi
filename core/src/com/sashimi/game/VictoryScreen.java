package com.sashimi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;

public class VictoryScreen extends GameOverScreen{
    public VictoryScreen(final Sashimi game, int score, int secondsElapsed) {
        super(game, score, secondsElapsed);
        text = "          YOU WIN!\n\n" +
                "Score:               " + score + "\n" +
                "You survived:    " + secondsElapsed + " seconds";
    }
}


