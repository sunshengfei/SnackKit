package com.freddon.android.snackkit.extension.tools;

import android.text.TextUtils;

import java.util.Map;


/**
 * Created by fred on 2016/11/5.
 */

public class EmptyUtil {

    /**
     * 判断空
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> boolean isEmpty(T t) {
        if (t == null) return true;
        if (t instanceof CharSequence) {
            return TextUtils.isEmpty((CharSequence) t);
        }
        if (t instanceof Map) {
            Map map = (Map) t;
            return map.entrySet().size() == 0;
        }
        if (t instanceof Iterable) {
            return !((Iterable) t).iterator().hasNext();
        }
        if (t instanceof Object[]) {
            return ((Object[]) t).length == 0;
        }
        return false;
    }
}
