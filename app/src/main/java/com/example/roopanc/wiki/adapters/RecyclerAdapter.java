package com.example.roopanc.wiki.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.roopanc.wiki.MainActivity;
import com.example.roopanc.wiki.MyCallbacks;
import com.example.roopanc.wiki.R;
import com.example.roopanc.wiki.data.DataItem;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    ArrayList<DataItem> dataItems;
    Context context;

    public RecyclerAdapter(Context context, ArrayList<DataItem> dataItems)
    {
        this.context = context;
        this.dataItems = dataItems;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        final DataItem dataItem = dataItems.get(position);
        if (dataItem != null){
            String title = dataItem.getTitle();
            String description = dataItem.getDescription();
            String pageid = dataItem.getPageid();
            String picUrl = dataItem.getThumbnailurl();

            if (title != null && !title.equals(""))
            {
                holder.nameTv.setText(title);
            }
            else {
                holder.nameTv.setText("-");
            }

            if (description != null && !description.equals(""))
            {
                holder.descriptionTv.setText(description);
            }
            else {
                holder.descriptionTv.setText("-");
            }

            if (picUrl != null && !picUrl.equals(""))
            {
                Uri uri = Uri.parse(picUrl);
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .centerCrop();
                Glide.with(context).applyDefaultRequestOptions(requestOptions).load(uri).into(holder.imageView);
            }
            else {
                holder.imageView.setImageDrawable(context.getDrawable(R.drawable.placeholder));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity)context).adapterCallback(dataItem, true);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout itemLayout;
        ImageView imageView;
        TextView nameTv, descriptionTv;

        public ViewHolder(View itemView) {
            super(itemView);

            itemLayout = itemView.findViewById(R.id.linearitem);
            imageView = itemView.findViewById(R.id.imageitem);
            nameTv = itemView.findViewById(R.id.nameitem);
            descriptionTv = itemView.findViewById(R.id.designationitem);
        }
    }
}
