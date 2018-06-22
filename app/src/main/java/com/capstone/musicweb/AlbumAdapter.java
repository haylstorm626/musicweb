package com.capstone.musicweb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Image;

public class AlbumAdapter extends BaseAdapter {

    private final List<Album> mItems;
    private Context mContext;
    private LayoutInflater mInflater;

    public AlbumAdapter(Context context, List<Album> items) {
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
        ViewHolder holder;

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.album_adapter, parent, false);

            holder = new ViewHolder();
            holder.albumImageView = (ImageView) convertView.findViewById(R.id.coverImageView);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        TextView titleTextView = holder.titleTextView;
        ImageView albumImageView = holder.albumImageView;


        Album item = mItems.get(position);

        titleTextView.setText(item.name);

        Image image = item.images.get(0);
        Picasso.with(mContext).load(image.url).placeholder(R.mipmap.ic_launcher).into(albumImageView);

        return convertView;
    }

    private static class ViewHolder {
        public TextView titleTextView;
        public ImageView albumImageView;
    }

}
