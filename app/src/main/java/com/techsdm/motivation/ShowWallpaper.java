package com.techsdm.motivation;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.techsdm.motivation.Model.WallpaperItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShowWallpaper extends AppCompatActivity {

    String gofavorite="0";
    Animation favoriteButtonSelect;
    ImageView favorite_uncheck_button;
    ImageView favorite_check_button;
    WallpaperItem wallpaperItem=new WallpaperItem();
    Animation onclick;
    //Context mcontext;
    Boolean isOpen=false;
    Animation fabOpen,fabClose,rotateForward,rotateBackward;
    FloatingActionButton fab_add;
    FloatingActionButton fab_download;
    FloatingActionButton fab_share;
    String a;
    ImageView imageView;
    String w_data=null;
    String w_data1=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_wallpaper);


        favorite_uncheck_button=(ImageView)findViewById(R.id.favorite_uncheck_button);
        favorite_check_button=(ImageView)findViewById(R.id.favorite_check_button);
        favorite_check_button.setVisibility(View.GONE);

        fab_add=(FloatingActionButton)findViewById(R.id.fab_add);
        fab_download=(FloatingActionButton)findViewById(R.id.fab_download);
        fab_share=(FloatingActionButton)findViewById(R.id.fab_share);

        fabOpen=AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose=AnimationUtils.loadAnimation(this, R.anim.fab_close);
        rotateForward=AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotateBackward=AnimationUtils.loadAnimation(this, R.anim.rotate_backward);
        onclick=AnimationUtils.loadAnimation(this,R.anim.onclick);
        favoriteButtonSelect=AnimationUtils.loadAnimation(this,R.anim.favorite_button_select);


        a="m";
        Bundle bundle=getIntent().getExtras();
        w_data=bundle.getString("name");
        w_data1=bundle.getString("category_name");
        if(w_data1==null)
        {
            gofavorite="1";
        }
        //Toast.makeText(getApplicationContext(),"w_data1"+w_data1,Toast.LENGTH_SHORT).show();

        imageView=(ImageView)findViewById(R.id.imageView);

        Picasso.get()
                .load(w_data)
                .fit()
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get()
                                .load(w_data)
                                .fit()
                                .error(R.drawable.ic_wallpaper_black_24dp)
                                .into(imageView, new Callback() {
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



        DatabaseHelper dbs=new DatabaseHelper(getBaseContext());
        Boolean check1=dbs.check(w_data);
        if(check1==true)
        {
            favorite_check_button.setVisibility(View.VISIBLE);
            favorite_uncheck_button.setVisibility(View.GONE);
            favorite_check_button.startAnimation(favoriteButtonSelect);
        }
        else if(check1==false)
        {
            favorite_check_button.setVisibility(View.GONE);
            favorite_uncheck_button.setVisibility(View.VISIBLE);
            favorite_uncheck_button.startAnimation(favoriteButtonSelect);
        }

    }


    public void favorite_uncheck_button_click(View view)     //For Check
    {
        favorite_check_button.setVisibility(View.VISIBLE);
        favorite_uncheck_button.setVisibility(View.GONE);
        favorite_check_button.startAnimation(favoriteButtonSelect);

        DatabaseHelper dbs=new DatabaseHelper(getBaseContext());

        Boolean check=dbs.add_data(w_data);
        if(check==true)
        {
            Toast.makeText(getApplicationContext(),"Added to Favorites",Toast.LENGTH_SHORT).show();
        }
        else {
         Toast.makeText(getApplicationContext(),"Error in Adding",Toast.LENGTH_SHORT).show();
        }



    }

    public void favorite_check_button_click(View view)   //For Uncheck
    {
        favorite_check_button.setVisibility(View.GONE);
        favorite_uncheck_button.setVisibility(View.VISIBLE);
        favorite_uncheck_button.startAnimation(favoriteButtonSelect);

        DatabaseHelper dbs=new DatabaseHelper(getBaseContext());
        w_data=w_data.toString().trim();
        dbs.delete(w_data);


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //overridePendingTransition(R.anim.anime_slide_in_left, R.anim.anime_slide_in_right);
        if(gofavorite.equals("1"))
        {
            Intent intent=new Intent(getApplicationContext(),Tabbed_Activity.class);
            startActivity(intent);
        }
        else if(!w_data1.equals(null))
        {
            Intent intent=new Intent(getApplicationContext(),ListWallpaper.class);
            Bundle bundle=new Bundle();
            bundle.putString("name",w_data1);
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }
    public void fab_add_click(View view)
    {
        if(isOpen)
        {
            fab_add.startAnimation(rotateForward);
            fab_download.startAnimation(fabClose);
            fab_share.startAnimation(fabClose);
            fab_download.setClickable(false);
            fab_share.setClickable(false);
            isOpen=false;
        }
        else
        {
            fab_add.startAnimation(rotateBackward);
            fab_download.startAnimation(fabOpen);
            fab_share.startAnimation(fabOpen);
            fab_download.setClickable(true);
            fab_share.setClickable(true);
            isOpen=true;
        }
    }

    public void fab_download_click(View view)
    {
        Glide.with(ShowWallpaper.this)
                .asBitmap()
                .load(w_data)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Intent intent=new Intent(Intent.ACTION_VIEW);
                        Uri uri=saveWallpaperAndGetUri(resource, wallpaperItem.imageLink);

                        if(uri!=null)
                        {
                            intent.setDataAndType(uri,"image/*");
                            ShowWallpaper.this.startActivity(Intent.createChooser(intent,"Motivation"));
                        }
                    }
                });
    }

    public void fab_share_click(View view)
    {
        fab_share.startAnimation(onclick);
        Glide.with(ShowWallpaper.this)
                .asBitmap()
                .load(w_data)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Intent intent=new Intent(Intent.ACTION_SEND);
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_STREAM,getLocalBitmapUri(resource));
                        ShowWallpaper.this.startActivity(intent.createChooser(intent,"Motivation"));
                    }
                });


    }
    private Uri getLocalBitmapUri(Bitmap bmp)
    {
        Uri bmpUri=null;
        File file=new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "Motivation"+System.currentTimeMillis()+".png");
        try {
            FileOutputStream out=new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG,90,out);
            out.close();
            bmpUri=Uri.fromFile(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
    private Uri saveWallpaperAndGetUri(Bitmap bitmap,String id) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                intent.setData(uri);
                ShowWallpaper.this.startActivity(intent);
            } else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},10000);
            }
            return null;
        }
        File folder=new File(Environment.getExternalStorageDirectory().toString(),"/Motivation");
        folder.mkdirs();
        File file=new File(folder,id+".jpg");
        try {
            FileOutputStream out=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
            out.flush();
            out.close();
            return  Uri.fromFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
