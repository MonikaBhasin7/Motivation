package com.techsdm.motivation;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techsdm.motivation.Adapter.CategoryAdapter;
import com.techsdm.motivation.Adapter.WallpaperAdapter;
import com.techsdm.motivation.Common.Common;
import com.techsdm.motivation.Model.CategoryItem;
import com.techsdm.motivation.Model.WallpaperItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * Created by Monika Bhasin on 05-08-2018.
 */

public class Tab1 extends android.support.v4.app.Fragment {

    Tab1 tab1;
    LottieAnimationView lt_loading_view;
    Toolbar toolbar;
    public RecyclerView recyclerView;
    private WallpaperAdapter mAdapter;
    List<WallpaperItem> wallpaperList=new ArrayList<WallpaperItem>();
    private RequestQueue mRequestQueue;
    //DatabaseReference dbcategories;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_home,container,false);
    }

    public Tab1() {
        /*toolbar=getView().findViewById(R.id.app_bar);
        toolbar.setTitle("Motivation");

        setSupportActionBar(toolbar);*/
        //dbcategories= FirebaseDatabase.getInstance().getReference("Category");
    }


    @Override
    public void onStart() {
        tab1=new Tab1();
        super.onStart();

        //Get Display Dimensions
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Common.display_height = displayMetrics.heightPixels;
        Common.display_width = displayMetrics.widthPixels;


        lt_loading_view = (LottieAnimationView) getActivity().findViewById(R.id.lt_loading_view);
        lt_loading_view.setVisibility(View.VISIBLE);
        lt_loading_view.loop(true);
        lt_loading_view.playAnimation();
        Cache cache=new DiskBasedCache(getActivity().getCacheDir(),1024*1024);
        Network network=new BasicNetwork(new HurlStack());
        mRequestQueue=new RequestQueue(cache,network);
        mRequestQueue.start();

        wallpaperList.clear();

        //mRequestQueue = Volley.newRequestQueue(getActivity());

        StringBuilder stringBuilder=new StringBuilder("https://api.unsplash.com/search/photos/?per_page=50&orientation=portrait&client_id=3a612ec63fdadcc415c856f07e0c173d2f818b90fac8f439c17e83c8b80530e8");
        stringBuilder.append("&query="+"Animal");
        //stringBuilder.append("&order_by="+"oldest");

        String url=stringBuilder.toString();
        lt_loading_view = (LottieAnimationView) getActivity().findViewById(R.id.lt_loading_view);
        lt_loading_view.setVisibility(View.VISIBLE);
        lt_loading_view.loop(true);
        lt_loading_view.playAnimation();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            WallpaperItem wallpaperItem = new WallpaperItem();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                //JSONObject hit = jsonArray.getJSONObject(i);

                                JSONObject results_object = jsonArray.getJSONObject(i);
                                String imageUrl=results_object.getJSONObject("urls").getString("regular");
                                int width=results_object.getInt("width");
                                int height=results_object.getInt("height");
                                int likes=results_object.getInt("likes");
                                String username=results_object.getJSONObject("user").getString("username");
                                String userphoto=results_object.getJSONObject("user").getString("profile_image");
                                wallpaperList.add(new WallpaperItem(imageUrl,width,height,imageUrl,likes,username,userphoto));
                                mRequestQueue.stop();
                            }
                            recyclerView=(RecyclerView)getView().findViewById(R.id.recycler_view);
                            mAdapter = new WallpaperAdapter(wallpaperList,getActivity());
                            recyclerView.setHasFixedSize(true);
                            /*RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getApplicationContext());
                             recyclerView.setLayoutManager(mLayoutManager);
                             recyclerView.setItemAnimator(new DefaultItemAnimator());*/
                            final LinearLayoutManager gridLayoutManager=new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(gridLayoutManager);
                            recyclerView.setAdapter(mAdapter);

                            lt_loading_view.pauseAnimation();
                            lt_loading_view.setVisibility(View.INVISIBLE);

                            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                                @Override
                                public void onClick(View view, int position) {
                                    WallpaperItem wallpaperItem = wallpaperList.get(position);
                                    //Toast.makeText(getApplicationContext(), cricketer.getCname() + " is selected!", Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(getContext(),ShowWallpaper.class);
                                    Common.imageLink=wallpaperItem.getImageLink();
                                    Common.width=wallpaperItem.getWidth();
                                    Common.height=wallpaperItem.getHeight();
                                    Common.username=wallpaperItem.getUsername();
                                    Common.userphoto=wallpaperItem.getUserphoto();
                                    startActivity(intent);
                                }

                                @Override
                                public void onLongClick(View view, int position) {

                                }
                            }));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);

    }
}
