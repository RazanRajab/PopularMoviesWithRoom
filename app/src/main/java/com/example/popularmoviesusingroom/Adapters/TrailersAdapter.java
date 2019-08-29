package com.example.popularmoviesusingroom.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviesusingroom.R;
import com.example.popularmoviesusingroom.TheMoviesAPI.Trailer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailersAdapter extends
        RecyclerView.Adapter<TrailersAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setTag(this);
            itemView.setOnClickListener(onTrailerClickListener);
            ButterKnife.bind(this, itemView);
        }
    }

    private List<Trailer> trailers;
    private View.OnClickListener onTrailerClickListener;
    private Context context;

    public TrailersAdapter(List<Trailer> trailers) {
        this.trailers = trailers;
    }

    public void setItemClickListener(View.OnClickListener clickListener) {
        onTrailerClickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View TaskView = inflater.inflate(R.layout.trailer_item, parent, false);

        return new ViewHolder(TaskView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Trailer n= trailers.get(position);
        holder.name.setText(n.getName());

    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }
}
