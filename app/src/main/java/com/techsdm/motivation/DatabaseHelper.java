package com.techsdm.motivation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.techsdm.motivation.Model.WallpaperItem;

import java.util.ArrayList;
import java.util.List;

import static android.provider.Telephony.Carriers.PASSWORD;

/**
 * Created by Monika Bhasin on 05-08-2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME="User.db";
    public static final String TABLE_NAME="Favorite_data";
    public static String IMAGE="IMAGE";
    public static String USERPHOTO="USERPHOTO";
    public static String USERNAME="USERNAME";
    public static String WIDTH="WIDTH";
    public static String HEIGHT="HEIGHT";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(IMAGE TEXT, USERPHOTO TEXT, USERNAME TEXT,WIDTH TEXT,HEIGHT TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public Boolean add_data(String image_data, String user_photo,String user_name,String width, String height)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("IMAGE",image_data);
        cv.put("USERPHOTO",user_photo);
        cv.put("USERNAME",user_name);
        cv.put("WIDTH",width);
        cv.put("HEIGHT",height);
        long res=db.insert(TABLE_NAME,null,cv);
        if(res == -1)
            return false;
        else
            return true;
    }

    public boolean check(String category_name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);

        if(res.moveToFirst())
        {
            do {
                String a=res.getString(res.getColumnIndex(IMAGE));

                if(category_name.equals(a))
                {
                    return true;
                }
            }while (res.moveToNext());

        }
        return false;
    }

    public List<WallpaperItem> getCarts()
    {
        List<WallpaperItem> result;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        result = new ArrayList<>();
        res.moveToFirst();

        if(res.moveToFirst())
        {
            do {
                String a=res.getString(res.getColumnIndex(IMAGE));
                String b=res.getString(res.getColumnIndex(USERPHOTO));
                String c=res.getString(res.getColumnIndex(USERNAME));
                String d=res.getString(res.getColumnIndex(WIDTH));
                String e=res.getString(res.getColumnIndex(HEIGHT));
                int di=Integer.parseInt(d);
                int ei=Integer.parseInt(e);
                WallpaperItem wallpaperItem=new WallpaperItem(a,di,ei,"0",0,b,c);
                Log.d("image->",a);
                result.add(wallpaperItem);

            }while (res.moveToNext());

        }
        return result;
    }


    public void delete(String image_name)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        //db.delete(TABLE_NAME, IMAGE + "=" + image_name, null);
        db.delete(TABLE_NAME, IMAGE+ "=?", new String[]{String.valueOf(image_name)});
    }

}
