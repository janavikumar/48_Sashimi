package com.sashimi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class Player extends Entity {
    protected OrthographicCamera camera;
    private Sound bubble;

    ArrayList<Bullet> bulletManager = new ArrayList<Bullet>();
    public Bullet bullet;

    final protected Rectangle hitbox;
    final Texture hitboxTexture;

    private int activeTouch = 0;
    private int touchMoveSpeed = 1500;
    private int keyMoveSpeed = 600;
    private int playerSpacing = 150;
    private Vector2 velocity;

    protected float invincibleDelay = 0;
    protected float invincibleTimeTotal = 0;
    protected boolean visibleToggle = true;

    private double prevHitTime = 0;
    private int score;
    public int enemiesHit;

    Player(GameScreen screen, int x, int y, String textureName) {
        super(screen, x, y, "Players/"+textureName);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screen.game.screenWidth, screen.game.screenHeight);
        bubble = Gdx.audio.newSound(Gdx.files.internal("Music/bubble.wav"));

        hitboxTexture = new Texture(Gdx.files.internal("Players/smallerHitbox.png"));
        hitbox = new Rectangle(x+position.getWidth()/2,y+position.getHeight()/2,hitboxTexture.getWidth(),hitboxTexture.getHeight());

        velocity = new Vector2(0,0);
        health = 10;
        bulletVelocity = new Vector2(0,20);
        firesBullets = true;
    }

    public Rectangle getHitbox() { return hitbox; }

    @Override
    boolean isHit(Rectangle other){
        return hitbox.overlaps(other);
    }

    public void setScore(double currentTime){
        double deltaTime = (int)(currentTime - prevHitTime);
        if (deltaTime == 0) deltaTime = 1; // Takes care of zeros
        score = score + (int)(1000.0/deltaTime);
        prevHitTime = currentTime;
        // System.out.println("Time difference: " + deltaTime);
        // System.out.println("Score: " + score);
    }

    public int getScore(){
        return score;
    }

    public void checkInvincibility(float deltaTime) {
        invincibleTimeTotal += deltaTime;
        invincibleDelay -= deltaTime;
        if (invincibleTimeTotal < 3) {
            if (invincibleDelay <= 0) {
                visibleToggle = !visibleToggle;
                visibleTexture = (visibleToggle);
                invincibleDelay += 0.07;
            }
        }
        else {
            invincibleTimeTotal = 0;
            invincibleDelay = 0;
            visibleTexture = true;
            invincible = false;
        }
    }

    public void fireBullet (float deltaTime) {
        // Toggles on second finger tap
        if (Gdx.input.isTouched(1) && activeTouch == 0) activeTouch = 1;
        else if (Gdx.input.isTouched(1) && activeTouch == 1) activeTouch = 0;

        // Hold SHIFT or tap with a second finger to go into focus shot mode
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || activeTouch == 1) {
            if (health > 0 && firesBullets) {
                fireDelay -= deltaTime;
                if(fireDelay <= 0) {
                    Bullet tempBullet = new Bullet(screen, (int)(hitbox.x-hitbox.getWidth()/2), (int)(hitbox.y-hitbox.getHeight()+position.getHeight()/2), "bubble.png", bulletVelocity);
                    bulletManager.add(tempBullet);
                    fireDelay += 0.1;
                    bubble.play();
                }
            }
        }
        // Default shooting
        else {
            if (health > 0 && firesBullets) {
                fireDelay -= deltaTime;
                if (fireDelay <= 0) {
                    Bullet tempBullet = new Bullet(screen, (int) (hitbox.x - hitbox.getWidth() / 2), (int) (hitbox.y - hitbox.getHeight() + position.getHeight() / 2), "bubble.png", bulletVelocity);
                    bulletManager.add(tempBullet);
                    Vector2 newVelocity = new Vector2(7, 19);
                    tempBullet = new Bullet(screen, (int) (hitbox.x - hitbox.getWidth() / 2), (int) (hitbox.y - hitbox.getHeight() + position.getHeight() / 2), "bubble.png", newVelocity);
                    bulletManager.add(tempBullet);
                    newVelocity = new Vector2(-7, 19);
                    tempBullet = new Bullet(screen, (int) (hitbox.x - hitbox.getWidth() / 2), (int) (hitbox.y - hitbox.getHeight() + position.getHeight() / 2), "bubble.png", newVelocity);
                    bulletManager.add(tempBullet);
                    fireDelay += 0.2;
                    bubble.play();
                }
            }
        }
    }

    public void renderBullets () {
        // Renders bullets
        for (int i=0; i<bulletManager.size(); i++) {
            bullet = bulletManager.get(i);
            bullet.update();
            // Saves memory by removing bullets that are out of bounds
            // Note: Changed to -30 for overlap of rectangle and boundary
            if(bullet.bulletLocation.x > -30 && bullet.bulletLocation.x < screen.game.screenWidth && bullet.bulletLocation.y > 0 && bullet.bulletLocation.y < screen.game.screenHeight)
                screen.game.batch.draw(bullet.texture, bullet.bulletLocation.x, bullet.bulletLocation.y);
            else {
                bulletManager.remove(i);
                if(bulletManager.size() > 0)
                    i--;
            }
        }
    }

    public void update(float deltaTime) {

        // Touch movement
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            // Find the vector to move along (x and y distances give direction and magnitude)
            float xDistance = (touchPos.x - position.getWidth()/2) - position.x;
            float yDistance = (touchPos.y + playerSpacing) - position.y;
            velocity.set(xDistance, yDistance);

            // Normalizes the above vector distance to obtain hypotenuse of 1 (single unit), then multiplies by speed scalar
            velocity = velocity.nor().scl(touchMoveSpeed);

            float xTravelDistance = velocity.x * deltaTime;
            float yTravelDistance = velocity.y * deltaTime;

            // If the distance between Player and touchPos is smaller than the distance traveled in one time refresh,
            // simply adding the velocity will cause the position to be overshot, causing it to overcompensate and therefore jitter.
            // Therefore, in that case, simply snap the player to the touchPosition to prevent overcompensation.
            if (Math.abs(xDistance) < Math.abs(xTravelDistance) || Math.abs(yDistance) < Math.abs(yTravelDistance)) {
                position.x = touchPos.x - position.getWidth()/2;
                position.y = touchPos.y + playerSpacing;
            }
            // Otherwise, add the travel components to current position
            else {
                position.x += xTravelDistance;
                position.y += yTravelDistance;
            }
        }

        // Hold CTRL to go into focus movement (keyboard only)
        keyMoveSpeed = (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) ? 350 : 600;

        // Keyboard input
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) position.x -= keyMoveSpeed * Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) position.x += keyMoveSpeed * Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) position.y += keyMoveSpeed * Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) position.y -= keyMoveSpeed * Gdx.graphics.getDeltaTime();

        // Keeps you in the game's boundaries
        if(position.x < 0) position.x = 0;
        if(position.x > screen.game.screenWidth - position.getWidth()) position.x = screen.game.screenWidth-position.getWidth();
        if(position.y < 0) position.y = 0;
        if(position.y > screen.game.screenHeight - position.getHeight()) position.y = screen.game.screenHeight-position.getHeight();

        // Updates hitbox position
        hitbox.x = position.x + position.getWidth()/2 - hitbox.getWidth()/2;
        hitbox.y = position.y + position.getHeight()/2 - hitbox.getHeight()/2;
    }

    void render(float deltaTime){
        camera.update();
        // Cool, makes the camera follow the fish
        // camera.position.set(position.x, position.y, 0);
        screen.game.batch.setProjectionMatrix(camera.combined);
        update(deltaTime);
        super.render();

        screen.game.batch.draw(hitboxTexture, hitbox.x, hitbox.y, hitbox.getWidth(), hitbox.getHeight());

        fireBullet(deltaTime);
        renderBullets();

        if (invincible) checkInvincibility(deltaTime);
    }
}
