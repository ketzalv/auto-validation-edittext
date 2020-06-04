package io.github.ketzalv.validationedittext

import android.content.Context
import android.content.DialogInterface
import android.text.*
import android.util.AttributeSet
import android.view.View.OnClickListener
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputLayout
import io.github.ketzalv.validationedittext.AlertFactory.showPickerDialg
import io.github.ketzalv.validationedittext.ValidateFormsUtils.isValidEmailAddress
import io.github.ketzalv.validationedittext.ValidateFormsUtils.isValidPhone
import io.github.ketzalv.validationedittext.ValidationUtils.cashFormat
import io.github.ketzalv.validationedittext.ValidationUtils.parseCurrencyAmount
import io.github.ketzalv.validationedittext.ValidationUtils.parseCurrencyAmountWithoutDecimal
import java.util.*
import java.util.regex.Pattern

/**
 * Created by Alberto Vazquez on 1/11/19 11:41 AM
 * Copyright (c) 2019. MIT License.
 * Last modified 1112/19 01:00 PM
 */
class ValidationEditText : AppCompatEditText, TextWatcher {
    private var mFormatType = ValidationType.defaulttype
    var customLocale: Locale = Locale.getDefault()

    //region flags
    var isAutoValidateEnable = true

    //region Getters&Setters
    var isShowMessageError = true
    private var mFirstime = true

    //endregion
    //region data
    private var mCurrentString = ""
    private var maxMount: Double = 0.0
    private var minMount: Double = 0.0
    private var regularExpression: String? = null

    //endregion
    //region messages
    var emptyMessage: String? = null
    var errorMessage: String? = null

    //endregion
    //region parameters
    @DrawableRes
    private var drawableOptions = R.drawable.ic_expand_more
    private var options: Array<String?>? = emptyArray()
    private var mAutoValidate: OnValidationListener? = null

