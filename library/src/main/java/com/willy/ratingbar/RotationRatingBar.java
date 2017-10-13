package com.willy.ratingbar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by nappannda on 2017/05/16.
 */

public class RotationRatingBar extends AnimationRatingBar {

    public RotationRatingBar(Context context) {
        super(context);
    }

    public RotationRatingBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RotationRatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void emptyRatingBar() {
        // Need to remove all previous runnable to prevent emptyRatingBar and fillRatingBar out of sync
//        sUiHandler.removeCallbacksAndMessages(null);

        mDelay = 0;
        mStopFillingFlag = true;

        for (final PartialView partialView : mPartialViews) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    partialView.setEmpty();
                }
            }, mDelay += 5);
        }
    }

    @Override
    protected void fillRatingBar(final float rating) {
        // Need to remove all previous runnable to prevent emptyRatingBar and fillRatingBar out of sync
//        sUiHandler.removeCallbacksAndMessages(null);

        mDelay = 0;
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
                        Animation rotation = AnimationUtils.loadAnimation(getContext(), R.anim.rotation);
                        partialView.startAnimation(rotation);
                    }

                }
            }, mDelay += 15);
        }
    }
}