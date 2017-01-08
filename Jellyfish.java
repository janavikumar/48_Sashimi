package com.sashimi.game;

import com.badlogic.gdx.math.Vector2;

public class Jellyfish extends Enemy {
    public Jellyfish(GameScreen screen, int x, int y, int moveCode) {
        super(screen,x,y,"jelly1.5x.png");
        moveSpeed = 5;
        bulletVelocity = new Vector2(0,-6);
        moveType = moveCode;
    }
}
