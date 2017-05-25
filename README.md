# SimpleRatingBar
[![JitPack](https://jitpack.io/v/ome450901/SimpleRatingBar.svg)](https://jitpack.io/#ome450901/SimpleRatingBar)

>This is a very simple RatingBar library, which you can just simply extend BaseRatingBar to implement your own animation RatingBar in a few steps!

Current we already have three RatingBars :
- BaseRatingBar  
    A RatingBar without any animation.
- ScaleRatingBar  
    A RatingBar with progressive and scale animation.
- RotationRatingBar (contributed by [nappannda](https://github.com/nappannda))  
    A RatingBar with progressive and rotate animation.

## Demo
![](images/demo.gif)  
Icon made by [Freepik](http://www.freepik.com/) from www.flaticon.com 

## Feature
- Support use touch to change rating
- Custom drawable's padding
- Custom your empty and filled drawable
- Click again to clear rating
- Rotate animation
- Scale animation

## How To Use
### Install
from JitPack:

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    compile 'com.github.ome450901:SimpleRatingBar:1.1.2'
}
```


### In Xml
```xml
<com.willy.ratingbar.ScaleRatingBar
        xmlns:rb="http://schemas.android.com/apk/res-auto"
        android:id="@+id/simpleRatingBar5"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        rb:numStars="3"
        rb:rating="2"
        rb:starPadding="15"
        rb:drawableEmpty="@drawable/start_empty"
        rb:drawableFilled="@drawable/star_filled">
</com.willy.ratingbar.ScaleRatingBar>
```

### In Your Code
```java
ScaleRatingBar ratingBar = new ScaleRatingBar(this);
ratingBar.setNumStars(5);
ratingBar.setRating(3);
ratingBar.setStarPadding(10);
ratingBar.setEmptyDrawableRes(R.drawable.start_empty);
ratingBar.setFilledDrawableRes(R.drawable.start_empty);
ratingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
    @Override
        public void onRatingChange(BaseRatingBar ratingBar, int rating) {
            Log.e(TAG, "onRatingChange: " + rating);
    }
});
```

## Want to Implement Your Own Animation?
#### Only 2 Steps you need to do:
- Create a class that extend `BaseRatingBar`
- Override the `emptyRatingBar` and `fillRatingBar` this two method, and then you can start implement your own animaion!

>You can follow [ScaleRatingBar](https://github.com/ome450901/SimpleRatingBar/blob/master/library/src/main/java/com/willy/ratingbar/ScaleRatingBar.java) to implement your own class.

## Todo
- Support float rating
- Implement some other animations
- Find a better way to implement animation

## About Me
Welcome to follow me on [Medium](https://medium.com/@ome450901).
