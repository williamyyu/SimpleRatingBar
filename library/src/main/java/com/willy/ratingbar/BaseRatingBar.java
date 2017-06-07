package com.willy.ratingbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willy on 2017/5/5.
 */

public class BaseRatingBar extends LinearLayout implements SimpleRatingBar {

    public interface OnRatingChangeListener {
        void onRatingChange(BaseRatingBar ratingBar, float rating);
    }

    public static final String TAG = "SimpleRatingBar";

    private static final int MAX_CLICK_DISTANCE = 5;

    private int mNumStars;
    private int mPadding = 0;
    private float mRating = -1;
    private float mPreviousRating = 0;

    private float mStartX;
    private float mStartY;

    private Drawable mEmptyDrawable;
    private Drawable mFilledDrawable;

    private OnRatingChangeListener mOnRatingChangeListener;

    protected List<PartialView> mPartialViews;

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
        float rating = typedArray.getFloat(R.styleable.RatingBarAttributes_rating, mRating);
        mNumStars = typedArray.getInt(R.styleable.RatingBarAttributes_numStars, mNumStars);
        mPadding = typedArray.getInt(R.styleable.RatingBarAttributes_starPadding, mPadding);
        mEmptyDrawable = typedArray.getDrawable(R.styleable.RatingBarAttributes_drawableEmpty);
        mFilledDrawable = typedArray.getDrawable(R.styleable.RatingBarAttributes_drawableFilled);
        typedArray.recycle();

        verifyParamsValue();

        initRatingView();
        setRating(rating);
    }

    private void verifyParamsValue() {
        if (mNumStars <= 0) {
            mNumStars = 5;
        }

        if (mPadding < 0) {
            mPadding = 0;
        }

        if (mEmptyDrawable == null) {
            mEmptyDrawable = ContextCompat.getDrawable(getContext(), R.drawable.empty);
        }

        if (mFilledDrawable == null) {
            mFilledDrawable = ContextCompat.getDrawable(getContext(), R.drawable.filled);
        }

    }

    private void initRatingView() {
        mPartialViews = new ArrayList<>();

        for (int i = 1; i <= mNumStars; i++) {
            PartialView partialView = getPartialView(i, mFilledDrawable, mEmptyDrawable);
            mPartialViews.add(partialView);
            addView(partialView);
        }
    }

    private PartialView getPartialView(final int ratingViewId, Drawable filledDrawable, Drawable emptyDrawable) {
        PartialView partialView = new PartialView(getContext());
        partialView.setId(ratingViewId);
        partialView.setPadding(mPadding, mPadding, mPadding, mPadding);
        partialView.setFilledDrawable(filledDrawable);
        partialView.setEmptyDrawable(emptyDrawable);
        return partialView;
    }

    /**
     * Retain this method to let other RatingBar can custom their decrease animation.
     */
    protected void emptyRatingBar() {
        fillRatingBar(0);
    }

    /**
     * Use {maxIntOfRating} because if the rating is 3.5
     * the view which id is 3 also need to be filled.
     */
    protected void fillRatingBar(final float rating) {
        for (PartialView partialView : mPartialViews) {
            int ratingViewId = partialView.getId();
            double maxIntOfRating = Math.ceil(rating);

            if (ratingViewId > maxIntOfRating) {
                partialView.setEmpty();
                continue;
            }

            if (ratingViewId == maxIntOfRating) {
                partialView.setPartialFilled(rating);
            } else {
                partialView.setFilled();
            }
        }
    }

    @Override
    public void setNumStars(int numStars) {
        if (numStars <= 0) {
            return;
        }

        mPartialViews.clear();
        removeAllViews();

        mNumStars = numStars;
        initRatingView();
    }

    @Override
    public int getNumStars() {
        return mNumStars;
    }

    @Override
    public void setRating(float rating) {
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

        if (mOnRatingChangeListener != null) {
            mOnRatingChangeListener.onRatingChange(this, mRating);
        }

        fillRatingBar(rating);
    }

    @Override
    public float getRating() {
        return mRating;
    }

    @Override
    public void setStarPadding(int ratingPadding) {
        if (ratingPadding < 0) {
            return;
        }

        mPadding = ratingPadding;

        for (PartialView partialView : mPartialViews) {
            partialView.setPadding(mPadding, mPadding, mPadding, mPadding);
        }
    }

    @Override
    public int getStarPadding() {
        return mPadding;
    }

    @Override
    public void setEmptyDrawableRes(@DrawableRes int res) {
        setEmptyDrawable(ContextCompat.getDrawable(getContext(), res));
    }

    @Override
    public void setFilledDrawableRes(@DrawableRes int res) {
        setFilledDrawable(ContextCompat.getDrawable(getContext(), res));
    }

    @Override
    public void setEmptyDrawable(Drawable drawable) {
        mEmptyDrawable = drawable;

        for (PartialView partialView : mPartialViews) {
            partialView.setEmptyDrawable(drawable);
        }
    }

    @Override
    public void setFilledDrawable(Drawable drawable) {
        mFilledDrawable = drawable;

        for (PartialView partialView : mPartialViews) {
            partialView.setFilledDrawable(drawable);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = eventX;
                mStartY = eventY;
                mPreviousRating = mRating;
                handleMoveEvent(eventX);
                break;
            case MotionEvent.ACTION_MOVE:
                handleMoveEvent(eventX);
                break;
            case MotionEvent.ACTION_UP:
                if (!isClickEvent(mStartX, eventX, mStartY, eventY)) {
                    return false;
                }

                handleClickEvent(eventX);
        }

        return true;
    }

    private void handleMoveEvent(float eventX) {
        for (PartialView partialView : mPartialViews) {
            if (eventX < partialView.getWidth() / 2f) {
                setRating(0);
                return;
            }

            if (!isPositionInRatingView(eventX, partialView)) {
                continue;
            }

            int rating = partialView.getId();
            if (mRating != rating) {
                setRating(rating);
            }
        }
    }

    private void handleClickEvent(float eventX) {
        for (PartialView partialView : mPartialViews) {
            if (!isPositionInRatingView(eventX, partialView)) {
                continue;
            }

            int rating = partialView.getId();
            if (mPreviousRating == rating) {
                setRating(0);
            } else {
                setRating(rating);
            }
            break;
        }
    }

    private boolean isPositionInRatingView(float eventX, View ratingView) {
        return eventX > ratingView.getLeft() && eventX < ratingView.getRight();
    }

    private boolean isClickEvent(float startX, float endX, float startY, float endY) {
        float differenceX = Math.abs(startX - endX);
        float differenceY = Math.abs(startY - endY);
        return !(differenceX > MAX_CLICK_DISTANCE || differenceY > MAX_CLICK_DISTANCE);
    }

    public void setOnRatingChangeListener(OnRatingChangeListener onRatingChangeListener) {
        mOnRatingChangeListener = onRatingChangeListener;
    }
}