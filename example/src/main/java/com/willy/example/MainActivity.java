package com.willy.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;
import com.willy.ratingbar.RotationRatingBar;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "SimpleRatingBar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ScaleRatingBar scaleRatingBar = (ScaleRatingBar) findViewById(R.id.simpleRatingBar);
        final BaseRatingBar baseRatingBar = (BaseRatingBar) findViewById(R.id.baseRatingBar);
        final RotationRatingBar rotationRatingBar = (RotationRatingBar) findViewById(R.id.rotationRatingBar);

        scaleRatingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, int rating) {
                Log.e(TAG, "onRatingChange: " + rating);
            }
        });

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentRating = scaleRatingBar.getRating();
                scaleRatingBar.setRating(currentRating + 1);

                currentRating = baseRatingBar.getRating();
                baseRatingBar.setRating(currentRating + 1);

                currentRating = rotationRatingBar.getRating();
                rotationRatingBar.setRating(currentRating + 1);
            }
        });
    }
}
