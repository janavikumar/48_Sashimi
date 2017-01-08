package com.sashimi.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;

public class EasyButton extends Rectangle{
    private Texture buttonTexture;

    EasyButton(String fileName){
        //Initialize Basic Rectangle
        super();

        //Load In Texture, along with width and height
        buttonTexture = new Texture(Gdx.files.internal("Buttons/"+fileName));
        this.setHeight(buttonTexture.getHeight());
        this.setWidth(buttonTexture.getWidth());
    }

    void setButtonTexture(Texture texture){
        this.buttonTexture = texture;
    }

    Texture getButtonTexture(){
        return buttonTexture;
    }

    //Converts a raw xIn and yIn from Gdx.input to spritebatch coordinate point
    boolean contains(int x, int y, int screenHeight){
        return (contains(x,y));
    }

    void dispose(){
        buttonTexture.dispose();
    }

}
