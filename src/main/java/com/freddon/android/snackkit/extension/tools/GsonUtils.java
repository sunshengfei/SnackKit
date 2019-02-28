package com.freddon.android.snackkit.extension.tools;


import com.freddon.android.snackkit.log.Loger;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Created by fred on 2016/11/7.
 */

public class GsonUtils {


    public static <T> T parseJson(String str, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }


    public static <T> T parseJson(String str, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }

    /**
     * 对象转换成json字符串
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        try {
            Gson gson = new Gson();
            return gson.toJson(obj);
        } catch (Exception e) {
            return "";
        }
    }

    public static JSONArray toJsonArray(Collection collection) {
        try {
            return new JSONArray(GsonUtils.toJson(collection));
        } catch (JSONException e) {
            Loger.e("GsonUtils",e.getMessage());
        }
        return new JSONArray();

    }

}
