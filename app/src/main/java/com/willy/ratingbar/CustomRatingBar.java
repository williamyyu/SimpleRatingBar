package com.willy.ratingbar;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by willy on 2017/5/5.
 */

public class CustomRatingBar extends LinearLayout {

    private static final int FILLED_STAR = 111;
    private static final int EMPTY_STAR = 222;

    private int mStarNums = 5;
    private int mRating = 0;

    private Handler mThreadHandler;
    private HandlerThread mThread;

    private SparseArray<ImageView> mImageViews;
    private SparseBooleanArray mImageStatus;

    public CustomRatingBar(Context context) {
        super(context);
        init();
    }

    public CustomRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mThread = new HandlerThread("test");
        mThread.start();
        mThreadHandler = new Handler(mThread.getLooper());

        mImageViews = new SparseArray<>();
        mImageStatus = new SparseBooleanArray();

        for (int i = 0; i < mStarNums; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setPadding(20, 20, 20, 20);
            imageView.setId(i);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.start_empty));
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int id = v.getId();
                    mThreadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mRating != id) {
                                mRating = id;
                                changeRatingBar(id);
                            } else {
                                //clear rating
                                mRating = -1;
                                for (int k = 0; k < mStarNums; k++) {
                                    Message message = new Message();
                                    message.arg1 = k;
                                    message.arg2 = EMPTY_STAR;
                                    sUiHandler.sendMessage(message);
                                    mImageStatus.put(k, false);
                                }
                            }
                        }
                    });
                }
            });
            addView(imageView);

            mImageViews.put(i, imageView);
            mImageStatus.put(i, false);
        }
    }

    private void changeRatingBar(int id) {
        if (mImageStatus.get(id) == true) {
            for (int i = mStarNums - 1; i >= 0; i--) {
                processImageStatus(i, id);
            }
        } else {
            for (int i = 0; i < mStarNums; i++) {
                processImageStatus(i, id);
            }
        }
    }

    private void processImageStatus(int index, int id) {
        Message message = new Message();
        message.arg1 = index;

        if (index <= id) {
            message.arg2 = FILLED_STAR;
            sUiHandler.sendMessage(message);
            mImageStatus.put(index, true);
        } else {
            message.arg2 = EMPTY_STAR;
            sUiHandler.sendMessage(message);
            mImageStatus.put(index, false);
        }

        try {
            Thread.sleep(40);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Handler sUiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
            Animation scaleDown = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);
            switch (msg.arg2) {
                case FILLED_STAR:
                    mImageViews.get(msg.arg1).setImageDrawable(getResources().getDrawable(R.drawable.star_filled));
                    mImageViews.get(msg.arg1).startAnimation(scaleUp);
                    mImageViews.get(msg.arg1).startAnimation(scaleDown);
                    break;
                case EMPTY_STAR:
                    mImageViews.get(msg.arg1).setImageDrawable(getResources().getDrawable(R.drawable.start_empty));
                    break;
            }
        }
    };

}

