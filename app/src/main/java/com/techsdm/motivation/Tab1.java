package com.techsdm.motivation;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techsdm.motivation.Adapter.CategoryAdapter;
import com.techsdm.motivation.Model.CategoryItem;

import java.util.ArrayList;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * Created by Monika Bhasin on 05-08-2018.
 */

public class Tab1 extends android.support.v4.app.Fragment {

    Toolbar toolbar;
    public RecyclerView recyclerView;
    private CategoryAdapter mAdapter;
    List<CategoryItem> categoryList=new ArrayList<CategoryItem>();
    DatabaseReference dbcategories;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_home,container,false);
    }

    public Tab1() {
        /*toolbar=getView().findViewById(R.id.app_bar);
        toolbar.setTitle("Motivation");

        setSupportActionBar(toolbar);*/
        dbcategories= FirebaseDatabase.getInstance().getReference("Category");
    }

    @Override
    public void onStart() {
        super.onStart();
        dbcategories= FirebaseDatabase.getInstance().getReference("Category");
        dbcategories.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryList.clear();
                CategoryItem categoryItem=new CategoryItem();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    categoryItem = postSnapshot.getValue(CategoryItem.class);

                    categoryList.add(categoryItem);
                }
                recyclerView=(RecyclerView)getView().findViewById(R.id.recycler_view);
                mAdapter = new CategoryAdapter(categoryList);
                RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);

                recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        CategoryItem categoryItem = categoryList.get(position);
                        //Toast.makeText(getApplicationContext(), cricketer.getCname() + " is selected!", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getContext(),ListWallpaper.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("name",categoryItem.getName().trim());
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
}
