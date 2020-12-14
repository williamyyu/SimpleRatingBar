package com.willy.ratingbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willy on 2017/5/5.
 */

public class BaseRatingBar extends LinearLayout implements SimpleRatingBar {

    public interface OnRatingChangeListener {
        void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser);
    }

    public static final String TAG = "SimpleRatingBar";

    private int mNumStars;
    private int mPadding = 20;
    private int mStarWidth;
    private int mStarHeight;
    private float mMinimumStars = 0;
    private float mRating = -1;
    private float mStepSize = 1f;
    private float mPreviousRating = 0;

    private boolean mIsIndicator = false;
    private boolean mIsScrollable = true;
    private boolean mIsClickable = true;
    private boolean mClearRatingEnabled = true;

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

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseRatingBar);
        final float rating = typedArray.getFloat(R.styleable.BaseRatingBar_srb_rating, 0);

        initParamsValue(typedArray, context);
        verifyParamsValue();
        initRatingView();
        setRating(rating);
    }

    private void initParamsValue(TypedArray typedArray, Context context) {
        mNumStars = typedArray.getInt(R.styleable.BaseRatingBar_srb_numStars, mNumStars);
        mStepSize = typedArray.getFloat(R.styleable.BaseRatingBar_srb_stepSize, mStepSize);
        mMinimumStars = typedArray.getFloat(R.styleable.BaseRatingBar_srb_minimumStars, mMinimumStars);
        mPadding = typedArray.getDimensionPixelSize(R.styleable.BaseRatingBar_srb_starPadding, mPadding);
        mStarWidth = typedArray.getDimensionPixelSize(R.styleable.BaseRatingBar_srb_starWidth, 0);
        mStarHeight = typedArray.getDimensionPixelSize(R.styleable.BaseRatingBar_srb_starHeight, 0);
        mEmptyDrawable = typedArray.hasValue(R.styleable.BaseRatingBar_srb_drawableEmpty) ? ContextCompat.getDrawable(context, typedArray.getResourceId(R.styleable.BaseRatingBar_srb_drawableEmpty, View.NO_ID)) : null;
        mFilledDrawable = typedArray.hasValue(R.styleable.BaseRatingBar_srb_drawableFilled) ? ContextCompat.getDrawable(context, typedArray.getResourceId(R.styleable.BaseRatingBar_srb_drawableFilled, View.NO_ID)) : null;
        mIsIndicator = typedArray.getBoolean(R.styleable.BaseRatingBar_srb_isIndicator, mIsIndicator);
        mIsScrollable = typedArray.getBoolean(R.styleable.BaseRatingBar_srb_scrollable, mIsScrollable);
        mIsClickable = typedArray.getBoolean(R.styleable.BaseRatingBar_srb_clickable, mIsClickable);
        mClearRatingEnabled = typedArray.getBoolean(R.styleable.BaseRatingBar_srb_clearRatingEnabled, mClearRatingEnabled);
        typedArray.recycle();
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

        if (mStepSize > 1.0f) {
            mStepSize = 1.0f;
        } else if (mStepSize < 0.1f) {
            mStepSize = 0.1f;
        }

        mMinimumStars = RatingBarUtils.getValidMinimumStars(mMinimumStars, mNumStars, mStepSize);
    }

    private void initRatingView() {
        mPartialViews = new ArrayList<>();

        for (int i = 1; i <= mNumStars; i++) {
            PartialView partialView = getPartialView(i, mStarWidth, mStarHeight, mPadding, mFilledDrawable, mEmptyDrawable);
            addView(partialView);

            mPartialViews.add(partialView);
        }
    }

    private PartialView getPartialView(final int partialViewId, int starWidth, int starHeight, int padding,
                                       Drawable filledDrawable, Drawable emptyDrawable) {
        PartialView partialView = new PartialView(getContext(), partialViewId, starWidth, starHeight, padding);
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
            int ratingViewId = (int) partialView.getTag();
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
        setRating(rating, false);
    }

    private void setRating(float rating, boolean fromUser) {
        if (rating > mNumStars) {
            rating = mNumStars;
        }

        if (rating < mMinimumStars) {
            rating = mMinimumStars;
        }

        if (mRating == rating) {
            return;
        }

        // Respect Step size. So if the defined step size is 0.5, and we're attributing it a 4.7 rating,
        // it should actually be set to `4.5` rating.
        float stepAbidingRating = Double.valueOf(Math.floor(rating/mStepSize)).floatValue() * mStepSize;

        mRating = stepAbidingRating;

        if (mOnRatingChangeListener != null) {
            mOnRatingChangeListener.onRatingChange(this, mRating, fromUser);
        }

        fillRatingBar(mRating);
    }

    @Override
    public float getRating() {
        return mRating;
    }

    @Override
    // Unit is pixel
    public void setStarWidth(@IntRange(from = 0) int starWidth) {
        mStarWidth = starWidth;
        for (PartialView partialView : mPartialViews) {
            partialView.setStarWidth(starWidth);
        }
    }

    @Override
    public int getStarWidth() {
        return mStarWidth;
    }

    @Override
    // Unit is pixel
    public void setStarHeight(@IntRange(from = 0) int starHeight) {
        mStarHeight = starHeight;
        for (PartialView partialView : mPartialViews) {
            partialView.setStarHeight(starHeight);
        }
    }

    @Override
    public int getStarHeight() {
        return mStarHeight;
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
        Drawable drawable = ContextCompat.getDrawable(getContext(), res);
        if (drawable != null) {
            setEmptyDrawable(drawable);
        }
    }

    @Override
    public void setFilledDrawableRes(@DrawableRes int res) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), res);

        if (drawable != null) {
            setFilledDrawable(drawable);
        }
    }

    @Override
    public void setEmptyDrawable(@NonNull Drawable drawable) {
        mEmptyDrawable = drawable;
        for (PartialView partialView : mPartialViews) {
            partialView.setEmptyDrawable(drawable);
        }
    }

    @Override
    public void setFilledDrawable(@NonNull Drawable drawable) {
        mFilledDrawable = drawable;
        for (PartialView partialView : mPartialViews) {
            partialView.setFilledDrawable(drawable);
        }
    }

    @Override
    public void setMinimumStars(@FloatRange(from = 0.0) float minimumStars) {
        mMinimumStars = RatingBarUtils.getValidMinimumStars(minimumStars, mNumStars, mStepSize);
    }

    @Override
    public boolean isIndicator() {
        return mIsIndicator;
    }

    @Override
    public void setIsIndicator(boolean indicator) {
        mIsIndicator = indicator;
    }

    @Override
    public boolean isScrollable() {
        return mIsScrollable;
    }

    @Override
    public void setScrollable(boolean scrollable) {
        mIsScrollable = scrollable;
    }

    @Override
    public boolean isClickable() {
        return mIsClickable;
    }

    @Override
    public void setClickable(boolean clickable) {
        this.mIsClickable = clickable;
    }

    @Override
    public void setClearRatingEnabled(boolean enabled) {
        this.mClearRatingEnabled = enabled;
    }

    @Override
    public boolean isClearRatingEnabled() {
        return mClearRatingEnabled;
    }

    @Override
    public float getStepSize() {
        return mStepSize;
    }

    @Override
    public void setStepSize(@FloatRange(from = 0.1, to = 1.0) float stepSize) {
        this.mStepSize = stepSize;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isIndicator()) {
            return false;
        }

        float eventX = event.getX();
        float eventY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = eventX;
                mStartY = eventY;
                mPreviousRating = mRating;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isScrollable()) {
                    return false;
                }

                handleMoveEvent(eventX);
                break;
            case MotionEvent.ACTION_UP:
                if (!RatingBarUtils.isClickEvent(mStartX, mStartY, event) || !isClickable()) {
                    return false;
                }

                handleClickEvent(eventX);
        }

        getParent().requestDisallowInterceptTouchEvent(true);
        return true;
    }

    private void handleMoveEvent(float eventX) {
        for (PartialView partialView : mPartialViews) {
            if (eventX < partialView.getWidth() / 10f + (mMinimumStars * partialView.getWidth())) {
                setRating(mMinimumStars, true);
                return;
            }

            if (!isPositionInRatingView(eventX, partialView)) {
                continue;
            }

            float rating = RatingBarUtils.calculateRating(partialView, mStepSize, eventX);

            if (mRating != rating) {
                setRating(rating, true);
            }
        }
    }

    private void handleClickEvent(float eventX) {
        for (PartialView partialView : mPartialViews) {
            if (!isPositionInRatingView(eventX, partialView)) {
                continue;
            }

            float rating = mStepSize == 1 ? (int) partialView.getTag() : RatingBarUtils.calculateRating(partialView, mStepSize, eventX);

            if (mPreviousRating == rating && isClearRatingEnabled()) {
                setRating(mMinimumStars, true);
            } else {
                setRating(rating, true);
            }
            break;
        }
    }

    private boolean isPositionInRatingView(float eventX, View ratingView) {
        return eventX > ratingView.getLeft() && eventX < ratingView.getRight();
    }

    public void setOnRatingChangeListener(OnRatingChangeListener onRatingChangeListener) {
        mOnRatingChangeListener = onRatingChangeListener;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.setRating(mRating);
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        setRating(ss.getRating());
    }
}