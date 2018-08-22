package com.techsdm.motivation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techsdm.motivation.Adapter.WallpaperAdapter;
import com.techsdm.motivation.Model.WallpaperItem;

import java.util.ArrayList;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * Created by Monika Bhasin on 05-08-2018.
 */

public class Tab3 extends Fragment {

    WallpaperAdapter mAdapter;
    RecyclerView recyclerView;
    List<WallpaperItem> result=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab3,container,false);
    }

    public Tab3()
    { }

    @Override
    public void onStart() {
        super.onStart();

        recyclerView=(RecyclerView)getView().findViewById(R.id.recyclerView);
        DatabaseHelper dbs=new DatabaseHelper(getContext());
        result = dbs.getCarts();

        mAdapter = new WallpaperAdapter(result);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                WallpaperItem wallpaperItem = result.get(position);
                //Toast.makeText(getApplicationContext(), cricketer.getCname() + " is selected!", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getContext(),ShowWallpaper.class);
                Bundle bundle=new Bundle();
                bundle.putString("name",wallpaperItem.getImageLink());
                //bundle.putString("category_name",data);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }
}
