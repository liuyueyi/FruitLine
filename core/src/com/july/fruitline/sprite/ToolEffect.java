package com.july.fruitline.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.july.fruitline.util.Assets;
import com.july.fruitline.util.Constants;

public class ToolEffect implements Poolable {
	Animation anim[];
	float duration;
	TextureRegion key;

	private int type;
	private float x, y;
	private float speed = 15 * Constants.hrate;
	private float addX, addY;

	public ToolEffect() {
		anim = new Animation[6];
		anim[Constants.CROSS_TOOl] = new Animation(0.2f,
				Assets.instance.toolEffects[Constants.CROSS_TOOl]);
		anim[Constants.VER_TOOL] = new Animation(0.2f,
				Assets.instance.toolEffects[Constants.VER_TOOL]);
		anim[Constants.HORN_TOOL] = new Animation(0.2f,
				Assets.instance.toolEffects[Constants.HORN_TOOL]);
		anim[Constants.TIME_TOOL] = new Animation(0.2f,
				Assets.instance.toolEffects[Constants.TIME_TOOL]);
		anim[Constants.STAR_TOOL] = new Animation(0.2f,
				Assets.instance.toolEffects[Constants.STAR_TOOL]);
		anim[Constants.BOMB_TOOL] = new Animation(0.3f,
				Assets.instance.toolEffects[Constants.BOMB_TOOL]);
	}

	public boolean show = false;

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		show = false;
		x = 0;
		y = 0;
		type = -1;
		time = 0;
		duration = 0;
		addX = 0;
		addY = 0;
	}

	public void show(float x, float y, int type) {
		this.x = x;
		this.y = y;
		duration = 0;
		this.type = type;
		show = true;
		time = 0;
		if (type == Constants.TIME_TOOL || type == Constants.STAR_TOOL) {
			addX = (Constants.width / 2 - x) / 30;
			addY = (Constants.fillY - y) / 30;
		}
	}

	int time;

	public void draw(Batch batch) {
		if (!show)
			return;
		if (time > 30)
			show = false;

		duration += Gdx.graphics.getDeltaTime();
		key = anim[type].getKeyFrame(duration, true);
		time++;
		switch (type) {
		case Constants.VER_TOOL:
			batch.draw(key, x, y - time * speed, Constants.fruitWidth,
					Constants.fruitHeight * 2);
			batch.draw(key, x, y + time * speed, Constants.fruitWidth / 2,
					Constants.fruitWidth, Constants.fruitWidth,
					Constants.fruitWidth * 2, 1, 1, 180);
			break;
		case Constants.HORN_TOOL:
			batch.draw(key, x + time * speed, y - Constants.fruitHeight * 0.5f,
					Constants.fruitWidth, Constants.fruitHeight * 2);
			batch.draw(key, x - time * speed, y - Constants.fruitHeight * 0.5f,
					Constants.fruitWidth / 2, Constants.fruitWidth,
					Constants.fruitWidth, Constants.fruitWidth * 2, 1, 1, 180);
			break;
		case Constants.CROSS_TOOl:
			batch.draw(key, x - time * speed, y, Constants.fruitWidth,
					Constants.fruitWidth);
			batch.draw(key, x + time * speed, y, Constants.fruitWidth * 0.75f,
					Constants.fruitWidth / 2, Constants.fruitWidth * 1.5f,
					Constants.fruitWidth, 1, 1, 180);
			batch.draw(key, x, y + time * speed, Constants.fruitWidth * 0.75f,
					Constants.fruitWidth / 2, Constants.fruitWidth * 1.5f,
					Constants.fruitWidth, 1, 1, 90);
			batch.draw(key, x, y - time * speed, Constants.fruitWidth * 0.75f,
					Constants.fruitWidth / 2, Constants.fruitWidth * 1.5f,
					Constants.fruitWidth, 1, 1, -90);
			break;
		case Constants.TIME_TOOL:
			batch.draw(key, x + addX * time, y + addY * time,
					Constants.fruitWidth / 2, Constants.fruitHeight / 2);
			break;

		case Constants.STAR_TOOL:
			batch.draw(key, x + addX * time, y + addY * time,
					Constants.fruitWidth / 2, Constants.fruitHeight / 2);
			break;
		case Constants.BOMB_TOOL:
			key = anim[type].getKeyFrame(duration, false);
			batch.draw(key, x - Constants.fruitWidth,
					y - Constants.fruitHeight, Constants.fruitWidth * 3,
					Constants.fruitHeight * 3);
			break;
		}
	}

}
