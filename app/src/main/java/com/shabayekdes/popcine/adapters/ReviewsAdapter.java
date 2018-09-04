package com.shabayekdes.popcine.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shabayekdes.popcine.R;
import com.shabayekdes.popcine.models.Review;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {
    List<Review> reviews;

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.authorTv.setText(review.getAuthor());
        holder.contentTv.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        if (reviews==null) {
            return 0;
        }else {
            return reviews.size();
        }
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        private TextView authorTv;
        private TextView contentTv;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            authorTv = itemView.findViewById(R.id.author_tv);
            contentTv = itemView.findViewById(R.id.content_tv);
        }
    }

    public void setReviews(List<Review> reviews){
        this.reviews = reviews;
        notifyDataSetChanged();
    }
}
