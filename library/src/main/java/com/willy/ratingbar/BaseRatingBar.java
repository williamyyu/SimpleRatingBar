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

    protected int mNumStars = 5;
    protected int mRating = 0;
    protected int previewRating = 0;
    protected int mPadding = 20;

    protected Drawable mEmptyDrawable;
    protected Drawable mFilledDrawable;

    private OnRatingChangeListener mOnRatingChangeListener;

    protected Map<ImageView, Boolean> mRatingViewStatus;
    protected Map<ImageView, Float> mRatingViewPosition;

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
        addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                initRatingViewPosition();
            }

            @Override
            public void onViewDetachedFromWindow(View v) {

            }
        });
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
        int rating;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                previewRating = mRating;
                break;
            case MotionEvent.ACTION_MOVE:
                for (final ImageView view : mRatingViewStatus.keySet()) {
                    int width = view.getWidth();

                    if (eventX < width / 2) {
                        setRating(0);
                    } else if (eventX > view.getX() && eventX < view.getX() + width) {
                        rating = view.getId();
                        setRating(rating);
                    }
                }
                return false;
            case MotionEvent.ACTION_UP:
                float duration = event.getEventTime() - event.getDownTime();
                if (duration < MAX_CLICK_DURATION) {
                    // Single click event
                    for (final ImageView view : mRatingViewStatus.keySet()) {
                        int width = view.getWidth();
                        if (eventX > view.getX() && eventX < view.getX() + width) {
                            rating = view.getId();
                            if (previewRating == rating) {
                                clearRating();
                                break;
                            }
                            setRating(view.getId());
                        }
                    }
                } else {
                    // Is a move event
                }

                break;
        }
        return true;
    }

    private void initRatingViewPosition() {
        if (mRatingViewPosition == null) {
            mRatingViewPosition = new LinkedHashMap<>();
        }

        for (final ImageView view : mRatingViewStatus.keySet()) {
            mRatingViewPosition.put(view, view.getX());
        }
    }

    private void removeAllRatingViews() {
        mRatingViewStatus.clear();
        mRatingViewPosition.clear();
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

        for (final ImageView view : mRatingViewStatus.keySet()) {
            if (!mRatingViewStatus.get(view)) {
                view.setImageDrawable(drawable);
            }
        }
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

        for (final ImageView view : mRatingViewStatus.keySet()) {
            if (mRatingViewStatus.get(view)) {
                view.setImageDrawable(drawable);
            }
        }
    }

    @Override
    public void setFilledDrawableRes(@DrawableRes int res) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setFilledDrawable(getContext().getDrawable(res));
        } else {
            setFilledDrawable(getContext().getResources().getDrawable(res));
        }
    }

    protected boolean hasRatingViews() {
        return mRatingViewStatus != null && mRatingViewStatus.size() > 0;
    }

    public void setOnRatingChangeListener(OnRatingChangeListener onRatingChangeListener) {
        mOnRatingChangeListener = onRatingChangeListener;
    }
}