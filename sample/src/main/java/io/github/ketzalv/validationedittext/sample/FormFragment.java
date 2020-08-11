package io.github.ketzalv.validationedittext.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.github.ketzalv.validationedittext.ValidationEditText;
import io.github.ketzalv.validationedittext.ValidationType;
import io.github.ketzalv.validationedittext.sample.base.BaseFragment;
import io.github.ketzalv.validationedittext.sample.utils.AlertUtils;

public class FormFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = "FormFragment";

    private List<ValidationEditText> addressRequiredFields = new ArrayList<>();
    private List<ValidationEditText> questionsRequiredFields = new ArrayList<>();
    private List<ValidationEditText> contactRequiredFields = new ArrayList<>();

    public static FormFragment newInstance() {

        Bundle args = new Bundle();

        FormFragment fragment = new FormFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getView() == null){
            return inflater.inflate(R.layout.fragment_form, container, false);
        }
        return getView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupFields(view);
    }

    private void setupFields(View view) {
        if(view == null){
            return;
        }
        if(!addressRequiredFields.isEmpty()){
            addressRequiredFields.clear();
        }
        addressRequiredFields.add((ValidationEditText) view.findViewById(R.id.edit_street));
        addressRequiredFields.add((ValidationEditText) view.findViewById(R.id.edit_ext_num));
        addressRequiredFields.add((ValidationEditText) view.findViewById(R.id.edit_zip_code));
        addressRequiredFields.add((ValidationEditText) view.findViewById(R.id.edit_city));
        addressRequiredFields.add((ValidationEditText) view.findViewById(R.id.edit_state));

        if(!questionsRequiredFields.isEmpty()){
            questionsRequiredFields.clear();
        }
        ValidationEditText editQuestion2 = view.findViewById(R.id.edit_question2);

        ValidationEditText editQuestion3 = view.findViewById(R.id.edit_question3);
        editQuestion3.setPickerOptions(new String[]{"Yes I'm from Italia", "Not i'm from Germany"}, null);

        questionsRequiredFields.add((ValidationEditText) view.findViewById(R.id.edit_question1));
        questionsRequiredFields.add(editQuestion2);
        questionsRequiredFields.add(editQuestion3);
        questionsRequiredFields.add((ValidationEditText) view.findViewById(R.id.edit_question4));

        if(!contactRequiredFields.isEmpty()){
            contactRequiredFields.clear();
        }
        contactRequiredFields.add((ValidationEditText) view.findViewById(R.id.edit_email));
        contactRequiredFields.add((ValidationEditText) view.findViewById(R.id.edit_comments));
    }

    private void initViews(View view) {
        if(view == null){
            return;
        }
        view.findViewById(R.id.button_add_address).setOnClickListener(this);
        view.findViewById(R.id.button_add_form).setOnClickListener(this);
        view.findViewById(R.id.button_send_comments).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_add_address:
                doValidateAddress();
                break;
            case R.id.button_add_form:
                doValidateAddForm();
                break;
            case R.id.button_send_comments:
                doValidateSendComents();
                break;
        }
    }

    private void doValidateAddress() {
        boolean isInvalidForm = isValidFields(addressRequiredFields);
        if(!isInvalidForm){
            onValidationAddressSuccess();
        }
    }

    private void doValidateAddForm() {
        boolean isInvalidForm = isValidFields(questionsRequiredFields);
        if(!isInvalidForm){
            onValidationQuestionSuccess();
        }
    }

    private void doValidateSendComents() {
        boolean isInvalidForm = isValidFields(contactRequiredFields);
        if(!isInvalidForm){
            onValidationContactSuccess();
        }
    }

    private void onValidationContactSuccess() {
        performActionForm(onGetDataForm(contactRequiredFields));
    }

    private void onValidationAddressSuccess() {
        //non requiredfields
        ValidationEditText editIntNum = getView().findViewById(R.id.edit_int_num);
        HashMap<String, String> addressData = onGetDataForm(addressRequiredFields);
        addressData.put(editIntNum.getHint().toString(), editIntNum.getText().toString());
        perfomAddressForm(addressData);
    }

    private void onValidationQuestionSuccess() {
        performActionForm(onGetDataForm(questionsRequiredFields));
    }

    private void performActionForm(HashMap<String, String> onGetDataForm) {
        AlertUtils.showGenericAlert(
                getActivity(),
                false,
                R.string.getdata,
                LoginFragment.convertHashMapToString(onGetDataForm)
        );
    }

    private void perfomAddressForm(HashMap<String, String> addressData) {
        AlertUtils.showGenericAlert(
                getActivity(),
                false,
                R.string.getdata,
                LoginFragment.convertHashMapToString(addressData)
        );
    }

    private boolean isValidFields(List<ValidationEditText> fields){
        boolean isInvalidForm = false;
        for(ValidationEditText editText: fields){
            if(!editText.isValidField()){
                isInvalidForm = true;
            }
        }
        return isInvalidForm;
    }

    private HashMap<String, String> onGetDataForm(List<ValidationEditText> fieldsRequireds){
        HashMap<String, String> data = new HashMap<>();
        for(ValidationEditText editText: fieldsRequireds){
            data.put(editText.getHint().toString(), editText.getText().toString());
        }
        return data;
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    private void setupEdittextProgramatically(){
        ValidationEditText validationEditText = new ValidationEditText(getActivity(), ValidationType.numberCurrency);
        validationEditText.setAutoValidateEnable(true);
        validationEditText.setShowMessageError(true);
        validationEditText.setHint("Type amount");
        validationEditText.setEmptyMessage("Empty Field");
        validationEditText.setErrorMessage("InvalidField");
        validationEditText.setCustomLocale(Locale.CANADA);
        validationEditText.setOnValidationListener(new ValidationEditText.OnValidationListener() {
            @Override
            public void onValidEditText(ValidationEditText editText, String text) {
                Toast.makeText(getActivity(), "Text valid typed -> " + text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInvalidEditText(ValidationEditText editText) {
                Toast.makeText(getActivity(), "Edittext invalid -> " + editText.getHint(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setupOptionsEdittext(LinearLayout view){
        ValidationEditText validationEditText = new ValidationEditText(getActivity(), ValidationType.text);
        validationEditText.setAutoValidateEnable(true);
        validationEditText.setShowMessageError(true);
        validationEditText.setHint("Do you like a coffee?");
        validationEditText.setEmptyMessage("Empty Field");
        validationEditText.setErrorMessage("Invalid Response");
        validationEditText.setPickerOptions(new String[]{"Yes", "No", "Maybe"}, new ValidationEditText.OptionsListener() {
            @Override
            public void onOptionSelected(ValidationEditText editText, String option) {
                Toast.makeText(getActivity(), "Text valid typed -> " + option, Toast.LENGTH_SHORT).show();
            }
        });
        view.addView(validationEditText);
    }
}
