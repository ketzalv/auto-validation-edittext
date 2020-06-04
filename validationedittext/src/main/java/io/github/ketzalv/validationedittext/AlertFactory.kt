package io.github.ketzalv.validationedittext

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

/**
 * Factory thats create AlertDialogs when is neededs
 */
object AlertFactory {
    @JvmStatic
    fun showPickerDialg(context: Context?, title: String?, items: Array<String?>?, callback: DialogInterface.OnClickListener?) {
        if (items == null || context == null) {
            return
        }
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
                .setCancelable(true)
                .setItems(items, callback)
                .create()
                .show()
    }
}