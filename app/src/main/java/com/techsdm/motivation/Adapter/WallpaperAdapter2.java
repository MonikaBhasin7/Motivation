package com.techsdm.motivation.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.techsdm.motivation.Model.WallpaperItem;
import com.techsdm.motivation.R;

import java.util.List;

/**
 * Created by Monika Bhasin on 19-07-2018.
 */

public class WallpaperAdapter2 extends RecyclerView.Adapter<WallpaperAdapter2.WallpaperViewHolder> {

    RequestOptions option;
    Context mContext;
    private List<WallpaperItem> wallpaperList;

    public WallpaperAdapter2(List<WallpaperItem> wallpaperList, Context context) {
        this.wallpaperList = wallpaperList;
        this.mContext=context;
        //Request Option for Glide
        option=new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888).diskCacheStrategy(DiskCacheStrategy.RESOURCE).fitCenter()/*.override(WindowManager.LayoutParams.MATCH_PARENT,250).centerCrop()*/.placeholder(R.drawable.animation_list).error(R.drawable.animation_list);
    }

    @NonNull
    @Override
    public WallpaperAdapter2.WallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.layout_wallpaper_item,parent,false);
        return new WallpaperAdapter2.WallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WallpaperAdapter2.WallpaperViewHolder holder, int position) {
        final WallpaperItem wallpaperItem=wallpaperList.get(position);

        /*Glide.with(ListWallpaper.class)
                .load(wallpaperItem.getImageLink())
                .into(holder.image);*/

        /*Picasso.get()
                .load(wallpaperItem.getImageLink())
               //.resize(300,300)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                //.centerInside()
                .into(holder.image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get()
                                .load(wallpaperItem.getImageLink()
                                )
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
                });*/

        Glide.with(mContext).load(wallpaperItem.getImageLink()).apply(option).into(holder.image);

        holder.text_download.setText(String.valueOf(wallpaperItem.getDownloads()));
    }

    @Override
    public int getItemCount() {
        return wallpaperList.size();
    }

    public class WallpaperViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView text_download;

        public WallpaperViewHolder(View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.image);
            text_download=(TextView)itemView.findViewById(R.id.text_download);
        }
    }
}
