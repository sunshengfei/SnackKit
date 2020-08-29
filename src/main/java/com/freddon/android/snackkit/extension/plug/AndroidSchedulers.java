package com.freddon.android.snackkit.extension.plug;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;

import java.util.concurrent.Executor;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by fred on 2018/4/27.
 */

public class AndroidSchedulers implements Executor {


    @Override
    public void execute(@NonNull Runnable runnable) {
        mHandler.post(runnable);
    }

    private static AndroidSchedulers instance;

    private final Scheduler mMainScheduler;
    private final Handler mHandler;

    private AndroidSchedulers() {
        mHandler = new Handler(Looper.myLooper());
        mMainScheduler = Schedulers.from(this);
    }

    public static Scheduler mainThread() {
        if (instance == null) {
            instance = new AndroidSchedulers();
        }
        return instance.mMainScheduler;
    }
}
