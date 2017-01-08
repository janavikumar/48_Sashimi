package com.sashimi.game;

import java.util.Random;

public class RandomEnemy extends Enemy {
    protected Random random = new Random();
    protected int targetX;
    protected int targetY;

    public RandomEnemy(GameScreen screen, int x, int y, String textureName) {
        super(screen,x,y,textureName);
        targetX = 70;
        targetY = 70;
        firesBullets = true;
    }

    void moveLeftAndRight(){
        if(position.contains(targetX,position.getY())){
            targetX = random.nextInt(screen.game.screenWidth);
        }
        if(position.getX() < targetX){
            position.setX(position.getX() + moveSpeed);
        }
        else{
            position.setX(position.getX() - moveSpeed);
        }
    }

    void moveUpAndDown(){
        if(position.contains(position.getX(),targetY)){
            targetY = random.nextInt(screen.game.screenWidth);
        }

        if(position.getY() < targetY){
            position.setY(position.getY() + moveSpeed);
        }
        else{
            position.setY(position.getY() - moveSpeed);
        }
    }

    void render() {
        moveLeftAndRight();
        moveUpAndDown();
        super.render();
    }
}
