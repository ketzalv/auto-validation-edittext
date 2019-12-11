package io.github.ketzalv.validationedittext.sample.utils;

import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import io.github.ketzalv.validationedittext.sample.R;

public class AlertUtils {

    public static void showGenericAlert(Context context, boolean cancelable, @StringRes int title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        builder.setTitle(title)
                .setCancelable(cancelable)
                .setMessage(message)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                })
                .create()
                .show();
    }
}

