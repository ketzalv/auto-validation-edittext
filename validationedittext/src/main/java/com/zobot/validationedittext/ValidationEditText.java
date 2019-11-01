/*
 * Created by Alberto Vazquez on 1/11/19 11:41 AM
 * Copyright (c) 2019. MIT License.
 * Last modified 1/11/19 11:40 AM
 */

package com.zobot.validationedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;



public class ValidationEditText extends AppCompatEditText {

    //region Constants
    private static final int CONST_POSTAL_CODE_SIZE = 5; //MX
    private static final int CONST_CELLPHONE_SIZE = 10; //MX
    //endregion

    private static final int ID_FORMAT_DEFAULT = -11;
    private static final int ID_FORMAT_EMAIL = 0;
    private static final int ID_FORMAT_PASSWORD = 1;
    private static final int ID_FORMAT_PHONE = 2;
    private static final int ID_FORMAT_ZIPCODE = 3;
    private static final int ID_FORMAT_TEXT = 4;
    private static final int ID_FORMAT_NUMBER = 5;
    private static final int ID_FORMAT_CELLPHONE = 6;
    private static final int ID_FORMAT_DATE = 7;
    private static final int ID_FORMAT_PERSONNAME = 8;
    private static final int ID_FORMAT_NUMBERCURRENCY = 9;
    private static final int ID_FORMAT_CURP = 10;
    private static final int ID_FORMAT_NUMBERCURRENCY_ROUNDED = 11;


    private int mFormatType = ID_FORMAT_DEFAULT;
    private ValidationTextWatcher textWatcher = new ValidationTextWatcher();
    private boolean mIsAutoValidateEnable = true;
    private boolean mShowMessageError = true;

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

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.ValidationEditText,
                    0, 0);
            try {
                mFormatType = typedArray.getInt(R.styleable.ValidationEditText_format, ID_FORMAT_DEFAULT);
                mIsAutoValidateEnable = typedArray.getBoolean(R.styleable.ValidationEditText_autoValidate, false);
                mShowMessageError = typedArray.getBoolean(R.styleable.ValidationEditText_showErrorMessage, false);
            } catch (Exception e) {
                mFormatType = ID_FORMAT_DEFAULT;
            } finally {
                typedArray.recycle();
            }
        }
        this.addTextChangedListener(textWatcher);
        configureType(mFormatType);
    }

    private void configureType(int mFormatType) {
        if (mFormatType != ID_FORMAT_DEFAULT) {
            switch (mFormatType) {
                case ID_FORMAT_EMAIL:
                    this.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    this.setMaxLines(1);
                    break;
                case ID_FORMAT_NUMBERCURRENCY:
                case ID_FORMAT_NUMBERCURRENCY_ROUNDED:
                case ID_FORMAT_NUMBER:
                    this.setMaxLines(1);
                    this.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
                case ID_FORMAT_ZIPCODE:
                    this.setMaxLines(1);
                    this.setFilters(new InputFilter[] {new InputFilter.LengthFilter(CONST_CELLPHONE_SIZE)});
                    this.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
                case ID_FORMAT_TEXT:
                    this.setMaxLines(1);
                    this.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
                case ID_FORMAT_CELLPHONE:
                    this.setMaxLines(1);
                    this.setFilters(new InputFilter[] {new InputFilter.LengthFilter(CONST_CELLPHONE_SIZE)});
                    this.setInputType(InputType.TYPE_CLASS_PHONE);
                    break;
                case ID_FORMAT_PHONE:
                    this.setMaxLines(1);
                    this.setInputType(InputType.TYPE_CLASS_PHONE);
                    break;
                case ID_FORMAT_CURP:
                    this.setMaxLines(1);
                    this.setFilters(new InputFilter[] {
                            new InputFilter.LengthFilter(18), //longitud
                            new InputFilter.AllCaps()});// input mayusculas
                    this.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
                case ID_FORMAT_PERSONNAME:
                    this.setMaxLines(1);
                    this.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                    break;
            }
        }
    }
}