package com.capstone.musicweb;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import kaaes.spotify.webapi.android.*;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Artists;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Pager;
import retrofit.*;
import retrofit.client.Response;

public class MainActivity extends BaseMenuActivity {

    SpotifyApi api = new SpotifyApi();
    SpotifyService spotify = api.getService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editTextName = (EditText) findViewById(R.id.txtArtist);
        editTextName.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i== EditorInfo.IME_ACTION_DONE){
                    String searchVal = textView.getText().toString();

                    spotify.searchArtists(searchVal, new Callback<ArtistsPager>() {
                        @Override
                        public void success(ArtistsPager artistPager, Response response) {
                            searchMe(artistPager);
                        }

                        @Override
                        public void failure(RetrofitError error) {}
                    });

                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    handled = true;
                }
                return handled;
            }
        }
        );

    }

    public void searchMe(ArtistsPager artistPager){
        ListView resultsList = (ListView) findViewById(R.id.searchList);

        final List<Artist> dankList = artistPager.artists.items;

        ArtistAdapter adapter = new ArtistAdapter(this, dankList);
        resultsList.setAdapter(adapter);

        final Context context = this;
        resultsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist selectedArtist = dankList.get(position);
                Intent detailIntent = new Intent(context, WebActivity.class);
                detailIntent.putExtra("artistId", selectedArtist.id);
                startActivity(detailIntent);
            }
        });
    }
}



