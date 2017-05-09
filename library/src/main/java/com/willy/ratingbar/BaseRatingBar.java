package com.willy.ratingbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Map;

/**
 * Created by willy on 2017/5/5.
 */

abstract class BaseRatingBar extends LinearLayout {

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
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.RatingBarAttributes_numStars:
                    mNumStars = typedArray.getInt(attr, 5);
                    break;
                case R.styleable.RatingBarAttributes_starPadding:
                    mPadding = typedArray.getInt(attr, 20);
                    break;
                case R.styleable.RatingBarAttributes_rating:
                    mRating = typedArray.getInt(attr, 0);
                    break;
                case R.styleable.RatingBarAttributes_drawableEmpty:
                    mEmptyDrawable = typedArray.getDrawable(attr);
                    break;
                case R.styleable.RatingBarAttributes_drawableFilled:
                    mFilledDrawable = typedArray.getDrawable(attr);
                    break;
            }
        }
        typedArray.recycle();

        init();
    }

    protected void init() {
        if (mEmptyDrawable == null) {
            mEmptyDrawable = getResources().getDrawable(R.drawable.start_empty);
        }

        if (mFilledDrawable == null) {
            mFilledDrawable = getResources().getDrawable(R.drawable.star_filled);
        }

        if (mRating > mNumStars) {
            mRating = mNumStars;
        }
    }

    abstract void fillRatingBar(int rating);

    public void setNumStars(int numStars) {
        if (numStars <= 0) {
            return;
        }

        mNumStars = numStars;
    }

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

    public int getRating() {
        return mRating;
    }

    public void setRatingPadding(int ratingPadding) {
        mPadding = ratingPadding;
    }

    public void setEmptyDrawable(Drawable drawable) {
        mEmptyDrawable = drawable;
    }

    public void setFilledDrawableId(Drawable drawable) {
        mFilledDrawable = drawable;
    }

}