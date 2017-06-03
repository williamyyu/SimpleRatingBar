package com.willy.ratingbar;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by willy on 2017/5/5.
 */

public class ScaleRatingBar extends BaseRatingBar {

    public ScaleRatingBar(Context context) {
        super(context);
    }

    public ScaleRatingBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScaleRatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Handler mUiHandler = new Handler();

    @Override
    protected void emptyRatingBar() {
        // Need to remove all previous runnable to prevent emptyRatingBar and fillRatingBar out of sync
        mUiHandler.removeCallbacksAndMessages(null);

        int delay = 0;
        for (final PartialView view : mRatingViewStatus.keySet()) {
            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setEmptyDrawable(mEmptyDrawable);
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
            if (view.getId() <= rating) {
                mUiHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setFilled();
                        mRatingViewStatus.put(view, true);
                        if (view.getId() == rating) {
                            Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
                            Animation scaleDown = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);
                            view.startAnimation(scaleUp);
                            view.startAnimation(scaleDown);
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

