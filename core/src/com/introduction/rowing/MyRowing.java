package com.introduction.rowing;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyRowing extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	public boolean moving = false;
	private Boat userBoat;
	Texture boatPicture;


	@Override
	public void create () {
		batch = new SpriteBatch();
		boatPicture = new Texture("boat.png");
		img = new Texture("badlogic.jpg");
		userBoat = new Boat(10,10,1, new Position(100,100), boatPicture);

	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
//		batch.draw(img, 0, 0);
		userBoat.update(Gdx.graphics.getDeltaTime());
		batch.draw(userBoat.getImage(), userBoat.getPosition().getX(), userBoat.getPosition().getY());
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

}

