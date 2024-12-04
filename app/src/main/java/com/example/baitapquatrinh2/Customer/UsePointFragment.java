package com.example.baitapquatrinh2.Customer;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.baitapquatrinh2.ContentProvider.CustomerProvider;
import com.example.baitapquatrinh2.Models.Customer;
import com.example.baitapquatrinh2.R;
import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UsePointFragment extends Fragment {

    private EditText etCustomerPhone, etNewPoint, etNote;
    private TextView tvCurrentPoint;
    private Button btnSave, btnSaveNext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.use_point_activity, container, false);

        etCustomerPhone = view.findViewById(R.id.etCustomerPhone);
        etNewPoint = view.findViewById(R.id.etNewPoint);
        etNote = view.findViewById(R.id.etNote);
        tvCurrentPoint = view.findViewById(R.id.etCurrentPoint);
        btnSave = view.findViewById(R.id.btnSave);
        btnSaveNext = view.findViewById(R.id.btnSaveNext);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCustomerData();
            }
        });

        etCustomerPhone.addTextChangedListener(new TextWatcher() {
            private static final long DELAY = 1500;
            private Handler handler = new Handler();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(() -> checkCustomerPhone(s.toString()), DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        etNewPoint.addTextChangedListener(new TextWatcher() {
            private static final long DELAY = 1000;
            private Handler handler = new Handler();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(() -> validateNewsPoints(s.toString()), DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void validateNewsPoints(String newPointText) {
        try {
            if (!newPointText.isEmpty()) {
                int newPoint = Integer.parseInt(newPointText);
                int currentPoint = Integer.parseInt(tvCurrentPoint.getText().toString());

                if (newPoint >= currentPoint) {
                    Toast.makeText(getContext(), "Số điểm phải thấp hơn điểm hiện tại", Toast.LENGTH_SHORT).show();
                    etNewPoint.setText("");
                }
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Vui lòng nhập số điểm hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkCustomerPhone(String phone) {
        List<Customer> customerList = CustomerProvider.loadCustomers(requireContext());

        boolean phoneExists = false;
        for (Customer customer : customerList) {
            if (customer.getPhoneNumber().equals(phone)) {
                tvCurrentPoint.setText(String.valueOf(customer.getCurrentPoint()));
                phoneExists = true;
                break;
            }
        }
        if (!phoneExists) {
            Log.d("PhoneInput", "Phone not found");
            Toast.makeText(getContext(), "Số điện thoại không tồn tại", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveCustomerData() {
        String phone = etCustomerPhone.getText().toString().trim();
        String newPointStr = etNewPoint.getText().toString().trim();
        String note = etNote.getText().toString().trim();

        if (phone.isEmpty() || newPointStr.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập số điện thoại và điểm mới", Toast.LENGTH_SHORT).show();
            return;
        }

        int newPoint = Integer.parseInt(newPointStr);
        int currentPoint = Integer.parseInt(tvCurrentPoint.getText().toString().trim());

        List <Customer> customerList = CustomerProvider.getCustomerList();
        Customer existingCustomer = null;

        for (Customer customer : customerList) {
            if (customer.getPhoneNumber().equals(phone)) {
                existingCustomer = customer;
                break;
            }
        }

        String creationDate = (existingCustomer != null) ? existingCustomer.getCreationDate() : getCurrentDate();
        Customer updatedCustomer = new Customer(phone, currentPoint - newPoint, creationDate, getCurrentDate(), note);

        boolean customerExists = false;
        for (int i = 0; i < customerList.size(); i++) {
            Customer customer = customerList.get(i);
            if (customer.getPhoneNumber().equals(phone)) {
                customerList.set(i, updatedCustomer);
                customerExists = true;
                break;
            }
        }
        if (!customerExists) {
            customerList.add(updatedCustomer);
        }

        saveCustomersToJson(requireContext(), customerList);
        Toast.makeText(getContext(), "Lưu dữ liệu thành công", Toast.LENGTH_SHORT).show();
    }

    private void saveCustomersToJson(Context context, List<Customer> customerList) {
        String json = new Gson().toJson(customerList);
        try (FileOutputStream fos = context.openFileOutput("customers.json", Context.MODE_PRIVATE)) {
            fos.write(json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Lỗi khi lưu dữ liệu", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

}
