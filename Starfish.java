package com.sashimi.game;

import com.badlogic.gdx.math.Vector2;

public class Starfish extends RandomEnemy {
    public Starfish(GameScreen screen, int x, int y) {
        super(screen,x,y,"starfish1.5x.png");
        moveSpeed = 7;
        bulletVelocity = new Vector2(0,-9);
    }

    @Override
    public void render(float deltaTime) {
        super.render();
    }
}
