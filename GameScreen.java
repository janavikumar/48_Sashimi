package com.sashimi.game;

import java.util.ArrayList;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import static com.badlogic.gdx.math.MathUtils.random;

public class GameScreen implements Screen {
    final Sashimi game;
    Music BGM;
    Sound hurt;
    Sound enemyDestroyed;

    protected int startingTime = (int)(System.currentTimeMillis()/1000);
    protected int secondsElapsed = 0;
    protected int justOnce = 0;

    protected Texture BGtexture;
    protected Rectangle BGposition;
    protected Texture BGtexture2;
    protected Rectangle BGposition2;
    protected int scrollSpeed = 4; // must be a factor of screenHeight (1280)

    protected Player you;

    protected Vector<Enemy> enemies = new Vector<Enemy>();
    ArrayList<Bullet> enemyBulletManager = new ArrayList<Bullet>();
    protected float enemySpawnDelay;
    protected float enemySpawnDelay2;
    protected int numEnemies;

    protected boolean rumbling = false;
    float rumbleTime = 0.2f;
    float cameraX, cameraY;
    float current_time = 0;
    float power = 7;
    float current_power = 0;
    boolean coordToggle = true;
    protected float rumbleDelay = 0;

    public EasyButton pauseButton;

    public GameScreen(final Sashimi game) {
        int yourWidth = 30;
        this.game = game;

        BGM = Gdx.audio.newMusic(Gdx.files.internal("Music/Sashimi.ogg"));
        hurt = Gdx.audio.newSound(Gdx.files.internal("Music/hurt.wav"));
        enemyDestroyed = Gdx.audio.newSound(Gdx.files.internal("Music/enemyDestroyed.wav"));

        BGtexture = new Texture(Gdx.files.internal("BG/BG1scrollable.png"));
        BGposition = new Rectangle(0,0,BGtexture.getWidth(),BGtexture.getHeight());
        BGtexture2 = new Texture(Gdx.files.internal("BG/BG1scrollable.png"));
        BGposition2 = new Rectangle(0,game.screenHeight,BGtexture2.getWidth(),BGtexture2.getHeight());

        you = new Player(this,game.screenWidth/2-yourWidth/2,100,"mrfish1.5x.png");
        //Set up menu button
        /*pauseButton = new EasyButton("Pause.png");
        pauseButton.setX((game.screenWidth / 2) - (pauseButton.getWidth() / 2));
        pauseButton.setY((game.screenHeight) - (pauseButton.getHeight() * 3 / 2));
        */
    }

    public void updateGameTime (int currentTime) {
        secondsElapsed = currentTime - startingTime;
        //System.out.println("Seconds elapsed: " + secondsElapsed);
    }

    public void spawnEnemies (float deltaTime) {
        // Example of a time-spawned enemy - testing purposes
        if (secondsElapsed > 2 && justOnce == 0) {
            Enemy tempEnemy = new Jellyfish(this, 500, 800, 1);
            enemies.add(tempEnemy);
            numEnemies++;
            justOnce++;
        }
        if (secondsElapsed > 3 && justOnce == 1) {
            Enemy tempEnemy = new Starfish(this, 200, 1000);
            enemies.add(tempEnemy);
            numEnemies++;
            justOnce++;
        }
    }

    public void spawnJellyRow(float deltaTime, int height, int direction) {
        // Spawns two enemies at a time, multiple times, to form a row
        enemySpawnDelay -= deltaTime;
        if (enemySpawnDelay <= 0) {
            int spawnPoint = (direction == 1) ? -50 : game.screenWidth+50;
            Enemy tempEnemy = new Jellyfish(this, spawnPoint, height, direction);
            enemies.add(tempEnemy);
            tempEnemy = new Jellyfish(this, spawnPoint, height-100, direction);
            enemies.add(tempEnemy);
            numEnemies += 2;
            enemySpawnDelay += 0.4;
        }
    }

    public void spawnJellySideColumns(float deltaTime, int spaceFromSides) {
        // Spawns two enemies at a time on surrounding sides
        enemySpawnDelay -= deltaTime;
        if (enemySpawnDelay <= 0) {
            Enemy tempEnemy = new Jellyfish(this, spaceFromSides, game.screenHeight+50, 3);
            tempEnemy.bulletVelocity = new Vector2(3,-6);
            enemies.add(tempEnemy);
            tempEnemy = new Jellyfish(this, game.screenWidth-spaceFromSides, game.screenHeight+25, 3);
            tempEnemy.bulletVelocity = new Vector2(-3,-6);
            enemies.add(tempEnemy);
            numEnemies += 2;
            enemySpawnDelay += 0.5;
        }
    }

    public void spawnRandomEnemies (float deltaTime) {
        // Spawn random enemies up until 20
        if (numEnemies < 20) {
            if (you.health > 0) {
                enemySpawnDelay -= deltaTime;
                if (enemySpawnDelay <= 0) {
                    int randomDeterminant = random(1);
                    Enemy randomEnemy = /*(1 == randomDeterminant) ? new Jellyfish(this, random(720), game.screenHeight, 0) :*/ new Starfish(this, random(720), game.screenHeight);
                    enemies.add(randomEnemy);
                    enemySpawnDelay += 0.2;
                    numEnemies++;
                }
            }
        }
    }

