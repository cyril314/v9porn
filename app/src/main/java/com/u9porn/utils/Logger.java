package com.u9porn.utils;

import android.util.Log;

/**
 * @AUTO
 * @Author AIM
 * @DATE 2024/11/29
 */
public class Logger {
    private static int currentLev = 4; // 当前log等级，上线后控制这个等级即可减少Log输出
    private static final int DEBUG_LEV = 4; // debug 等级
    private static final int INFO_LEV = 3; // info 等级
    private static final int WARNING_LEV = 2; // warning 等级
    private static final int ERROR_LEV = 1; // error 等级

    private String tag; // 当前日志的 TAG

    // 私有构造方法，防止直接实例化
    private Logger(String tag) {
        this.tag = tag;
    }

    // 提供静态方法获取 Logger 实例
    public static Logger t(String tag) {
        return new Logger(tag);
    }

    public static void d(String html, String log) {
        Log.d(html, log);
    }

    public Logger d(String format, Object... args) {
        return this.d(String.format(format, args));
    }

    public Logger d(String log) {
        if (currentLev >= DEBUG_LEV) {
            Log.d(tag, log);
        }
        return this;
    }

    public Logger i(String log) {
        if (currentLev >= INFO_LEV) {
            Log.i(tag, log);
        }
        return this;
    }

    public Logger w(String log) {
        if (currentLev >= WARNING_LEV) {
            Log.w(tag, log);
        }
        return this;
    }

    public Logger e(String log) {
        if (currentLev >= ERROR_LEV) {
            Log.e(tag, log);
        }
        return this;
    }
}