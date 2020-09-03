package com.example.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;

public class DeleteUpdateArtistActivity extends AppCompatActivity {
    private Artist artist;
    private RecyclerView mRecyclerView;
    private ArrayList<Track> trackArrayList = new ArrayList<>();
    private TrackAdapter trackAdapter;
    private EditText etArtistName, etTrackName;
    private dbHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_update_artist);

        Intent intent = getIntent();
        artist = (Artist) intent.getSerializableExtra("artist");

        databaseHelper = new dbHelper(this);
        ArrayList<Track> tracks = databaseHelper.getAllTracks(artist.getId());
        trackArrayList.addAll(tracks);
        trackAdapter = new TrackAdapter(this, trackArrayList);
        mRecyclerView = findViewById(R.id.trackRecycler);
        mRecyclerView.setAdapter(trackAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        etArtistName = (EditText) findViewById(R.id.etArtistName);
        etTrackName = (EditText) findViewById(R.id.etTrackName);
        etArtistName.setText(artist.getArtistname());
        etTrackName.setText(artist.getTrackName());
    }
    public void btnUpdateOnClick(View view){
        databaseHelper.updateArtists(artist.getId(),etArtistName.getText().toString(),etTrackName.getText().toString());
        Toast.makeText(DeleteUpdateArtistActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(DeleteUpdateArtistActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void btnDeleteOnClick(View view){
        databaseHelper.deleteArtist(artist.getId());
        Toast.makeText(DeleteUpdateArtistActivity.this , "Deleted Successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(DeleteUpdateArtistActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
