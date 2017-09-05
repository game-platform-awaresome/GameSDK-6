package com.game.tobin.util;

import android.util.Log;

/**
 * 日志工具类。统一管理日志信息的显示/隐藏
 *
 * @author Tobin
 *
 * @since 2016-04-11 上午11:11:11
 */
public class LogUtil {
    /** 日志输出时的TAG */
    private static String mTag = "TobinLogUtil";

    /**
     * 是否已经显示佛祖
     */
    private static boolean isShowBuddha = true;

    /**
     * 是否允许输出log
     *  mDebuggable < 1：  不允许
     *  mDebuggable >= 1： 根据等级允许
     * */
    public static int mDebuggable = 10;

    /** 日志输出级别V */
    private static final int LEVEL_VERBOSE = 1;

    /** 日志输出级别D */
    private static final int LEVEL_DEBUG = 2;

    /** 日志输出级别I */
    private static final int LEVEL_INFO = 3;

    /** 日志输出级别W */
    private static final int LEVEL_WARN = 4;

    /** 日志输出级别E */
    private static final int LEVEL_ERROR = 5;

    private LogUtil() throws InstantiationException {
        showBuddha();
        throw new InstantiationException("This class is not created for instantiation");
    }

    /**
     * 佛祖显灵
     */
    public static void showBuddha() {
        if (isShowBuddha) {
            return;
        }
        isShowBuddha = true;
        Log.i(mTag, "                            _ooOoo_");
        Log.i(mTag, "                           o8888888o");
        Log.i(mTag, "                          88\" . \"88");
        Log.i(mTag, "                           (| -_- |)");
        Log.i(mTag, "                            O\\ = /O");
        Log.i(mTag, "                        ____/`---'\\____");
        Log.i(mTag, "                      .   ' \\| |// `.");
        Log.i(mTag, "                       / \\\\||| : |||// \\");
        Log.i(mTag, "                     / _||||| -:- |||||- \\");
        Log.i(mTag, "                       | | \\\\\\ - /// | |");
        Log.i(mTag, "                     | \\_| ''\\---/'' | |");
        Log.i(mTag, "                      \\ .-\\__ `-` ___/-. /");
        Log.i(mTag, "                   ___`. .' /--.--\\ `. . __");
        Log.i(mTag, "                .\"\" '< `.___\\_<|>_/___.' >'\"\".");
        Log.i(mTag, "               | | : `- \\`.;`\\ _ /`;.`/ - ` : | |");
        Log.i(mTag, "                 \\ \\ `-. \\_ __\\ /__ _/ .-` / /");
        Log.i(mTag, "         ======`-.____`-.___\\_____/___.-`____.-'======");
        Log.i(mTag, "                            `=---='");
        Log.i(mTag, "         .............................................");
        Log.i(mTag, "         				信佛祖，无bug");
        Log.i(mTag, "         				del bug ...");
    }

    /**
     * 以级别为v 的形式输出LOG
     * */
    public static void v(String msg) {
        if (mDebuggable >= LEVEL_VERBOSE) {
            Log.v(mTag, msg);
        }
    }

    /** 以级别为 d 的形式输出LOG */
    public static void d(String msg) {
        if (mDebuggable >= LEVEL_DEBUG) {
            Log.d(mTag, msg);
        }
    }

    /** 以级别为 d 的形式输出LOG */
    public static void d(String tag,String msg) {
        if (mDebuggable >= LEVEL_DEBUG) {
            Log.d(tag, msg);
        }
    }

    /** 以级别为 i 的形式输出LOG */
    public static void i(String msg) {
        if (mDebuggable >= LEVEL_INFO) {
            Log.i(mTag, msg);
        }
    }

    /** 以级别为 i 的形式输出LOG */
    public static void i(String tag,String msg) {
        if (mDebuggable >= LEVEL_INFO) {
            Log.i(tag, msg);
        }
    }

    /**
     * 以级别为 w 的形式输出LOG
     * */
    public static void w(String msg) {
        if (mDebuggable >= LEVEL_WARN) {
            Log.w(mTag, msg);
        }
    }

    /**
     * 以级别为 w 的形式输出LOG
     * */
    public static void w(String tag,String msg) {
        if (mDebuggable >= LEVEL_WARN) {
            Log.w(tag, msg);
        }
    }

    /**
     * 以级别为 w 的形式输出Throwable
     * */
    public static void w(Throwable tr) {
        if (mDebuggable >= LEVEL_WARN) {
            Log.w(mTag, "", tr);
        }
    }

    /**
     * 以级别为 w 的形式输出LOG信息和Throwable
     * */
    public static void w(String msg, Throwable tr) {
        if (mDebuggable >= LEVEL_WARN && null != msg) {
            Log.w(mTag, msg, tr);
        }
    }

    /**
     * 以级别为 e 的形式输出LOG
     *
     * */
    public static void e(String msg) {
        if (mDebuggable >= LEVEL_ERROR) {
            Log.e(mTag, msg);
        }
    }

    /**
     * 以级别为 e 的形式输出LOG
     *
     * */
    public static void e(String tag,String msg) {
        if (mDebuggable >= LEVEL_ERROR) {
            Log.e(tag, msg);
        }
    }

    /**
     * 以级别为 e 的形式输出Throwable
     * */
    public static void e(Throwable tr) {
        if (mDebuggable >= LEVEL_ERROR) {
            Log.e(mTag, "", tr);
        }
    }

    /** 以级别为 e 的形式输出LOG信息和Throwable */
    public static void e(String msg, Throwable tr) {
        if (mDebuggable >= LEVEL_ERROR && null != msg) {
            Log.e(mTag, msg, tr);
        }
    }

}
