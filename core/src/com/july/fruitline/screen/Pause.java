package com.july.fruitline.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.july.fruitline.manager.MusicManager;
import com.july.fruitline.sprite.Background;
import com.july.fruitline.util.Assets;
import com.july.fruitline.util.Constants;

public class Pause extends MyDialog {
	// public static Pause pause = new Pause();
	// Game game;
	// Stage stage;
	// Background back;

	int level;
	TextureRegion bar;
	TextureRegion barFill;
	float fill;
	float dragX;
	Image drag;

	// Label info;
	Label scoreLabel;
	Label levelLabel;

	Button ctuBtn;
	ButtonStyle notStyle;
	ButtonStyle runStyle;
	Button retry;
	Button menu;
	Button left;
	Button right;
	float starY;

	public Pause() {
		inputMult = new InputMultiplexer();

		stage = new Stage();
		back = new Background(Constants.PAUSE_BG);
		initListener();

		TextureRegionDrawable[] ctnDraw = new TextureRegionDrawable[] {
				new TextureRegionDrawable(
						Assets.instance.btns[Constants.CONTINUE_BTN][0]),
				new TextureRegionDrawable(
						Assets.instance.btns[Constants.CONTINUE_BTN][1]),
				new TextureRegionDrawable(
						Assets.instance.btns[Constants.CONTINUE_BTN][2]) };
		notStyle = new ButtonStyle(ctnDraw[2], ctnDraw[2], null);
		runStyle = new ButtonStyle(ctnDraw[0], ctnDraw[1], null);

		ctuBtn = new MyButton(Assets.instance.btns[Constants.CONTINUE_BTN]);
		ctuBtn.setBounds(Constants.continueBtnX, Constants.continueBtnY,
				Constants.continueBtnWidth, Constants.continueBtnHeight);

		retry = new MyButton(Assets.instance.btns[Constants.RETRY_BTN]);
		retry.setBounds(Constants.retryBtnX, Constants.pauseBtnY,
				Constants.pauseBtnWidth, Constants.pauseBtnHeight);

		menu = new MyButton(Assets.instance.btns[Constants.MENU_BTN]);
		menu.setBounds(Constants.backBtnX, Constants.pauseBtnY,
				Constants.pauseBtnWidth, Constants.pauseBtnHeight);

		initDrag();
		initLabel();
		left = new MyButton(Assets.instance.btns[Constants.LEFT_BTN]);
		left.setBounds(Constants.barX - Constants.headWidth * 1.5f,
				Constants.pauseAddY, Constants.pauseAddSize,
				Constants.pauseAddSize);

		right = new MyButton(Assets.instance.btns[Constants.RIGHT_BTN]);
		right.setBounds(Constants.barX + Constants.barWidth,
				Constants.pauseAddY, Constants.pauseAddSize,
				Constants.pauseAddSize);
	}

