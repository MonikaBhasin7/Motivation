package com.techsdm.motivation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(IMAGE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public Boolean add_data(String image_data)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("IMAGE",image_data);
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


        if(res.moveToFirst())
        {
            do {
                String a=res.getString(res.getColumnIndex(IMAGE));
                WallpaperItem wallpaperItem=new WallpaperItem(a);
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
