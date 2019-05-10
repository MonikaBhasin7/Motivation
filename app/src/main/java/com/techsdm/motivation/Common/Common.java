package com.techsdm.motivation.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.techsdm.motivation.Model.WallpaperItem;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

/**
 * Created by Monika Bhasin on 14-08-2018.
 */

public class Common {

    public static String collection_id="collection_id";   //collection_id
    public static String page_number="page_number";   //collection_id
    public static List<String> collection_list = new ArrayList<String>();
    public static String collection_index="collection_index";     //Index of Collection

    public static String search_query="random";

    public static String imageLink=null;
    public static int width;
    public static int height;
    public static String username;
    public static String userphoto;

    public static int going_from_FindActivity;

    public static int display_width;
    public static int display_height;

    public static String query=null;






    public static boolean isConnectedToInternet(Context context)
    {
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null)
        {
            NetworkInfo[] info=connectivityManager.getAllNetworkInfo();
            if(info!=null)
            {
                for(int i=0;i<info.length;i++)
                {
                    if(info[i].getState()== NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
