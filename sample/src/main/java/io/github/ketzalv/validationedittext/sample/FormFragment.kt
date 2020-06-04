package io.github.ketzalv.validationedittext.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import io.github.ketzalv.validationedittext.ValidationEditText
import io.github.ketzalv.validationedittext.ValidationEditText.OnValidationListener
import io.github.ketzalv.validationedittext.ValidationEditText.OptionsListener
import io.github.ketzalv.validationedittext.ValidationType
import io.github.ketzalv.validationedittext.sample.base.BaseFragment
import io.github.ketzalv.validationedittext.sample.utils.AlertUtils.showGenericAlert
import kotlinx.android.synthetic.main.fragment_form.*
import java.util.*

class FormFragment : BaseFragment(), View.OnClickListener {
    private val addressRequiredFields: MutableList<ValidationEditText> = ArrayList()
    private val questionsRequiredFields: MutableList<ValidationEditText> = ArrayList()
    private val contactRequiredFields: MutableList<ValidationEditText> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if (view == null) {
            inflater.inflate(R.layout.fragment_form, container, false)
        } else view
    }

    override val fragmentTag: String?
        get() = TAG

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupFields(view)
    }

    private fun setupFields(view: View?) {
        if (view == null) {
            return
        }
        if (!addressRequiredFields.isEmpty()) {
            addressRequiredFields.clear()
        }
        addressRequiredFields.add(edit_street)
        addressRequiredFields.add(edit_ext_num)
        addressRequiredFields.add(edit_zip_code)
        addressRequiredFields.add(edit_city)
        addressRequiredFields.add(edit_state)
        if (questionsRequiredFields.isNotEmpty()) {
            questionsRequiredFields.clear()
        }
        edit_question3.setPickerOptions(arrayOf("Yes I'm from Italia", "Not i'm from Germany"), null)
        questionsRequiredFields.add(edit_question1)
        questionsRequiredFields.add(edit_question2)
        questionsRequiredFields.add(edit_question3)
        questionsRequiredFields.add(edit_question4)
        if (contactRequiredFields.isNotEmpty()) {
            contactRequiredFields.clear()
        }
        contactRequiredFields.add(edit_email)
        contactRequiredFields.add(edit_comments)
    }

    private fun initViews(view: View?) {
        if (view == null) {
            return
        }
        button_add_address?.setOnClickListener(this)
        button_add_form?.setOnClickListener(this)
        button_send_comments?.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_add_address -> doValidateAddress()
            R.id.button_add_form -> doValidateAddForm()
            R.id.button_send_comments -> doValidateSendComents()
        }
    }

    private fun doValidateAddress() {
        val isInvalidForm = isValidFields(addressRequiredFields)
        if (!isInvalidForm) {
            onValidationAddressSuccess()
        }
    }

    private fun doValidateAddForm() {
        val isInvalidForm = isValidFields(questionsRequiredFields)
        if (!isInvalidForm) {
            onValidationQuestionSuccess()
        }
    }

    private fun doValidateSendComents() {
        val isInvalidForm = isValidFields(contactRequiredFields)
        if (!isInvalidForm) {
            onValidationContactSuccess()
        }
    }

    private fun onValidationContactSuccess() {
        performActionForm(onGetDataForm(contactRequiredFields))
    }

    private fun onValidationAddressSuccess() {
        //non requiredfields
        val addressData = onGetDataForm(addressRequiredFields)
        addressData[edit_int_num?.hint.toString()] = edit_int_num?.text.toString()
        perfomAddressForm(addressData)
    }

    private fun onValidationQuestionSuccess() {
        performActionForm(onGetDataForm(questionsRequiredFields))
    }

    private fun performActionForm(onGetDataForm: HashMap<String, String>) {
        showGenericAlert(
                activity,
                false,
                R.string.getdata,
                io.github.ketzalv.validationedittext.sample.LoginFragment.convertHashMapToString(onGetDataForm)
        )
    }

    private fun perfomAddressForm(addressData: HashMap<String, String>) {
        showGenericAlert(
                activity,
                false,
                R.string.getdata,
                io.github.ketzalv.validationedittext.sample.LoginFragment.convertHashMapToString(addressData)
        )
    }

    private fun isValidFields(fields: List<ValidationEditText>): Boolean {
        var isInvalidForm = false
        for (editText in fields) {
            if (!editText.isValidField) {
                isInvalidForm = true
            }
        }
        return isInvalidForm
    }

    private fun onGetDataForm(fieldsRequireds: List<ValidationEditText>): HashMap<String, String> {
        val data = HashMap<String, String>()
        for (editText in fieldsRequireds) {
            data[editText.hint.toString()] = editText.text.toString()
        }
        return data
    }

    private fun setupEdittextProgramatically() {
        val validationEditText: ValidationEditText = ValidationEditText(activity!!, ValidationType.numberCurrency)
        validationEditText.isAutoValidateEnable = true
        validationEditText.isShowMessageError = true
        validationEditText.setHint("Type amount")
        validationEditText.emptyMessage = "Empty Field"
        validationEditText.errorMessage = "InvalidField"
        validationEditText.customLocale = Locale.CANADA
        validationEditText.setOnValidationListener(object : OnValidationListener {
            override fun onValidEditText(editText: ValidationEditText?, text: String?) {
                Toast.makeText(activity, "Text valid typed -> $text", Toast.LENGTH_SHORT).show()
            }

            override fun onInvalidEditText(editText: ValidationEditText?) {
                Toast.makeText(activity, "Edittext invalid -> " + editText!!.hint, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupOptionsEdittext(view: LinearLayout) {
        val validationEditText = ValidationEditText(activity!!, ValidationType.text)
        validationEditText.isAutoValidateEnable = true
        validationEditText.isShowMessageError = true
        validationEditText.setHint("Do you like a coffee?")
        validationEditText.emptyMessage = "Empty Field"
        validationEditText.errorMessage = "Invalid Response"
        validationEditText.setPickerOptions(arrayOf("Yes", "No", "Maybe"), object : OptionsListener {
            override fun onOptionSelected(editText: ValidationEditText?, option: String?) {
                Toast.makeText(activity, "Text valid typed -> $option", Toast.LENGTH_SHORT).show()
            }
        })
        view.addView(validationEditText)
    }

    companion object {
        const val TAG = "FormFragment"

        @JvmStatic
        fun newInstance(): FormFragment {
            val args = Bundle()
            val fragment = FormFragment()
            fragment.arguments = args
            return fragment
        }
    }
}