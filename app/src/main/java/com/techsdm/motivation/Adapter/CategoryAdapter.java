package com.techsdm.motivation.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.techsdm.motivation.Model.CategoryItem;
import com.techsdm.motivation.R;
import com.techsdm.motivation.Tab1;

import java.util.List;

/**
 * Created by Monika Bhasin on 17-07-2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    RequestOptions option;
    Context mContext;
    private List<CategoryItem> categoryList;

    public CategoryAdapter(List<CategoryItem> categoryList,Context mContext) {
        this.categoryList = categoryList;
        this.mContext=mContext;

        //Request Option for Glide
        option=new RequestOptions().centerCrop().placeholder(R.drawable.button_shape_download).error(R.drawable.ic_wallpaper_black_24dp);
    }

    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.list_item_layout,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder holder, int position) {
        final CategoryItem categoryItem=categoryList.get(position);




       /* Picasso.get()
                .load(categoryItem.getImageLink())
                .resize(500,500)
                .centerInside()
                .into(holder.image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get()
                                .load(categoryItem.getImageLink())
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
       Glide.with(mContext).load(categoryItem.getImageLink()).apply(option).into(holder.image);
        holder.name.setText(categoryItem.getName());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView image;

        public CategoryViewHolder(View itemView) {
            super(itemView);

            name=(TextView)itemView.findViewById(R.id.name);
            image=(ImageView)itemView.findViewById(R.id.image);
        }
    }
}