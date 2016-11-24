package com.easyeat.util;

import com.easyeat.BuildConfig;
import com.easyeat.Config;

public final class Log {
    private static boolean debug = BuildConfig.DEBUG;
    private static final String TAG = Config.TAG;

    private Log() {
    }

    public static int v(String tag, String msg) {
        if (debug) {
            return android.util.Log.v(tag, msg);
        }
        return 0;
    }

    public static int v(String tag, String msg, Throwable tr) {
        if (debug) {
            return android.util.Log.v(tag, msg, tr);
        }
        return 0;
    }

    public static int d(String tag, String msg) {
        if (debug) {
            return android.util.Log.d(tag, msg);
        }
        return 0;
    }

    public static int d(String msg) {
        if (debug) {
            return android.util.Log.d(TAG, msg);
        }
        return 0;
    }

    public static int d(String tag, String msg, Throwable tr) {
        if (debug) {
            return android.util.Log.d(tag, msg, tr);
        }
        return 0;
    }

    public static int i(String tag, String msg) {
        if (debug) {
            return android.util.Log.i(tag, msg);
        }
        return 0;
    }

    public static int i(String tag, String msg, Throwable tr) {
        if (debug) {
            return android.util.Log.i(tag, msg, tr);
        }
        return 0;
    }

    public static int w(String tag, String msg) {
        if (debug) {
            return android.util.Log.w(tag, msg);
        }
        return 0;
    }

    public static int w(String tag, String msg, Throwable tr) {
        if (debug) {
            return android.util.Log.w(tag, msg, tr);
        }
        return 0;
    }

    public static int w(String tag, Throwable tr) {
        if (debug) {
            return android.util.Log.w(tag, tr);
        }
        return 0;
    }

    public static int e(String msg) {
        return e(TAG, msg);
    }

    public static int e(String tag, String msg) {
        if (debug) {
            return android.util.Log.e(tag, msg);
        }
        return 0;
    }

    public static int e(String tag, String msg, Throwable tr) {
        if (debug) {
            return android.util.Log.e(tag, msg, tr);
        }
        return 0;
    }
}