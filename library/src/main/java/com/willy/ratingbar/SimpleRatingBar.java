package com.willy.ratingbar;

import android.graphics.drawable.Drawable;

/**
 * Created by willy on 2017/5/10.
 */

public interface SimpleRatingBar {

    void setNumStars(int numStars);

    int getNumStars();

    void setRating(int rating);

    int getRating();

    void setStarPadding(int ratingPadding);

    int getStarPadding();

    void setEmptyDrawable(Drawable drawable);

    void setFilledDrawable(Drawable drawable);

}
