package com.u9porn.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author AIM
 * @Des 随机获取User-Agent
 * @DATE 2021/9/17
 */
public class AgentUtil {

	private static List<String> agentList = new ArrayList<String>();
	private static Random mRandom = new Random();
	private static String WIN = "Windows NT 6.1";
	private static String CFT = "Mozilla/5.0 (%s; WOW64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/%s Safari/537.36";
	private static String FT = "Mozilla/5.0 (%s; Win64; x64; rv:%s) Gecko/20100101 Firefox/%s";

	static {
		for (int i = 0; i < 3; i++) {
			if ((i & 2) == 0) {
				agentList.add(String.format(CFT, WIN, getRandomChrome()));
			} else {
				String randomFirefox = getRandomFirefox();
				agentList.add(String.format(FT, WIN, randomFirefox, randomFirefox));
			}
		}
	}

	private static String getRandomChrome() {
		StringBuilder sb = new StringBuilder();
		sb.append(mRandom.nextInt(11) + 90).append(".0.");
		sb.append(mRandom.nextInt(600) + 4000).append(".");
		sb.append(mRandom.nextInt(21) + 60);
		return sb.toString();
	}

	private static String getRandomFirefox() {
		StringBuilder sb = new StringBuilder();
		sb.append(mRandom.nextInt(13) + 80).append(".0");
		return sb.toString();
	}

	public static String getAgent() {
		return agentList.get(ThreadLocalRandom.current().nextInt(agentList.size()));
	}
}
