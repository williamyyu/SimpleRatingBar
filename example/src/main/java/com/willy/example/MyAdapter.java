package com.willy.example;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willy on 2017/10/3.
 */

public class MyAdapter
        extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context mContext;
    private List<Float> list;

    public MyAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();

        for (int i = 1; i < 20; i++) {
            list.add(i % 6f);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_ratingbar, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.ratingBar.setTag(position);
        holder.ratingBar.setRating(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder
            extends RecyclerView.ViewHolder {

        private ScaleRatingBar ratingBar;

        public MyViewHolder(View itemView) {
            super(itemView);
//            ratingBar = itemView.findViewById(R.id.ratingBar);
//            ratingBar = itemView.findViewById(R.id.ratingBar);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            ratingBar.setOnRatingChangeListener((ratingBar, rating, fromUser) -> {
                int position = (int) ratingBar.getTag();
                list.set(position, rating);
            });
        }
    }
}
