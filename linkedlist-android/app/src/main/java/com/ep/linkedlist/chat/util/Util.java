package com.ep.linkedlist.chat.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

/**
 * Created by Alessandro Barreto on 23/06/2016.
 */
public class Util {

    public static final String URL_STORAGE_REFERENCE = "gs://linkedlist-566ea.appspot.com";
    public static final String FOLDER_STORAGE_IMG = "images";

    public static String local(String latitudeFinal,String longitudeFinal){
        return "https://maps.googleapis.com/maps/api/staticmap?center="+latitudeFinal+","+longitudeFinal+"&zoom=18&size=280x280&markers=color:red|"+latitudeFinal+","+longitudeFinal;
    }

}
