package com.july.fruitline.util;

import com.badlogic.gdx.Gdx;

public class Constants {
	public static float width = Gdx.graphics.getWidth();
	public static float height = Gdx.graphics.getHeight();
	public static float wrate = width / 480f;
	public static float hrate = height / 800f;

	// Game State
	public final static int RUN = 0;
	public final static int SUCCESS = 1;
	public final static int FAIL = 2;
	public final static int PAUSE = 3;
	public final static int OVER = 4;

	// back ground type
	public final static int MENU_BG = 0;
	public final static int GAME_BG = 1;
	public final static int SUCCESS_BG = 2;
	public final static int FAILURE_BG = 3;
	public final static int PAUSE_BG = 4;
	public final static int FRUIT_BG = 5;

	// button type
	public final static int HELP_BTN = 0;
	public final static int RUN_BTN = 1;
	public final static int BACK_BTN = 2;
	public final static int MORE_BTN = 3;
	public final static int PLAY_BTN = 4;

	public final static int CONTINUE_BTN = 5;
	public final static int MENU_BTN = 6;
	public final static int RETRY_BTN = 7;
	public final static int LEFT_BTN = 8;
	public final static int RIGHT_BTN = 9;

	public final static int MUSIC_DRW = 1;
	public final static int SOUND_DRW = 0;

	// special fruit
	public final static int STAR = 7;
	public final static int GOAL_1 = 8;
	public final static int GOAL_2 = 9;
	public final static int GOAL_3 = 10;
	public final static int WOOD = 11;
	public final static int ANY = 12;

	// menu button size
	public final static float playWidth = 182f * wrate;
	public final static float playHeight = 130f * hrate;
	public final static float playY = (height - playHeight) / 2;
	public final static float playX = (width - playWidth) / 2;

	public final static float menuWidth = 56f * wrate;
	public final static float menuHeight = menuWidth;
	public final static float menuY = playY - menuHeight * 2;
	public final static float menuX = (width - 5 * menuWidth) / 2;
	public final static float menuAddX = 1.33f * menuWidth;

	// game layout
	public static float fruitBgY = 0.1f * height;
	public static float fruitBgHeight = height * 0.67f;
	public static float fruitBgWidth = fruitBgHeight * 450 / 574f;
	public static float fruitBgX = (width - fruitBgWidth) / 2;

	public static float fruitWidth = fruitBgWidth * 0.955f / 7;
	public static float fruitHeight = fruitWidth;
	public static float fruitX = fruitBgX + 0.02f * fruitBgWidth;
	public static float fruitY = fruitBgY + 0.02f * fruitBgHeight;

	public final static float timeWidth = fruitBgWidth;
	public final static float timeHeight = timeWidth * 79f / 618f;
	public final static float timeX = fruitBgX;
	public final static float timeY = fruitBgY + fruitBgHeight + 0.1f
			* fruitHeight;

	public final static float fillHeight = timeHeight * 40f / 79f;
	public final static float fillWidth = timeWidth * 481f / 618f;
	public final static float fillX = timeX + timeWidth * 0.101f;
	public final static float fillY = timeY + timeHeight * 0.25f;

	public final static float pauseWidth = timeHeight * 0.67f;
	public final static float pauseHeight = pauseWidth;
	public final static float pauseX = fillX + fillWidth + 0.1f * pauseWidth;
	public final static float pauseY = timeY + pauseWidth * 0.17f;

	// 显示关卡和分数，星星的参数，主要用于Score类中
	public final static float labelWidth = 0.33f * timeWidth;
	public final static float labelHeight = timeHeight;
	public final static float labelY = timeY + timeHeight * 1.02f;
	public final static float scoreStarX = timeX + timeWidth - pauseWidth;
	public final static float scoreStarSize = pauseWidth;

	// public final static float starWidth = pauseWidth;
	public final static int duration = 20; // 动画时间
	public final static int wait = 25; // 等待时间

	// pause dialog layout
	public final static float pauseBgWidth = fruitBgWidth; // 0.9f * width;
	public final static float pauseBgHeight = pauseBgWidth;
	public final static float pauseBgX = fruitBgX;// 0.05f * width;
	public final static float pauseBgY = (height - pauseBgHeight) / 2;
	public final static float continueBtnWidth = pauseBgWidth * 180f / 440;
	public final static float continueBtnHeight = continueBtnWidth;
	public final static float continueBtnX = pauseBgX
			+ (pauseBgWidth - continueBtnWidth) / 2;
	public final static float continueBtnY = pauseBgY
			+ (pauseBgHeight - continueBtnHeight) / 3;
	public final static float pauseBtnWidth = continueBtnWidth * 128f / 180f;
	public final static float pauseBtnHeight = pauseBtnWidth * 46f / 126f;
	public final static float pauseBtnY = continueBtnY - pauseBtnHeight * 1.1f;
	public final static float backBtnX = pauseBgX + pauseBgWidth / 4f
			- pauseBtnWidth / 2f;
	public final static float retryBtnX = backBtnX + pauseBgWidth / 2f;

	public final static float barWidth = 0.7f * pauseBgWidth;
	public final static float barHeight = 20f * hrate;
	public final static float barX = pauseBgX + 0.15f * pauseBgWidth;
	public final static float barY = continueBtnY + continueBtnHeight * 1.1f;
	public final static float headWidth = 40f * hrate;
	public final static float headHeight = headWidth;
	public final static float headY = Constants.barY - 0.5f
			* (Constants.headHeight - Constants.barHeight);
	public final static float pauseAddSize = 1.5f * headWidth;
	public final static float pauseAddY = barY - pauseAddSize / 2;

	// result dialog
	public final static float resultWidth = 344 * wrate;
	public final static float resultHeight = resultWidth * 300f / 344f;
	public final static float resultX = (width - resultWidth) / 2f;
	public final static float resultY = (height - resultHeight) / 2f;
	public final static float resultBtnWidth = 128f * wrate;
	public final static float resultBtnHeight = 46f / 128f * resultBtnWidth;
	public final static float resultBtnY = resultBtnHeight * 0.5f + resultY;
	public final static float resultBtnX = resultX + resultWidth / 4f
			- resultBtnWidth / 2f;

	public final static float starHeight = resultBtnHeight * 1.3f;
	public final static float starWidth = 75f / 73f * starHeight;
	public final static float starY = resultBtnY + resultBtnHeight;
	public final static float starX = resultX + (resultWidth - 4 * starWidth)
			/ 2;

	// tool
	public final static int TIME_TOOL = 0;
	public final static int BOMB_TOOL = 1;
	public final static int VER_TOOL = 2;
	public final static int STAR_TOOL = 3;
	public final static int HORN_TOOL = 4;
	public final static int CROSS_TOOl = 5;

	// help
	public final static int REMOVE_HELP = 1;
	public final static int STAR_HELP = 2;
	public final static int WIN_HELP = 3;

	// 调用android具体接口的参数
	public final static int EXIT = 0;
	public final static int ADWALL = 1;
	public final static int CHAPING = 2;
	public final static int KAIPING = 3;

	public final static int START_LEVEL = 4;
	public final static int FAIL_LEVEL = 5;
	public final static int FINISH_LEVEL = 6;
}
