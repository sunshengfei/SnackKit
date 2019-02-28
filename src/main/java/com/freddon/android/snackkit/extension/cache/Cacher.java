package com.freddon.android.snackkit.extension.cache;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.collection.LruCache;


/**
 * Created by fred on 2017/2/20.
 */
public class Cacher {

    private static LruCache<String, Bitmap> lruCache;

    private int size = 4 * 1024 * 1024;

    private static Cacher cacher;

    public static Cacher getInstance() {
        if (cacher == null) {
            cacher = new Cacher();
        }
        cacher.init();
        return cacher;
    }


    public void init() {
        if (lruCache == null) {
            lruCache = new LruCache<String, Bitmap>(size) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount();
                }

                @Override
                protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                    super.entryRemoved(evicted, key, oldValue, newValue);
                    if (evicted && oldValue != null) {
                        oldValue.recycle();
                    }
                }
            };
        }
    }


    public void put(@NonNull String key, @NonNull Bitmap value) {
        lruCache.put(key, value);
    }

    public Bitmap get(@NonNull String key) {
        return lruCache.get(key);
    }


    public Bitmap remove(@NonNull String key) {
        return lruCache.remove(key);
    }
}
