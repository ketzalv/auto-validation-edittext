package io.github.ketzalv.validationedittext.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.ketzalv.validationedittext.ValidationEditText
import io.github.ketzalv.validationedittext.sample.base.BaseFragment
import io.github.ketzalv.validationedittext.sample.base.IValidateFormsCycle
import io.github.ketzalv.validationedittext.sample.utils.AlertUtils.showGenericAlert
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.*

class LoginFragment : BaseFragment(), IValidateFormsCycle, View.OnClickListener {
    private val requiredFields: MutableList<ValidationEditText> = ArrayList()
    private val data = HashMap<String, String>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if (view == null) {
            inflater.inflate(R.layout.fragment_login, container, false)
        } else view
    }

    override val fragmentTag: String?
        get() = TAG

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_log_in?.setOnClickListener(this)
        setRequiredFields(view)
    }

    private fun setRequiredFields(view: View?) {
        if (view == null) {
            return
        }
        if (!requiredFields.isEmpty()) {
            requiredFields.clear()
        }
        requiredFields.add(edit_email)
        requiredFields.add(edit_password)
    }

    override fun onClick(v: View) {
        onValidateForm()
    }

    override fun setValuesDefaultForm() {}
    override fun onValidateForm() {
        var isInvalidForm = false
        for (editText in requiredFields) {
            if (!editText.isValidField) {
                isInvalidForm = true
            }
        }
        if (isInvalidForm) {
            return
        }
        onValidationSuccess()
    }

    override fun onGetData() {
        if (!data.isEmpty()) {
            data.clear()
        }
        for (editText in requiredFields) {
            data[editText.hint.toString()] = editText.text.toString()
        }
    }

    override fun onValidationSuccess() {
        onGetData()
        perfomActionFormValid()
    }

    private fun perfomActionFormValid() {
        showGenericAlert(activity,
                true,
                R.string.getdata,
                convertHashMapToString(data))
    }

    companion object {
        const val TAG = "LoginFragment"
        fun newInstance(): LoginFragment {
            val args = Bundle()
            val fragment = LoginFragment()
            fragment.arguments = args
            return fragment
        }

        @JvmStatic
        fun convertHashMapToString(map: HashMap<String, String>): String {
            val builder = StringBuilder()
            for (key in map.keys) {
                builder.append(key)
                        .append(": ")
                        .append(map[key])
                        .append("\n")
            }
            return builder.toString()
        }
    }
}