package com.example.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class DeleteUpdateTrackActivity extends AppCompatActivity {

    private Track track;
    private EditText  etTrackName, etGenreName;
    private dbHelper databaseHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_update_track);

        Intent intent = getIntent();
        track = (Track) intent.getSerializableExtra("track");
        databaseHelper = new dbHelper(this);
        etTrackName = (EditText) findViewById(R.id.etTrackName);
        etGenreName = (EditText) findViewById(R.id.etGenreName);
        etGenreName.setText(track.getGenreName());
        etTrackName.setText(track.getTrackName());
    }
    public void btnUpdateOnClickt(View view){

        databaseHelper.updateTracks(track.getId(), etGenreName.getText().toString(),etTrackName.getText().toString());
        Toast.makeText(DeleteUpdateTrackActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(DeleteUpdateTrackActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void btnDeleteOnClickt(View view){

        databaseHelper.deleteTracks(track.getId());
        Toast.makeText(DeleteUpdateTrackActivity.this , "Deleted Successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(DeleteUpdateTrackActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
