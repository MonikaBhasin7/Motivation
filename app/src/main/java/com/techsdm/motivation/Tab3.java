package com.techsdm.motivation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techsdm.motivation.Adapter.WallpaperAdapter;
import com.techsdm.motivation.Common.Common;
import com.techsdm.motivation.Model.WallpaperItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Monika Bhasin on 05-08-2018.
 */

public class Tab3 extends Fragment {

    WallpaperAdapter mAdapter;
    RecyclerView recyclerView;
    List<WallpaperItem> result = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab3, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        DatabaseHelper dbs=new DatabaseHelper(getContext());
        result = dbs.getCarts();
    }
}