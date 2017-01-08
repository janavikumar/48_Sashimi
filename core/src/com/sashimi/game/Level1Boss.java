package com.sashimi.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class Level1Boss extends RandomEnemy{
    private int stage = 0;
    private int stageDuration = 0;
    private int direction = 1;
    //private Entity beam = new Entity(screen,(int)this.getPosition().x,(int)this.getPosition().y,"Bullets/beam2.png" );

    private Texture stages[] = new Texture[7];

    Level1Boss(GameScreen screen, int x, int y){
        super(screen, x, y, "Stage1.png");
        health = 50;
        stages[0] = new Texture(Gdx.files.internal("Enemies/Stage1.png"));
        stages[1] = new Texture(Gdx.files.internal("Enemies/Stage2.png"));
        stages[2] = new Texture(Gdx.files.internal("Enemies/Stage3.png"));
        stages[3] = new Texture(Gdx.files.internal("Enemies/Stage4.png"));
        stages[4] = new Texture(Gdx.files.internal("Enemies/Stage5.png"));
        stages[5] = new Texture(Gdx.files.internal("Enemies/Stage6.png"));
        stages[6] = new Texture(Gdx.files.internal("Enemies/Stage7.png"));
        this.moveSpeed = 3;
    }

    // The below actually just adds bullets to an external bulletManager (from the game screen)
    // All it really does is add a Bullet with the entity's position
    @Override
    public void fireBullet (float deltaTime, ArrayList<Bullet> bulletManager) {
        if (health > 0 && firesBullets) {
            fireDelay -= deltaTime;
            if(fireDelay <= 0) {
                Bullet tempBullet = new Bullet(screen, (int)(position.x+position.getHeight()/4), (int)(position.y-position.getHeight()), "beam2.png", bulletVelocity);
                bulletManager.add(tempBullet);
                fireDelay += 0.7;
            }
        }
    }

    @Override
    void render(float deltaTime){
        moveLeftAndRight();
        screen.game.batch.draw(stages[stage],this.getPosition().x,this.getPosition().y);
        if(stageDuration == 0){
            //Calculate new stage
            stage = stage+direction;
            //If not in a known stage, switch direction
            if(stage == 7 || stage ==-1){
                direction = direction * -1;
                stage = stage + direction;
            }

        }
        if(stage == 6){/*
            beam.setX((int)this.getPosition().getX());
            beam.setY((int)this.getPosition().getY()-30);
            screen.game.batch.draw(beam.texture,beam.getPosition().getX(),beam.getPosition().getY());
            stageDuration = (stageDuration +1)%21;*/
        }
        else {
            stageDuration = (stageDuration + 1) % 7;
        }

    }

    @Override
    void dispose() {
        super.dispose();
        for(int i=0; i<stages.length; i++){
            stages[i].dispose();
        }
    }




}
