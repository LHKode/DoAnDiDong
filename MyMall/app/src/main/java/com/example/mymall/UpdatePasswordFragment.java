package com.example.mymall;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdatePasswordFragment extends Fragment {

    public UpdatePasswordFragment() {
        // Required empty public constructor
    }

    private EditText oldPassword,newPassword, ConfirmPassword;
    private Button updateBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_update_password, container, false);

        oldPassword = view.findViewById(R.id.old_password);
        newPassword = view.findViewById(R.id.new_password);
        ConfirmPassword = view.findViewById(R.id.comfirm_new_password);
        updateBtn = view.findViewById(R.id.update_password_btn);

        oldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return  view;
    }
    private void checkInputs(){
        if(!TextUtils.isEmpty(oldPassword.getText()) && oldPassword.length()>=8){
            if(!TextUtils.isEmpty(newPassword.getText()) && newPassword.length()>=8){
                if(!TextUtils.isEmpty(ConfirmPassword.getText()) && ConfirmPassword.length()>=8){
                    updateBtn.setEnabled(true);
                    updateBtn.setTextColor(Color.rgb(255,255,255));
                } else{
                    updateBtn.setEnabled(false);
                    updateBtn.setTextColor(Color.argb(50,255,255,255));
                }
            } else{
                updateBtn.setEnabled(false);
                updateBtn.setTextColor(Color.argb(50,255,255,255));
            }
        } else{
            updateBtn.setEnabled(false);
            updateBtn.setTextColor(Color.argb(50,255,255,255));
        }
    }
    private  void  checkEmailAndPassword(){

        Drawable customErrorIcon =getResources().getDrawable(R.mipmap.custom_error_icon);
        customErrorIcon.setBounds(0,0,customErrorIcon.getIntrinsicWidth(),customErrorIcon.getIntrinsicHeight());
            if(newPassword.getText().toString().equals(ConfirmPassword.getText().toString())) {
                   //update password
             } else{
                ConfirmPassword.setError("Mật khẩu không trùng khớp!",customErrorIcon);
            }
    }
}
