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
	private Boat userBoat;
	Texture boatPicture;


	@Override
	public void create () {
		batch = new SpriteBatch();
		boatPicture = new Texture("boat.png");
		userBoat = new Boat(10,10,1, new Position(1000,1000), boatPicture);

	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		userBoat.update(Gdx.graphics.getDeltaTime());
		batch.draw(userBoat.getImage(), userBoat.getPosition().getX(), userBoat.getPosition().getY(), userBoat.getWidth(), userBoat.getHeight());
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

}

