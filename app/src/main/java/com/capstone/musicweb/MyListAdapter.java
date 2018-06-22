package com.capstone.musicweb;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.ArtistSimple;

public class MyListAdapter extends BaseAdapter {


    private final List<Track> mItems;
    private Context mContext;
    private LayoutInflater mInflater;
    private Player mPlayer;

    public MyListAdapter(Context context, List<Track> items) {
        mContext = context;
        mItems = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext.bindService(PlayerService.getIntent(mContext), mServiceConnection, Activity.BIND_AUTO_CREATE);
    }

    private static class ViewHolder {
        public TextView titleTextView;
        public TextView subtitleTextView;
        public ImageView playImageView;
        public ImageView delImageView;
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
            convertView = mInflater.inflate(R.layout.my_list_adapter, parent, false);

            holder = new ViewHolder();
            holder.playImageView = (ImageView) convertView.findViewById(R.id.playImageView);
            holder.delImageView = (ImageView) convertView.findViewById(R.id.delImageView);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
            holder.subtitleTextView = (TextView) convertView.findViewById(R.id.subtitleTextView);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        TextView titleTextView = holder.titleTextView;
        TextView subtitleTextView = holder.subtitleTextView;
        ImageView playImageView = holder.playImageView;
        ImageView delImageView = holder.delImageView;

        final Track item = mItems.get(position);

        titleTextView.setText(item.name);
        subtitleTextView.setText(item.artists.get(0).name);


        holder.playImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTrack(item);
                Toast.makeText(v.getContext(), "Previewing " + item.name, Toast.LENGTH_SHORT).show();
            }
        });

        holder.delImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog(v, item);
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

    public void selectTrack(Track item) {
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

    //dialog for deleting from mylist
    public void deleteDialog(View view, Track mItem) {
        final View v = view;
        final Track item = mItem;
        new AlertDialog.Builder(mContext)
                .setTitle("Delete track")
                .setMessage("Are you sure you want to delete '" + item.name + "' by " + item.artists.get(0).name + " from your list?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MyList list = MyList.getInstance();
                        list.delete(item, mContext);
                        Toast.makeText(v.getContext(), "Deleted " + item.name + " from your list", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                        // delete entry
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}

