/*
 * Created by Alberto Vazquez on 1/11/19 11:48 AM
 * Copyright (c) 2019. MIT License.
 * Last modified 1/11/19 11:48 AM
 */

package com.zobot.validationedittext;

import android.text.Editable;
import android.text.TextWatcher;

public class ValidationTextWatcher implements TextWatcher {

    private ValidationEditText editText;
    private ValidationType mFormatType = ValidationType.defaulttype;
    private String mCurrentString;

    public ValidationTextWatcher(ValidationType formatType,ValidationEditText editText){
        this.mFormatType = formatType;
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
//        if (!mFormatType.equals(ValidationType.defaulttype)) {
//            editText.removeTextChangedListener(this);
//            String currentString = s.toString();
//            switch (mFormatType) {
//                case numberCurrencyRounded:
//                    if (currentString.length() == 0) {
//                        editText.setText("");
//                        mCurrentString = "";
//                    } else if (!currentString.equalsIgnoreCase(mCurrentString)) {
//                        String formatted = UiUtils.cashFormat(parseCurrencyAmountWithoutDecimal(s.toString()));
//                        mCurrentString = formatted;
//                        editText.setText(formatted);
//                        editText.setSelection(formatted.length());
//                    }
//                    break;
//                case numberCurrency://numberCurrency
//                    if (currentString.length() == 0) {
//                        editText.setText("");
//                        mCurrentString = "";
//                    } else if (!currentString.equalsIgnoreCase(mCurrentString)) {
//                        String formatted = UiUtils.cashFormat((parseCurrencyAmount(s.toString()) / 100));
//                        mCurrentString = formatted;
//                        editText.setText(formatted);
//                        editText.setSelection(formatted.length());
//                    }
//                    break;
//                case zipcode:
//                    break;
//                default:
//                    mCurrentString = currentString;
//                    break;
//            }
//            if (mIsAutoValidateEnable && !mFirstime) {
//                editText.validateEditText(mFormatType, mCurrentString);
//            }
//            editText.addTextChangedListener(this);
//            mFirstime = false;
//        }
    }

    public String getCurrentString() {
        return mCurrentString;
    }

    public void setCurrentString(String mCurrentString) {
        this.mCurrentString = mCurrentString;
    }
}