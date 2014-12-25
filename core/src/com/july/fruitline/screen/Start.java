package com.july.fruitline.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.july.fruitline.MainGame;
import com.july.fruitline.manager.FruitEffectManager;
import com.july.fruitline.manager.MusicManager;
import com.july.fruitline.manager.ToolEffectManager;
import com.july.fruitline.util.Assets;
import com.july.fruitline.util.Constants;

public class Start extends ScreenAdapter {
	MainGame game;

	Stage stage;
	SpriteBatch batch;
	TextureAtlas atlas;

	TextureRegion bk;
	TextureRegion tip;
	float x, y, width, height;

	float w, h;
	int time;

	Label label;

	boolean stop;

	public Start(MainGame game) {
		this.game = game;

		stage = new Stage();
		batch = new SpriteBatch();

		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();

		atlas = new TextureAtlas(Gdx.files.internal("img/tip/tip.pack"));
		bk = atlas.findRegion("tipBg");
		int type = (int) (Math.random() * 5);
		width = w * 0.8f;
		x = 0.1f * w;
		if (type != 0) {
			height = width * 0.45f;
		} else
			height = width * 0.75f;
		y = (h - height) / 2;
		tip = atlas.findRegion("tip", type);
		time = 0;

		BitmapFont f = new BitmapFont();
		f.setScale(h / 480f);
		label = new Label("0%", new LabelStyle(new BitmapFont(), Color.BLACK));
		stage.addActor(label);
		label.setWidth(width);
		label.setPosition(width / 2, h * 0.2f);

		stop = false;
	}

	private void loadResource() {
		Assets.getInstance().init();
		FruitEffectManager.manager.init();
		ToolEffectManager.manager.init();
		MusicManager.manager.loadMusic();
		MusicManager.manager.loadSound();
		Assets.instance.load();
	}

	public void run() {
		stop = false;
		time = 99;
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.draw(bk, 0, 0, w, h);
		batch.draw(tip, x, y, width, height);
		batch.end();

		label.setText("loading: " + (time) + "%");
		if (!stop && ++time >= 100) {
			game.setScreen(new Menu(game));
			this.dispose();
		}

		stage.draw();

		if (time == 2)
			game.event.notify(this, Constants.KAIPING);

		if (time == 69) {
			loadResource();
		}
	}

	@Override
	public void dispose() {
		stage.dispose();
		batch.dispose();
		atlas.dispose();
	}
}
