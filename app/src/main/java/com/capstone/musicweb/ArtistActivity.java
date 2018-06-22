package com.capstone.musicweb;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Paint;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Albums;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ArtistActivity extends BaseMenuActivity {

    SpotifyApi api = new SpotifyApi();
    SpotifyService spotify = api.getService();


    Button butt;
    String backNodeArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);

        String enteredValId = this.getIntent().getExtras().getString("artistId");
        backNodeArtist = this.getIntent().getExtras().getString("nodeArtistId");

        spotify.getArtist(enteredValId, new Callback<Artist>() {
            @Override
            public void success(Artist artist, Response response) {
                artistName(artist.name);
                topTracks(artist.id);
                artistAlbums(artist.id);
                relatedButton(artist.id);
            }

            @Override
            public void failure(RetrofitError error){}
        });
    }

     public void artistName(String artistName){
        TextView textElement = (TextView) findViewById(R.id.textArtistName);
        textElement.setText(artistName);
    }

    public void relatedButton(String artistID){
        final String aID= artistID;
        butt=(Button)findViewById(R.id.buttToWeb);
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(getApplicationContext(), WebActivity.class);
                detailIntent.putExtra("artistId", aID);
                startActivity(detailIntent);
            }
        });
    }

    public void topTracks(String artistId){
        final ListView theListView = (ListView) findViewById(R.id.listTopFive);
        spotify.getArtistTopTrack(artistId, "US", new SpotifyCallback<Tracks>() {
            @Override
            public void failure(SpotifyError error) {}
            @Override
            public void success(Tracks tracks, Response response) {
                printTracks(tracks, theListView);
            }
        } );

    }

    public void printTracks (Tracks tracks, ListView theListView){
        final List<Track> dankList = tracks.tracks;

        TrackAdapter adapter = new TrackAdapter(this, dankList);
        theListView.setAdapter(adapter);
    }

    public void artistAlbums(String artistId){
        final String aId = artistId;

        spotify.getArtistAlbums(aId, new SpotifyCallback<Pager<Album>>() {
            @Override
            public void failure(SpotifyError error) {}

            @Override
            public void success(Pager<Album> albumPager, Response response) {
                printAlbums(albumPager, aId);
            }
        } );
    }

    public static boolean check(List<Album> list, String id) {
        for (Album object : list) {
            if (object.name.equals(id)) {
                return true;
            }
        }
        return false;
    }

    public void printAlbums (Pager<Album> albumPager, String aId){
        final GridView theGridView = (GridView) findViewById(R.id.gridAlbums);
        final List<Album> l1 = albumPager.items;
        final List<Album> l2 = new ArrayList<>();

        for(int i=0;i<l1.size();i++) { //for every item in l1
            if (check(l2, albumPager.items.get(i).name)) { //if name exists in l2,
                i++;                                        //then skip that album
            }else{
                l2.add(albumPager.items.get(i));            //else, add that album
            }
        }

        final String artistId = aId;

        AlbumAdapter adapter = new AlbumAdapter(this, l2);
        theGridView.setAdapter(adapter);
        final Context context = this;
        theGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Album selectedAlbum = l2.get(position);
                Intent detailIntent = new Intent(context, AlbumActivity.class);
                detailIntent.putExtra("albumId", selectedAlbum.id);
                detailIntent.putExtra("artistId", artistId);
                startActivity(detailIntent);
            }
        });

    }

    @Override
    public void onBackPressed()
    {
        Intent detailIntent = new Intent(getApplicationContext(), WebActivity.class);
        detailIntent.putExtra("artistId", backNodeArtist);
        startActivity(detailIntent);

    }


}