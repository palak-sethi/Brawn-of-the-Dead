package com.coinmaster.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import java.util.*;
import java.util.Random;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class CoinMaster extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	Texture dizzy;
	int manState = 0;
	int pause = 0;
	float gravity = 0.6f;
	float velocity = 0;
	int manY = 0;
	Rectangle manRectangle;
	int score = 0;
	int high = 0;
	BitmapFont font;
	int gameState = 0;
	Random random;

	ArrayList<Integer> coinXs = new ArrayList<Integer>();
	ArrayList<Integer> coinYs = new ArrayList<Integer>();
	ArrayList<Rectangle> coinRectangles = new ArrayList<Rectangle>();
	Texture coin;
	int coinCount;

	ArrayList<Integer> bombXs = new ArrayList<Integer>();
	ArrayList<Integer> bombYs = new ArrayList<Integer>();
	ArrayList<Rectangle> bombRectangles = new ArrayList<Rectangle>();
	Texture bomb;
	int bombCount;


	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("background.jpg");
		dizzy = new Texture("dead-1.png");
		man = new Texture[7];
		man[0] = new Texture("Run-0.png");
		man[1] = new Texture("Run-1.png");
		man[2] = new Texture("Run-2.png");
		man[3] = new Texture("Run-3.png");
		man[4] = new Texture("Run-4.png");
		man[5] = new Texture("Run-5.png");
		man[6] = new Texture("Run-6.png");



		manY = 0;

		coin = new Texture("brain.png");
		bomb = new Texture("fire.png");
		random = new Random();

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

	}
	public void makeCoin() {
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		coinYs.add((int)height);
		coinXs.add(Gdx.graphics.getWidth());
	}

	public void makeBomb() {
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		bombYs.add((int)height);
		bombXs.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		if(gameState == 1) {
			//GAME IS LIVE
			//BOMBS
			if(bombCount < 250) {
				bombCount++;
			}
			else {
				bombCount = 0;
				makeBomb();
			}
			bombRectangles.clear();
			for(int i = 0; i<bombXs.size(); i++) {
				batch.draw(bomb, bombXs.get(i), bombYs.get(i));
				bombXs.set(i, bombXs.get(i)-12);
				bombRectangles.add(new Rectangle(bombXs.get(i), bombYs.get(i), bomb.getWidth(), bomb.getHeight()));
			}


			//COINS
			if(coinCount < 100) {
				coinCount++;
			}
			else {
				coinCount = 0;
				makeCoin();
			}
			coinRectangles.clear();
			for(int i = 0; i<coinXs.size(); i++) {
				batch.draw(coin, coinXs.get(i), coinYs.get(i));
				coinXs.set(i, coinXs.get(i)-8);
				coinRectangles.add(new Rectangle(coinXs.get(i), coinYs.get(i), coin.getWidth(), coin.getHeight()));
			}


			if(Gdx.input.justTouched()) {
				velocity = -15;
			}
			if(pause < 8) {
				pause++;
			}
			else {
				pause = 0;
				if(manState < 6) {
					manState++;
				}
				else {
					manState = 0;
				}
			}

			velocity += gravity;
			manY -= velocity;

			if(manY <= 0) {
				manY = 0;
			}

		}
		else if(gameState == 0) {
			//STARTING POSITION
			if(Gdx.input.justTouched()) {
				gameState = 1;
			}
		}
		else if(gameState == 2) {
			//GAME OVER
			if(Gdx.input.justTouched()) {
				gameState = 1;
				manY = 0;
				score = 0;
				velocity = 0;
				coinXs.clear();
				coinYs.clear();
				coinRectangles.clear();
				coinCount = 0;
				bombXs.clear();
				bombYs.clear();
				bombRectangles.clear();
				bombCount = 0;
			}
		}
		if(gameState == 2) {
			batch.draw(dizzy,Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY);
		}
		else{
			batch.draw(man[manState],100 , manY);
		}
		manRectangle = new Rectangle(100 , manY, man[manState].getWidth(), man[manState].getHeight());

		for(int i =0; i<coinRectangles.size(); i++) {
			if(Intersector.overlaps(manRectangle, coinRectangles.get(i))) {
				score++;
				if (score>high) {
					high = score;
				}
				coinRectangles.remove(i);
				coinXs.remove(i);
				coinYs.remove(i);
				break;
			}
		}

		for(int i =0; i<bombRectangles.size(); i++) {
			if(Intersector.overlaps(manRectangle, bombRectangles.get(i))) {
				gameState = 2;

			}
		}

		font.draw(batch, String.valueOf(score),Gdx.graphics.getWidth() - 150, Gdx.graphics.getHeight() - 50);
		font.draw(batch, String.valueOf(high),Gdx.graphics.getWidth() - 150, Gdx.graphics.getHeight() - 250);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
