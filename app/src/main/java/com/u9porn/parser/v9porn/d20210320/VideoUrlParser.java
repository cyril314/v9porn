package com.u9porn.parser.v9porn.d20210320;

import com.orhanobut.logger.Logger;
import com.u9porn.MyApplication;
import com.u9porn.data.db.entity.VideoResult;
import com.u9porn.data.model.User;
import com.u9porn.parser.v9porn.BaseVideoPlayUrlParser;
import com.u9porn.parser.v9porn.VideoPlayUrlParser;
import com.u9porn.utils.AppUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.inject.Inject;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class VideoUrlParser extends BaseVideoPlayUrlParser implements VideoPlayUrlParser {

	private static final String TAG = VideoUrlParser.class.getSimpleName();

	@Inject
	public VideoUrlParser() {

	}

	@Override
	public VideoResult parseVideoPlayUrl(String html, User user) {
		VideoResult videoResult = new VideoResult();
		Document document = Jsoup.parse(html);
		Element e = document.getElementById("player_one").selectFirst("script");
		String script = e.outerHtml();
		String aac = script.substring(script.indexOf("strencode2(") + "strencode2(".length() + 1, script.indexOf(")") - 1);
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		try {
			String srcScript = AppUtils.getAssetsAsString(MyApplication.getInstance(), "20210320.js");
			engine.eval(srcScript);
		} catch (ScriptException scriptException) {
			scriptException.printStackTrace();
		}
		if (engine instanceof Invocable) {
			Invocable invocable = (Invocable) engine;
			parserOtherInfo(document, videoResult, user);
			JavaScriptInterface executeMethod = invocable.getInterface(JavaScriptInterface.class);
			String source = executeMethod.strencode2(aac);
			String videoUrl = source.substring(source.indexOf("src='") + 5, source.indexOf("' type"));
			videoResult.setVideoUrl(videoUrl);
		}

		return videoResult;
	}

	public interface JavaScriptInterface {
		String strencode2(String str1);
	}

}
