package com.techsdm.motivation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.VolumeShaper;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.service.wallpaper.WallpaperService;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.techsdm.motivation.Common.Common;
import com.techsdm.motivation.Model.WallpaperItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ShowWallpaper extends AppCompatActivity {

    int REQUEST_SET_LIVE_WALLPAPER = 200;
    Bitmap bitmap;
    URL url;
    RequestOptions option;
    String usernames,userphotos;
    ImageView userphoto;
    TextView username;
    LottieAnimationView lt_loading_view,progress_bar;
    RelativeLayout root;
    int pot=0,land=0;
    Tab2 tab2;
    int width,height;
    String gofavorite="0",gorecent="0";
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
    FloatingActionButton fab_share,fab_set;
    String a;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_wallpaper);

        username=(TextView)findViewById(R.id.username);
        userphoto=(ImageView)findViewById(R.id.userphoto);

        root=(RelativeLayout)findViewById(R.id.root);

        favorite_uncheck_button=(ImageView)findViewById(R.id.favorite_uncheck_button);
        favorite_check_button=(ImageView)findViewById(R.id.favorite_check_button);
        favorite_check_button.setVisibility(View.INVISIBLE);
        favorite_uncheck_button.setVisibility(View.INVISIBLE);

        fab_add=(FloatingActionButton)findViewById(R.id.fab_add);
        fab_download=(FloatingActionButton)findViewById(R.id.fab_download);
        fab_share=(FloatingActionButton)findViewById(R.id.fab_share);
        fab_set=(FloatingActionButton)findViewById(R.id.fab_set);

        fabOpen=AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose=AnimationUtils.loadAnimation(this, R.anim.fab_close);
        rotateForward=AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotateBackward=AnimationUtils.loadAnimation(this, R.anim.rotate_backward);
        onclick=AnimationUtils.loadAnimation(this,R.anim.onclick);
        favoriteButtonSelect=AnimationUtils.loadAnimation(this,R.anim.favorite_button_select);


        option=new RequestOptions().override(Common.width,Common.height).placeholder(R.drawable.animation_list).error(R.drawable.animation_list);
        a="m";
        progress_bar=(LottieAnimationView)findViewById(R.id.progress_bar);

        lt_loading_view = (LottieAnimationView) findViewById(R.id.lt_loading_view);
        lt_loading_view.setVisibility(View.VISIBLE);
        lt_loading_view.loop(true);
        lt_loading_view.playAnimation();
        imageView=(ImageView)findViewById(R.id.imageView);

        /*ImageLoaderConfiguration configuration= new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(configuration);

        ImageLoader imageLoader=ImageLoader.getInstance();
        imageLoader.displayImage(Common.imageLink, imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                bitmap=loadedImage;

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });*/
        //Showing data like wallpaper,userphot,username
        Glide.with(this).load(Common.imageLink).apply(option).into(imageView);
        Glide.with(this).load(Common.userphoto).into(userphoto);
        //Toast.makeText(getApplicationContext(),"Username"+Common.username,Toast.LENGTH_SHORT).show();

        username.setText(Common.username);

        lt_loading_view.setVisibility(View.INVISIBLE);






        DatabaseHelper dbs=new DatabaseHelper(getBaseContext());
        Boolean check1=dbs.check(Common.imageLink);
        /*if(check1==true)
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
        }*/

    }


    /*public void favorite_uncheck_button_click(View view)     //For Check
    {
        favorite_check_button.setVisibility(View.VISIBLE);
        favorite_uncheck_button.setVisibility(View.GONE);
        favorite_check_button.startAnimation(favoriteButtonSelect);

        DatabaseHelper dbs=new DatabaseHelper(getBaseContext());

        Toast.makeText(getApplicationContext(),"Data"+Common.imageLink+","+Common.userphoto+","+Common.username,Toast.LENGTH_SHORT).show();
        Boolean check=dbs.add_data(Common.imageLink,Common.userphoto,Common.username,String.valueOf(Common.width),String.valueOf(Common.height));
        if(check==true)
        {
            Toast.makeText(getApplicationContext(),"Added to Favorites",Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(),Common.userphoto+Common.username,Toast.LENGTH_SHORT).show();
        }
        else {
         Toast.makeText(getApplicationContext(),"Error in Adding",Toast.LENGTH_SHORT).show();
        }



    }

    public void favorite_check_button_click(View view)   //For Uncheck
    {
        favorite_check_button.setVisibility(View.GONE);
        favorite_check_button.setVisibility(View.INVISIBLE);
        favorite_uncheck_button.setVisibility(View.VISIBLE);
        favorite_uncheck_button.startAnimation(favoriteButtonSelect);

        DatabaseHelper dbs=new DatabaseHelper(getBaseContext());
        Common.imageLink=Common.imageLink.toString().trim();
        dbs.delete(Common.imageLink);


    }*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(Common.going_from_FindActivity==1)
        {
            Common.going_from_FindActivity=0;
            Intent intent=new Intent(getApplicationContext(),FindActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent=new Intent(getApplicationContext(),Tabbed_Activity.class);
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
            fab_set.startAnimation(fabClose);
            fab_download.setClickable(false);
            fab_share.setClickable(false);
            fab_set.setClickable(false);
            isOpen=false;
        }
        else
        {
            fab_add.startAnimation(rotateBackward);
            fab_download.startAnimation(fabOpen);
            fab_share.startAnimation(fabOpen);
            fab_set.startAnimation(fabOpen);
            fab_download.setClickable(true);
            fab_share.setClickable(true);
            fab_set.setClickable(true);
            isOpen=true;
        }
    }

    public void fab_download_click(View view)
    {

        Glide.with(this)
                .asBitmap()
                .load(Common.imageLink)
                .into(new SimpleTarget<Bitmap>() {
                          @Override
                          public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {

                              Intent intent = new Intent(Intent.ACTION_VIEW);


                              Uri uri = saveWallpaperAndGetUri(resource, Common.username);

                              if (uri != null) {
                                  intent.setDataAndType(uri, "image/*");
                                  getApplicationContext().startActivity(Intent.createChooser(intent, "Motivation"));
                                  intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                              }
                          }
                      }
                );

    }

    public Uri  saveWallpaperAndGetUri(Bitmap bitmap,String id) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat
                    .shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);

                this.startActivity(intent);

            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
            return null;
        }

        File folder = new File(Environment.getExternalStorageDirectory().toString() + "/motivation");
        folder.mkdirs();

        File file = new File(folder, id + ".jpg");
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            return Uri.fromFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 10000:
            {
                if(grantResults.length>0&& grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    saveWallpaperAndGetUri();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Enable Permission to Download Image",Toast.LENGTH_SHORT).show();
                }
            }

        }
    }*/

    public void fab_share_click(View view)
    {
        fab_share.startAnimation(onclick);
        Glide.with(ShowWallpaper.this)
                .asBitmap()
                .load(Common.imageLink)
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


    public void fab_set_click(View view)
    {
        progress_bar.setVisibility(View.VISIBLE);
        progress_bar.loop(true);
        progress_bar.playAnimation();



        //if(bitmap!=null)
        //{
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(ShowWallpaper.this);

                //wallpaperManager.ACTION_CROP_AND_SET_WALLPAPER;
                /*wallpaperManager.getCropAndSetWallpaperIntent(Uri.parse(Common.imageLink));
                wallpaperManager.setBitmap(bitmap);
                wallpaperManager.suggestDesiredDimensions(720,1280);
                Toast.makeText(this, "Wallpaper Set", Toast.LENGTH_SHORT).show();*/

                Intent intent;
                ComponentName component = new ComponentName(getPackageName(), getPackageName() + ".LiveWallpaperService");
                intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, component);
                startActivityForResult(intent, REQUEST_SET_LIVE_WALLPAPER);

        //}
        //else
        //{
          //  Toast.makeText(getApplicationContext(),"Bitmap is not", Toast.LENGTH_SHORT).show();
        //}




        //Glide.with(this).load(Common.imageLink).apply(option).into((ImageView) target);

        /*try {
            url=new URL(Common.imageLink);
            Toast.makeText(getApplicationContext(),"URL"+url,Toast.LENGTH_SHORT).show();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
        }


        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        try {
            wallpaperManager.setBitmap(bitmap);

            wallpaperManager.suggestDesiredDimensions(720,1280);
            Toast.makeText(this, "Wallpaper Set", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }*/
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
}