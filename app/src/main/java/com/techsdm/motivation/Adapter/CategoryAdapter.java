package com.techsdm.motivation.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.techsdm.motivation.Model.CategoryItem;
import com.techsdm.motivation.R;

import java.util.List;

/**
 * Created by Monika Bhasin on 17-07-2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    private List<CategoryItem> categoryList;

    public CategoryAdapter(List<CategoryItem> categoryList) {
        this.categoryList = categoryList;
    }

    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.list_item_layout,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CategoryAdapter.CategoryViewHolder holder, int position) {
        final CategoryItem categoryItem=categoryList.get(position);

        Picasso.get()
                .load(categoryItem.getImageLink())
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .rotate(90f)
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
                });
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