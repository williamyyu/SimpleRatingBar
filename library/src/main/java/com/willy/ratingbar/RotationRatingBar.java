package com.willy.ratingbar;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by nappannda on 2017/05/16.
 */

public class RotationRatingBar extends BaseRatingBar {

    public RotationRatingBar(Context context) {
        super(context);
    }

    public RotationRatingBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RotationRatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private final Handler mUiHandler = new Handler();

    @Override
    protected void emptyRatingBar() {
        // Need to remove all previous runnable to prevent emptyRatingBar and fillRatingBar out of sync
        mUiHandler.removeCallbacksAndMessages(null);

        int delay = 0;
        for (final PartialView view : mRatingViewStatus.keySet()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setEmpty();
                    mRatingViewStatus.put(view, false);
                }
            }, delay += 5);
        }
    }

    @Override
    protected void fillRatingBar(final float rating) {
        // Need to remove all previous runnable to prevent emptyRatingBar and fillRatingBar out of sync
        mUiHandler.removeCallbacksAndMessages(null);

        int delay = 0;
        for (final PartialView view : mRatingViewStatus.keySet()) {
            if (view.getId() <= rating + 1) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (view.getId() == (int) rating + 1) {
                            view.setPartial(rating);
                        } else {
                            view.setFilled();
                        }

                        mRatingViewStatus.put(view, true);
                        if (view.getId() == rating) {
                            Animation rotation = AnimationUtils.loadAnimation(getContext(), R.anim.rotation);
                            view.startAnimation(rotation);
                        }
                    }
                }, delay += 15);
            } else {
                view.setEmpty();
                mRatingViewStatus.put(view, false);
            }
        }
    }
}