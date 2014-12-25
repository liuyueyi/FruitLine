package com.july.fruitline.sprite;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.july.fruitline.util.Assets;
import com.july.fruitline.util.Constants;

public class Background extends GameObject {
	int type;

	public Background(float x, float y, float width, float height, int type) {
		init(x, y, width, height, type);
		// TODO Auto-generated constructor stub
	}

	public Background(int type) {
		switch (type) {
		case Constants.MENU_BG:
		case Constants.GAME_BG:
			init(0, 0, Constants.width, Constants.height, type);
			break;
		case Constants.SUCCESS_BG:
		case Constants.FAILURE_BG:
			init(Constants.resultX, Constants.resultY, Constants.resultWidth,
					Constants.resultHeight, type);
			break;
		case Constants.PAUSE_BG:
			init(Constants.pauseBgX, Constants.pauseBgY,
					Constants.pauseBgWidth, Constants.pauseBgHeight, type);
			break;
		case Constants.FRUIT_BG:

			init(Constants.fruitBgX, Constants.fruitBgY,
					Constants.fruitBgWidth, Constants.fruitBgHeight, type);
			break;
		}
	}

	private void init(float x, float y, float width, float height, int type) {
		bounds = new Rectangle(x, y, width, height);
		this.type = type;
	}

	public void setType(int type) {
		if (this.type == Constants.SUCCESS_BG
				&& type == Constants.FAILURE_BG
				|| (this.type == Constants.FAILURE_BG && type == Constants.SUCCESS_BG))
			this.type = type;
	}

	public void draw(Batch batch) {
		batch.draw(Assets.instance.bgs[type], bounds.x, bounds.y, bounds.width,
				bounds.height);
	}
}
