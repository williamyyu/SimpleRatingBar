package com.willy.ratingbar;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;

/**
 * Created by willy on 2017/5/10.
 */
interface SimpleRatingBar {

    void setNumStars(int numStars);

    int getNumStars();

    void setRating(float rating);

    float getRating();

    void setStarWidth(@IntRange(from = 0) int starWidth);

    int getStarWidth();

    void setStarHeight(@IntRange(from = 0) int starHeight);

    int getStarHeight();

    void setStarPadding(int ratingPadding);

    int getStarPadding();

    void setEmptyDrawable(Drawable drawable);

    void setEmptyDrawableRes(@DrawableRes int res);

    void setFilledDrawable(Drawable drawable);

    void setFilledDrawableRes(@DrawableRes int res);

    void setMinimumStars(@FloatRange(from = 0.0) float minimumStars);

    boolean isIndicator();

    void setIsIndicator(boolean indicator);

    boolean isScrollable();

    void setScrollable(boolean scrollable);

    boolean isClickable();

    void setClickable(boolean clickable);

    void setClearRatingEnabled(boolean enabled);

    boolean isClearRatingEnabled();

    float getStepSize();

    void setStepSize(@FloatRange(from = 0.1, to = 1.0) float stepSize);


}