    //endregion
    //region constructors
    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, type: ValidationType?) : super(context, attrs) {
        if (type != null) {
            mFormatType = type
        }
        init(context, attrs)
    }

    constructor(context: Context, type: ValidationType?) : super(context) {
        if (type != null) {
            mFormatType = type
        }
        init(context, null)
    }

    //endregion
    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = context.theme.obtainStyledAttributes(
                    attrs,
                    R.styleable.ValidationEditText,
                    0, 0)
            try {
                mFormatType = ValidationType.fromId(typedArray.getInt(R.styleable.ValidationEditText_format, -11))
                isAutoValidateEnable = typedArray.getBoolean(R.styleable.ValidationEditText_autoValidate, false)
                isShowMessageError = typedArray.getBoolean(R.styleable.ValidationEditText_showErrorMessage, false)
                emptyMessage = typedArray.getString(R.styleable.ValidationEditText_errorEmptyMessage)
                errorMessage = typedArray.getString(R.styleable.ValidationEditText_errorMessage)
                regularExpression = typedArray.getString(R.styleable.ValidationEditText_regularExpression)
                minMount = typedArray.getFloat(R.styleable.ValidationEditText_minAmount, 0f).toDouble()
                maxMount = typedArray.getFloat(R.styleable.ValidationEditText_maxAmount, 0f).toDouble()
                drawableOptions = typedArray.getResourceId(R.styleable.ValidationEditText_drawableOptions, R.drawable.ic_expand_more)
                try {
                    val id = typedArray.getResourceId(R.styleable.ValidationEditText_options, 0)
                    options = resources.getStringArray(id)
                } catch (e: Exception) {
                }
            } catch (e: Exception) {
                mFormatType = ValidationType.defaulttype
            } finally {
                typedArray.recycle()
            }
        }
        super.addTextChangedListener(this)
        configureType(mFormatType)
        options?.let {
            if(it.isNotEmpty()){
                setPickerOptions(options, null)
            }
        }
    }

    private fun configureType(mFormatType: ValidationType) {
        if (mFormatType != ValidationType.defaulttype) {
            when (mFormatType) {
                ValidationType.email -> {
                    this.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    this.maxLines = 1
                }
                ValidationType.numberCurrency, ValidationType.numberCurrencyRounded, ValidationType.number -> {
                    this.maxLines = 1
                    this.inputType = InputType.TYPE_CLASS_NUMBER
                }
                ValidationType.zipcode -> {
                    this.maxLines = 1
                    this.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(CONST_POSTAL_CODE_SIZE))
                    this.inputType = InputType.TYPE_CLASS_NUMBER
                }
                ValidationType.text -> {
                    this.maxLines = 1
                    this.inputType = InputType.TYPE_CLASS_TEXT
                }
                ValidationType.cellphone -> {
                    this.maxLines = 1
                    this.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(CONST_CELLPHONE_SIZE))
                    this.inputType = InputType.TYPE_CLASS_PHONE
                }
                ValidationType.phone -> {
                    this.maxLines = 1
                    this.inputType = InputType.TYPE_CLASS_PHONE
                }
                ValidationType.curp -> {
                    this.maxLines = 1
                    this.filters = arrayOf(
                            InputFilter.LengthFilter(CONST_CURP_SIZE),  //longitud
                            InputFilter.AllCaps()) // input mayusculas
                    this.inputType = InputType.TYPE_CLASS_TEXT
                }
                ValidationType.personName -> {
                    this.maxLines = 1
                    this.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                }
                ValidationType.password -> {
                    this.maxLines = 1
                    this.inputType = InputType.TYPE_CLASS_TEXT or
                            InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
                ValidationType.date -> {
                    this.maxLines = 1
                    this.inputType = InputType.TYPE_CLASS_DATETIME or InputType.TYPE_DATETIME_VARIATION_DATE
                }
                else -> {
                    this.maxLines = 0
                    this.inputType = InputType.TYPE_CLASS_TEXT
                }
            }
        } else {
            this.maxLines = 0
            this.inputType = InputType.TYPE_CLASS_TEXT
        }
    }

    override fun getHint(): CharSequence {
        return if (super.getHint() == null && inputLayoutContainer != null) {
            inputLayoutContainer!!.hint!!
        } else super.getHint()
    }

    val inputLayoutContainer: TextInputLayout?
        get() {
            if (this.parent != null) {
                if (this.parent is TextInputLayout) {
                    return this.parent as TextInputLayout
                } else {
                    if (this.parent.parent != null && this.parent.parent is TextInputLayout) {
                        return this.parent.parent as TextInputLayout
                    }
                }
            }
            return null
        }

    val isValidField: Boolean
        get() = validateEditText(mFormatType, mCurrentString)

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(s: Editable) {
        if (mFormatType != ValidationType.defaulttype) {
            removeTextChangedListener(this)
            val currentString = s.toString()
            when (mFormatType) {
                ValidationType.numberCurrencyRounded -> if (currentString.isEmpty()) {
                    this@ValidationEditText.setText("")
                    mFirstime = true
                    mCurrentString = ""
                } else if (!currentString.equals(mCurrentString, ignoreCase = true)) {
                    val formatted: String = cashFormat(customLocale, parseCurrencyAmountWithoutDecimal(s.toString()))
                    mCurrentString = formatted
                    this@ValidationEditText.setText(formatted)
                    this@ValidationEditText.setSelection(formatted.length)
                }
                ValidationType.numberCurrency -> if (currentString.isEmpty()) {
                    this@ValidationEditText.setText("")
                    mFirstime = true
                    mCurrentString = ""
                } else if (!currentString.equals(mCurrentString, ignoreCase = true)) {
                    val formatted: String = cashFormat(customLocale, parseCurrencyAmount(s.toString()) / 100)
                    mCurrentString = formatted
                    this@ValidationEditText.setText(formatted)
                    this@ValidationEditText.setSelection(formatted.length)
                }
                else -> mCurrentString = currentString
            }
            if (isAutoValidateEnable && !mFirstime) {
                validateEditText(mFormatType, mCurrentString)
            }
            addTextChangedListener(this)
            mFirstime = false
        }
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

    private fun validateEditText(mFormatType: ValidationType, currentString: String): Boolean {
        var validField = true
        var errorMessage: String? = null
        if (TextUtils.isEmpty(currentString.trim { it <= ' ' })) {
            errorMessage = if (emptyMessage != null) emptyMessage else context.getString(R.string.msg_empty_edittext)
            validField = false
        } else if (regularExpression != null) {
            val customizePattern = Pattern.compile(regularExpression)
            validField = customizePattern.matcher(currentString).matches()
            if (!validField) {
                errorMessage = if (this.errorMessage != null) this.errorMessage else context.getString(R.string.msg_invalid_edittext)
            }
        } else {
            when (mFormatType) {
                ValidationType.email -> {
                    validField = isValidEmailAddress(currentString)
                    if (!validField) {
                        errorMessage = if (this.errorMessage != null) this.errorMessage else context.getString(R.string.msg_invalid_edittext)
                    }
                }
                ValidationType.numberCurrencyRounded, ValidationType.numberCurrency -> {
                    val currentMount: Double = parseCurrencyAmount(currentString) / 100
                    if (currentMount == 0.0 || currentMount < minMount || currentMount > maxMount && maxMount > 0) {
                        validField = false
                        errorMessage = context.getString(R.string.msg_invalid_edittext_currency, cashFormat(customLocale, minMount), cashFormat(customLocale, maxMount))
                    }
                }
                ValidationType.phone, ValidationType.cellphone -> {
                    validField = isValidPhone(currentString)
                    if (!validField) {
                        errorMessage = if (this.errorMessage != null) this.errorMessage else context.getString(R.string.msg_invalid_edittext)
                    }
                }
                ValidationType.curp -> {
                    validField = currentString.length == 18
                    if (!validField) {
                        errorMessage = if (this.errorMessage != null) this.errorMessage else context.getString(R.string.msg_invalid_edittext)
                    }
                }
            }
        }
        if (mAutoValidate != null) {
            if (validField) {
                mAutoValidate!!.onValidEditText(this@ValidationEditText, mCurrentString)
            } else {
                mAutoValidate!!.onInvalidEditText(this@ValidationEditText)
            }
        }
        if (isShowMessageError) {
            setErrorTextInputLayout(errorMessage)
        }
        return validField
    }

    private fun setErrorTextInputLayout(errorMessage: String?) {
        if (inputLayoutContainer != null) {
            inputLayoutContainer!!.error = errorMessage
        } else {
            super.setError(errorMessage)
        }
    }

    private fun setErrorTextInputLayout(errorMessage: CharSequence) {
        if (inputLayoutContainer != null) {
            inputLayoutContainer!!.error = errorMessage
        } else {
            super.setError(errorMessage)
        }
    }

    override fun setError(error: CharSequence) {
        setErrorTextInputLayout(error)
    }

    fun setPickerOptions(options: Array<String?>?, listener: OptionsListener?) {
        if (options == null) {
            return
        }
        enablePickerMode(OnClickListener {
            showPickerDialg(
                    context,
                    hint.toString(),
                    options,
                    DialogInterface.OnClickListener { dialog, which ->
                        setText(options[which])
                        listener?.onOptionSelected(this@ValidationEditText, options[which])
                    }
            )
        })
    }

    fun removeDrawableOptions() {
        setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
    }

    fun enablePickerMode(listener: OnClickListener?) {
        setOnClickListener(listener)
        isLongClickable = false
        isClickable = true
        isFocusable = false
        inputType = InputType.TYPE_NULL
        isCursorVisible = false
        setDrawableOptions(drawableOptions)
    }

    fun disablePickerMode() {
        setOnClickListener(null)
        isLongClickable = true
        isClickable = false
        isFocusable = true
        isCursorVisible = true
        removeDrawableOptions()
        configureType(mFormatType)
    }

    fun setOnValidationListener(mAutoValidate: OnValidationListener?) {
        this.mAutoValidate = mAutoValidate
    }

    val amount: Double
        get() = when (mFormatType) {
            ValidationType.numberCurrency, ValidationType.numberCurrencyRounded -> parseCurrencyAmount(this.text.toString())
            else -> 0.0
        }

    var formatType: ValidationType?
        get() = mFormatType
        set(mFormatType) {
            if (mFormatType != null) {
                this.mFormatType = mFormatType
                configureType(mFormatType)
            }
        }

    fun getDrawableOptions(): Int {
        return drawableOptions
    }

    fun setDrawableOptions(drawableOptions: Int) {
        this.drawableOptions = drawableOptions
        setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableOptions, 0)
    }

    interface OptionsListener {
        fun onOptionSelected(editText: ValidationEditText?, option: String?)
    }

    interface OnValidationListener {
        fun onValidEditText(editText: ValidationEditText?, text: String?)
        fun onInvalidEditText(editText: ValidationEditText?)
    } //endregion

    companion object {
        //region Constants
        private const val CONST_POSTAL_CODE_SIZE = 5 //MX
        private const val CONST_CELLPHONE_SIZE = 10 //MX
        private const val CONST_CURP_SIZE = 18
    }
}