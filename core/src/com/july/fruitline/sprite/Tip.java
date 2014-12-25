package com.july.fruitline.sprite;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.july.fruitline.util.Assets;
import com.july.fruitline.util.Constants;

public class Tip extends Label {
	private static float w = Constants.fruitWidth * 3;
	private static float x = (Constants.width - w) / 2;
	private static float y = Constants.fruitBgY + Constants.fruitBgHeight / 2;
	private static float addX = (Constants.timeX + Constants.timeWidth * 0.4f
			- x - 20) / 20;
	private static float addY = (Constants.timeY + Constants.timeHeight - y - 50) / 20;
	private int time;

	public Tip(int count) {
		super("" + count * 10, Assets.instance.numStyle);

		setAlignment(Align.center);
		setWidth(w);
		setPosition(x, y);

		time = 0;
	}

	@Override
	public void act(float delta) {
		time++;
		if (time < 20) {
			setPosition(x + time, y + 2.5f * time);
		} else {
			setPosition(x + 20 + addX * (time - 20), y + 50 + addY
					* (time - 20));
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if (time >= 40) {
			this.remove();
		}
	}
}
