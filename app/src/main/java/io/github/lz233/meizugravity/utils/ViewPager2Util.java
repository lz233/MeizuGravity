package io.github.lz233.meizugravity.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.viewpager2.widget.ViewPager2;

public class ViewPager2Util {
    /**
     * 保存前一个animatedValue
     */
    private static int previousValue;

    /**
     * 设置当前Item
     * @param pager    viewpager2
     * @param item     下一个跳转的item
     * @param duration scroll时长
     */
    public static void setCurrentItem(final ViewPager2 pager, int item, long duration,int padding) {
        previousValue = 0;
        int currentItem = pager.getCurrentItem();
        int pagePxHeight = pager.getHeight();
        //减掉边距
        int pxToDrag = (pagePxHeight-padding) * (item - currentItem);
        final ValueAnimator animator = ValueAnimator.ofInt(0, pxToDrag);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (int) animation.getAnimatedValue();
                float currentPxToDrag = (float) (currentValue - previousValue);
                pager.fakeDragBy(-currentPxToDrag);
                previousValue = currentValue;
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                pager.beginFakeDrag();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                pager.endFakeDrag();
            }

            @Override
            public void onAnimationCancel(Animator animation) { }

            @Override
            public void onAnimationRepeat(Animator animation) { }
        });
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(duration);
        animator.start();
    }
}
