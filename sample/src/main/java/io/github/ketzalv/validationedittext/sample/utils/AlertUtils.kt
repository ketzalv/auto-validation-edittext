package io.github.ketzalv.validationedittext.sample.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import io.github.ketzalv.validationedittext.sample.R

object AlertUtils {
    fun showGenericAlert(context: Context?, cancelable: Boolean, @StringRes title: Int, message: String?) {
        val builder = AlertDialog.Builder(context!!, R.style.Theme_MaterialComponents_Light_Dialog_Alert)
        builder.setTitle(title)
                .setCancelable(cancelable)
                .setMessage(message)
                .setPositiveButton(R.string.accept) { dialog, which -> }
                .setOnCancelListener { }
                .create()
                .show()
    }
}