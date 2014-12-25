package com.july.fruitline.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.july.fruitline.manager.MusicManager;
import com.july.fruitline.sprite.Background;
import com.july.fruitline.util.Assets;
import com.july.fruitline.util.Constants;

public class Result extends MyDialog {
	// public static Result result = new Result();

	boolean win;
	MyButton menu;
	MyButton retry;

	Label score;

	public Result() {
		stage = new Stage();
		initListener();
		inputMult = new InputMultiplexer();

		menu = new MyButton(Assets.instance.btns[Constants.BACK_BTN]);
		menu.setBounds(Constants.resultBtnX, Constants.resultBtnY,
				Constants.resultBtnWidth, Constants.resultBtnHeight);

		retry = new MyButton(Assets.instance.btns[Constants.RUN_BTN]);
		retry.setBounds(Constants.resultBtnX + Constants.resultWidth / 2,
				Constants.resultBtnY, Constants.resultBtnWidth,
				Constants.resultBtnHeight);

		back = new Background(Constants.SUCCESS_BG);

		score = new Label("", Assets.instance.numStyle);

		score.setPosition(0, Constants.starY + Constants.starHeight * 1.3f);
		score.setWidth(Constants.width);
		score.setAlignment(Align.center);
		stage.addActor(score);
	}

	@Override
	protected void initListener() {
		listener = new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				MusicManager.manager.playSound(MusicManager.BUTTON);
				if (event.getListenerActor() == menu) {
					game.game.setScreen(new Menu(game.game));
					game.dispose();
				} else if (event.getListenerActor() == retry) {
					if (win) // game win, and continue play
						game.upgrade();
					else
						// game lose, and retry this level
						game.init();
				}
			}
		};
	}

	int time = 10;
	float addY = (Constants.height - Constants.resultY) / time;
	int scoreValue;
	int scoreStart;
	int baseAddScore;

	/**
	 * 
	 * @param game
	 * @param win
	 * @param num
	 *            star number
	 */
	public void show(Game game, Boolean win, int num, int scoreValue) {
		time = 10;

		this.game = game;
		this.win = win;
		inputMult.clear();
		inputMult.addProcessor(stage);
		inputMult.addProcessor(game);
		Gdx.input.setInputProcessor(inputMult);

		if (win) {
			// 统计胜利关卡信息
			game.game.event.notify(this, Constants.FINISH_LEVEL);
			if (Assets.getInstance().maxLevel >= 50
					&& Assets.getInstance().currentLevel % 3 == 0)
				game.game.event.notify(this, Constants.CHAPING);

			MusicManager.manager.playSound(MusicManager.SUCCEED);
			this.num = num;
			rotation[0] = 0;
			rotation[1] = 0;
			rotation[2] = 0;

			this.scoreValue = scoreValue;
			baseAddScore = scoreValue / 80;
			if (baseAddScore == 0)
				baseAddScore = 2;

			scoreStart = scoreValue % baseAddScore;
			score.setText("" + scoreStart);
			back.setType(Constants.SUCCESS_BG);

			Assets.instance.saveScore(scoreValue, num); // 保存结果

			// 胜利后，更新currentLevel
			Assets.instance.currentLevel++;
			if (Assets.instance.maxLevel < Assets.instance.currentLevel)
				Assets.instance.maxLevel = Assets.instance.currentLevel;
		} else {
			// 统计失败关卡信息
			game.game.event.notify(this, Constants.FAIL_LEVEL);
			MusicManager.manager.playSound(MusicManager.GAMEOVER);
			score.setText("");
			back.setType(Constants.FAILURE_BG);
		}
	}

	int rotation[] = new int[3];
	int num;

	public void draw(Batch batch) {
		if (time > 0) {
			time--;
			back.bounds.y = Constants.resultY + addY * time;
			menu.setY(Constants.resultBtnY + addY * time);
			retry.setY(menu.getY());
			score.setY(Constants.starY + Constants.starHeight * 1.3f + addY
					* time);
		}

		batch.begin();
		back.draw(batch);
		batch.end();

		stage.draw();
		stage.act();

		batch.begin();
		if (win) {
			if (scoreStart < scoreValue) {
				scoreStart += baseAddScore;
				score.setText(scoreStart + "");
			}
			drawStar(batch);
		}
		batch.end();
	}

	public void drawStar(Batch batch) {
		// 绘制星星背景
		batch.draw(Assets.instance.star[1], Constants.starX, Constants.starY
				+ time * addY, Constants.starWidth, Constants.starHeight);
		batch.draw(Assets.instance.star[1], Constants.starX
				+ Constants.starWidth * 1.5f, Constants.starY + time * addY,
				Constants.starWidth, Constants.starHeight);
		batch.draw(Assets.instance.star[1], Constants.starX
				+ Constants.starWidth * 3, Constants.starY + time * addY,
				Constants.starWidth, Constants.starHeight);
		if (time > 0)
			return;
		switch (num) {
		case 3:
			if (rotation[1] == 360)
				drawStar(batch, 3);
		case 2:
			if (rotation[0] == 360)
				drawStar(batch, 2);
		case 1:
			drawStar(batch, 1);
			break;
		}
	}

	float temp;

	private void drawStar(Batch batch, int index) {
		--index;
		if (rotation[index] < 360)
			rotation[index] += 10;
		temp = rotation[index] / 360f;
		batch.draw(Assets.instance.star[0], Constants.starX
				+ Constants.starWidth * 1.5f * index, Constants.starY
				+ (1 - temp) * Constants.starHeight, Constants.starWidth / 2,
				Constants.starHeight / 2, Constants.starWidth,
				Constants.starHeight, 2 - temp, 2 - temp, rotation[index]);
	}
}
