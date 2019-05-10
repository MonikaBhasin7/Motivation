package com.techsdm.motivation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.techsdm.motivation.Adapter.WallpaperAdapter;
import com.techsdm.motivation.Adapter.WallpaperAdapter2;
import com.techsdm.motivation.Common.Common;
import com.techsdm.motivation.Model.WallpaperItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

/**
 * Created by Monika Bhasin on 05-08-2018.
 */

public class Tab2 extends android.support.v4.app.Fragment{

    int page_per_collection=1;
    int page_numberi;
    String page_number;
    Button next_button;
    private boolean isScrolling = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    Tab2 tab2;
    Tabbed_Activity tabbed_activity;
    FragmentManager fragmentManager;
    String exact_coll_id;
    String url = "https://pixabay.com/api/?key=5303976-fd6581ad4ac165d1b75cc15b3&q=green&image_type=photo";
    LottieAnimationView lt_loading_view;
    private RequestQueue mRequestQueue;
    private RequestQueue mRequestQueue1;
    android.support.v7.widget.Toolbar toolbar;
    private WallpaperAdapter2 mAdapter;
    List<WallpaperItem> wallpaperList = new ArrayList<WallpaperItem>();
    DatabaseReference dbwallpaper;
    private RecyclerView recyclerView;
    List<String> collectionListy = new ArrayList<String>();
    List<String> collectionListi = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab2, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Toast.makeText(getActivity(),"On Start",Toast.LENGTH_SHORT).show();
        //lt_loading_view = (LottieAnimationView) getActivity().findViewById(R.id.lt_loading_view);
        //lt_loading_view.setVisibility(View.VISIBLE);
        //lt_loading_view.loop(true);
        //lt_loading_view.playAnimation();
        Paper.init(getActivity());
        String this_id=Paper.book().read(Common.collection_id);
        String this_index=Paper.book().read(Common.collection_index);
        //Toast.makeText(getActivity(),"This Index-"+this_index,Toast.LENGTH_SHORT).show();
        page_number=Paper.book().read(Common.page_number);
        //Paper.book().write(String.valueOf(Common.page_number),page_number);

        if(page_number==null)
        {
            page_number=String.valueOf(0);
            Paper.book().write(String.valueOf(Common.page_number),page_number);
        }
        //Toast.makeText(getActivity(),"this_index"+this_index,Toast.LENGTH_SHORT).show();

        //next_button=(Button)getActivity().findViewById(R.id.next_button);
        //next_button.setOnClickListener(this);
        //next_button.setVisibility(View.INVISIBLE);

        if(this_id==null || Integer.parseInt(this_index)>=9)
        {
            page_numberi=Integer.parseInt(page_number);
            page_numberi=page_numberi+1;
            Paper.book().write(Common.page_number,String.valueOf(page_numberi));
            //Toast.makeText(getActivity(),"Page_number-->"+Paper.book().read(Common.page_number),Toast.LENGTH_SHORT).show();
            mRequestQueue1 = Volley.newRequestQueue(getActivity());
            collectionListy.clear();
            collectionListi.clear();
            Paper.book().write("collection_list",collectionListy);
            StringBuilder stringBuilder=new StringBuilder("https://api.unsplash.com/search/collections/?query=animals&client_id=3a612ec63fdadcc415c856f07e0c173d2f818b90fac8f439c17e83c8b80530e8");
            stringBuilder.append("&page="+Paper.book().read(Common.page_number));
            String url=stringBuilder.toString();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("results");
                                WallpaperItem wallpaperItem = new WallpaperItem();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject results_object = jsonArray.getJSONObject(i);
                                    int id = results_object.getInt("id");
                                    //Toast.makeText(getActivity(),"id->"+id,Toast.LENGTH_SHORT).show();
                                    collectionListy.add(String.valueOf(id));
                                }
                                Paper.book().write("collection_list",collectionListy);
                                Paper.book().write(Common.collection_id,collectionListy.get(0));
                                Paper.book().write(Common.collection_index,"0");
                                collectionListi=Paper.book().read("collection_list");
                                //Toast.makeText(getActivity(),"list->"+collectionListi,Toast.LENGTH_SHORT).show();
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

