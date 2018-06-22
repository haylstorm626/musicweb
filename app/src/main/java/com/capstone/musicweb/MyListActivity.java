package com.capstone.musicweb;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class MyListActivity extends BaseMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        MyList list = MyList.getInstance();
        final ListView theListView = (ListView) findViewById(R.id.listMy);

        View empty = findViewById(R.id.textNoneSaved);
        theListView.setEmptyView(empty);

        final List<Track> dankList = list.load(getApplicationContext()); //= LOAD LIST

        MyListAdapter adapter = new MyListAdapter(this, dankList);
        theListView.setAdapter(adapter);

    }
}
