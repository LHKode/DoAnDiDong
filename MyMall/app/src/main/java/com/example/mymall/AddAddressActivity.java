package com.example.mymall;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {
    private Spinner citySpinner;
    private EditText locality;
    private EditText name;
    private EditText mobileNo;
    private Button saveBtn;

    private String [] cityList;
    private String selectedCity;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Thêm địa chỉ giao hàng");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /////loading dialog
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        /////loading dialog
        cityList = getResources().getStringArray(R.array.vietnam_states);
        citySpinner = findViewById(R.id.city_spinner);
        locality = findViewById(R.id.locality);
        name = findViewById(R.id.name);
        mobileNo = findViewById(R.id.mobile_no);
        saveBtn = findViewById(R.id.save_btn);

        ArrayAdapter spinnerAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,cityList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        citySpinner.setAdapter(spinnerAdapter);

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCity = cityList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(locality.getText())){
                    if(!TextUtils.isEmpty(name.getText())){
                        if ((!TextUtils.isEmpty(mobileNo.getText()))){

                            loadingDialog.show();

                            final String fullAdrress = citySpinner.getSelectedItem().toString() +" "+  locality.getText().toString();

                            Map<String,Object> addAddress = new HashMap();
                            addAddress.put("list_size",(long)DBqueries.addressesModelList.size()+1);
                            addAddress.put("fullname_"+String.valueOf((long)DBqueries.addressesModelList.size()+1),name.getText().toString());
                            addAddress.put("address_"+String.valueOf((long)DBqueries.addressesModelList.size()+1),fullAdrress);
                            addAddress.put("mobile_"+String.valueOf((long)DBqueries.addressesModelList.size()+1),mobileNo.getText().toString());
                            addAddress.put("selected_"+String.valueOf((long)DBqueries.addressesModelList.size()+1),true);
                            if(DBqueries.addressesModelList.size() > 0) {
                                addAddress.put("selected_" + (DBqueries.selectedAddress + 1), false);
                            }
                            FirebaseFirestore.getInstance().collection("USERS")
                                    .document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                                    .document("MY_ADDRESSES")
                                    .update(addAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        if(DBqueries.addressesModelList.size() > 0) {
                                            DBqueries.addressesModelList.get(DBqueries.selectedAddress).setSelected(false);
                                        }
                                        DBqueries.addressesModelList.add(new AddressesModel(name.getText().toString(),fullAdrress,mobileNo.getText().toString(),true));
                                        if(getIntent().getStringExtra("INTENT").equals("deliveryIntent")) {
                                            Intent deliveryIntent = new Intent(AddAddressActivity.this, DeliveryActivity.class);
                                            startActivity(deliveryIntent);
                                        }else {
                                            MyAddressesActivity.refreshItem(DBqueries.selectedAddress,DBqueries.addressesModelList.size() - 1);
                                        }
                                        DBqueries.selectedAddress = DBqueries.addressesModelList.size() - 1;
                                        finish();

                                    }else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(AddAddressActivity.this,error,Toast.LENGTH_SHORT).show();
                                    }
                                    loadingDialog.dismiss();
                                }
                            });

                        }else {
                            mobileNo.requestFocus();
                            Toast.makeText(AddAddressActivity.this,"Hãy nhập Số điện thoại !",Toast.LENGTH_SHORT).show();;
                        }
                    }else {
                        name.requestFocus();
                        Toast.makeText(AddAddressActivity.this,"Hãy nhập Tên người nhận hàng !",Toast.LENGTH_SHORT).show();;
                    }
                }else {
                    locality.requestFocus();
                    Toast.makeText(AddAddressActivity.this,"Hãy nhập Địa chỉ giao hàng !",Toast.LENGTH_SHORT).show();;
                }


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
