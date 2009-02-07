package jsfun.utils;

import jline.ConsoleRunner;

public class Console {
	public static void main(String[] args) throws Exception {
		try {
			ConsoleRunner.main(args);
		} catch (Throwable e) {
			e.printStackTrace();

		}
	}
}