	@Override
	protected void initListener() {
		listener = new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (event.getListenerActor() == ctuBtn) {
					if (level == Assets.instance.currentLevel) {
						MusicManager.manager.playSound(MusicManager.BUTTON);
						game.setInputProcessor();
					} else if (level <= Assets.instance.maxLevel) {
						MusicManager.manager.playSound(MusicManager.BUTTON);
						Assets.instance.currentLevel = level;
						if (Assets.instance.maxLevel < Assets.instance.currentLevel)
							Assets.instance.maxLevel = Assets.instance.currentLevel;
						game.upgrade();
					} else {
						MusicManager.manager.playSound(MusicManager.USENESS);
					}
				} else {
					MusicManager.manager.playSound(MusicManager.BUTTON);
					if (event.getListenerActor() == left) {
						setLevel(level - 1);
					} else if (event.getListenerActor() == right) {
						setLevel(level + 1);
					} else if (event.getListenerActor() == menu) {
						game.game.setScreen(new Menu(game.game));
						game.dispose();
					} else if (event.getListenerActor() == retry) {
						game.init();
					}
				}
			}
		};
	}

	private void initLabel() {
		levelLabel = new Label("关卡: ", Assets.instance.infoStyle);
		levelLabel.setWidth(Constants.barWidth * 0.8f);
		levelLabel.setX(Constants.barX);
		levelLabel.setAlignment(Align.center);
		levelLabel.setColor(Color.RED);
		stage.addActor(levelLabel);

		scoreLabel = new Label("分数: 0", Assets.instance.infoStyle);
		scoreLabel.setX(Constants.barX + Constants.fillWidth * 0.2f);
		// scoreLabel.setAlignment(Align.center);
		scoreLabel.setColor(Color.BLACK);
		stage.addActor(scoreLabel);

		starY = Constants.barY + Constants.headHeight;
	}

	int starNum = 0;

	private void setText() {
		String[] str = Assets.instance.getScore(level).split(",");
		if (str[0].equals("0")) {
			scoreLabel.setText("");
		} else
			scoreLabel.setText("最高分: " + str[0]);
		if (level == Assets.instance.currentLevel) {
			levelLabel.setColor(Color.RED);
			levelLabel.setText("当前关: " + level);
		} else {
			levelLabel.setColor(Color.BLUE);
			levelLabel.setText("选择关: " + level);
		}

		starNum = Integer.parseInt(str[1]);
	}

	private void setLevel(int level) {
		if (level <= 0 || level > 320)
			return;
		this.level = level;
		setText();
		fill = level / 320f;
		dragX = Constants.barX + fill
				* (Constants.barWidth - 0.8f * Constants.headWidth) - 0.1f
				* Constants.headWidth;
		drag.setX(dragX);

		if (level > Assets.instance.maxLevel) {
			ctuBtn.setStyle(notStyle);
		} else {
			ctuBtn.setStyle(runStyle);
		}
	}

	private void initDrag() {
		bar = Assets.instance.pause.findRegion("bar", 0);
		barFill = Assets.instance.pause.findRegion("bar", 1);

		drag = new Image(Assets.instance.pause.findRegion("bar", 3));
		drag.setY(Constants.headY);
		drag.setSize(Constants.headWidth, Constants.headHeight);
		stage.addActor(drag);
		drag.addListener(new DragListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return super.touchDown(event, x, y, pointer, button);
			}

			public void touchDragged(InputEvent event, float x, float y,
					int pointer) {
				dragX += x;
				if (dragX >= Constants.barX + Constants.barWidth - 0.9f
						* Constants.headWidth) {
					dragX = Constants.barX + Constants.barWidth - 0.9f
							* Constants.headWidth;
				} else if (dragX <= Constants.barX - 0.1f * Constants.headWidth) {
					dragX = Constants.barX - 0.1f * Constants.headWidth;
				}

				fill = (dragX - Constants.barX + 0.1f * Constants.headWidth)
						/ (Constants.barWidth - 0.8f * Constants.headWidth);
				level = (int) (fill * 320);
				setText();
				drag.setX(dragX);

				if (level > Assets.instance.maxLevel) {
					ctuBtn.setStyle(notStyle);
				} else {
					ctuBtn.setStyle(runStyle);
				}
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
			}
		});
	}

	// 用于设置窗口进入动画的参数
	int actionTime = 10;
	float addY = (Constants.height - Constants.pauseBgY) / actionTime;
	static int count = 0;

	public void show(Game game, int level) {
		this.game = game;

		if (Assets.getInstance().maxLevel >= 30 && (++count) > 20
				&& (count % 3 == 0)) {
			game.game.event.notify(this, Constants.CHAPING);
		}

		setLevel(level);
		inputMult.clear();
		inputMult.addProcessor(stage);
		inputMult.addProcessor(game);
		Gdx.input.setInputProcessor(inputMult);

		actionTime = 20;
	}

	public void draw(Batch batch) {
		if (actionTime > 0) {
			actionTime--;
			back.bounds.y = Constants.pauseBgY + actionTime * addY;
			ctuBtn.setY(Constants.continueBtnY + actionTime * addY);
			retry.setY(Constants.pauseBtnY + actionTime * addY);
			menu.setY(Constants.pauseBtnY + actionTime * addY);
			left.setY(Constants.pauseAddY + actionTime * addY);
			right.setY(Constants.pauseAddY + actionTime * addY);
			drag.setY(Constants.headY + actionTime * addY);
			levelLabel.setY(Constants.barY + Constants.headHeight + actionTime
					* addY);
			scoreLabel.setY(levelLabel.getY() + levelLabel.getHeight());
		}

		batch.begin();
		back.draw(batch);
		batch.draw(bar, Constants.barX, Constants.barY + actionTime * addY,
				Constants.barWidth, Constants.barHeight);
		batch.draw(barFill, Constants.barX, Constants.barY + actionTime * addY,
				Constants.barWidth * fill, Constants.barHeight);
		batch.end();

		stage.draw();
		stage.act();

		batch.begin();
		batch.draw(Assets.instance.star[1], right.getX(), starY + actionTime
				* addY, Constants.headWidth, Constants.headHeight);
		batch.draw(Assets.instance.star[1], right.getX() - Constants.headWidth,
				starY + actionTime * addY, Constants.headWidth,
				Constants.headHeight);
		batch.draw(Assets.instance.star[1], right.getX() - 2
				* Constants.headWidth, starY + actionTime * addY,
				Constants.headWidth, Constants.headHeight);

		switch (starNum) {
		case 3:
			batch.draw(Assets.instance.star[0], right.getX(), starY
					+ actionTime * addY, Constants.headWidth,
					Constants.headHeight);
		case 2:
			batch.draw(Assets.instance.star[0], right.getX()
					- Constants.headWidth, starY + actionTime * addY,
					Constants.headWidth, Constants.headHeight);
		case 1:
			batch.draw(Assets.instance.star[0], right.getX() - 2
					* Constants.headWidth, starY + actionTime * addY,
					Constants.headWidth, Constants.headHeight);
			break;
		}
		batch.end();
	}
}
