package com.capstone.musicweb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;

public class ArtistAdapter extends BaseAdapter {

    private final List<Artist> mItems;
    private Context mContext;
    private LayoutInflater mInflater;

    public ArtistAdapter(Context context, List<Artist> items) {
        mContext = context;
        mItems = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        View rowView = mInflater.inflate(R.layout.artist_adapter, parent, false);
        Artist item = mItems.get(position);

        TextView titleTextView =
                (TextView) rowView.findViewById(R.id.titleTextView);

        titleTextView.setText(item.name);

        return rowView;
    }
}
