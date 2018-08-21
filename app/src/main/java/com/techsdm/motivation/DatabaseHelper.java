package com.techsdm.motivation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.Telephony.Carriers.PASSWORD;

/**
 * Created by Monika Bhasin on 05-08-2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME="User.db";
    public static final String TABLE_NAME="Favorite_data";
    public static final String ID="ID";
    public static String IMAGE="IMAGE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,IMAGE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long add_data(String image)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("IMAGE",image);
        long res=db.insert(TABLE_NAME,null,cv);
        return res;
    }


    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public int check_data(String image)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        //"SELECT * FROM MyTable WHERE " + username + " =? AND" + password + " =?", null
        String Query="SELECT * FROM +"+TABLE_NAME+ "WHERE"+ IMAGE + "=" + image;
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount()!=0)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

}
