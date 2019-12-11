package io.github.ketzalv.validationedittext;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

/**
 * Factory thats create AlertDialogs when is neededs
 */
public class AlertFactory {

    public static void showPickerDialg(Context context, String title, String[] items, DialogInterface.OnClickListener callback) {
        if (items == null || context == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setCancelable(true)
                .setItems(items, callback)
                .create()
                .show();
    }
}
