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

		userBoat = new Boat(1, new Position(650,-230), boatPicture, 1, 3, 5, 1, 0, 0);

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
		batch.draw(userBoat.getImage(), userBoat.getPosition().getX(), userBoat.getPosition().getY(), userBoat.getWidth(), userBoat.getHeight());
		userBoat.updateKeys(Gdx.graphics.getDeltaTime());

		//update boat's y position every 5 frames
		if(currentFrameIndex % 5 == 0) {
			userBoat.updateY(Gdx.graphics.getDeltaTime());
		}

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
        boatPicture.dispose();
	}
}
