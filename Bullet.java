package com.sashimi.game;

import com.badlogic.gdx.math.Vector2;

public class Bullet extends Entity {
    public Vector2 bulletLocation = new Vector2(0,0);
    private Vector2 bulletVelocity = new Vector2(0,0);

    public Bullet(GameScreen screen, int x, int y, String textureName, int velocity) {
        super(screen, x, y, "Bullets/"+textureName);
        bulletLocation = new Vector2(x,y);
        bulletVelocity = new Vector2(0,velocity);
    }

    public Bullet(GameScreen screen, int x, int y, String textureName, Vector2 velocity) {
        super(screen, x, y, "Bullets/"+textureName);
        bulletLocation = new Vector2(x,y);
        bulletVelocity = velocity;
    }

    public void setVelocity (float x, float y) {
        bulletVelocity.set(x, y);
    }

    public void update() {
        bulletLocation.x += bulletVelocity.x;
        bulletLocation.y += bulletVelocity.y;
        position.setPosition(bulletLocation);
    }
}
