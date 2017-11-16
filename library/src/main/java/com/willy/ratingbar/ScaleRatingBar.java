package com.willy.ratingbar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by willy on 2017/5/5.
 */

public class ScaleRatingBar extends AnimationRatingBar {

    public ScaleRatingBar(Context context) {
        super(context);
    }

    public ScaleRatingBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScaleRatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void emptyRatingBar() {
        // Need to remove all previous runnable to prevent emptyRatingBar and fillRatingBar out of sync
//        mHandler.removeCallbacksAndMessages(null);

        mDelay = 0;
        mStopFillingFlag = true;

        for (final PartialView view : mPartialViews) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setEmpty();
                }
            }, mDelay += 5);
        }
    }

    @Override
    protected void fillRatingBar(final float rating) {
        // Need to remove all previous runnable to prevent emptyRatingBar and fillRatingBar out of sync
//        mHandler.removeCallbacksAndMessages(null);

        mDelay = 0;
        mStopFillingFlag = false;

        for (final PartialView partialView : mPartialViews) {
            final int ratingViewId = (int) partialView.getTag();
            final double maxIntOfRating = Math.ceil(rating);

            if (ratingViewId > maxIntOfRating) {
                partialView.setEmpty();
                continue;
            }

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mStopFillingFlag) {
                        return;
                    }

                    if (ratingViewId == maxIntOfRating) {
                        partialView.setPartialFilled(rating);
                    } else {
                        partialView.setFilled();
                    }

                    if (ratingViewId == rating) {
                        Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
                        Animation scaleDown = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);
                        partialView.startAnimation(scaleUp);
                        partialView.startAnimation(scaleDown);
                    }
                }
            }, mDelay += 15);
        }
    }
}

