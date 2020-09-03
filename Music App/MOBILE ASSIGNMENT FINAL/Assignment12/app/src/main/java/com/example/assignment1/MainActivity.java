package com.example.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button btnSave;
    private Button btnDisplayAll;
    private EditText etArtistName;
    private EditText etTrackName;
    private EditText etGenreName;
    private dbHelper databaseHelper;
    private RecyclerView mRecyclerView;
    private ArrayList<Artist> artistModelArrayList;
    private ArtistAdapter artistsCustomAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        databaseHelper = new dbHelper(this);
        mRecyclerView = findViewById(R.id.artistRecycler);

        artistModelArrayList = databaseHelper.getAllArtists();
        artistsCustomAdapter = new ArtistAdapter(this, artistModelArrayList);
        mRecyclerView.setAdapter(artistsCustomAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    public void btnNewArtistTrack(View view) {
        Intent intent = new Intent(this, NewArtistTrack.class);
        startActivity(intent);
    }


}

