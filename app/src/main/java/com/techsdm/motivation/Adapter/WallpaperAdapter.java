package com.techsdm.motivation.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideContext;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.techsdm.motivation.ListWallpaper;
import com.techsdm.motivation.Model.CategoryItem;
import com.techsdm.motivation.Model.WallpaperItem;
import com.techsdm.motivation.R;

import java.util.List;

/**
 * Created by Monika Bhasin on 19-07-2018.
 */

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.WallpaperViewHolder> {

    private List<WallpaperItem> wallpaperList;

    public WallpaperAdapter(List<WallpaperItem> wallpaperList) {
        this.wallpaperList = wallpaperList;
    }

    @NonNull
    @Override
    public WallpaperAdapter.WallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.layout_wallpaper_item,parent,false);
        return new WallpaperAdapter.WallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WallpaperAdapter.WallpaperViewHolder holder, int position) {
        final WallpaperItem wallpaperItem=wallpaperList.get(position);

        Picasso.get()
                .load(wallpaperItem.getImageLink())
                .fit()
                .into(holder.image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get()
                                .load(wallpaperItem.getImageLink())
                                .error(R.drawable.ic_wallpaper_black_24dp)
                                .into(holder.image, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Log.e("error","Wallpaper not Fetched");
                                    }
                                });
                    }
                });
    }

    @Override
    public int getItemCount() {
        return wallpaperList.size();
    }

    public class WallpaperViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        public WallpaperViewHolder(View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.image);
        }
    }
}
