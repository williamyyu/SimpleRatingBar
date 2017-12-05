package com.willy.ratingbar;

import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by willy on 2017/6/3.
 */

class PartialView extends RelativeLayout {

    private ImageView mFilledView;
    private ImageView mEmptyView;
    private int mStarWidth = 0;
    private int mStarHeight = 0;

    public PartialView(Context context, int starWidth, int startHeight) {
        super(context);
        mStarWidth = starWidth;
        mStarHeight = startHeight;
        init();
    }

    public PartialView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PartialView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                mStarWidth == 0 ? LayoutParams.WRAP_CONTENT : mStarWidth,
                mStarHeight == 0 ? LayoutParams.WRAP_CONTENT : mStarHeight);

        mFilledView = new ImageView(getContext());
        mFilledView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(mFilledView, params);

        mEmptyView = new ImageView(getContext());
        mEmptyView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(mEmptyView, params);

        setEmpty();
    }

    public void setFilledDrawable(Drawable drawable) {
        if (drawable.getConstantState() == null) {
            return;
        }

        ClipDrawable clipDrawable = new ClipDrawable(drawable.getConstantState().newDrawable(), Gravity.START, ClipDrawable.HORIZONTAL);
        mFilledView.setImageDrawable(clipDrawable);
    }

    public void setEmptyDrawable(Drawable drawable) {
        if (drawable.getConstantState() == null) {
            return;
        }

        ClipDrawable clipDrawable = new ClipDrawable(drawable.getConstantState().newDrawable(), Gravity.END, ClipDrawable.HORIZONTAL);
        mEmptyView.setImageDrawable(clipDrawable);
    }

    public void setFilled() {
        mFilledView.setImageLevel(10000);
        mEmptyView.setImageLevel(0);
    }

    public void setPartialFilled(float rating) {
        float percentage = rating % 1;
        int level = (int) (10000 * percentage);
        level = level == 0 ? 10000 : level;
        mFilledView.setImageLevel(level);
        mEmptyView.setImageLevel(10000 - level);
    }

    public void setEmpty() {
        mFilledView.setImageLevel(0);
        mEmptyView.setImageLevel(10000);
    }

}
