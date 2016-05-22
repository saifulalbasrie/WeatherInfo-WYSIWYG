package com.kressx_genesis.saifullah.wysiwyg.util;

import android.app.Activity;
import android.app.ProgressDialog;

import com.kressx_genesis.saifullah.wysiwyg.R;

/**
 * Created by Saifullah on 18/05/2016.
 */
public class Utility {

    private static ProgressDialog progressDialog;

    public static void showBusyIndicator(Activity activity, String msg){
        closeBusyIndicator();
        //progressDialog = ProgressDialog.show(activity, null, msg,true,true);
        progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(msg);
        progressDialog.show();

    }

    public static void closeBusyIndicator(){
        if(progressDialog!=null && progressDialog.isShowing())
            progressDialog.dismiss();

        progressDialog = null;
    }
}
