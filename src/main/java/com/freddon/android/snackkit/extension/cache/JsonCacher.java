package com.freddon.android.snackkit.extension.cache;

import androidx.annotation.NonNull;
import androidx.collection.LruCache;

import com.freddon.android.snackkit.extension.tools.GsonUtils;


/**
 * Created by fred on 2017/2/20.
 */
public class JsonCacher {

    private static  LruCache<String, Object> lruCache;

    private int size = 4 * 1024 * 1024;

    private static JsonCacher cacher;

    public static JsonCacher getInstance() {
        if (cacher == null) {
            cacher = new JsonCacher();
        }
        cacher.init();
        return cacher;
    }


    public void init() {
        if (lruCache == null) {

            lruCache = new LruCache<String, Object>(size) {
                @Override
                protected int sizeOf(String key, Object value) {
                    return GsonUtils.toJson(value).length();
                }

                @Override
                protected void entryRemoved(boolean evicted, String key, Object oldValue, Object newValue) {
                    super.entryRemoved(evicted, key, oldValue, newValue);
//                    if (evicted && oldValue != null) {
//                        oldValue=null;
//                    }
                }
            };
        }
    }


    public void put(@NonNull String key, @NonNull Object value) {
        lruCache.put(key, value);
    }

    public Object get(@NonNull String key) {
        return lruCache.get(key);
    }


    public Object remove(@NonNull String key) {
        return lruCache.remove(key);
    }
}
