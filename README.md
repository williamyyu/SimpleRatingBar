# SimpleRatingBar
[![JitPack](https://jitpack.io/v/ome450901/SimpleRatingBar.svg)](https://jitpack.io/#ome450901/SimpleRatingBar)

There are two RatingBars provided:
1. BaseRatingBar 
A RatingBar without any animation.
2. ScaleRatingBar
A RatingBar with progressive and scale animation.

## Demo
![](images/demo.gif)  
Icon made by [Freepik](http://www.freepik.com/) from www.flaticon.com 

## Feature
- Scale animation
- Custom drawable's padding
- Custom your empty and filled drawable
- Click again to clear rating

## How To Use
Using Gradle from JitPack:

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    compile 'com.github.ome450901:SimpleRatingBar:1.0'
}
```

## Usages

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
ratingBar.setEmptyDrawable(getResources().getDrawable(R.drawable.start_empty));
ratingBar.setFilledDrawable(getResources().getDrawable(R.drawable.start_empty));
```

## Todo
- Use touch event to change rating
- Implement some other animations
- Find a better way to implement animation
