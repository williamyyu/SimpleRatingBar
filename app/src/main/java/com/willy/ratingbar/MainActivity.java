package com.willy.ratingbar;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.layout);

        final SimpleRatingBar ratingBar = (SimpleRatingBar) findViewById(R.id.simpleRatingBar);

        final android.widget.RatingBar ratingBar1 = new RatingBar(this);
        ratingBar1.setNumStars(8);

        SimpleRatingBar ratingBar2 = new SimpleRatingBar(this);
        ratingBar2.setNumStars(8);
        ratingBar2.setRating(2);
        constraintLayout.addView(ratingBar1);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentRating = ratingBar.getRating();
                ratingBar.setRating(currentRating + 1);

                ratingBar1.setNumStars(3);
            }
        });
    }
}
