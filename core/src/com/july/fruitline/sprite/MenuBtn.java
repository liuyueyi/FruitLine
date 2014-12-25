package com.july.fruitline.sprite;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.july.fruitline.manager.MusicManager;
import com.july.fruitline.screen.Game;
import com.july.fruitline.screen.Menu;
import com.july.fruitline.util.Assets;
import com.july.fruitline.util.Constants;

public class MenuBtn extends Group {
	Menu menu;
	Image play;
	MenuToolBtn help;
	Image music;
	Image sound;
	public Image helpImage;
	MenuToolBtn more;

	public boolean showHelp;

	private class MenuToolBtn extends Button {
		MenuToolBtn(ButtonStyle style) {
			super(style);
			setSize(Constants.menuWidth, Constants.menuHeight);
			setY(Constants.menuY);
			addListener(listener);
			MenuBtn.this.addActor(this);
		}
	}

	private EventListener listener = new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			MusicManager.manager.playSound(MusicManager.BUTTON);

			if (event.getListenerActor() == play) {
				menu.dispose();
				menu.game.setScreen(new Game(menu.game));
			} else if (event.getListenerActor() == help) {
				showHelp = true;
				addActor(helpImage);
			} else if (event.getListenerActor() == sound) {
				if (Assets.instance.isSound) {
					MusicManager.manager.stopSound();
					sound.setDrawable(Assets.instance.menuBtnDrawable[1]);
				} else {
					Assets.instance.isSound = true;
					sound.setDrawable(Assets.instance.menuBtnDrawable[0]);
				}

			} else if (event.getListenerActor() == music) {
				if (Assets.instance.isMusic) {
					MusicManager.manager.stopMusic();
					music.setDrawable(Assets.instance.menuBtnDrawable[3]);
				} else {
					Assets.instance.isMusic = true;
					MusicManager.manager.playMusic();
					music.setDrawable(Assets.instance.menuBtnDrawable[2]);
				}
			} else if (event.getListenerActor() == helpImage) {
				showHelp = false;
				helpImage.remove();
			} else {
				// more button click, 显示积分墙
				menu.game.event.notify(this, Constants.ADWALL);
			}
		}
	};

	public MenuBtn(Menu menu) {
		showHelp = false;
		this.menu = menu;
		help = new MenuToolBtn(new ButtonStyle(new TextureRegionDrawable(
				Assets.instance.btns[Constants.HELP_BTN][0]),
				new TextureRegionDrawable(
						Assets.instance.btns[Constants.HELP_BTN][1]), null));
		help.setX(Constants.menuX);

		helpImage = new Image(Assets.instance.help[0]);
		helpImage.setBounds(0, 0, Constants.width, Constants.height);
		helpImage.addListener(listener);

		if (Assets.instance.isMusic) {
			music = new Image(Assets.instance.menuBtnDrawable[2]);
		} else {
			music = new Image(Assets.instance.menuBtnDrawable[3]);
		}
		music.setBounds(Constants.menuX + Constants.menuAddX, Constants.menuY,
				Constants.menuWidth, Constants.menuHeight);
		music.addListener(listener);
		addActor(music);
		// music.setX(Constants.menuX + Constants.menuAddX);

		if (Assets.instance.isSound) {
			sound = new Image(Assets.instance.menuBtnDrawable[0]);
		} else
			sound = new Image(Assets.instance.menuBtnDrawable[1]);
		sound.setBounds(music.getX() + Constants.menuAddX, Constants.menuY,
				Constants.menuWidth, Constants.menuHeight);
		sound.addListener(listener);
		addActor(sound);

		more = new MenuToolBtn(new ButtonStyle(new TextureRegionDrawable(
				Assets.instance.btns[Constants.MORE_BTN][0]),
				new TextureRegionDrawable(
						Assets.instance.btns[Constants.MORE_BTN][1]), null));
		more.setX(sound.getX() + Constants.menuAddX);

		play = new Image(Assets.instance.btns[Constants.PLAY_BTN][0]);
		play.setBounds(Constants.playX, Constants.playY, Constants.playWidth,
				Constants.playHeight);
		play.addListener(listener);
		play.setOrigin(play.getWidth() / 2, play.getHeight() / 2);
		Action scal1 = Actions.scaleTo(1.01f, 1.01f, 0.1f);
		Action scal2 = Actions.scaleTo(0.97f, 0.97f, 0.1f);
		play.addAction(Actions.forever(Actions.sequence(scal1, scal2)));
		addActor(play);
	}
}
