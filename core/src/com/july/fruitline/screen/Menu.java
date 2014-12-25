package com.july.fruitline.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.july.fruitline.MainGame;
import com.july.fruitline.manager.MusicManager;
import com.july.fruitline.sprite.Background;
import com.july.fruitline.sprite.MenuBtn;
import com.july.fruitline.util.Assets;
import com.july.fruitline.util.Constants;

public class Menu extends MyScreen {
	public MainGame game;
	SpriteBatch batch;
	Stage stage;
	InputMultiplexer mul;

	Background back;
	MenuBtn btn;

	Image title;

	@Override
	public void setInputProcessor() {
		// TODO Auto-generated method stub
		Gdx.input.setInputProcessor(mul);
	}

	float x, y;
	float width, height;
	float addY;
	int time;

	public Menu(MainGame game) {
		this.game = game;
		stage = new Stage();
		batch = new SpriteBatch();
		mul = new InputMultiplexer();
		mul.addProcessor(this);
		mul.addProcessor(stage);
		Gdx.input.setInputProcessor(mul);

		back = new Background(Constants.MENU_BG);

		x = Constants.width * 0.05f;
		y = Constants.playY + Constants.playHeight * 1.2f;
		width = Constants.width * 0.9f;
		height = 236f / 413f * 0.8f * Constants.width;
		title = new Image(Assets.instance.title);
		title.setBounds(x, Constants.height, width, height);
		stage.addActor(title);

		btn = new MenuBtn(this);
		stage.addActor(btn);

		Gdx.input.setCatchBackKey(true);
		MusicManager.manager.playMusic();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(mul);
		Action a1 = Actions.moveTo(x, y - 25, Gdx.graphics.getDeltaTime() * 15);
		Action a2 = Actions.moveTo(x, y + 12, Gdx.graphics.getDeltaTime() * 8);
		Action a3 = Actions.moveTo(x, y - 6, Gdx.graphics.getDeltaTime() * 4);
		Action a4 = Actions.moveTo(x, y, Gdx.graphics.getDeltaTime() * 4);
		title.addAction(Actions.sequence(a1, a2, a3, a4));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		back.draw(batch);
		batch.end();

		stage.draw();
		stage.act();
	}

	@Override
	public void dispose() {
		batch.dispose();
		stage.dispose();
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		MusicManager.manager.playSound(MusicManager.BUTTON);
		if (keycode == Keys.BACK || keycode == Keys.A) {
			if (btn.showHelp) {
				btn.showHelp = false;
				btn.helpImage.remove();
				return true;
			}

			game.event.notify(this, Constants.EXIT);
			return true;
		}
		return false;
	}
}
