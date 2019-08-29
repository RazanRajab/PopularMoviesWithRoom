package com.example.popularmoviesusingroom.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviesusingroom.R;
import com.example.popularmoviesusingroom.TheMoviesAPI.Review;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsAdapter extends
        RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.author)
        TextView author;
        @BindView(R.id.review)
        TextView content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setTag(this);
            itemView.setOnClickListener(onReviewClickListener);
            ButterKnife.bind(this, itemView);
        }
    }

    private List<Review> reviews;
    private View.OnClickListener onReviewClickListener;
    private Context context;

    public ReviewsAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void setItemClickListener(View.OnClickListener clickListener) {
        onReviewClickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View TaskView = inflater.inflate(R.layout.single_review, parent, false);

        return new ViewHolder(TaskView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.ViewHolder holder, int position) {

        Review r= reviews.get(position);
        holder.author.setText(r.getAuthor());
        holder.content.setText(r.getContent());

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}
