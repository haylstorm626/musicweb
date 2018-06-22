package com.capstone.musicweb;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class AlbumTrackAdapter extends BaseAdapter {


    private final List<Track> mItems;
    private Context mContext;
    private LayoutInflater mInflater;
    private Player mPlayer;

    public AlbumTrackAdapter(Context context, List<Track> items) {
        mContext = context;
        mItems = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext.bindService(PlayerService.getIntent(mContext), mServiceConnection, Activity.BIND_AUTO_CREATE);
    }

    private static class ViewHolder {
        public TextView titleTextView;
        public TextView numberTextView;
        public ImageView playImageView;
        public ImageView addImageView;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.album_track_adapter, parent, false);

            holder = new ViewHolder();
            holder.playImageView = (ImageView) convertView.findViewById(R.id.playImageView);
            holder.addImageView = (ImageView) convertView.findViewById(R.id.addImageView);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
            holder.numberTextView = (TextView) convertView.findViewById(R.id.numberTextView);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        TextView titleTextView = holder.titleTextView;
        TextView numberTextView = holder.numberTextView;
        ImageView playImageView = holder.playImageView;
        ImageView addImageView = holder.addImageView;

        final Track item = mItems.get(position);

        titleTextView.setText(item.name);
        numberTextView.setText(String.valueOf(item.track_number));

        holder.playImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playTrack(item);
                Toast.makeText(v.getContext(), "Previewing " + item.name, Toast.LENGTH_SHORT).show();
            }
        });

        holder.addImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addToList(item);
                Toast.makeText(v.getContext(), "Added " + item.name + " to your list!", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayer = ((PlayerService.PlayerBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mPlayer = null;
        }
    };

    public void addToList(Track item){
        MyList list = MyList.getInstance();
        list.addTrack(item, mContext);

    }

    public void playTrack(Track item) {
        String previewUrl = item.preview_url;

        if (previewUrl == null) {return;}
        if (mPlayer == null) {return;}

        String currentTrackUrl = mPlayer.getCurrentTrack();

        if (currentTrackUrl == null || !currentTrackUrl.equals(previewUrl)) {
            mPlayer.play(previewUrl);
        } else if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.resume();
        }
    }
}
