package com.july.fruitline.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.july.fruitline.screen.Game;
import com.july.fruitline.sprite.Box;
import com.july.fruitline.sprite.ToolFruit;
import com.july.fruitline.util.Assets;
import com.july.fruitline.util.Constants;

public class FruitFactory {
	// public static FruitFactory factory = new FruitFactory();
	private Game game;

	Pool<ToolFruit> fruitPool;
	ToolFruit[][] fruits;
	Box box;
	public final int ROW = 9;
	public final int COLUMN = 7;
	boolean removed = false;
	int duration = 0;

	boolean exchange = false;

	public FruitFactory() {
		fruitPool = Pools.get(ToolFruit.class);
		fruits = new ToolFruit[ROW][COLUMN];
		box = new Box();
	}

	public void init(int level, Game game) {
		// 统计关卡信息
		game.game.event.notify(this, Constants.START_LEVEL);
		// 播放进入游戏音乐
		MusicManager.manager.playSound(MusicManager.START);

		this.game = game;
		box.init(level);
		initSpecialFruitArgu(level);

		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COLUMN; j++) {
				fruits[i][j] = Pools.obtain(ToolFruit.class);
				fruits[i][j].init(i, j);
			}
		}

		initLastSelect();

		if (judgeAll())
			autoRemove(10);
	}

	private void initSpecialFruitArgu(int level) {
		star = 0;
		getStarNum = 0;
		goal = 0;
		if (level < 20)
			getGoalNum = 1;
		else if (level < 50)
			getGoalNum = 2;
		else
			getGoalNum = 3;
	}

	public void freeAll() {
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COLUMN; j++) {
				fruitPool.free(fruits[i][j]);
				fruits[i][j] = null;
			}
		}
	}

	public void draw(Batch batch) {
		if (removed) { // 在消除水果动画执行完毕之后，再次进行判断移动后的水果是否存在可消除的对象
			if (duration++ == Constants.wait + Constants.duration) {
				duration = 0;
				removed = false;
				if (judgeAll()) {
					autoRemove(Constants.wait);
				} else if (!preJudge()) {
					do { // 当不存在可以被消除的水果时，随机为一个水果添加ANY道具
						int row = (int) (Math.random() * ROW);
						int column = (int) (Math.random() * COLUMN);
						if (fruits[row][column].getType() < Constants.STAR) {
							fruits[row][column].setTool(Constants.ANY);
							break;
						}
						initLastSelect(); // 重置提示选择fruit的信息
					} while (true);
				}
			}
		}

		if (exchange) { // 在交换动画执行完毕之后，再执行消除动画
			if (duration++ == 10) {
				duration = 0;
				exchange = false;
				autoRemove(Constants.wait);
			}
		}

		box.draw(batch);

		for (int i = ROW - 1; i >= 0; i--) {
			for (int j = 0; j < COLUMN; j++) {
				if (fruits[i][j] != null)
					fruits[i][j].draw(batch);
			}
		}

		showTipTime += Gdx.graphics.getDeltaTime();
		if (showTipTime >= 8 && game.gameState == Constants.RUN) {
			lastRow = tipRow;
			lastColumn = tipColumn;
		}

		if (lastRow != NOT_USED) // 绘制选中框
			batch.draw(Assets.instance.selected, Constants.fruitX
					+ Constants.fruitWidth * lastColumn, Constants.fruitY
					+ Constants.fruitHeight * lastRow, Constants.fruitWidth,
					Constants.fruitHeight);

		FruitEffectManager.manager.draw(batch);
		ToolEffectManager.manager.draw(batch);
	}

	public boolean click(int row, int column) {
		if (exchange || removed)
			return false;

		showTipTime = 0;
		if (fruits[row][column].tool == Constants.ANY) {
			autoRemoveSameFruit(fruits[row][column].getType());
			return true;
		}

		if (lastRow == NOT_USED) {
			return updateLastSelect(row, column);
		}

		if ((Math.abs(row - lastRow) == 1 && column == lastColumn)
				|| (Math.abs(column - lastColumn) == 1 && row == lastRow)) {
			return exchange(row, column);
		} else
			return updateLastSelect(row, column);
	}

	public boolean paned(int firstRow, int firstColumn, int secondRow,
			int secondColumn) {
		if (exchange || removed)
			return false;

		showTipTime = 0;
		if (fruits[firstRow][firstColumn].tool == Constants.ANY) {
			autoRemoveSameFruit(fruits[firstRow][firstColumn].getType());
			return true;
		}

		if (fruits[secondRow][secondColumn].tool == Constants.ANY) {
			autoRemoveSameFruit(fruits[secondRow][secondColumn].getType());
			return true;
		}

		lastRow = firstRow;
		lastColumn = firstColumn;
		boolean tag = exchange(secondRow, secondColumn);
		initLastSelect();
		return tag;
	}

	int lastRow;
	int lastColumn;
	final int NOT_USED = -1;

	private void initLastSelect() {
		lastRow = NOT_USED;
		lastColumn = NOT_USED;
	}

	private boolean updateLastSelect(int row, int column) {
		lastRow = row;
		lastColumn = column;
		return false;
	}

	/**
	 * 执行交换动画，若交换后存在可消除的对象，则设置标志exchange=true, 后面将在交换动画执行完毕之后自动消除可消除水果
	 * 
	 * @param row
	 * @param column
	 * @return true 表示存在可消除对象
	 */
	private boolean exchange(int row, int column) {
		ToolFruit temp = fruits[row][column];
		fruits[row][column] = fruits[lastRow][lastColumn];
		fruits[lastRow][lastColumn] = temp;

		float x = fruits[row][column].bounds.x;
		float y = fruits[row][column].bounds.y;
		if (judgeAll()) {
			fruits[row][column].addExchangeAction(fruits[lastRow][lastColumn],
					0, 10, false);
			fruits[lastRow][lastColumn].addExchangeAction(x, y, 0, 10, false);
			initLastSelect();
			exchange = true;
			return true;
		} else {
			fruits[row][column].addExchangeAction(fruits[lastRow][lastColumn],
					0, 20, true);
			fruits[lastRow][lastColumn].addExchangeAction(x, y, 0, 20, true);

			temp = fruits[row][column];
			fruits[row][column] = fruits[lastRow][lastColumn];
			fruits[lastRow][lastColumn] = temp;
			return updateLastSelect(row, column);
		}
	}

	int goal = 0;
	int star = 0;
	int getStarNum = 0;
	int getGoalNum = 0;

	float showTipTime;

	/**
	 * 自动消除所有的可消除水果
	 * 
	 * @param wait
	 */
	private void autoRemove(int wait) {
		showTipTime = 0;

		duration = 0;
		removed = true;
		int count = 0;
		for (int row = 0; row < ROW; row++) {
			for (int column = 0; column < COLUMN; column++) {
				if (fruits[row][column].canRemove) {
					box.removeBox(row, column);
					// tag为true表示该水果为道具消除，因此不现实该水果自带的道具效果
					FruitEffectManager.manager.show(!fruits[row][column].tag,
							fruits[row][column].tool,
							fruits[row][column].getType(),
							fruits[row][column].bounds.x,
							fruits[row][column].bounds.y);
					count++;
					if (fruits[row][column].getType() == Constants.TIME_TOOL)
						game.addTime();
				}
			}
		}
		game.addScore(count); // 增加分数

		for (int column = 0; column < COLUMN; column++) {
			removeColumn(wait, column);
		}

		// 判断星星，奖杯道具是否落地
		if (star > 0 || goal > 0) {
			for (int column = 0; column < COLUMN; column++) {
				switch (fruits[0][column].getType()) {
				case Constants.STAR:
					// get one star
					fruits[0][column].canRemove = true;
					getStarNum++;
					game.addStar();
					MusicManager.manager.playSound(MusicManager.STAR);
					// 奖励大量时间
					game.addTime(getStarNum);
					break;
				case Constants.GOAL_1:
				case Constants.GOAL_2:
				case Constants.GOAL_3:
					fruits[0][column].canRemove = true;
					MusicManager.manager.playSound(MusicManager.GREAT);
					if (--getGoalNum == 0) {
						fruits[0][column].canRemove = false;
						game.gameOver(true, getStarNum);
					}
				}
			}
		}

		MusicManager.manager.playSound(MusicManager.FRUIT);
	}

	/**
	 * 点击变换色的水果时，自动消除所有与type类型的水果
	 * 
	 * @param type
	 */
	private void autoRemoveSameFruit(int type) {
		for (int row = 0; row < ROW; row++) {
			for (int column = 0; column < COLUMN; column++) {
				if (fruits[row][column].getType() == type)
					fruits[row][column].setTaged();
			}
		}
		autoRemove(10);
	}

	private boolean isFruitRemoved(int row, int column) {
		return fruits[row][column] == null || fruits[row][column].canRemove;
	}

	private void removeColumn(int wait, int column) {
		boolean tag = false;
		for (int row = 0; row < ROW; row++) {
			if (isFruitRemoved(row, column)) { // 该水果可以消除，则向上找到一个下降到该位置
				tag = true;
				for (int i = row + 1; i < ROW; i++) {
					if (!isFruitRemoved(i, column)) { // 找到可移动的水果添加下移动画，并回收消除对象
						tag = false;
						if (fruits[row][column] != null)
							fruitPool.free(fruits[row][column]);
						fruits[row][column] = null;
						fruits[i][column].addMoveDownAction(wait,
								Constants.duration, row);
						fruits[row][column] = fruits[i][column];
						fruits[i][column] = null;
						break;
					}
				}
				if (tag) { // 新增一个水果
					fruits[row][column] = fruitPool.obtain();
					if (star < 3 && box.ifAddStar(star + 1)) { // 如果所有的box都清零，则新添加一个星星
						if (Assets.instance.currentLevel == 1 && star == 0) {
							game.showHelp(Constants.STAR_HELP); // 显示星星提示
						}

						star++;
						fruits[row][column]
								.initSpecial(wait, Constants.duration, row,
										column, Constants.STAR);
						fruits[row][column].tool = Constants.STAR_TOOL;
					} else if (goal < getGoalNum && box.ifOver()) {
						if (Assets.instance.currentLevel == 1) {
							game.showHelp(Constants.WIN_HELP); // 显示胜利提示
						}

						MusicManager.manager.playSound(MusicManager.GREAT);
						fruits[row][column].initSpecial(wait,
								Constants.duration, row, column,
								Constants.GOAL_1 + goal);
						goal++;
					} else {
						fruits[row][column].init(wait, Constants.duration, row,
								column);
					}
				}
			}
		}
	}

	private boolean judgeAll() {
		for (int row = 0; row < ROW; row++)
			judgeRow(row);

		for (int column = 0; column < COLUMN; column++)
			judgeColumn(column);

		return addTool();
	}

	private void tagColumn(int column) {
		for (int row = 0; row < ROW; row++) {
			fruits[row][column].setTaged();
		}
	}

	private void tagRow(int row) {
		for (int column = 0; column < COLUMN; column++)
			fruits[row][column].setTaged();
	}

	private void tagFruit(int row, int column) {
		if (row < 0 || row >= ROW || column < 0 || column >= COLUMN)
			return;
		fruits[row][column].setTaged();
	}

	private void tagNine(int row, int column) {
		for (int i = row - 1; i <= row + 1; i++)
			for (int j = column - 1; j <= column + 1; j++)
				tagFruit(i, j);
	}

	private void removeTool(int row, int column) {
		int i = row + 1;
		while (i < ROW) {
			if (!fruits[row][column].sameRemovedWith(fruits[i][column])) {
				break;
			}
			if (fruits[i][column].tag) {
				fruits[i][column].removeTool();
			}
			i++;
		}

		i = row - 1;
		while (i >= 0) {
			if (!fruits[row][column].sameRemovedWith(fruits[i][column])) {
				break;
			}
			if (fruits[i][column].tag) {
				fruits[i][column].removeTool();
			}
			i--;
		}

		i = column + 1;
		while (i < COLUMN) {
			if (!fruits[row][column].sameRemovedWith(fruits[row][i]))
				break;
			if (fruits[row][i].tag) {
				fruits[row][i].removeTool();
			}
			i++;
		}

		i = column - 1;
		while (i >= 0) {
			if (!fruits[row][column].sameRemovedWith(fruits[row][i]))
				break;
			if (fruits[row][i].tag) {
				fruits[row][i].removeTool();
			}
			i--;
		}
	}

	private void threeFruitRemove(int row) {
		for (int column = 0; column < COLUMN - 2; column++) {
			if (fruits[row][column].sameRemovedWith(fruits[row][column + 1],
					fruits[row][column + 2])) { // 存在一行三消
				int i = 0;
				while (i < 3) {
					if ((row >= 1 && fruits[row][column + i]
							.sameRemovedWith(fruits[row - 1][column + i]))
							|| (row < ROW - 1 && fruits[row][column + i]
									.sameRemovedWith(fruits[row + 1][column + i]))) {
						// 添加cross道具
						fruits[row][column + i].setTool(Constants.CROSS_TOOl);
						removeTool(row, column + i);
						break;
					}
					i++;
				}
				if (i < 3) { // 表示上面找到可以添加cross道具的水果
					break;
				}
				while (column + i < COLUMN) {
					if (!fruits[row][column].sameRemovedWith(fruits[row][column
							+ i])) {
						break;
					}
					if ((row < ROW - 1 && fruits[row][column + i]
							.sameRemovedWith(fruits[row + 1][column + i]))
							|| (row >= 1 && fruits[row][column + i]
									.sameRemovedWith(fruits[row - 1][column + i]))) {
						// 添加cross道具
						fruits[row][column + i].setTool(Constants.CROSS_TOOl);
						removeTool(row, column + i);
						break;
					}
					i++;
				}
			}
		}
	}

	private boolean addTool() {
		boolean hasRemoveFruit = false;
		// 扫描所有可消除的水果，为满足条件的水果添加道具，且不消除该水果
		for (int row = 0; row < ROW - 1; row++) {
			threeFruitRemove(row);
		}

		for (int row = 0; row < ROW; row++) {
			for (int column = 0; column < COLUMN; column++) {
				if (!hasRemoveFruit && fruits[row][column].canRemove)
					hasRemoveFruit = true;
				if (fruits[row][column].tag) { // 表示新增加道具的水果，重置，保证不被消除
					fruits[row][column].tag = false;
					fruits[row][column].canRemove = false;
				}
			}
		}

		// 扫描所有的可消除水果，如果消除的水果中存在道具，则添加道具消除水果项
		for (int row = 0; row < ROW; row++) {
			for (int column = 0; column < COLUMN; column++) {
				if (!fruits[row][column].canRemove || fruits[row][column].tag) // 水果不能消除或者水果为道具消除的，不用显示道具效果
					continue;

				if (fruits[row][column].tool == Constants.VER_TOOL) {
					tagColumn(column);
					// fruits[row][column].tag = false;
				} else if (fruits[row][column].tool == Constants.HORN_TOOL) {
					tagRow(row);
					// fruits[row][column].tag = false;
					break;
				} else if (fruits[row][column].tool == Constants.CROSS_TOOl) {
					tagColumn(column);
					tagRow(row);
					// fruits[row][column].tag = false;
					break;
				} else if (fruits[row][column].tool == Constants.BOMB_TOOL) {
					tagNine(row, column);
					// fruits[row][column].tag = false;
				}
			}
		}

		return hasRemoveFruit;
	}

	/**
	 * 判断一行内是否存在可消除的水果
	 * 
	 * @param row
	 * @param wait
	 * @return
	 */
	private void judgeRow(int row) {
		int count = 0;
		int lastColumn = -1;
		for (int column = 0; column < COLUMN; column++) {
			if (count == 0) {
				if ((column < COLUMN - 2)
						&& fruits[row][column]
								.compareTo(fruits[row][column + 1])) {
					if (fruits[row][column].compareTo(fruits[row][column + 2])) {
						fruits[row][column].canRemove = true;
						fruits[row][column + 1].canRemove = true;
						fruits[row][column + 2].canRemove = true;
						column += 2;
						count = 3;
					} else {
						count = 0;
						column++;
					}
				}
			} else {
				if (fruits[row][column - 1].compareTo(fruits[row][column])) {
					fruits[row][column].canRemove = true;
					if (lastColumn == -1) {
						fruits[row][column].setTool(Constants.HORN_TOOL);
						lastColumn = column;
						// 表示该水果添加了道具，后面在addTool时，要求将该水果的canRemove设置为false，即不消除改水果
						// System.out.println("add hrom");
					}
					if (++count == 5) {
						fruits[row][lastColumn].tool = -1;
						fruits[row][lastColumn].tag = false;
						fruits[row][column - 2].setTool(Constants.BOMB_TOOL);
						// System.out.println("add bomb");
					}
				} else {
					column--;
					count = 0;
				}
			}
		}
	}

	/**
	 * 判断一列是否存在可消除的水果
	 * 
	 * @param column
	 */
	private void judgeColumn(int column) {
		int count = 0;
		int lastRow = -1;
		for (int row = 0; row < ROW; row++) {
			if (count == 0) {
				if ((row < ROW - 2)
						&& fruits[row][column]
								.compareTo(fruits[row + 1][column])) {
					if (fruits[row][column].compareTo(fruits[row + 2][column])) {
						fruits[row][column].canRemove = true;
						fruits[row + 1][column].canRemove = true;
						fruits[row + 2][column].canRemove = true;
						row += 2;
						count = 3;
					} else {
						count = 0;
						row++;
					}
				}
			} else {
				if (fruits[row - 1][column].compareTo(fruits[row][column])) {
					fruits[row][column].canRemove = true;
					if (lastRow == -1) {
						fruits[row][column].setTool(Constants.VER_TOOL);
						lastRow = row;
						// System.out.println("add ver");
					}
					if (++count == 5) {
						fruits[lastRow][column].tool = -1;
						fruits[lastRow][column].tag = false;
						fruits[row - 2][column].setTool(Constants.BOMB_TOOL);
						// System.out.println("add bomb");
					}
				} else {
					count = 0;
					row--;
				}
			}
		}
	}

	int tipRow, tipColumn;

	/**
	 * 判断是否存在可以消除的水果
	 * 
	 * @return
	 */
	public boolean preJudge() {
		for (int row = 0; row < ROW; row++) {
			for (int column = 0; column < COLUMN; column++) {
				if (compareTowFruit(row, column, row, column + 1)) {
					if (compareTowFruit(row, column, row, column + 3)) { // 右3
						tipRow = row;
						tipColumn = column + 3;
						return true;
					}
					if (compareTowFruit(row, column, row, column - 2)) { // 左2
						tipRow = row;
						tipColumn = column - 2;
						return true;
					}
					if (compareTowFruit(row, column, row - 1, column + 2)) { // 右2下1
						tipRow = row - 1;
						tipColumn = column + 2;
						return true;
					}
					if (compareTowFruit(row, column, row + 1, column + 2)) { // 右2上1
						tipRow = row + 1;
						tipColumn = column + 2;
						return true;
					}
					if (compareTowFruit(row, column, row - 1, column - 1)) { // 左1下1
						tipRow = row - 1;
						tipColumn = column - 1;
						return true;
					}
					if (compareTowFruit(row, column, row + 1, column - 1)) { // 左1上1
						tipRow = row + 1;
						tipColumn = column - 1;
						return true;
					}
				}
				if (compareTowFruit(row, column, row + 1, column)) {
					if (compareTowFruit(row, column, row + 3, column)) { // 上3
						tipRow = row + 3;
						tipColumn = column;
						return true;
					}
					if (compareTowFruit(row, column, row - 2, column)) { // 下2
						tipRow = row - 2;
						tipColumn = column;
						return true;
					}
					if (compareTowFruit(row, column, row + 2, column + 1)) { // 上2右1
						tipRow = row + 2;
						tipColumn = column + 1;
						return true;
					}
					if (compareTowFruit(row, column, row + 2, column - 1)) { // 上2左1
						tipRow = row + 2;
						tipColumn = column - 1;
						return true;
					}
					if (compareTowFruit(row, column, row - 1, column + 1)) { // 下1右1
						tipRow = row - 1;
						tipColumn = column + 1;
						return true;
					}
					if (compareTowFruit(row, column, row - 1, column - 1)) { // 下1左1
						tipRow = row - 1;
						tipColumn = column - 1;
						return true;
					}
				}

				if (compareTowFruit(row, column, row + 1, column + 1) // 中间
						|| compareTowFruit(row, column, row - 1, column - 1)) {
					if (compareTowFruit(row, column, row + 1, column - 1)
							|| compareTowFruit(row, column, row - 1, column + 1)) {
						tipRow = row;
						tipColumn = column;
						return true;
					}
				}
			}
		}

		return false;
	}

	private boolean compareTowFruit(int row1, int column1, int row2, int column2) {
		if (row2 >= ROW || column2 >= COLUMN || row2 < 0 || column2 < 0)
			return false;
		return fruits[row1][column1].compareTo(fruits[row2][column2]);
	}
}
