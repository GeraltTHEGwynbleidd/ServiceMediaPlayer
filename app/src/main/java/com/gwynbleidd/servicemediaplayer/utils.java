package com.gwynbleidd.servicemediaplayer;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;


public class utils {
    public static AlertDialog.Builder warningDialog(Context context, String message){
        final AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle("Warning");
        adb.setMessage(message);
        adb.setIcon(android.R.drawable.ic_dialog_alert);
        return adb;
    }
}
