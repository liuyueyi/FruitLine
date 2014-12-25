package com.july.fruitline.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Assets {
	public static Assets instance = new Assets();
	public BitmapFont font;
	public LabelStyle style;
	public BitmapFont num;
	public LabelStyle numStyle;
	public BitmapFont info;
	public LabelStyle infoStyle;

	public TextureAtlas menu;
	public TextureAtlas game;
	public TextureAtlas effect;
	public TextureAtlas pause;
	public TextureAtlas result;

	public TextureRegion[] bgs;
	public TextureRegion title;
	public TextureRegion[] help;
	public TextureRegion[][] btns;
	public TextureRegion[] fruit;
	public TextureRegion[] box;
	public TextureRegion[] tools;
	public TextureRegion[][] toolEffects;

	public TextureRegionDrawable[] menuBtnDrawable;

	public TextureRegion selected;
	public TextureRegion[] star;

	public int currentLevel;
	public int maxLevel;
	public Preferences set;
	public Preferences score;
	public boolean isSound;
	public boolean isMusic;

	private Assets() {
	}

	public static Assets getInstance() {
		if (instance == null) {
			instance = new Assets();
		}
		return instance;
	}

	public void init() {
		font = new BitmapFont(Gdx.files.internal("font/font.fnt"));
		font.setScale(Constants.wrate, Constants.hrate);
		style = new LabelStyle(font, Color.WHITE);

		num = new BitmapFont(Gdx.files.internal("font/tip.fnt"));
		num.setScale(Constants.wrate, Constants.hrate);
		numStyle = new LabelStyle(num, Color.WHITE);

		info = new BitmapFont(Gdx.files.internal("font/menu.fnt"));
		info.setScale(Constants.wrate, Constants.hrate);
		infoStyle = new LabelStyle(info, Color.WHITE);

		menu = new TextureAtlas(Gdx.files.internal("img/menu/menu.pack"));
		game = new TextureAtlas(Gdx.files.internal("img/game/game.pack"));
		effect = new TextureAtlas(Gdx.files.internal("img/effect/effect.pack"));
		pause = new TextureAtlas(Gdx.files.internal("img/pause/pause.pack"));
		result = new TextureAtlas(Gdx.files.internal("img/result/result.pack"));

		initBg();
		initHelp();
		initBtn();
		initFruit();
		initBox();
		initTools();
		initStar();
		initToolEffects();
	}

	private void initBg() {
		bgs = new TextureRegion[6];
		bgs[Constants.MENU_BG] = menu.findRegion("bg");
		bgs[Constants.GAME_BG] = game.findRegion("bg");
		bgs[Constants.FRUIT_BG] = game.findRegion("game_bg");
		bgs[Constants.PAUSE_BG] = pause.findRegion("bg");
		bgs[Constants.SUCCESS_BG] = result.findRegion("success_bg");
		bgs[Constants.FAILURE_BG] = result.findRegion("fail_bg");
		title = menu.findRegion("title");
	}

	private void initHelp() {
		help = new TextureRegion[4];
		help[0] = menu.findRegion("help", -1);
		for (int i = 0; i < 3; i++)
			help[i + 1] = game.findRegion("help", i);
	}

	private void initBtn() {
		btns = new TextureRegion[10][];
		btns[Constants.HELP_BTN] = new TextureRegion[2];
		btns[Constants.HELP_BTN][0] = menu.findRegion("help", 0);
		btns[Constants.HELP_BTN][1] = menu.findRegion("help", 1);

		btns[Constants.MORE_BTN] = new TextureRegion[2];
		btns[Constants.MORE_BTN][0] = menu.findRegion("more", 0);
		btns[Constants.MORE_BTN][1] = menu.findRegion("more", 1);

		btns[Constants.PLAY_BTN] = new TextureRegion[1];
		btns[Constants.PLAY_BTN][0] = menu.findRegion("play");

		// pause btns
		btns[Constants.RETRY_BTN] = new TextureRegion[2];
		btns[Constants.RETRY_BTN][0] = pause.findRegion("new", 0);
		btns[Constants.RETRY_BTN][1] = pause.findRegion("new", 1);

		btns[Constants.MENU_BTN] = new TextureRegion[2];
		btns[Constants.MENU_BTN][0] = pause.findRegion("return", 0);
		btns[Constants.MENU_BTN][1] = pause.findRegion("return", 1);

		btns[Constants.LEFT_BTN] = new TextureRegion[2];
		btns[Constants.LEFT_BTN][0] = pause.findRegion("left", 0);
		btns[Constants.LEFT_BTN][1] = pause.findRegion("left", 1);

		btns[Constants.RIGHT_BTN] = new TextureRegion[2];
		btns[Constants.RIGHT_BTN][0] = pause.findRegion("right", 0);
		btns[Constants.RIGHT_BTN][1] = pause.findRegion("right", 1);

		btns[Constants.CONTINUE_BTN] = new TextureRegion[3];
		btns[Constants.CONTINUE_BTN][0] = pause.findRegion("continue", 0);
		btns[Constants.CONTINUE_BTN][1] = pause.findRegion("continue", 1);
		btns[Constants.CONTINUE_BTN][2] = pause.findRegion("continue", 2);

		btns[Constants.RUN_BTN] = new TextureRegion[2];
		btns[Constants.RUN_BTN][0] = result.findRegion("play", 0);
		btns[Constants.RUN_BTN][1] = result.findRegion("play", 1);

		btns[Constants.BACK_BTN] = new TextureRegion[2];
		btns[Constants.BACK_BTN][0] = result.findRegion("return", 0);
		btns[Constants.BACK_BTN][1] = result.findRegion("return", 1);

		menuBtnDrawable = new TextureRegionDrawable[4];
		menuBtnDrawable[0] = new TextureRegionDrawable(menu.findRegion("sound",
				0));
		menuBtnDrawable[1] = new TextureRegionDrawable(menu.findRegion("sound",
				1));
		menuBtnDrawable[2] = new TextureRegionDrawable(menu.findRegion("music",
				0));
		menuBtnDrawable[3] = new TextureRegionDrawable(menu.findRegion("music",
				1));

	}

	private void initFruit() {
		fruit = new TextureRegion[12];
		for (int i = 0; i < 7; i++)
			fruit[i] = game.findRegion("f", i);

		fruit[Constants.STAR] = result.findRegion("star", 0);
		fruit[Constants.GOAL_1] = game.findRegion("goal", 0);
		fruit[Constants.GOAL_2] = game.findRegion("goal", 1);
		fruit[Constants.GOAL_3] = game.findRegion("goal", 2);
		fruit[Constants.WOOD] = game.findRegion("wood");

		selected = game.findRegion("selected");
	}

	private void initBox() {
		box = new TextureRegion[4];
		for (int i = 0; i < 3; i++)
			box[i] = game.findRegion("kuang", i);
		box[3] = game.findRegion("wood");
	}

	private void initTools() {
		tools = new TextureRegion[6];
		tools[Constants.TIME_TOOL] = effect.findRegion("tag", 0);
		tools[Constants.BOMB_TOOL] = effect.findRegion("tag", 1);
		tools[Constants.HORN_TOOL] = effect.findRegion("tag", 2);
		tools[Constants.STAR_TOOL] = effect.findRegion("tag", 3);
		tools[Constants.VER_TOOL] = effect.findRegion("tag", 4);
		tools[Constants.CROSS_TOOl] = effect.findRegion("tag", 5);
	}

	private void initToolEffects() {
		toolEffects = new TextureRegion[6][];
		toolEffects[Constants.CROSS_TOOl] = new TextureRegion[6];
		for (int i = 0; i < 6; i++)
			toolEffects[Constants.CROSS_TOOl][i] = effect.findRegion("c", i);

		toolEffects[Constants.HORN_TOOL] = new TextureRegion[7];
		for (int i = 0; i < 7; i++)
			toolEffects[Constants.HORN_TOOL][i] = effect.findRegion("hron", i);

		toolEffects[Constants.VER_TOOL] = new TextureRegion[4];
		for (int i = 0; i < 4; i++)
			toolEffects[Constants.VER_TOOL][i] = effect.findRegion("v", i);

		toolEffects[Constants.TIME_TOOL] = new TextureRegion[1];
		toolEffects[Constants.TIME_TOOL][0] = tools[Constants.TIME_TOOL];

		toolEffects[Constants.STAR_TOOL] = new TextureRegion[1];
		toolEffects[Constants.STAR_TOOL][0] = tools[Constants.STAR_TOOL];

		toolEffects[Constants.BOMB_TOOL] = new TextureRegion[4];
		for (int i = 0; i < 4; i++)
			toolEffects[Constants.BOMB_TOOL][i] = effect.findRegion("bomb", i);
	}

	private void initStar() {
		star = new TextureRegion[2];
		star[0] = result.findRegion("star", 0);
		star[1] = result.findRegion("star", 1);
	}

	private final String KEY = "wu[]09-=,.uibnqw";

	private int decrypt(String cipher) {
		try {
			if (cipher.equals("ERROR"))
				return 1;
			String tmp = AESCrypt.decrypt(cipher, KEY);
			if (tmp.equals("ERROR")) {
				return 1;
			}
			return Integer.parseInt(tmp);
		} catch (Exception e) {
			return 1;
		}
	}

	private String encrypt(int text) {
		try {
			return AESCrypt.encrypt("" + text, KEY);
		} catch (Exception e) {
			return "ERROR";
		}
	}

	public void load() {
		set = Gdx.app.getPreferences("set");
		isMusic = set.getBoolean("music", true);
		isSound = set.getBoolean("sound", true);

		currentLevel = decrypt(set.getString("c_xxl", "ERROR"));
		maxLevel = decrypt(set.getString("c_mxl", "ERROR")) - 3;
		score = Gdx.app.getPreferences("score");
	}

	public void save() {
		set.putString("c_xxl", encrypt(currentLevel)).flush();
		set.putString("c_mxl", encrypt(maxLevel + 3)).flush();
		set.putBoolean("music", isMusic).flush();
		set.putBoolean("sound", isSound).flush();
		System.out.println("save sets!");
	}

	public void saveScore(int scoreValue, int starNum) {
		String[] temp = score.getString("" + currentLevel, "0,0").split(",");
		int bestScore = Integer.parseInt(temp[0]);
		int bestNum = Integer.parseInt(temp[1]);
		if (bestScore < scoreValue)
			bestScore = scoreValue;
		if (bestNum < starNum)
			bestNum = starNum;

		score.putString("" + currentLevel, bestScore + "," + bestNum).flush();
	}

	public String getScore(int level) {
		return score.getString("" + level, "0,0");
	}

	public void dispose() {
		save();

		menu.dispose();
		game.dispose();
		effect.dispose();
		pause.dispose();
		result.dispose();

		instance = null;
	}
}
