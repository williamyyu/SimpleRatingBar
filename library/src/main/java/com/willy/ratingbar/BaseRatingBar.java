package com.willy.ratingbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by willy on 2017/5/5.
 */

public class BaseRatingBar extends LinearLayout implements SimpleRatingBar {

    protected int mNumStars = 5;
    protected int mRating = 0;
    protected int mPadding = 20;

    protected Drawable mEmptyDrawable;
    protected Drawable mFilledDrawable;

    protected Map<ImageView, Boolean> mRatingViewStatus;

    public BaseRatingBar(Context context) {
        this(context, null);
    }

    /* Call by xml layout */
    public BaseRatingBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context      context
     * @param attrs        attributes from XML => app:mainText="mainText"
     * @param defStyleAttr attributes from default style (Application theme or activity theme)
     */
    public BaseRatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBarAttributes);
        mNumStars = typedArray.getInt(R.styleable.RatingBarAttributes_numStars, mNumStars);
        mPadding = typedArray.getInt(R.styleable.RatingBarAttributes_starPadding, mPadding);
        mRating = typedArray.getInt(R.styleable.RatingBarAttributes_rating, mRating);
        mEmptyDrawable = typedArray.getDrawable(R.styleable.RatingBarAttributes_drawableEmpty);
        mFilledDrawable = typedArray.getDrawable(R.styleable.RatingBarAttributes_drawableFilled);
        typedArray.recycle();

        if (mEmptyDrawable == null) {
            mEmptyDrawable = getResources().getDrawable(R.drawable.start_empty);
        }

        if (mFilledDrawable == null) {
            mFilledDrawable = getResources().getDrawable(R.drawable.star_filled);
        }

        initRatingView();
    }

    private void initRatingView() {
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

        setRating(mRating);
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
                    clearRating();
                    return;
                }

                setRating(rating);
            }
        });
        return imageView;
    }

    private void removeAllRatingViews() {
        mRatingViewStatus.clear();
        removeAllViews();
    }

    protected void clearRating() {
        mRating = 0;

        if (mRatingViewStatus.size() <= 0) {
            return;
        }

        for (final ImageView view : mRatingViewStatus.keySet()) {
            view.setImageDrawable(mEmptyDrawable);
            mRatingViewStatus.put(view, false);
        }
    }

    protected void fillRatingBar(final int rating) {
        for (final ImageView view : mRatingViewStatus.keySet()) {
            if (view.getId() <= rating) {
                view.setImageDrawable(mFilledDrawable);
                mRatingViewStatus.put(view, true);
            } else {
                view.setImageDrawable(mEmptyDrawable);
                mRatingViewStatus.put(view, false);
            }
        }
    }

    @Override
    public void setNumStars(int numStars) {
        if (numStars <= 0) {
            return;
        }

        removeAllRatingViews();
        mNumStars = numStars;
        initRatingView();
    }

    @Override
    public int getNumStars() {
        return mNumStars;
    }

    @Override
    public void setRating(int rating) {
        if (rating > mNumStars) {
            rating = mNumStars;
        }

        if (rating < 0) {
            rating = 0;
        }

        if (mRating == rating) {
            return;
        }

        mRating = rating;
        fillRatingBar(rating);
    }

    @Override
    public int getRating() {
        return mRating;
    }

    @Override
    public void setStarPadding(int ratingPadding) {
        mPadding = ratingPadding;

        for (final ImageView view : mRatingViewStatus.keySet()) {
            view.setPadding(mPadding, mPadding, mPadding, mPadding);
        }
    }

    @Override
    public int getStarPadding() {
        return mPadding;
    }

    @Override
    public void setEmptyDrawable(Drawable drawable) {
        mEmptyDrawable = drawable;
    }

    @Override
    public void setFilledDrawable(Drawable drawable) {
        mFilledDrawable = drawable;
    }
}