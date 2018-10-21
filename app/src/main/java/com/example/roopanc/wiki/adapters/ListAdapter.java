package com.example.roopanc.wiki.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.roopanc.wiki.R;
import com.example.roopanc.wiki.data.DataItem;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<DataItem> {

    private ArrayList<DataItem> dataItems;
    Context context;
    boolean recents;

    public ListAdapter(ArrayList<DataItem> dataItems, Context context, boolean recents)
    {
        super(context, R.layout.item, dataItems);
        this.context = context;
        this.dataItems =dataItems;
        this.recents = recents;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtDescription;
        ImageView thumbnail;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DataItem dataItem = dataItems.get(position);
        final ViewHolder viewHolder;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.item, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.nameitem);
            viewHolder.txtDescription = convertView.findViewById(R.id.designationitem);
            viewHolder.thumbnail = convertView.findViewById(R.id.imageitem);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (dataItem != null)
        {
            viewHolder.txtName.setText(dataItem.getTitle());
            viewHolder.txtDescription.setText(dataItem.getDescription());

            String imageUrl = dataItem.getThumbnailurl();
            if (imageUrl != null){
                Uri uri = Uri.parse(imageUrl);
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .centerCrop();
                Glide.with(context).applyDefaultRequestOptions(requestOptions).load(uri).into(viewHolder.thumbnail);
            }
            else {
                viewHolder.thumbnail.setImageDrawable(context.getDrawable(R.drawable.placeholder));
            }
        }

        return convertView;
    }

    @Override
    public int getCount() {
        if (recents){
            if (dataItems.size() >  5)
            {
                return 5;
            }
            else {
                dataItems.size();
            }
        }
        return dataItems.size();
    }
}
