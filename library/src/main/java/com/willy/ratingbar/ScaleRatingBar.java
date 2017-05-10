package com.willy.ratingbar;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by willy on 2017/5/5.
 */

public class ScaleRatingBar extends BaseRatingBar {

    public static final String TAG = "SimpleRatingBar";

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
    protected void clearRating() {
        mRating = 0;

        if (mRatingViewStatus.size() <= 0) {
            return;
        }

        int delay = 0;
        for (final ImageView view : mRatingViewStatus.keySet()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setImageDrawable(mEmptyDrawable);
                    mRatingViewStatus.put(view, false);
                }
            }, delay += 5);
        }
    }

    @Override
    protected void fillRatingBar(final int rating) {
        int delay = 0;
        for (final ImageView view : mRatingViewStatus.keySet()) {
            if (view.getId() <= rating) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setImageDrawable(mFilledDrawable);
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
                view.setImageDrawable(mEmptyDrawable);
                mRatingViewStatus.put(view, false);
            }
        }
    }
}

