package com.shabayekdes.popcine.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shabayekdes.popcine.R;
import com.squareup.picasso.Picasso;
import com.shabayekdes.popcine.models.Trailer;
import com.shabayekdes.popcine.utilities.NetworkUtils;

import java.util.List;


public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {

    private List<Trailer> trailers;
    private Context context;
    private TrailerClickHandler trailerClickHandler;

    public TrailersAdapter(Context context, TrailerClickHandler trailerClickHandler) {
        this.context = context;
        this.trailerClickHandler = trailerClickHandler;
    }



    public interface TrailerClickHandler{
        void onClickHandler(Trailer trailer);
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        Trailer trailer = trailers.get(position);
        holder.trailerNameTv.setText(trailer.getName());
        Uri uri = NetworkUtils.youTubeImageUrlBuilder(trailer);
        Picasso.with(context)
                .load(uri)
                .into(holder.trailerIv);
    }

    @Override
    public int getItemCount() {
        if (trailers != null){
            return trailers.size();
        }else {
            return 0;
        }
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView trailerNameTv;
        private ImageView trailerIv;
        public TrailerViewHolder(View itemView) {
            super(itemView);
            trailerNameTv = itemView.findViewById(R.id.trailer_name_tv);
            trailerIv = itemView.findViewById(R.id.trailer_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Trailer trailer = trailers.get(position);
            trailerClickHandler.onClickHandler(trailer);
        }
    }

    public void setTrailers(List<Trailer> trailers){
        this.trailers = trailers;
        notifyDataSetChanged();
    }
}
