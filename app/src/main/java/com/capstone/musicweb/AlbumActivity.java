package com.capstone.musicweb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AlbumActivity extends BaseMenuActivity {

    SpotifyApi api = new SpotifyApi();
    SpotifyService spotify = api.getService();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        //get albumID and artistID from artist activity
        String chosenAlbId = this.getIntent().getExtras().getString("albumId");
        String enteredValId = this.getIntent().getExtras().getString("artistId");
        artistName(enteredValId);
        artistAlbum(chosenAlbId);
        albumTracks(chosenAlbId);
    }

    //get artist from ID and set artist name to textview
    public void artistName(String enteredValId){
        spotify.getArtist(enteredValId, new Callback<Artist>() {
            @Override
            public void success(Artist artist, Response response) {
                TextView textElement = (TextView) findViewById(R.id.textArtistName);
                textElement.setText(artist.name);
            }

            @Override
            public void failure(RetrofitError error) {}
        });
    }

    //get album from ID and set album name to textview
    public void artistAlbum(String chosenAlbId){
        spotify.getAlbum(chosenAlbId, new Callback<Album>() {
            @Override
            public void success(Album album, Response response) {
                TextView textElement = (TextView) findViewById(R.id.textAlbumName);
                textElement.setText(album.name);
                ImageView imgView = (ImageView) findViewById(R.id.imgAlbum);
                Image image = album.images.get(0);
                Picasso.with(getApplicationContext()).load(image.url).placeholder(R.mipmap.ic_launcher).into(imgView);
            }

            @Override
            public void failure(RetrofitError error) {}
        });
    }

    //get album tracks using album ID
    public void albumTracks(String chosenAlbId){
        final ListView theListView = (ListView) findViewById(R.id.listTracks);
        spotify.getAlbumTracks(chosenAlbId, new SpotifyCallback<Pager<Track>>() {
            @Override
            public void failure(SpotifyError error){}

            @Override
            public void success(Pager<Track> trackPager, Response response) {
                printTracks(trackPager, theListView);
            }
        } );
    }

    //display tracks with a listview
    public void printTracks (Pager<Track> trackPager, ListView theListView){
        final List<Track> dankList = trackPager.items;

        AlbumTrackAdapter adapter = new AlbumTrackAdapter(this, dankList);
        theListView.setAdapter(adapter);
    }

}
