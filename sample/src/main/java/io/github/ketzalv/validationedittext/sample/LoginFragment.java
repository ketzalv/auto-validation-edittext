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

public class LoginFragment extends BaseFragment implements IValidateFormsCycle, View.OnClickListener {

    public static final String TAG = "LoginFragment";
    private List<ValidationEditText> requiredFields = new ArrayList<>();
    private HashMap<String, String> data = new HashMap<>();

    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getView() == null){
            return inflater.inflate(R.layout.fragment_login, container, false);
        }
        return getView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.button_log_in).setOnClickListener(this);
        setRequiredFields(view);
    }

    private void setRequiredFields(View view) {
        if(view == null){
            return;
        }
        if(!requiredFields.isEmpty()){
            requiredFields.clear();
        }
        requiredFields.add((ValidationEditText) view.findViewById(R.id.edit_email));
        requiredFields.add((ValidationEditText) view.findViewById(R.id.edit_password));
    }

    @Override
    public void onClick(View v) {
        onValidateForm();
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
        perfomActionFormValid();

    }

    private void perfomActionFormValid() {
        AlertUtils.showGenericAlert(getActivity(),
                true,
                R.string.getdata,
                convertHashMapToString(data));
    }

    public static String convertHashMapToString(HashMap<String, String> map){
        StringBuilder builder = new StringBuilder();
        for(String key: map.keySet()){
            builder.append(key)
                    .append(": ")
                    .append(map.get(key))
                    .append("\n");
        }
        return builder.toString();
    }
}
