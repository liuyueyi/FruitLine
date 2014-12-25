package com.july.fruitline.sprite;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.july.fruitline.util.Assets;
import com.july.fruitline.util.Constants;

public class Score {
	Label level;
	Label score;
	int levelValue;
	int scoreValue;

	private class MyLabel extends Label {
		MyLabel(String text) {
			super(text, Assets.instance.style);
			// setAlignment(Align.center);
			setSize(Constants.labelWidth, Constants.labelHeight);
			setY(Constants.labelY);
		}
	}

	public Score(int currentLevel, Stage stage) {
		levelValue = currentLevel;
		scoreValue = 0;
		starNum = 0;
		level = new MyLabel("关卡:" + levelValue);
		level.setX(Constants.timeX);
		score = new MyLabel("分数:" + scoreValue);
		score.setX(level.getWidth() + level.getX());
		stage.addActor(level);
		stage.addActor(score);
	}

	/**
	 * 表示关卡不变，重置分数
	 */
	public void init() {
		count = 0;
		time = 0;
		starNum = 0;
		scoreValue = 0;
		score.setText("分数:" + scoreValue);
	}

	public int getLevel() {
		return levelValue;
	}

	public void updateLevel() {
		levelValue = Assets.instance.currentLevel;
		level.setText("关卡:" + levelValue);
	}

	public void addScore() {
		scoreValue += 10;
		score.setText("分数:" + scoreValue);
	}

	int count;
	int time;

	/**
	 * 更新分数，主要是消除水果后增添分数事调用，通过消除水果数目来动态添加分数
	 * 
	 * @param count
	 */
	public void updateScore(int count) {
		this.count += count;
		time = 0;
	}

	/**
	 * 在GameScreen的render方法中调用,用于显示动态增加分数的效果
	 */
	private void updateScore() {
		if (count > 0) {
			if ((++time) % 5 == 0) {
				addScore();
				count--;
			}
		}
	}

	public int getScore() {
		if (count > 0)
			return scoreValue + 10 * count;
		return scoreValue;
	}

	int starNum; // star number

	/**
	 * 添加星星数目，当收集到一个星星时调用
	 */
	public void addStarNum() {
		if (starNum < 3)
			starNum++;
	}

	public void drawStarAndScore(Batch batch) {
		updateScore(); // 动态显示分数
		batch.draw(Assets.instance.star[1], Constants.scoreStarX, level.getY(),
				Constants.scoreStarSize, Constants.scoreStarSize);
		batch.draw(Assets.instance.star[1], Constants.scoreStarX
				- Constants.scoreStarSize, level.getY(),
				Constants.scoreStarSize, Constants.scoreStarSize);
		batch.draw(Assets.instance.star[1], Constants.scoreStarX - 2
				* Constants.scoreStarSize, level.getY(),
				Constants.scoreStarSize, Constants.scoreStarSize);

		switch (starNum) {
		case 3:
			batch.draw(Assets.instance.star[0], Constants.scoreStarX,
					level.getY(), Constants.scoreStarSize,
					Constants.scoreStarSize);
		case 2:
			batch.draw(Assets.instance.star[0], Constants.scoreStarX
					- Constants.scoreStarSize, level.getY(),
					Constants.scoreStarSize, Constants.scoreStarSize);
		case 1:
			batch.draw(Assets.instance.star[0], Constants.scoreStarX - 2
					* Constants.scoreStarSize, level.getY(),
					Constants.scoreStarSize, Constants.scoreStarSize);
			break;
		}
	}
}
