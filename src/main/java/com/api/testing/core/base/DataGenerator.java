package base;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

public class DataGenerator {
	private DataGenerator() {

	}

	public static String randomEmail(String domain) {
		String randomString = RandomStringUtils.randomAlphabetic(15);
		return "training" + randomString + "@" + domain;
	}

	public static String randomDate() {
		double d = 0;
		String Random;
		int num = 1;
		{
			while (true) {
				int final_limit = 28; // Specify the maximum limit
				d = Math.random() * final_limit;
				num = (int) d;
				Random = String.valueOf(num);
				return Random;
			}
		}
	}

	public static String randomPassword() {
		return RandomStringUtils.randomAlphabetic(8);
	}

	public static String randomUsername() {
		return "training" + RandomStringUtils.randomAlphabetic(15);
	}

	public static int randomInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	public static double randomDouble() {
		Random rand = new Random();
		return rand.nextDouble();
	}

	public static String randomString(int limit) {
		return RandomStringUtils.randomAlphabetic(limit);
	}

	public static String randomStringNum(int limit) {
		return RandomStringUtils.randomNumeric(limit);
	}

	public static String timestampedString(String text) {
		return text + System.currentTimeMillis();
	}

	public static long getTimestamp() {
		return System.currentTimeMillis() / 1000L;
	}
}