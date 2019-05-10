package com.techsdm.motivation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Toolbar;

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
import com.techsdm.motivation.Adapter.WallpaperAdapter;
import com.techsdm.motivation.Common.Common;
import com.techsdm.motivation.Model.WallpaperItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FindActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    android.support.v7.widget.Toolbar toolbar;
    LottieAnimationView lt_loading_view;
    SearchView search_bar;
    public RecyclerView recyclerView;
    private WallpaperAdapter mAdapter;
    List<WallpaperItem> wallpaperList=new ArrayList<WallpaperItem>();
    private RequestQueue mRequestQueue;
    String search_query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //hkjdfsh

        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        search_bar=(SearchView)findViewById(R.id.search_bar);
        search_bar.setOnQueryTextListener(this);
        search_bar.setQuery(Common.search_query,true);

    }

    @Override
    public void onStart() {
        super.onStart();

        fetchdata();

    }

    void fetchdata()
    {
        Cache cache=new DiskBasedCache(getCacheDir(),1024*1024);
        Network network=new BasicNetwork(new HurlStack());
        mRequestQueue=new RequestQueue(cache,network);
        mRequestQueue.start();

        wallpaperList.clear();

        //mRequestQueue = Volley.newRequestQueue(getActivity());

        StringBuilder stringBuilder=new StringBuilder("https://api.unsplash.com/search/photos/?client_id=3a612ec63fdadcc415c856f07e0c173d2f818b90fac8f439c17e83c8b80530e8");
        stringBuilder.append("&query="+Common.search_query);
        stringBuilder.append("&order_by="+"oldest");

        String url=stringBuilder.toString();
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
                            mAdapter = new WallpaperAdapter(wallpaperList,getApplicationContext());
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
                                    Common.imageLink=wallpaperItem.getImageLink();
                                    Common.width=wallpaperItem.getWidth();
                                    Common.height=wallpaperItem.getHeight();
                                    Common.username=wallpaperItem.getUsername();
                                    Common.userphoto=wallpaperItem.getUserphoto();

                                    Common.going_from_FindActivity=1;
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
                lt_loading_view = (LottieAnimationView)findViewById(R.id.lt_loading_view);
                lt_loading_view.setVisibility(View.VISIBLE);
                lt_loading_view.loop(true);
                lt_loading_view.playAnimation();
            }
        });

        mRequestQueue.add(request);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        Common.search_query=s;
        fetchdata();
        return false;
    }
}
