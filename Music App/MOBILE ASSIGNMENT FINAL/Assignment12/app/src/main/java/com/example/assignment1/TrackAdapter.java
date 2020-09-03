package com.example.assignment1;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<Track> trackArrayList;
    public TrackAdapter(Context context, ArrayList<Track> trackList){
        mInflater = LayoutInflater.from(context);
        this.trackArrayList = trackList;
    }

    @NonNull
    @Override
    public TrackAdapter.TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_listview_track, parent, false);
        return new TrackViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackAdapter.TrackViewHolder holder, int position) {
        Track mCurrent = trackArrayList.get(position);
        holder.trackItemView.setText(mCurrent.getTrackName());
        holder.genreItemView.setText(mCurrent.getGenreName());
    }
    public class TrackViewHolder extends RecyclerView.ViewHolder{
        public final TextView trackItemView;
        public final TextView genreItemView;
        final TrackAdapter mAdapter;
        public TrackViewHolder(@NonNull View itemView, TrackAdapter adapter) {
            super(itemView);
            trackItemView = itemView.findViewById(R.id.trackName);
            genreItemView = itemView.findViewById(R.id.genreName);
            this.mAdapter = adapter;
            trackItemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DeleteUpdateTrackActivity.class);
                    Track track = trackArrayList.get(position);
                    intent.putExtra("track", track);
                    context.startActivity(intent);
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return trackArrayList.size();
    }
}
