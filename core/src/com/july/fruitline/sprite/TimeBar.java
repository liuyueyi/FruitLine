package com.july.fruitline.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.july.fruitline.manager.MusicManager;
import com.july.fruitline.util.Assets;
import com.july.fruitline.util.Constants;

public class TimeBar {
	TextureRegion bg;
	TextureRegion fill;
	public Image pause;

	float total;
	float leftTime;
	boolean paused;
	boolean over;

	public TimeBar(int level) {
		bg = Assets.instance.game.findRegion("time_bg");
		fill = Assets.instance.game.findRegion("time");
		pause = new Image(Assets.instance.game.findRegion("pause"));
		pause.setBounds(Constants.pauseX, Constants.pauseY,
				Constants.pauseWidth, Constants.pauseHeight);
		init(level);
	}

	/**
	 * init time bar by the current level, the time can be calculated by the
	 * level
	 * 
	 * @param level
	 *            current level
	 */
	public void init(int level) {
		total = 420 + level;
		leftTime = total;
		paused = false;
		over = false;
	}

	public void run() {
		paused = false;
	}

	public void stop() {
		paused = true;
	}

	public void addTime() {
		if (leftTime < total)
			leftTime += 1f;
	}

	public void addTime(int value) {
		leftTime += value;
		if (leftTime > total)
			leftTime = total;
	}

	public boolean isOver() {
		return over;
	}

	boolean playTimeWarnning;
	float timeCount;

	public void draw(Batch batch) {
		batch.draw(bg, Constants.timeX, Constants.timeY, Constants.timeWidth,
				Constants.timeHeight);

		if (!paused && !over) {
			if (!playTimeWarnning && leftTime <= 15) {
				playTimeWarnning = true;
				timeCount = 0;
			}

			if (playTimeWarnning && leftTime > 15) {
				playTimeWarnning = false;
				timeCount = 0;
			}

			if (playTimeWarnning && (++timeCount) % 30 == 0) {
				MusicManager.manager.playSound(MusicManager.TIME_WARNING);
			}

			leftTime -= Gdx.graphics.getDeltaTime();
			if (leftTime <= 0) {
				over = true;
				return;
			}
		}
		batch.draw(fill, Constants.fillX, Constants.fillY, Constants.fillWidth
				* (leftTime / total), Constants.fillHeight);
	}
}
