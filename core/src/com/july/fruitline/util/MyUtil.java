package com.july.fruitline.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class MyUtil {
	public static MyUtil util = new MyUtil();

	FileHandle file;

	private MyUtil() {
		file = Gdx.files.internal("lev.d");
	}

	public int[][] readLevel(int lev) {
		int[][] level = new int[9][7];

		String text = file.readString();
		String[] args = text.split(";");
		String[] lines = args[lev].split("\n");
		// for (String line : lines)
		// System.out.print("-->" + line);
		// System.out.println("---over---");

		int start = 0;
		while (!lines[start].contains(","))
			start++; // 找到有效数据的起始行

		// System.out.println("---start---" + start + " length " +
		// lines.length);

		for (int i = start; i < start + 9; i++) {
			String[] temp = lines[i].split(",");
			for (int j = 0; j < 7; j++) {
				level[i - start][j] = Integer.parseInt(temp[j]);
			}
			// System.out.println("--" + (i - start));
		}
		return level;
	}
}
