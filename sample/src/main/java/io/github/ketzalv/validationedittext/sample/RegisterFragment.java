package io.github.ketzalv.validationedittext.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.github.ketzalv.validationedittext.ValidationEditText;
import io.github.ketzalv.validationedittext.sample.base.BaseFragment;
import io.github.ketzalv.validationedittext.sample.base.IValidateFormsCycle;
import io.github.ketzalv.validationedittext.sample.utils.AlertUtils;

public class RegisterFragment extends BaseFragment implements IValidateFormsCycle, View.OnClickListener {

    public static final String TAG = "RegisterFragment";

    private List<ValidationEditText> requiredFields = new ArrayList<>();
    private HashMap<String, String> data = new HashMap<>();

    public static RegisterFragment newInstance() {

        Bundle args = new Bundle();

        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getView() == null){
            return inflater.inflate(R.layout.fragment_register, container, false);
        }
        return getView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupRequiredFields(view);
    }

    private void setupRequiredFields(View view) {
        if(view == null){
            return;
        }
        if(!requiredFields.isEmpty()){
            requiredFields.clear();
        }
        requiredFields.add((ValidationEditText) view.findViewById(R.id.edit_username));
        requiredFields.add((ValidationEditText) view.findViewById(R.id.edit_email));
        requiredFields.add((ValidationEditText) view.findViewById(R.id.edit_name));
        requiredFields.add((ValidationEditText) view.findViewById(R.id.edit_lastname));
        requiredFields.add((ValidationEditText) view.findViewById(R.id.edit_surname));
        requiredFields.add((ValidationEditText) view.findViewById(R.id.edit_birthdate));

    }

    private void initViews(View view) {
        view.findViewById(R.id.button_register).setOnClickListener(this);
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    public void setValuesDefaultForm() {

    }

    @Override
    public void onValidateForm() {
        boolean isInvalidForm = false;
        for(ValidationEditText editText: requiredFields){
            if(!editText.isValidField()){
                isInvalidForm = true;
            }
        }
        if(isInvalidForm){
            return;
        }
        onValidationSuccess();
    }

    @Override
    public void onGetData() {
        if(!data.isEmpty()){
            data.clear();
        }
        for(ValidationEditText editText: requiredFields){
            data.put(editText.getHint().toString(), editText.getText().toString());
        }
    }

    @Override
    public void onValidationSuccess() {
        onGetData();
        perfomSendData();
    }

    @Override
    public void onClick(View v) {
        onValidateForm();
    }

    private void perfomSendData() {
        AlertUtils.showGenericAlert(getActivity(),
                true,
                R.string.getdata,
                LoginFragment.convertHashMapToString(data));
    }
}