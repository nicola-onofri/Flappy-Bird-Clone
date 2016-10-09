package com.nicolaonofri.flappbird.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
    public SpriteBatch batch;
    public Texture background;
    public Texture tube_up;
    public Texture tube_down;
    public Texture[] birds;
    public Random randomNumberGenerator;

    //Keep track of the actual state of the flap
    public int flapState = 0;

    //Only the Y will change during the game
    public float birdY = 0;
    public float velocity = 0;

    //Keep track of the actual state of the game
    public int gameState = 0;

    public float gravity = 1.5f;
    public float tube_gap = 400;
    public float tube_offset;
    public float maximumTubeOffset;
    public final float offset = 100;


    @Override
    public void create() {
        //Character or animation
        batch = new SpriteBatch();

        //Texture -> Image in game programming
        background = new Texture("bg.png");
        birds = new Texture[2];

        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");

        birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;

        tube_up = new Texture("toptube.png");
        tube_down = new Texture("bottomtube.png");

        maximumTubeOffset = Gdx.graphics.getHeight() / 2 - tube_gap / 2 - offset;
        randomNumberGenerator = new Random();
    }

    //Called everytime (with a default delay)
    @Override
    public void render() {
        batch.begin();
        //Draw background -> the order the components are drown is the order they are placed (z-index)
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState != 0) { //Game is active
            batch.draw(tube_up, Gdx.graphics.getWidth() / 2 - tube_up.getWidth() / 2, Gdx.graphics.getHeight() / 2 + tube_gap / 2 + tube_offset);
            batch.draw(tube_down, Gdx.graphics.getWidth() / 2 - tube_down.getWidth() / 2, Gdx.graphics.getHeight() / 2 - tube_gap / 2 - tube_down.getHeight() + tube_offset);

            if (Gdx.input.justTouched()) {
                //Reset speed if the screen is touched
                velocity = -30;

                //Set tube offset
                tube_offset = (randomNumberGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - tube_gap - offset * 2);
            }

            if (birdY > 0 || velocity < 0) {
                velocity += gravity;
                birdY -= velocity;
            }
        } else {
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        }

        //Animate the sprite
        flapState = flapState == 0 ? 1 : 0;


        //Draw the passed Texture at the given coordinates of the screen, with the given size
        batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
        batch.end();
    }
}
