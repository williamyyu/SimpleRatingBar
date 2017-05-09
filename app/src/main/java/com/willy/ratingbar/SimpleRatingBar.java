package com.willy.ratingbar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.LinkedHashMap;

/**
 * Created by willy on 2017/5/5.
 */

public class SimpleRatingBar extends BaseRatingBar {

    public static final String TAG = "SimpleRatingBar";

    public SimpleRatingBar(Context context) {
        super(context);
    }

    public SimpleRatingBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleRatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        super.init();
        initRatingViews();
    }

    private void initRatingViews() {
        mRatingViewStatus = new LinkedHashMap<>();

        for (int i = 1; i <= mNumStars; i++) {
            ImageView ratingView;
            if (i <= mRating) {
                ratingView = getRatingView(i, mFilledDrawable);
                mRatingViewStatus.put(ratingView, true);
            } else {
                ratingView = getRatingView(i, mEmptyDrawable);
                mRatingViewStatus.put(ratingView, false);
            }
            addView(ratingView);
        }
    }

    private ImageView getRatingView(final int ratingViewId, Drawable drawable) {
        ImageView imageView = new ImageView(getContext());
        imageView.setId(ratingViewId);
        imageView.setPadding(mPadding, mPadding, mPadding, mPadding);
        imageView.setImageDrawable(drawable);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int rating = v.getId();

                if (mRating == rating) {
                    clearRatingBar();
                    return;
                }

                setRating(rating);
            }
        });
        return imageView;
    }

    private void clearRatingBar() {
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

