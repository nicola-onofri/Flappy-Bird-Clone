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
    public float birdY = 0;
    public float velocity = 0;
    public float maximumTubeOffset;
    public float tube_distance;

    //Keep track of the actual state of the game
    public int gameState = 0;

    //Constants
    public final float gravity = 1.7f;
    public final float impulse = -27;
    public final float tube_gap = 500;
    public final float distance_offset = 200;
    public final int numberOfTubes = 4;
    public final float tube_speed = 8;

    //We use a set of 4 tubes on the screen and everytime a pair of tubes
    //reaches the left side of the screen we reset its position on the right side
    public float[] tube_offset = new float[numberOfTubes];
    public float[] tubeX = new float[numberOfTubes];


    @Override
    public void create() {
        //Character or animation
        batch = new SpriteBatch();

//        background = new Texture("galaxy.jpg");
        background = new Texture("bg.png");
        birds = new Texture[2];

        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");
//        birds[0] = new Texture("android-arms-up.png");
//        birds[1] = new Texture("android-arms-down.png");


        tube_up = new Texture("toptube.png");
        tube_down = new Texture("bottomtube.png");

        birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;
        tube_distance = Gdx.graphics.getWidth() / 2 + distance_offset;
        maximumTubeOffset = Gdx.graphics.getHeight() / 2 - tube_gap / 2 - 100;

        randomNumberGenerator = new Random();

        for (int i = 0; i < numberOfTubes; i++) {
            tube_offset[i] = (randomNumberGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - tube_gap - maximumTubeOffset);
            tubeX[i] = Gdx.graphics.getWidth() / 2 - tube_up.getWidth() / 2 + i * tube_distance;
        }
    }

    //Called everytime (with a default delay)
    @Override
    public void render() {
        batch.begin();
        //Draw background -> the order the components are drown is the order they are placed (z-index)
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState != 0) { //Game is active
            if (Gdx.input.justTouched()) {
                //Reset speed if the screen is touched
                velocity = impulse;
            }

            for (int i = 0; i < numberOfTubes; i++) {

                //Shift tubes back to the right side of the screen
                if (tubeX[i] < -tube_up.getWidth()) {
                    tubeX[i] += numberOfTubes * tube_distance;
                }

                tubeX[i] -= tube_speed;
                batch.draw(tube_up, tubeX[i], Gdx.graphics.getHeight() / 2 + tube_gap / 2 + tube_offset[i]);
                batch.draw(tube_down, tubeX[i], Gdx.graphics.getHeight() / 2 - tube_gap / 2 - tube_down.getHeight() + tube_offset[i]);
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
