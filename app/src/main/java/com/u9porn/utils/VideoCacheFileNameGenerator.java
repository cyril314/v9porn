package com.u9porn.utils;

import com.danikula.videocache.file.FileNameGenerator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author
 * @date 2017/11/23
 * @describe
 */
public class VideoCacheFileNameGenerator implements FileNameGenerator {
	// Urls contain mutable parts (parameter 'sessionToken') and stable video's id (parameter 'videoId').
	@Override
	public String generate(String url) {
		int startIndex = url.lastIndexOf("/");
		String reg = ".+(.mp4|.m3u8|.ts)";
		Matcher mt = Pattern.compile(reg).matcher(url);
		int endIndex = -1;
		if (mt.find()) {
			String regSuffix = mt.group(1);
			if (regSuffix.equals(".ts")) {
				startIndex = 0;
			}
			endIndex = url.indexOf(regSuffix);
		}
		try {
			return url.substring(startIndex, endIndex) + ".temp";
		} catch (Exception e) {
			e.printStackTrace();
			return url + ".temp";
		}
	}
}
