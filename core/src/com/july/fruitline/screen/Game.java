package com.july.fruitline.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.july.fruitline.MainGame;
import com.july.fruitline.manager.FruitFactory;
import com.july.fruitline.manager.MusicManager;
import com.july.fruitline.sprite.Background;
import com.july.fruitline.sprite.Score;
import com.july.fruitline.sprite.TimeBar;
import com.july.fruitline.sprite.Tip;
import com.july.fruitline.util.Assets;
import com.july.fruitline.util.Constants;

public class Game extends GestureScreen {
	public MainGame game;
	public int gameState;
	Background back;
	Background fruitBack;

	Image help;

	TimeBar time;
	Score score;

	Pause pause;
	Result result;
	FruitFactory factory;

	public Game(MainGame game) {
		this.game = game;
		stage = new Stage();
		batch = new SpriteBatch();
		inputMult = new InputMultiplexer();
		inputMult.addProcessor(stage);
		inputMult.addProcessor(new GestureDetector(this));
		inputMult.addProcessor(this);
		Gdx.input.setInputProcessor(inputMult);

		back = new Background(Constants.GAME_BG);
		fruitBack = new Background(Constants.FRUIT_BG);
		// FruitFactory.factory.init(Assets.instance.currentLevel, this);
		factory = new FruitFactory();
		factory.init(Assets.instance.currentLevel, this);

		score = new Score(Assets.instance.currentLevel, stage);
		time = new TimeBar(score.getLevel());
		stage.addActor(time.pause);
		time.pause.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showPause();
			}
		});
		Gdx.input.setCatchBackKey(true);

		initHelp(Constants.REMOVE_HELP);
	}

	boolean showHelp;

	public void initHelp(int type) {
		if (Assets.instance.currentLevel == 1) {
			if (help == null) {
				help = new Image(Assets.instance.help[type]);
				help.setWidth(Constants.fruitBgWidth);
				help.setHeight(help.getWidth() * 0.267f);
			}
			help.setDrawable(new TextureRegionDrawable(
					Assets.instance.help[type]));
			System.out.println("t" + type);
			help.setPosition(Constants.width, Constants.fruitBgY + 7
					* Constants.fruitHeight - help.getHeight());
			stage.addActor(help);

			Action a1 = Actions.moveTo(Constants.fruitBgX, help.getY(),
					Gdx.graphics.getDeltaTime() * 20);
			showHelp = true;
			help.addAction(a1);
		}
	}

	/**
	 * retry this level and initialize all the data
	 */
	public void init() {
		score.init();
		time.init(score.getLevel());
		// FruitFactory.factory.freeAll();
		// FruitFactory.factory.init(score.getLevel(), this);
		factory.freeAll();
		factory.init(score.getLevel(), this);
		setInputProcessor();
	}

	/**
	 * update current level and initialize all the data 当升级和重新选择关卡时，调用
	 */
	public void upgrade() {
		score.updateLevel();
		init();
	}

	public void showHelp(int type) {
		initHelp(type);
	}

	public void hideHelp() {
		showHelp = false;
		help.addAction(Actions.moveTo(-help.getWidth() - 10, help.getY(),
				Gdx.graphics.getDeltaTime() * 20));
	}

	/**
	 * 消除水果后，添加分数
	 * 
	 * @param count
	 */
	public void addScore(int count) {
		if (score != null) {
			stage.addActor(new Tip(count));
			score.updateScore(count);
		}
	}

	/**
	 * 消除有时间道具的水果后，增加时间
	 */
	public void addTime() {
		if (time != null)
			time.addTime();
	}

	Action a1, a2, a3;
	Action t1, t2, t3, t4;

	/**
	 * 消除星星后，奖励大量时间
	 */
	public void addTime(int value) {
		time.addTime(10 * value);
	}

	/**
	 * 消除星星后调用
	 */
	public void addStar() {
		score.addStarNum();
	}

	public void gameOver(boolean win, int num) {
		time.stop();
		if (result == null)
			result = new Result();
		result.show(this, win, num, score.getScore());
		gameState = Constants.OVER;
	}

	public void showPause() {
		time.stop();
		if (pause == null) {
			pause = new Pause();
		}
		pause.show(Game.this, score.getLevel());
		gameState = Constants.PAUSE;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		back.draw(batch);
		time.draw(batch);
		fruitBack.draw(batch);
		// FruitFactory.factory.draw(batch);
		factory.draw(batch);
		// 绘制星星和动态增加分数
		score.drawStarAndScore(batch);
		batch.end();

		stage.draw();
		stage.act();

		if (gameState == Constants.PAUSE) {
			pause.draw(batch);
			// Result.result.draw(batch);
		} else if (gameState == Constants.RUN && time.isOver()) {
			gameOver(false, 0);
		} else if (gameState == Constants.OVER) {
			result.draw(batch);
		}

	}

	@Override
	public void pause() {
		Assets.instance.save();
		if (gameState == Constants.RUN) {
			time.stop();
			gameState = Constants.PAUSE;
			if (pause == null) {
				pause = new Pause();
			}
			pause.show(Game.this, score.getLevel());
		}
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		MusicManager.manager.playSound(MusicManager.BUTTON);
		if (keycode == Keys.BACK || keycode == Keys.A) {
			if (gameState == Constants.RUN)
				showPause();
			else if (gameState == Constants.PAUSE)
				setInputProcessor();
			else if (gameState == Constants.OVER) {
				this.dispose();
				game.setScreen(new Menu(game));
			}
			return true;
		}
		if (keycode == Keys.R) { // 测试显示游戏成功的对话框
			gameOver(true, 3);
			return true;
		}

		if (keycode == Keys.F) { // 测试显示游戏成功的对话框
			gameOver(false, 0);
			return true;
		}
		return false;
	}

	@Override
	public void dispose() {
		stage.dispose();
		batch.dispose();
		factory.freeAll();
	}

	@Override
	public void setInputProcessor() {
		// TODO Auto-generated method stub
		gameState = Constants.RUN;
		time.run();
		Gdx.input.setInputProcessor(inputMult);
	}

	private int getRow(float y) {
		return (int) ((Constants.height - y - Constants.fruitBgY) / Constants.fruitHeight);
	}

	private int getColumn(float x) {
		return (int) ((x - Constants.fruitBgX) / Constants.fruitWidth);
	}

	/**
	 * judge if tap the fruit cell
	 * 
	 * @param row
	 * @param column
	 * @return true do not tap the fruit; false means tap fruit
	 */
	private boolean notInGameBox(int row, int column) {
		// return (row < 0 || row >= FruitFactory.factory.ROW || column < 0 ||
		// column >= FruitFactory.factory.COLUMN);
		return (row < 0 || row >= factory.ROW || column < 0 || column >= factory.COLUMN);
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		int row = getRow(y);
		int column = getColumn(x);
		if (notInGameBox(row, column))
			return false;

		// FruitFactory.factory.click(row, column);
		if (!factory.click(row, column)) {
			MusicManager.manager.playSound(MusicManager.SELECT);
		}
		return true;
	}

	private float firstX = 0;
	private float firstY = 0;
	private boolean paned = false;

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		if (showHelp) {
			hideHelp();
			return true;
		}

		firstX = x;
		firstY = y;
		paned = true;
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		if (showHelp) {
			hideHelp();
			return true;
		}

		int firstColumn = getColumn(firstX);
		int firstRow = getRow(firstY);

		int secondColumn = getColumn(x);
		int secondRow = getRow(y);

		if (notInGameBox(firstRow, firstColumn)
				|| notInGameBox(secondRow, secondColumn))
			return false;

		if (paned
				&& ((Math.abs(firstRow - secondRow) == 1 && firstColumn == secondColumn) || (Math
						.abs(firstColumn - secondColumn) == 1 && firstRow == secondRow))) {
			paned = false;
			// FruitFactory.factory.paned(firstRow, firstColumn, secondRow,
			// secondColumn);
			if (!factory.paned(firstRow, firstColumn, secondRow, secondColumn)) {
				MusicManager.manager.playSound(MusicManager.SELECT);
			}
		}

		return true;
	}
}
