package com.example.assignment1;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class dbHelper extends SQLiteOpenHelper{

    public static  String DATABASE_NAME = "artist_database.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLE_ARTIST = "artist";
    private static final String KEY_ID = "_id";
    private static final String KEY_ARTISTNAME_COLUMN = "ARTISTNAME_COLUMN";
    private static final String KEY_TRACKNAME_COLUMN = "TRACKNAME_COLUMN";
    private static final String CREATE_DATABASE_TABLE_ARTIST = "CREATE TABLE IF NOT EXISTS "
            + DATABASE_TABLE_ARTIST +"(" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ARTISTNAME_COLUMN + " TEXT,"
            + KEY_TRACKNAME_COLUMN + " TEXT );";


    private static final String DATABASE_TABLE_TRACK = "track";
    private static final String KEY_IDT = "_id";
    private static final String KEY_TRACKNAME_COLUMNT = "TRACKNAMET_COLUMN";
    private static final String KEY_GENRE_COLUMN = "GENRE_COLUMN";
    private static final String CREATE_DATABASE_TABLE_TRACK = "CREATE TABLE IF NOT EXISTS "
            + DATABASE_TABLE_TRACK +"(" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TRACKNAME_COLUMNT + " TEXT,"
            + KEY_GENRE_COLUMN + " TEXT, _fid TEXT, FOREIGN KEY (\"_fid\") REFERENCES \"artist\"(\"_id\") ON UPDATE CASCADE);";

    public dbHelper(Context context){ super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_DATABASE_TABLE_ARTIST);
        db.execSQL(CREATE_DATABASE_TABLE_TRACK);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS '" + DATABASE_TABLE_ARTIST + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + DATABASE_TABLE_TRACK + "'");
        onCreate(db);
    }
    public long addArtist(String artistName, String trackName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_ARTISTNAME_COLUMN, artistName);
        newValues.put(KEY_TRACKNAME_COLUMN, trackName);
        return db.insert(DATABASE_TABLE_ARTIST, null, newValues);
    }


    public void addTrack(String trackName, String genreName, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_GENRE_COLUMN, genreName);
        newValues.put(KEY_TRACKNAME_COLUMNT, trackName);
        newValues.put("_fid", id);
        db.insert(DATABASE_TABLE_TRACK, null, newValues);
    }
    public ArrayList<Artist>getAllArtists(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Artist>artistsModelArrayList = new ArrayList<Artist>();
        String selectQuery = "SELECT * FROM " + DATABASE_TABLE_ARTIST;
        Cursor cursor = db.rawQuery(selectQuery, null);
        while(cursor.moveToNext()){
            Artist artist = new Artist();
            artist.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            artist.setArtistName((cursor.getString(cursor.getColumnIndex(KEY_ARTISTNAME_COLUMN))));
            artist.setTrackName(cursor.getString(cursor.getColumnIndex(KEY_TRACKNAME_COLUMN)));
                artistsModelArrayList.add(artist);
        }
        return artistsModelArrayList;
    }
    public ArrayList<Track>getAllTracks(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Track> trackModelArrayList = new ArrayList<Track>();
        String selectQuery = "SELECT * FROM " + DATABASE_TABLE_TRACK + " WHERE _fid=" + id + ";";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Track track = new Track();
                track.setId(cursor.getInt(cursor.getColumnIndex(KEY_IDT)));
                track.setGenreName((cursor.getString(cursor.getColumnIndex(KEY_GENRE_COLUMN))));
                track.setTrackName(cursor.getString(cursor.getColumnIndex(KEY_TRACKNAME_COLUMNT)));
                trackModelArrayList.add(track);
            } while (cursor.moveToNext());
        }

        return trackModelArrayList;
    }
    public void updateArtists(int id, String artistName, String trackName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(KEY_ARTISTNAME_COLUMN, artistName);
        updatedValues.put(KEY_TRACKNAME_COLUMN, trackName);
        String where = KEY_ID + "= ?";
        String whereArgs[] = new String[]{String.valueOf(id)};
        db.update(DATABASE_TABLE_ARTIST, updatedValues, where, whereArgs);
    }
    public void updateTracks(int id, String genreName, String trackName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(KEY_TRACKNAME_COLUMNT, trackName);
        updatedValues.put(KEY_GENRE_COLUMN, genreName);
        String where = KEY_ID + "= ?";
        String whereArgs[] = new String[]{String.valueOf(id)};
        db.update(DATABASE_TABLE_TRACK, updatedValues, where, whereArgs);
    }
    public void deleteArtist(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE_ARTIST, KEY_ID+ " =?", new String[]{String.valueOf(id)});
    }
    public void deleteTracks(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE_TRACK, KEY_ID+ " =?", new String[]{String.valueOf(id)});
    }
}
