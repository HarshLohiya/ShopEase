package com.example.newEcom.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newEcom.R;
import com.example.newEcom.model.CartItemModel;
import com.example.newEcom.model.ReviewModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;

public class ReviewAdapter extends FirestoreRecyclerAdapter<ReviewModel, ReviewAdapter.ReviewViewHolder> {
    private Context context;
    private AppCompatActivity activity;

    float totalRating = 0;
    boolean gotRating = false;

    public ReviewAdapter(@NonNull FirestoreRecyclerOptions<ReviewModel> options, Context context){
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review_adapter,parent,false);
        activity = (AppCompatActivity) view.getContext();
        return new ReviewViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ReviewAdapter.ReviewViewHolder holder, int position, @NonNull ReviewModel model) {
//        if (position == 0 && !gotRating) {
//            calculateTotalPrice();
//        }

        holder.nameTextView.setText(model.getName());
        Timestamp timestamp = model.getTimestamp();
        String date = new SimpleDateFormat("dd MMMM yyyy").format(timestamp.toDate());
        holder.dateTextView.setText(date);
        holder.ratingBar.setRating(model.getRating());
        holder.titleTextView.setText(model.getTitle());
        holder.reviewTextView.setText(model.getReview());
    }

//    private void calculateTotalPrice() {
//        gotRating = true;
//        int n = getItemCount();
//        for (ReviewModel model : getSnapshots()) {
//            totalRating += model.getRating();
//        }
//        Intent intent = new Intent("rating");
//        intent.putExtra("rating", totalRating/n);
//        intent.putExtra("noOfRating", n);
//        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
//    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, dateTextView, titleTextView, reviewTextView;
        RatingBar ratingBar;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            reviewTextView = itemView.findViewById(R.id.reviewTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
