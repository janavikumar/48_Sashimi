package com.sashimi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Basic Class to quickly generate and render sprites for a game
 * Contains functionality to maintain that the sprite stays within boundaries
 * Also contains functionality to see if the sprite is hit
 */
public class Entity {
    final GameScreen screen;
    final protected Rectangle position;
    //Removed final because some characters have multiple stages
    Texture texture;
    public int health;
    protected Vector2 bulletVelocity;
    protected float fireDelay;
    protected boolean firesBullets;
    protected boolean visibleTexture = true;
    protected boolean invincible = false;

    Entity(GameScreen screen, int x, int y, String textureName){
        texture = new Texture(Gdx.files.internal(textureName));
        position = new Rectangle(x,y,texture.getWidth(),texture.getHeight());
        this.screen = screen;
    }

    Rectangle getPosition(){ return position; }

    //Keeps X within the boundaries of the game screen
    void setX(int x) {
        if (x < screen.game.screenWidth) {
            position.setX(0);
        } else if (x + position.getWidth() > screen.game.screenWidth) {
            position.setX(screen.game.screenWidth - position.getWidth());
        } else {
            position.setX(x);
        }
    }

    //Keeps Y position in the boundaries of the game screen
    void setY(int y) {
        if(y < 0){
            position.setY(0);
        }
        else if( y + position.getHeight() > screen.game.screenHeight){
            position.setY(screen.game.screenWidth - position.getHeight());
        }
        else{
            position.setY(y);
        }
    }

    //Checks if the rectangle is touched by any other rectangle
    boolean isHit(Rectangle other){
        return position.overlaps(other);
    }

    boolean isHit(int x, int y) {
        return (position.contains(x, y));
    }

    void render(){
        if (visibleTexture) screen.game.batch.draw(texture, position.x, position.y, position.getWidth(), position.getHeight());
    }

    void dispose(){
        texture.dispose();
    }
}
