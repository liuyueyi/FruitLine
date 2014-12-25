package com.july.fruitline.screen;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.july.fruitline.sprite.Background;

public abstract class MyDialog {
	InputMultiplexer inputMult;
	Background back;
	Game game;
	Stage stage;
	EventListener listener;

	class MyButton extends Button {
		public MyButton(TextureRegion up, TextureRegion down) {
			super(new TextureRegionDrawable(up),
					new TextureRegionDrawable(down));
			addListener(listener);
			stage.addActor(this);
		}

		public MyButton(TextureRegion[] bk) {
			this(bk[0], bk[1]);
		}
	}

	protected abstract void initListener();

	public void dispose() {
		stage.dispose();
	}
}
