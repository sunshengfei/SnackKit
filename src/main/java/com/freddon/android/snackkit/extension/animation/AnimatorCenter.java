package com.freddon.android.snackkit.extension.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by fred on 16/8/8.
 */
public class AnimatorCenter {

    public static final int NORMAL_SPAN = 400;

    public static Animator rotateAnimator(View v, float fromDegree, float toDegree, long duration) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(v, "rotation", fromDegree, toDegree);
        objectAnimator.setDuration(duration);
        return objectAnimator;
    }

    /**
     * 同时执行一系列动画
     *
     * @return
     */
    public static void startAsyncAnimator(Animator.AnimatorListener listener, long duration, Animator... items) {
        AnimatorSet set = new AnimatorSet();
        set.setDuration(duration);
        if (listener != null) set.addListener(listener);
        set.playTogether(items);
        set.start();
    }


    /**
     * 依次执行一系列动画
     *
     * @param duration
     * @param items
     */
    public static void startSyncAnimator(Animator.AnimatorListener listener, long duration, Animator... items) {
        AnimatorSet set = new AnimatorSet();
        set.setDuration(duration);
        if (listener != null) set.addListener(listener);
        set.playSequentially(items);
        set.start();
    }


    /**
     * 重复5次结束
     *
     * @param view
     */
    public static void shake(@NonNull View view) {
        if (view == null) return;
//        view.clearAnimation();
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        final AnimatorSet set = new AnimatorSet();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationX", -8, 8);
        objectAnimator.setDuration(100);
        //设置插值器 动画速度存在正加速度
        objectAnimator.setInterpolator(new AccelerateInterpolator());
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        //在100毫秒内左右5趟
        objectAnimator.setRepeatCount(5);
        //最后归位
        ObjectAnimator objectAnimatorEnd = ObjectAnimator.ofFloat(view, "translationX", view.getTranslationX(), 0);
        objectAnimatorEnd.setDuration(10);
        //延迟5秒执行
        set.setStartDelay(5000);
        //序列动画 按顺序执行
        set.playSequentially(objectAnimator, objectAnimatorEnd);
        objectAnimatorEnd.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //动画集执行5次
                if (atomicInteger.addAndGet(1) < 5) {
                    set.start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
    }
}
