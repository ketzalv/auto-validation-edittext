package io.github.ketzalv.validationedittext.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.ketzalv.validationedittext.ValidationEditText
import io.github.ketzalv.validationedittext.sample.LoginFragment.Companion.convertHashMapToString
import io.github.ketzalv.validationedittext.sample.base.BaseFragment
import io.github.ketzalv.validationedittext.sample.base.IValidateFormsCycle
import io.github.ketzalv.validationedittext.sample.utils.AlertUtils.showGenericAlert
import kotlinx.android.synthetic.main.fragment_register.*
import java.util.*

class RegisterFragment : BaseFragment(), IValidateFormsCycle, View.OnClickListener {
    private val requiredFields: MutableList<ValidationEditText> = ArrayList()
    private val data = HashMap<String, String>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if (view == null) {
            inflater.inflate(R.layout.fragment_register, container, false)
        } else view
    }

    override val fragmentTag: String?
        get() = TAG

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupRequiredFields(view)
    }

    private fun setupRequiredFields(view: View?) {
        if (view == null) {
            return
        }
        if (!requiredFields.isEmpty()) {
            requiredFields.clear()
        }
        requiredFields.add(edit_username)
        requiredFields.add(edit_email)
        requiredFields.add(edit_name)
        requiredFields.add(edit_lastname)
        requiredFields.add(edit_surname)
        requiredFields.add(edit_birthdate)
    }

    private fun initViews(view: View) {
        button_register?.setOnClickListener(this)
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
        perfomSendData()
    }

    override fun onClick(v: View) {
        onValidateForm()
    }

    private fun perfomSendData() {
        showGenericAlert(activity,
                true,
                R.string.getdata,
                convertHashMapToString(data))
    }

    companion object {
        const val TAG = "RegisterFragment"
        fun newInstance(): RegisterFragment {
            val args = Bundle()
            val fragment = RegisterFragment()
            fragment.arguments = args
            return fragment
        }
    }
}