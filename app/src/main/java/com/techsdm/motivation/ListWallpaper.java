package com.techsdm.motivation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.techsdm.motivation.Adapter.CategoryAdapter;
import com.techsdm.motivation.Adapter.WallpaperAdapter;
import com.techsdm.motivation.Model.CategoryItem;
import com.techsdm.motivation.Model.WallpaperItem;

import java.util.ArrayList;
import java.util.List;

public class ListWallpaper extends AppCompatActivity {


    android.support.v7.widget.Toolbar toolbar;
    private WallpaperAdapter mAdapter;
    List<WallpaperItem> wallpaperList=new ArrayList<WallpaperItem>();
    DatabaseReference dbwallpaper;
    String data;
    String data1;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_wallpaper);

        toolbar=findViewById(R.id.app_bar);



        Bundle bundle=getIntent().getExtras();
        data=bundle.getString("name");
        data=data.trim();
        toolbar.setTitle(data);
        setSupportActionBar(toolbar);
        /*if(!data.equals("m"))
        {
            data1=data;
        }
        if(data.equals("m"))
        {
            data=data1;
        }*/
        //Toast.makeText(getApplicationContext(),"Select->"+data,Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onStart() {
        super.onStart();
        dbwallpaper = FirebaseDatabase.getInstance().getReference("Wallpaper").child(data);
        dbwallpaper.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                WallpaperItem wallpaperItem = new WallpaperItem();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    wallpaperItem = postSnapshot.getValue(WallpaperItem.class);
                    wallpaperList.add(wallpaperItem);
                }
                recyclerView=(RecyclerView)findViewById(R.id.recycler_view1);
                mAdapter = new WallpaperAdapter(wallpaperList);
                recyclerView.setHasFixedSize(true);
                /*RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());*/
                GridLayoutManager gridLayoutManager=new GridLayoutManager(getApplicationContext(),2);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(mAdapter);


                recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        WallpaperItem wallpaperItem = wallpaperList.get(position);
                        //Toast.makeText(getApplicationContext(), cricketer.getCname() + " is selected!", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),ShowWallpaper.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("name",wallpaperItem.getImageLink());
                        bundle.putString("category_name",data);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //overridePendingTransition(R.anim.anime_slide_in_left, R.anim.anime_slide_in_right);
        Intent intent=new Intent(ListWallpaper.this,Tabbed_Activity.class);
        startActivity(intent);
    }
}
