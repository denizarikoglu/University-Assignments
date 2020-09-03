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

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>{
    private ArrayList<Artist> artistArrayList;
    private LayoutInflater mInflater;
    public ArtistAdapter(Context context, ArrayList<Artist>artistList){
        mInflater = LayoutInflater.from(context);
        this.artistArrayList = artistList;
    }
    @NonNull
    @Override
    public ArtistAdapter.ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_listview_artist, parent, false);
        return new ArtistViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistAdapter.ArtistViewHolder holder, int position) {
        String mCurrent = String.valueOf(artistArrayList.get(position));
        holder.artistItemView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return artistArrayList.size();
    }

    public class ArtistViewHolder extends RecyclerView.ViewHolder {

        public final TextView artistItemView;
        final ArtistAdapter mAdapter;
        public ArtistViewHolder(View itemView, ArtistAdapter adapter){
            super(itemView);
            artistItemView = itemView.findViewById(R.id.artistName);
            this.mAdapter = adapter;
            artistItemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int position = getLayoutPosition();
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DeleteUpdateArtistActivity.class);
                    Artist artist = artistArrayList.get(position);
                    intent.putExtra("artist", artist);
                    context.startActivity(intent);
                }
            });
        }
    }
}