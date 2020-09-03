package com.example.assignment1;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NewArtistTrack extends AppCompatActivity {
    private Button btnSave;
    private Button btnDisplayAll;
    private dbHelper databaseHelper;
    private RecyclerView mRecyclerView;
    private ArrayList<Artist> artistModelArrayList;
    private ArtistAdapter artistsCustomAdapter;
    private EditText etArtistName;
    private EditText etTrackName;
    private EditText etGenreName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_artist_track);

        databaseHelper = new dbHelper(this);

        etArtistName = (EditText) findViewById(R.id.etArtistName);
        etTrackName = (EditText) findViewById(R.id.etTrackName);
        etGenreName = (EditText) findViewById(R.id.etGenreName);
    }
    public void btnSaveOnClick(View view){
        long id = databaseHelper.addArtist(etArtistName.getText().toString(), etTrackName.getText().toString());

        databaseHelper.addTrack(etTrackName.getText().toString(), etGenreName.getText().toString(), Math.toIntExact(id));
        etTrackName.setText("");
        etArtistName.setText("");
        etGenreName.setText("");
        Toast.makeText(NewArtistTrack.this, "Stored Successfully!", Toast.LENGTH_SHORT).show();
    }
    public void btnDisplayAllOnClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
