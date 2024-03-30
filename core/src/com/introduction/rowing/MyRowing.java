package com.introduction.rowing;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyRowing extends ApplicationAdapter {
	SpriteBatch batch;
	Texture boat;
	TextureRegion[] water;

	float stateTime = 0;
	float frameDuration = 0.1f; // Adjust the frame duration as needed
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		boat = new Texture("boat-top-view-2.png");

		// Water GIF setup
		water = new TextureRegion[5];
		for(int i = 0; i < water.length; i++)
			water[i] = new TextureRegion(new Texture("water-frames//frame_" + i + "_delay-0.1s.gif"));

	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 1, 0, 1);
		batch.begin();

		// Water flow (GIF)
		stateTime += Gdx.graphics.getDeltaTime();
		int currentFrameIndex = (int) (stateTime / frameDuration) % water.length;
		batch.draw(water[currentFrameIndex], 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		batch.draw(boat, 650, -200, 200, 600);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		boat.dispose();
	}
}
