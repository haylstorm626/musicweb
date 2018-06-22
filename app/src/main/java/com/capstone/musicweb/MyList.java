package com.capstone.musicweb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import kaaes.spotify.webapi.android.models.Track;

import static android.content.Context.MODE_PRIVATE;

public class MyList implements Serializable {

    private List<Track> mItems;
    private final static MyList myListInstance = new MyList();
    public static MyList getInstance() {
        return myListInstance;
    }

    private MyList() {
    }

    public void addTrack(Track item, Context context){
        if (mItems == null){
            mItems = new ArrayList<>();
        }
        mItems.add(item);
        save(context);
        //accessed from TrackAdapter and AlbumTrackAdapter
    }
    public void delete(Track item, Context context) {
        mItems.remove(item);
        save(context);
        //accessed from MyListActivity
    }
    public void save(Context context) {
        //save list
        SharedPreferences mPrefs = context.getSharedPreferences("theList", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mItems);
        prefsEditor.putString("myJson", json);
        prefsEditor.apply();
        //accessed from delete() and add()
    }
    public List<Track> load(Context context) {
        if (mItems == null){
            mItems = new ArrayList<>();
            return mItems;
        }
        SharedPreferences mPrefs = context.getSharedPreferences("theList", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("myJson", "");
        if (json.isEmpty()) {
            mItems = new ArrayList<>();
        } else {
            Type type = new TypeToken<List<Track>>() {}.getType();
            mItems = gson.fromJson(json, type);

        }

        return mItems;
    }
}