    public void renderEnemiesAndBullets (float deltaTime) {
        // Renders enemies
        for(Enemy e: enemies) {
            if (e != null) {
                e.render(deltaTime);
                e.fireBullet(deltaTime, enemyBulletManager);
            }
        }
        // Render enemy bullets
        for (int i=0; i<enemyBulletManager.size(); i++) {
            Bullet savedBullet = enemyBulletManager.get(i);
            savedBullet.update();
            // Saves memory by deleting bullets out of bounds
            if(savedBullet.bulletLocation.x > -30 && savedBullet.bulletLocation.x < game.screenWidth && savedBullet.bulletLocation.y > -500 && savedBullet.bulletLocation.y < game.screenHeight)
                game.batch.draw(savedBullet.texture, savedBullet.bulletLocation.x, savedBullet.bulletLocation.y);
            else {
                enemyBulletManager.remove(i);
                if(enemyBulletManager.size() > 0)
                    i--;
            }
        }
    }

    public void checkForCollisions (float deltaTime) {
        for(int i=0; i<enemies.size(); i++){
            Enemy e = enemies.get(i);

            // Handles enemy collisions
            if (!you.invincible) {
                if (e.isHit(you.getHitbox())) {
                    rumbling = true;
                    if (!you.invincible) you.health--; // COMMENT OUT FOR INVINCIBILITY
                    hurt.play();
                    you.invincible = true;
                    if (!e.invincible) e.health--;
                    System.out.println("Collided with Enemy. Your HP: " + you.health);
                    if (e.health <= 0) {
                        e.dispose();
                        enemies.remove(e);
                        you.setScore(System.currentTimeMillis());
                        numEnemies--;
                        enemyDestroyed.play();
                    }
                }
            }

            //Handles Bullet Collisions
            // From your bullets:
            for(int j=0; j<you.bulletManager.size(); j++){
                if(e.isHit(you.bulletManager.get(j).getPosition())){
                    if (!e.invincible) e.health--;
                    you.bulletManager.get(j).dispose();
                    you.bulletManager.remove(j);
                    j--;
                    if (e.health <= 0) {
                        e.dispose();
                        enemies.remove(e);
                        you.setScore(System.currentTimeMillis());
                        numEnemies--;
                        enemyDestroyed.play();
                    }
                }
            }
            // For enemy bullets:
            for(int j=0; j<enemyBulletManager.size(); j++) {
                if (!you.invincible) {
                    if (you.isHit(enemyBulletManager.get(j).getPosition())) {
                        rumbling = true;
                        you.health--; // COMMENT OUT FOR INVINCIBILITY
                        hurt.play();
                        you.invincible = true;
                        System.out.println("Hit by Bullet. Your HP: " + you.health);
                        enemyBulletManager.get(j).dispose();
                        enemyBulletManager.remove(j);
                        j--;
                    }
                }
            }

            // Delete enemies out of bounds, with 100 pixel buffer for spawning
            if(e.position.x < -100 || e.position.x > game.screenWidth+100 || e.position.y < -100 || e.position.y > game.screenHeight+100) {
                e.dispose();
                enemies.remove(e);
                numEnemies--;
                System.out.println("Disposed of enemy");
            }
        }
    }

    public void checkPlayerHealth () {
        // If your health is 0, go back to title screen
        if (you.health <= 0){
            System.out.println("Score: " + you.getScore());
            game.gameOver(you.getScore(), secondsElapsed);
        }
    }

    public void performRumble(float deltaTime) {
        rumbleDelay -= deltaTime;
        if(current_time <= rumbleTime) {
            if (rumbleDelay <= 0) {
                current_power = power * ((rumbleTime - current_time) / rumbleTime);
                cameraX = ((coordToggle) ? 0.8f-deltaTime : -0.8f-deltaTime) * 2 * current_power;
                cameraY = ((coordToggle) ? -1.3f-deltaTime : 1.3f-deltaTime) * 2 * current_power;

                // Set the camera to new x/y position
                you.camera.translate(-cameraX, -cameraY);
                current_time += deltaTime;
                coordToggle = !coordToggle;
                rumbleDelay += 0.07;
            }
        } else {
            // Reset camera position and values
            you.camera.setToOrtho(false, game.screenWidth, game.screenHeight);
            current_time = 0;
            current_power = 0;
            rumbling = false;
        }
    }

    public void scrollBG() {
        BGposition.y -= scrollSpeed;
        BGposition2.y -= scrollSpeed;
        if (BGposition.y == -game.screenHeight) BGposition.y = game.screenHeight;
        if (BGposition2.y == -game.screenHeight) BGposition2.y = game.screenHeight;
    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(BGtexture, BGposition.x, BGposition.y, BGposition.getWidth(), BGposition.getHeight());
        game.batch.draw(BGtexture2, BGposition2.x, BGposition2.y, BGposition2.getWidth(), BGposition2.getHeight());
        updateGameTime((int) (System.currentTimeMillis() / 1000));
        scrollBG();

        you.render(delta);

        spawnEnemies(delta);
        renderEnemiesAndBullets(delta);

        checkForCollisions(delta);

        checkPlayerHealth();

        if (rumbling) performRumble(delta);

        //Add pause button (temporary, will be improved later)
        //game.batch.draw(pauseButton.getButtonTexture(), pauseButton.getX(), pauseButton.getY());
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        BGM.setLooping(true);
        BGM.setVolume((float) 0.6);
        BGM.play();
    }

    @Override
    public void hide() {
        BGM.stop();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
//        pauseButton.dispose();
        you.dispose();
        BGtexture.dispose();
        for(Enemy e: enemies){
            e.dispose();
        }
        BGM.dispose();
        hurt.dispose();
        enemyDestroyed.dispose();
    }
}
