package com.willy.ratingbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by willy on 2017/5/5.
 */

public class BaseRatingBar extends LinearLayout implements SimpleRatingBar {

    public interface OnRatingChangeListener {
        void onRatingChange(BaseRatingBar ratingBar, int rating);
    }

    public static final String TAG = "SimpleRatingBar";

    public static final int MAX_CLICK_DURATION = 200;
    private static final int MAX_CLICK_DISTANCE = 5;

    private int mNumStars = 5;
    private int mRating = 0;
    private int mPreviousRating = 0;
    private int mPadding = 20;
    private int mStarWidth;
    private int mStarHeight;

    private float mStartX;
    private float mStartY;

    protected Drawable mEmptyDrawable;
    protected Drawable mFilledDrawable;

    private OnRatingChangeListener mOnRatingChangeListener;

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
        mStarWidth = typedArray.getDimensionPixelSize(R.styleable.RatingBarAttributes_starWidth, 0);
        mStarHeight = typedArray.getDimensionPixelSize(R.styleable.RatingBarAttributes_starHeight, 0);
        typedArray.recycle();

        if (mEmptyDrawable == null) {
            mEmptyDrawable = getResources().getDrawable(R.drawable.empty);
        }

        if (mFilledDrawable == null) {
            mFilledDrawable = getResources().getDrawable(R.drawable.filled);
        }

        initRatingView();
    }

    private void initRatingView() {
        mRatingViewStatus = new LinkedHashMap<>();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                mStarWidth == 0 ? LayoutParams.WRAP_CONTENT : mStarWidth,
                mStarHeight == 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : mStarHeight);

        for (int i = 1; i <= mNumStars; i++) {
            ImageView ratingView;
            if (i <= mRating) {
                ratingView = getRatingView(i, mFilledDrawable);
                mRatingViewStatus.put(ratingView, true);
            } else {
                ratingView = getRatingView(i, mEmptyDrawable);
                mRatingViewStatus.put(ratingView, false);
            }
            addView(ratingView, params);
        }

        setRating(mRating);
    }

    private ImageView getRatingView(final int ratingViewId, Drawable drawable) {
        ImageView imageView = new ImageView(getContext());
        imageView.setId(ratingViewId);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(mPadding, mPadding, mPadding, mPadding);
        imageView.setImageDrawable(drawable);
        return imageView;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        int rating;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = eventX;
                mStartY = eventY;
                mPreviousRating = mRating;
                modifyRating(eventX);
                break;
            case MotionEvent.ACTION_MOVE:
                modifyRating(eventX);
                break;
            case MotionEvent.ACTION_UP:
                if (!isClickEvent(mStartX, eventX, mStartY, eventY)) {
                    return false;
                }

                for (final ImageView view : mRatingViewStatus.keySet()) {
                    if (!isPositionInRatingView(eventX, view)) {
                        continue;
                    }

                    rating = view.getId();
                    if (mPreviousRating == rating) {
                        clearRating();
                    } else {
                        setRating(view.getId());
                    }
                    break;
                }
        }

        return true;
    }

    private void modifyRating(float eventX) {
        for (final ImageView view : mRatingViewStatus.keySet()) {

            if (eventX < view.getWidth() / 2f) {
                setRating(0);
                return;
            }

            if (isPositionInRatingView(eventX, view)) {
                int rating = view.getId();
                setRating(rating);
            }
        }
    }

    private boolean isPositionInRatingView(float eventX, View ratingView) {
        return eventX > ratingView.getX() && eventX < ratingView.getX() + ratingView.getWidth();
    }

    private void removeAllRatingViews() {
        mRatingViewStatus.clear();
        removeAllViews();
    }

    private void clearRating() {
        mRating = 0;
        if (mOnRatingChangeListener != null) {
            mOnRatingChangeListener.onRatingChange(BaseRatingBar.this, 0);
        }
        emptyRatingBar();
    }

    private boolean isClickEvent(float startX, float endX, float startY, float endY) {
        float differenceX = Math.abs(startX - endX);
        float differenceY = Math.abs(startY - endY);
        return !(differenceX > MAX_CLICK_DISTANCE || differenceY > MAX_CLICK_DISTANCE);
    }

    /**
     * Retain this method to let other RatingBar can custom their decrease animation.
     */
    protected void emptyRatingBar() {
        fillRatingBar(0);
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
        if (!hasRatingViews()) {
            return;
        }

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
    public int getRating() {
        return mRating;
    }

    @Override
    public void setStarPadding(int ratingPadding) {
        if (ratingPadding < 0) {
            return;
        }

        if (!hasRatingViews()) {
            return;
        }

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

        if (!hasRatingViews()) {
            return;
        }

        changeRatingViewDrawable(mEmptyDrawable);

    }

    @Override
    public void setEmptyDrawableRes(@DrawableRes int res) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setEmptyDrawable(getContext().getDrawable(res));
        } else {
            setEmptyDrawable(getContext().getResources().getDrawable(res));
        }
    }

    @Override
    public void setFilledDrawable(Drawable drawable) {
        mFilledDrawable = drawable;

        if (!hasRatingViews()) {
            return;
        }

        changeRatingViewDrawable(mFilledDrawable);
    }

    @Override
    public void setFilledDrawableRes(@DrawableRes int res) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setFilledDrawable(getContext().getDrawable(res));
        } else {
            setFilledDrawable(getContext().getResources().getDrawable(res));
        }
    }

    private void changeRatingViewDrawable(Drawable drawable) {
        for (Map.Entry<ImageView, Boolean> entry : mRatingViewStatus.entrySet()) {
            if (entry.getValue()) {
                entry.getKey().setImageDrawable(drawable);
            }
        }
    }

    private boolean hasRatingViews() {
        return mRatingViewStatus != null && mRatingViewStatus.size() > 0;
    }

    public void setOnRatingChangeListener(OnRatingChangeListener onRatingChangeListener) {
        mOnRatingChangeListener = onRatingChangeListener;
    }
}