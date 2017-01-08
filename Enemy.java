package com.sashimi.game;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class for Enemy Creation and AI
 */
public class Enemy extends Entity {
    protected int moveSpeed = 6;
    protected int moveType;

    Enemy(GameScreen screen, int x, int y, String textureName){
        super(screen,x,y,"Enemies/"+textureName);
        health = 1; // Default health
        bulletVelocity = new Vector2(0,-8); // Default velocity
        firesBullets = true;
        moveType = 0;
    }

    // The below actually just adds bullets to an external bulletManager (from the game screen)
    // All it really does is add a Bullet with the entity's position
    public void fireBullet (float deltaTime, ArrayList<Bullet> bulletManager) {
        if (health > 0 && firesBullets) {
            fireDelay -= deltaTime;
            if(fireDelay <= 0) {
                Bullet tempBullet = new Bullet(screen, (int)(position.x+position.getHeight()/4), (int)(position.y-position.getHeight()/4), "enemyBubble.png", bulletVelocity);
                bulletManager.add(tempBullet);
                fireDelay += 0.4;
            }
        }
    }

    void render(float delta){
        super.render();
        // Horizontal movement (right)
        if (moveType == 1) {position.x += moveSpeed;}
        // Horizontal movement (left)
        else if (moveType == 2) {position.x -= moveSpeed;}
        // Vertical movement (down)
        else if (moveType == 3) {position.y -= moveSpeed;}
    }
}
