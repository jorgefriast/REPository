package com.introduction.rowing;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyRowing extends ApplicationAdapter {
	SpriteBatch batch;
	TextureRegion[] water;

	float stateTime = 0;
	float frameDuration = 0.1f; // Adjust the frame duration as needed
	
	private Boat userBoat;
	Texture boatPicture;


	@Override
	public void create () {
		batch = new SpriteBatch();
        boatPicture = new Texture("boat-top-view-2.png");

		// Water GIF setup
		water = new TextureRegion[5];
		for(int i = 0; i < water.length; i++)
			water[i] = new TextureRegion(new Texture("water-frames//frame_" + i + "_delay-0.1s.gif"));

		userBoat = new Boat(10,10,1, new Position(650,0), boatPicture);

	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 1, 0, 1);
		batch.begin();
		// Water flow (GIF)
		stateTime += Gdx.graphics.getDeltaTime();
		int currentFrameIndex = (int) (stateTime / frameDuration) % water.length;
		batch.draw(water[currentFrameIndex], 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//boat
		userBoat.update(Gdx.graphics.getDeltaTime());
		batch.draw(userBoat.getImage(), userBoat.getPosition().getX(), userBoat.getPosition().getY(), userBoat.getWidth(), userBoat.getHeight());


		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
        boatPicture.dispose();
	}
}
