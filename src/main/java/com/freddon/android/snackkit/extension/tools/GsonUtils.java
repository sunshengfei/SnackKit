package com.freddon.android.snackkit.extension.tools;


import androidx.annotation.NonNull;

import com.freddon.android.snackkit.log.Loger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fred on 2016/11/7.
 */

public class GsonUtils {


    public static <T> T parseJson(JsonElement str, Type type) {
        GsonBuilder builder = new GsonBuilder();
        builder.enableComplexMapKeySerialization();
        Gson gson = builder.create();
        return gson.fromJson(str, type);
    }


    public static <T> T parseJson(String str, Type type) {
        GsonBuilder builder = new GsonBuilder();
        builder.enableComplexMapKeySerialization();
        Gson gson = builder.create();
        return gson.fromJson(str, type);
    }


    public static <T> T parseJson(String str, Class<T> type) {
        return parseJson(str, type, null, null);
    }


    public static <T> T parseJson(String str, Class<T> type, Class abstractClass, TypeAdapter adapter) {
        GsonBuilder builder = new GsonBuilder();
        builder.enableComplexMapKeySerialization();
        if (adapter != null && abstractClass != null)
            builder.registerTypeAdapter(abstractClass, adapter);
        Gson gson = builder.create();
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
            GsonBuilder builder = new GsonBuilder();
            builder.enableComplexMapKeySerialization();
            Gson gson = builder.create();
            return gson.toJson(obj);
        } catch (Exception e) {
            return "";
        }
    }

    public static String pretty(String string) {
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            builder.enableComplexMapKeySerialization();
            Gson gson = builder.create();
            if (string.startsWith("[")) {
                List p = gson.fromJson(string, List.class);
                return gson.toJson(p);
            } else {
                HashMap p = gson.fromJson(string, HashMap.class);
                return gson.toJson(p);
            }
        } catch (Exception e) {
            return string;
        }
    }

    public static JSONArray toJsonArray(Collection collection) {
        try {
            return new JSONArray(GsonUtils.toJson(collection));
        } catch (JSONException e) {
            Loger.e("GsonUtils", e.getMessage());
        }
        return new JSONArray();

    }

}
