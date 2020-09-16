package io.github.ketzalv.validationedittext;
/*
 * Created by Alberto Vazquez on 1/11/19 11:41 AM
 * Copyright (c) 2019. MIT License.
 * Last modified 1112/19 01:00 PM
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;
import java.util.regex.Pattern;

import static io.github.ketzalv.validationedittext.ValidationUtils.parseCurrencyAmount;
import static io.github.ketzalv.validationedittext.ValidationUtils.parseCurrencyAmountWithoutDecimal;


public class ValidationEditText extends AppCompatEditText implements TextWatcher {

    //region Constants
    private static final int CONST_POSTAL_CODE_SIZE = 5; //MX
    private static final int CONST_CELLPHONE_SIZE = 10; //MX
    private static final int CONST_CURP_SIZE = 18;
    //endregion


    private ValidationType mFormatType = ValidationType.defaulttype;
    private Locale customLocale = Locale.getDefault();


    //region flags
    private boolean mIsAutoValidateEnable = true;
    private boolean mShowMessageError = true;
    private boolean mFirstime = true;
    //endregion

    //region data
    private String mCurrentString = "";
    private double mMaxMount = 0;
    private double mMinMount = 0;
    private String mRegularExpression;
    //endregion

    //region messages
    private String mEmptyMessage;
    private String mErrorMessage;
    //endregion

    //region parameters
    @DrawableRes
    private int drawableOptions = R.drawable.ic_expand_more;
    private String[] options;
    private OnValidationListener mAutoValidate;

    //endregion

    //region constructors
    public ValidationEditText(Context context) {
        super(context);
        init(context, null);
    }

    public ValidationEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ValidationEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    public ValidationEditText(Context context, AttributeSet attrs, ValidationType type) {
        super(context, attrs);
        if(type != null){
        mFormatType = type;
        }
        init(context, attrs);
    }
    public ValidationEditText(Context context, ValidationType type) {
        super(context);
        if(type != null){
            mFormatType = type;
        }
        init(context, null);
    }

    //endregion

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.ValidationEditText,
                    0, 0);
            try {
                mFormatType = ValidationType.fromId(typedArray.getInt(R.styleable.ValidationEditText_format, -11));
                mIsAutoValidateEnable = typedArray.getBoolean(R.styleable.ValidationEditText_autoValidate, false);
                mShowMessageError = typedArray.getBoolean(R.styleable.ValidationEditText_showErrorMessage, false);
                mEmptyMessage = typedArray.getString(R.styleable.ValidationEditText_errorEmptyMessage);
                mErrorMessage = typedArray.getString(R.styleable.ValidationEditText_errorMessage);
                mRegularExpression = typedArray.getString(R.styleable.ValidationEditText_regularExpression);
                mMinMount = typedArray.getFloat(R.styleable.ValidationEditText_minAmount, 0);
                mMaxMount = typedArray.getFloat(R.styleable.ValidationEditText_maxAmount, 0);
                drawableOptions = typedArray.getResourceId(R.styleable.ValidationEditText_drawableOptions, R.drawable.ic_expand_more);
                try {
                    int id = typedArray.getResourceId(R.styleable.ValidationEditText_options, 0);
                    if (id != 0) {
                        options = getResources().getStringArray(id);
                    }
                } catch (Exception e) {

                }
            } catch (Exception e) {
                mFormatType = ValidationType.defaulttype;
            } finally {
                typedArray.recycle();
            }
        }
        super.addTextChangedListener(this);
        configureType(mFormatType);
        if (options != null && options.length > 0) {
            setPickerOptions(options, null);
        }
    }

    private void configureType(ValidationType mFormatType) {
        if (!mFormatType.equals(ValidationType.defaulttype)) {
            switch (mFormatType) {
                case email:
                    this.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    this.setMaxLines(1);
                    break;
                case numberCurrency:
                case numberCurrencyRounded:
                case number:
                    this.setMaxLines(1);
                    this.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
                case zipcode:
                    this.setMaxLines(1);
                    this.setFilters(new InputFilter[]{new InputFilter.LengthFilter(CONST_POSTAL_CODE_SIZE)});
                    this.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
                case text:
                    this.setMaxLines(1);
                    this.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
                case cellphone:
                    this.setMaxLines(1);
                    this.setFilters(new InputFilter[]{new InputFilter.LengthFilter(CONST_CELLPHONE_SIZE)});
                    this.setInputType(InputType.TYPE_CLASS_PHONE);
                    break;
                case phone:
                    this.setMaxLines(1);
                    this.setInputType(InputType.TYPE_CLASS_PHONE);
                    break;
                case curp:
                    this.setMaxLines(1);
                    this.setFilters(new InputFilter[]{
                            new InputFilter.LengthFilter(CONST_CURP_SIZE), //longitud
                            new InputFilter.AllCaps()});// input mayusculas
                    this.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
                case personName:
                    this.setMaxLines(1);
                    this.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                    break;
                case password:
                    this.setMaxLines(1);
                    Typeface cache = this.getTypeface();
                    this.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    this.setTypeface(cache);
                    break;
                case date:
                    this.setMaxLines(1);
                    this.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
                    break;
            }
        }
    }

    @Override
    public CharSequence getHint() {
        if (super.getHint() == null && getInputLayoutContainer() != null) {
            return getInputLayoutContainer().getHint();
        }
        return super.getHint();
    }

    public TextInputLayout getInputLayoutContainer() {
        if (this.getParent() != null) {
            if (this.getParent() instanceof TextInputLayout) {
                return (TextInputLayout) this.getParent();
            } else {
                if (this.getParent().getParent() != null && this.getParent().getParent() instanceof TextInputLayout) {
                    return (TextInputLayout) this.getParent().getParent();
                }
            }
        }
        return null;
    }


    public boolean isValidField() {
        return validateEditText(mFormatType, mCurrentString);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!mFormatType.equals(ValidationType.defaulttype)) {
            ValidationEditText.this.removeTextChangedListener(this);
            String currentString = s.toString();
            switch (mFormatType) {
                case numberCurrencyRounded:
                    if (currentString.length() == 0) {
                        ValidationEditText.this.setText("");
                        mFirstime = true;
                        mCurrentString = "";
                    } else if (!currentString.equalsIgnoreCase(mCurrentString)) {
                        String formatted = ValidationUtils.cashFormat(customLocale, parseCurrencyAmountWithoutDecimal(s.toString()));
                        mCurrentString = formatted;
                        ValidationEditText.this.setText(formatted);
                        ValidationEditText.this.setSelection(formatted.length());
                    }
                    break;
                case numberCurrency://numberCurrency
                    if (currentString.length() == 0) {
                        ValidationEditText.this.setText("");
                        mFirstime = true;
                        mCurrentString = "";
                    } else if (!currentString.equalsIgnoreCase(mCurrentString)) {
                        String formatted = ValidationUtils.cashFormat(customLocale, (parseCurrencyAmount(s.toString()) / 100));
                        mCurrentString = formatted;
                        ValidationEditText.this.setText(formatted);
                        ValidationEditText.this.setSelection(formatted.length());
                    }
                    break;
                default:
                    mCurrentString = currentString;
                    break;
            }
            if (mIsAutoValidateEnable && !mFirstime) {
                validateEditText(mFormatType, mCurrentString);
            }
            ValidationEditText.this.addTextChangedListener(this);
            mFirstime = false;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    private boolean validateEditText(ValidationType mFormatType, String currentString) {
        boolean validField = true;
        String errorMessage = null;
        if (TextUtils.isEmpty(currentString.trim())) {
            errorMessage = mEmptyMessage != null ? mEmptyMessage : getContext().getString(R.string.msg_empty_edittext);
            validField = false;
        } else if (mRegularExpression != null) {
            Pattern customizePattern = Pattern.compile(mRegularExpression);
            validField = customizePattern.matcher(currentString).matches();
            if (!validField) {
                errorMessage = mErrorMessage != null ? mErrorMessage : getContext().getString(R.string.msg_invalid_edittext);
            }
        } else {
            switch (mFormatType) {
                case email:
                    validField = ValidateFormsUtils.isValidEmailAddress(currentString);
                    if (!validField) {
                        errorMessage = mErrorMessage != null ? mErrorMessage : getContext().getString(R.string.msg_invalid_edittext);
                    }
                    break;
                case numberCurrencyRounded:
                case numberCurrency:
                    double currentMount = (parseCurrencyAmount(currentString) / 100);
                    if ((currentMount == 0 || currentMount < mMinMount) || ((currentMount > mMaxMount) && mMaxMount > 0)) {
                        validField = false;
                        errorMessage = getContext().getString(R.string.msg_invalid_edittext_currency, ValidationUtils.cashFormat(customLocale, mMinMount), ValidationUtils.cashFormat(customLocale, mMaxMount));
                    }
                    break;
                case phone:
                case cellphone:
                    validField = ValidateFormsUtils.isValidPhone(currentString);
                    if (!validField) {
                        errorMessage = mErrorMessage != null ? mErrorMessage : getContext().getString(R.string.msg_invalid_edittext);
                    }
                    break;
                case curp:
                    validField = currentString.length() == 18;
                    if(!validField){
                        errorMessage = mErrorMessage != null ? mErrorMessage : getContext().getString(R.string.msg_invalid_edittext);
                    }
                    break;
            }
        }
        if (mAutoValidate != null) {
            if (validField) {
                mAutoValidate.onValidEditText(ValidationEditText.this, mCurrentString);
            } else {
                mAutoValidate.onInvalidEditText(ValidationEditText.this);
            }
        }
        if (mShowMessageError) {
            setErrorTextInputLayout(errorMessage);
        }
        return validField;
    }

    private void setErrorTextInputLayout(String errorMessage) {
        if (getInputLayoutContainer() != null) {
            getInputLayoutContainer().setError(errorMessage);
        } else {
            super.setError(errorMessage);
        }
    }

    private void setErrorTextInputLayout(CharSequence errorMessage) {
        if (getInputLayoutContainer() != null) {
            getInputLayoutContainer().setError(errorMessage);
        } else {
            super.setError(errorMessage);
        }
    }

    @Override
    public void setError(CharSequence error) {
        setErrorTextInputLayout(error);
    }

    public void setPickerOptions(final String[] options, final OptionsListener listener) {
        if (options == null) {
            return;
        }
        enablePickerMode(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertFactory.showPickerDialg(
                        getContext(),
                        getHint().toString(),
                        options,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setText(options[which]);
                                if (listener != null) {
                                    listener.onOptionSelected(ValidationEditText.this, options[which]);
                                }
                            }
                        }
                );
            }
        });
    }

    public void setDrawableOptions(int drawableOptions) {
        this.drawableOptions = drawableOptions;
        setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableOptions, 0);
    }

    public void removeDrawableOptions() {
        setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }

    public void enablePickerMode(View.OnClickListener listener) {
        setOnClickListener(listener);
        setLongClickable(false);
        setClickable(true);
        setFocusable(false);
        setInputType(InputType.TYPE_NULL);
        setCursorVisible(false);
        setDrawableOptions(drawableOptions);
    }

    public void disablePickerMode() {
        setOnClickListener(null);
        setLongClickable(true);
        setClickable(false);
        setFocusable(true);
        setCursorVisible(true);
        removeDrawableOptions();
        configureType(mFormatType);
    }

    public interface OptionsListener {
        void onOptionSelected(ValidationEditText editText, String option);
    }

    public interface OnValidationListener {
        void onValidEditText(ValidationEditText editText, String text);

        void onInvalidEditText(ValidationEditText editText);
    }

    //region Getters&Setters

    public boolean isAutoValidateEnable() {
        return mIsAutoValidateEnable;
    }

    public void setAutoValidateEnable(boolean mIsAutoValidateEnable) {
        this.mIsAutoValidateEnable = mIsAutoValidateEnable;
    }

    public void setOnValidationListener(OnValidationListener mAutoValidate) {
        this.mAutoValidate = mAutoValidate;
    }

    public boolean isShowMessageError() {
        return mShowMessageError;
    }

    public void setShowMessageError(boolean mShowMessageError) {
        this.mShowMessageError = mShowMessageError;
    }

    public double getMaxMount() {
        return mMaxMount;
    }

    public void setMaxMount(double mMaxMount) {
        this.mMaxMount = mMaxMount;
    }

    public double getMinMount() {
        return mMinMount;
    }

    public void setMinMount(double mMinMount) {
        this.mMinMount = mMinMount;
    }

    public String getRegularExpression() {
        return mRegularExpression;
    }

    public void setRegularExpression(String mRegularExpression) {
        this.mRegularExpression = mRegularExpression;
    }

    public String getEmptyMessage() {
        return mEmptyMessage;
    }

    public void setEmptyMessage(String mEmptyMessage) {
        this.mEmptyMessage = mEmptyMessage;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public void setErrorMessage(String mErrorMessage) {
        this.mErrorMessage = mErrorMessage;
    }

    public Locale getCustomLocale() {
        return customLocale;
    }

    public void setCustomLocale(Locale customLocale) {
        this.customLocale = customLocale;
    }

    public Double getAmount(){
        switch (mFormatType){
            case numberCurrency:
            case numberCurrencyRounded:
                return parseCurrencyAmount(this.getText().toString());
            default:
                return 0d;
        }
    }

    public ValidationType getFormatType() {
        return mFormatType;
    }

    public void setFormatType(ValidationType mFormatType) {
        if(mFormatType != null){
            this.mFormatType = mFormatType;
            configureType(mFormatType);
        }
    }

    public int getDrawableOptions() {
        return drawableOptions;
    }


    //endregion
}