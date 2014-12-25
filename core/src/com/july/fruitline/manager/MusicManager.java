package com.july.fruitline.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.july.fruitline.util.Assets;

public class MusicManager {
	public static MusicManager manager = new MusicManager();

	private Music bg;
	private Sound[] sound;

	public static final int BUTTON = 6;
	public static final int START = 7;
	public static final int TIME_WARNING = 8;
	public static final int GAMEOVER = 9;
	public static final int SUCCEED = 10;

	public static final int SELECT = 5;

	public static final int VER = 2;
	public static final int HRON = 3;
	public static final int BOMB = 1;
	public static final int CROSS = 4;
	public static final int TIME = 0;

	public static final int GREAT = 14;
	public static final int STAR = 13;
	public static final int FRUIT = 11;
	public static final int USENESS = 12;

	private MusicManager() {
	}

	public void loadMusic() {
		bg = Gdx.audio.newMusic(Gdx.files.internal("audio/background.mp3"));
		bg.setLooping(true);
		bg.setVolume(0.4f);
	}

	public void loadSound() {
		sound = new Sound[15];
		sound[BUTTON] = Gdx.audio.newSound(Gdx.files
				.internal("audio/button.ogg"));
		sound[START] = Gdx.audio.newSound(Gdx.files.internal("audio/go.ogg"));
		sound[TIME_WARNING] = Gdx.audio.newSound(Gdx.files
				.internal("audio/time_warning.ogg"));
		sound[GAMEOVER] = Gdx.audio.newSound(Gdx.files
				.internal("audio/fail.ogg"));
		sound[SUCCEED] = Gdx.audio.newSound(Gdx.files
				.internal("audio/success.ogg"));
		sound[SELECT] = sound[BUTTON];
		sound[VER] = Gdx.audio.newSound(Gdx.files.internal("audio/hv.ogg"));
		sound[HRON] = sound[VER];
		sound[BOMB] = Gdx.audio.newSound(Gdx.files.internal("audio/bomb.ogg"));
		sound[CROSS] = Gdx.audio
				.newSound(Gdx.files.internal("audio/cross.ogg"));
		sound[TIME] = Gdx.audio.newSound(Gdx.files
				.internal("audio/addtime.ogg"));
		sound[STAR] = Gdx.audio.newSound(Gdx.files.internal("audio/star.ogg"));
		sound[FRUIT] = Gdx.audio
				.newSound(Gdx.files.internal("audio/fruit.ogg"));
		sound[USENESS] = Gdx.audio.newSound(Gdx.files
				.internal("audio/useness.wav"));
		sound[GREAT] = Gdx.audio
				.newSound(Gdx.files.internal("audio/great.ogg"));
	}

	public void playSound(int type) {
		if (Assets.instance.isSound) {
			if (type == TIME_WARNING)
				sound[type].setVolume(sound[type].play(), 0.05f);
			else
				sound[type].setVolume(sound[type].play(), 0.3f);
		}
	}

	public void stopSound() {
		Assets.instance.isSound = false;
	}

	public void playMusic() {
		if (Assets.instance.isMusic) {
			bg.play();
		}
	}

	public void stopMusic() {
		Assets.instance.isMusic = false;
		bg.stop();
	}

	public void dispose() {
		if (bg != null)
			bg.dispose();

		if (sound != null) {
			for (Sound s : sound) {
				s.dispose();
			}
		}
	}
}