            mRequestQueue1.add(request);
        }
        else if(Integer.parseInt(this_index)<9)
        {
            exact_coll_id=Paper.book().read(Common.collection_id);    //String done
            //Toast.makeText(getActivity(),"exact_coll_id"+exact_coll_id,Toast.LENGTH_SHORT).show();
            String coll_index=Paper.book().read(Common.collection_index);
            int coll_indexy=Integer.parseInt(coll_index);       //String done
            coll_indexy=coll_indexy+1;       //String done
            Paper.book().write(Common.collection_index,String.valueOf(coll_indexy));
            //Toast.makeText(getActivity(),"coll_indexy"+coll_indexy,Toast.LENGTH_SHORT).show();
            //int next_coll_id=Paper.book().read(Common.collection_list.get(coll_indexy));
            //Paper.book().write(Common.collection_id,String.valueOf(next_coll_id));
            collectionListi=Paper.book().read("collection_list");
            String coll_indexy_int=collectionListi.get(coll_indexy);
            Paper.book().write(Common.collection_id,coll_indexy_int);
        }

    }

    // @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onStart() {
        super.onStart();
        tab2=new Tab2();
        wallpaperList.clear();
        //mRequestQueue = Volley.newRequestQueue(getActivity());
        Cache cache=new DiskBasedCache(getActivity().getCacheDir(),1024*1024);
        Network network=new BasicNetwork(new HurlStack());
        mRequestQueue=new RequestQueue(cache,network);
        mRequestQueue.start();
        String str= "https://api.unsplash.com/collections/xyz/photos/?client_id=3a612ec63fdadcc415c856f07e0c173d2f818b90fac8f439c17e83c8b80530e8";
        str = str.replace("xyz",String.valueOf(exact_coll_id));
        StringBuilder stringBuilder=new StringBuilder(str);
        //stringBuilder.append("&orientation="+"portrait");
        //stringBuilder.append("&collections="+exact_coll_id);
        //Toast.makeText(getActivity(),"Coll_Id"+exact_coll_id,Toast.LENGTH_SHORT).show();

        String url=stringBuilder.toString();
        //lt_loading_view = (LottieAnimationView) getActivity().findViewById(R.id.lt_loading_view);
        //lt_loading_view.setVisibility(View.VISIBLE);
        //lt_loading_view.loop(true);
        //lt_loading_view.playAnimation();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            //WallpaperItem wallpaperItem = new WallpaperItem();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject results_object = response.getJSONObject(i);
                                String imageUrl=results_object.getJSONObject("urls").getString("regular");
                                int width=results_object.getInt("width");
                                int height=results_object.getInt("height");
                                int likes=results_object.getInt("likes");
                                String username=results_object.getJSONObject("user").getString("username");
                                String userphoto=results_object.getJSONObject("user").getJSONObject("profile_image").getString("small");
                                wallpaperList.add(new WallpaperItem(imageUrl,width,height,imageUrl,likes,username,userphoto));

                            }
                            recyclerView=(RecyclerView)getView().findViewById(R.id.recycler_view1);
                            mAdapter = new WallpaperAdapter2(wallpaperList,getActivity());
                            recyclerView.setHasFixedSize(true);
                            final LinearLayoutManager gridLayoutManager=new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(gridLayoutManager);
                            recyclerView.setAdapter(mAdapter);
                            //lt_loading_view.setVisibility(View.GONE);


                            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                    super.onScrollStateChanged(recyclerView, newState);

                                    if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                                    {
                                        isScrolling=true;
                                        Log.d("State->", String.valueOf(isScrolling));
                                    }
                                    else if(newState ==AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                                        isScrolling=false;
                                        Log.d("State->", String.valueOf(isScrolling));
                                    }
                                }

                                @Override
                                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                    super.onScrolled(recyclerView, dx, dy);

                                    if(dy > 0) //check for scroll down
                                    {
                                        visibleItemCount = gridLayoutManager.getChildCount();
                                        totalItemCount = gridLayoutManager.getItemCount();
                                        pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();
                                        Log.d("visibleItemCount",String.valueOf(visibleItemCount));
                                        Log.d("totalItemCount",String.valueOf(totalItemCount));
                                        Log.d("pastVisiblesItems",String.valueOf(pastVisiblesItems));

                                        if (isScrolling)
                                        {
                                            //next_button.setVisibility(View.INVISIBLE);
                                            if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                                            {
                                                isScrolling = false;
                                                //Log.d("dd","Last Item");
                                                //Toast.makeText(getActivity().getApplicationContext(),"On the Edge",Toast.LENGTH_SHORT).show();
                                                //Do pagination.. i.e. fetch new data
                                                //next_button.setVisibility(View.VISIBLE);
                                                fetchdata();
                                            }
                                        }
                                    }
                                    if(dx>0)
                                    {
                                        //next_button.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });




                            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                                @Override
                                public void onClick(View view, int position) {

                                    WallpaperItem wallpaperItem = wallpaperList.get(position);
                                    Common.imageLink=wallpaperItem.getImageLink();
                                    Common.width=wallpaperItem.getWidth();
                                    Common.height=wallpaperItem.getHeight();
                                    Common.username=wallpaperItem.getUsername();
                                    Common.userphoto=wallpaperItem.getUserphoto();
                                    Intent intent=new Intent(getActivity(),ShowWallpaper.class);
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

    public  void fetchdata()
    {
                page_per_collection=page_per_collection+1;
                //Toast.makeText(getActivity(),"page_per_collection"+page_per_collection,Toast.LENGTH_SHORT).show();
                String str= "https://api.unsplash.com/collections/xyz/photos/?client_id=3a612ec63fdadcc415c856f07e0c173d2f818b90fac8f439c17e83c8b80530e8";
                str = str.replace("xyz",String.valueOf(exact_coll_id));
                StringBuilder stringBuilder=new StringBuilder(str);
                stringBuilder.append("&page="+page_per_collection);

                String url=stringBuilder.toString();

                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject results_object = response.getJSONObject(i);
                                        String imageUrl=results_object.getJSONObject("urls").getString("regular");
                                        int width=results_object.getInt("width");
                                        int height=results_object.getInt("height");
                                        int likes=results_object.getInt("likes");
                                        String username=results_object.getJSONObject("user").getString("username");
                                        String userphoto=results_object.getJSONObject("user").getJSONObject("profile_image").getString("small");
                                        wallpaperList.add(new WallpaperItem(imageUrl,width,height,imageUrl,likes,username,userphoto));

                                    }

                                    mAdapter.notifyDataSetChanged();
                                    //lt_loading_view.pauseAnimation();
                                    //lt_loading_view.setVisibility(View.INVISIBLE);


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
                //request.setShouldCache(false);
                //mRequestQueue.getCache().clear();
                mRequestQueue.add(request);

    }
}
