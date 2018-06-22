package com.capstone.musicweb;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Artists;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class WebActivity extends BaseMenuActivity {

    SpotifyApi api = new SpotifyApi();
    SpotifyService spotify = api.getService();

    String nodeArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        nodeArtist = this.getIntent().getExtras().getString("artistId");

        spotify.getArtist(nodeArtist, new Callback<Artist>() {
            @Override
            public void success(Artist artist, Response response) {
                node[0] = artist; //node0 is the root, artist entry
                getRelatedArtists(artist);
            }

            @Override
            public void failure(RetrofitError error) {}
        });
    }



    public Artist[] node = new Artist[66];
    int cnt = 0;
    int a,b,c,d,e;

    public static boolean check(final Artist[] array, final Artist v) {
        for (final Artist e : array)
                if (e == v || v.equals(e)) {
                    return true;
                }
        return false;
    }

    private void recurse(Artists L1){
        a=1; b=2; c=3;
        for(int i=0; i<5; i++) {
            node[a] = L1.artists.get(i);
            getRelated(node[a]);
            a=a+13;
        }
    }
    private void recurseL2(Artists L2){
        d=0;
        for (int j = 0; j < 3; j++) {
            while(check(node, L2.artists.get(d))){
                d++;
            }
            node[b] = L2.artists.get(d);
            getRelated2(node[b]);
            b=b+4;
        } b++;
    }

    private void recurseL3(Artists L3){
        if(cnt==3){c++; cnt=0;}
        e=0;
        for (int k = 0; k < 3; k++) {
            while(check(node, L3.artists.get(e))){
                e++;
            }
            node[c] = L3.artists.get(e);
            c++;
        } c++; cnt++;
        if (node[65] != null){
            testWeb();
        }
    }
    private void getRelated(Artist artist){
        spotify.getRelatedArtists(artist.id, new Callback<Artists>(){
            @Override
            public void success(Artists artists, Response response) {
                recurseL2(artists);
            }

            @Override
            public void failure(RetrofitError error) {}
        });
    }
    private void getRelated2(Artist artist){
        spotify.getRelatedArtists(artist.id, new Callback<Artists>(){
            @Override
            public void success(Artists artists, Response response) {
                recurseL3(artists);
            }

            @Override
            public void failure(RetrofitError error) {}
        });
    }

    public void getRelatedArtists(Artist artist){

        spotify.getRelatedArtists(artist.id, new Callback<Artists>(){
            @Override
            public void success(Artists artists, Response response) {
                recurse(artists);
            }
            @Override
            public void failure(RetrofitError error) {}
        });
    }

    public void testWeb(){
        WebView myWeb = (WebView) findViewById(R.id.myWeb);

        for (int i=0; i<66; i++){
            Log.d("Node", node[i].name); //test
           myWeb.addMenuItem(node[i].name, i);
        }

        final Context context = this;
        myWeb.setListener(new WebView.IMenuListener() {
            @Override
            public void onMenuClick(WebView.MenuCircle item) {
                Artist selectedArtist = node[item.id];
                Intent detailIntent = new Intent(context, ArtistActivity.class); //GOING TO THE WEB
                detailIntent.putExtra("artistId", selectedArtist.id);
                detailIntent.putExtra("nodeArtistId", nodeArtist);
                startActivity(detailIntent);
            }
        });

        myWeb.invalidate(); //refresh web cause it's drunk

    }
}
