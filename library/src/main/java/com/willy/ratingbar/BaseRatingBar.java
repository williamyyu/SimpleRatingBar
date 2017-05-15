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

    public interface OnRatingChangeListener {
        void onRatingChange(BaseRatingBar ratingBar, int rating);
    }

    public static final String TAG = "SimpleRatingBar";

    protected int mNumStars = 5;
    protected int mRating = 0;
    protected int mPadding = 20;

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

                if (!hasRatingViews()) {
                    return;
                }

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

    private void clearRating() {
        mRating = 0;
        if (mOnRatingChangeListener != null) {
            mOnRatingChangeListener.onRatingChange(BaseRatingBar.this, 0);
        }
        emptyRatingBar();
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
        mPadding = ratingPadding;

        if (!hasRatingViews()) {
            return;
        }

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

        for (final ImageView view : mRatingViewStatus.keySet()) {
            if (!mRatingViewStatus.get(view)) {
                view.setImageDrawable(drawable);
            }
        }
    }

    @Override
    public void setFilledDrawable(Drawable drawable) {
        mFilledDrawable = drawable;

        if (!hasRatingViews()) {
            return;
        }

        for (final ImageView view : mRatingViewStatus.keySet()) {
            if (mRatingViewStatus.get(view)) {
                view.setImageDrawable(drawable);
            }
        }
    }

    protected boolean hasRatingViews() {
        return mRatingViewStatus != null && mRatingViewStatus.size() > 0;
    }

    public void setOnRatingChangeListener(OnRatingChangeListener onRatingChangeListener) {
        mOnRatingChangeListener = onRatingChangeListener;
    }
}