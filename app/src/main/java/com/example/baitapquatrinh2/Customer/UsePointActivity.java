package com.example.baitapquatrinh2.Customer;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baitapquatrinh2.ContentProvider.CustomerProvider;
import com.example.baitapquatrinh2.LoadData.CustomerData;
import com.example.baitapquatrinh2.Models.Customer;
import com.example.baitapquatrinh2.R;
import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UsePointActivity extends AppCompatActivity {

    private EditText etCustomerPhone, etNewPoint, etNote;
    private TextView tvCurrentPoint;
    private Button btnSave, btnSaveNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.use_point_activity);

        // Map views to variables using updated IDs
        etCustomerPhone = findViewById(R.id.etCustomerPhone);
        etNewPoint = findViewById(R.id.etNewPoint);
        etNote = findViewById(R.id.etNote);
        tvCurrentPoint = findViewById(R.id.etCurrentPoint);
        btnSave = findViewById(R.id.btnSave);
        btnSaveNext = findViewById(R.id.btnSaveNext);

        // Set click listener for SAVE button
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
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Không cần thiết cho chức năng tìm kiếm, nhưng có thể dùng để tối ưu hóa.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Xử lý thay đổi văn bản tại đây (sử dụng delay)
                handler.removeCallbacksAndMessages(null);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Gọi phương thức kiểm tra số điện thoại sau khi người dùng nhập xong
                        Log.d("PhoneInput", "User entered phone: " + charSequence.toString());  // Log số điện thoại nhập vào
                        checkCustomerPhone(charSequence.toString());
                        Toast.makeText(getApplicationContext(), "Lấy dữ liệu thành công", Toast.LENGTH_SHORT).show();


                    }
                }, DELAY);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Bạn có thể thực hiện các hành động khác sau khi thay đổi văn bản, nếu cần.
            }
        });
        etNewPoint.addTextChangedListener(new TextWatcher() {
            private static final long DELAY = 1000;
            private Handler handler = new Handler();

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Không cần thiết cho chức năng tìm kiếm, nhưng có thể dùng để tối ưu hóa.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Xử lý thay đổi văn bản tại đây (sử dụng delay)
                handler.removeCallbacksAndMessages(null);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        validateNewsPoints(charSequence.toString());
                    }
                }, DELAY);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Bạn có thể thực hiện các hành động khác sau khi thay đổi văn bản, nếu cần.
            }
        });


    }
    private void validateNewsPoints(String newPointText) {
        try {
            if (!newPointText.isEmpty()) {
                int newPoint = Integer.parseInt(newPointText);

                // Lấy điểm hiện tại từ TextView và chuyển thành int
                int currentPoint = Integer.parseInt(tvCurrentPoint.getText().toString());

                // Kiểm tra xem số điểm mới nhập có nhỏ hơn số điểm hiện tại không
                if (newPoint >= currentPoint) {
                    Toast.makeText(getApplicationContext(), "Số điểm phải thấp hơn điểm hiện tại", Toast.LENGTH_SHORT).show();
                    etNewPoint.setText("");  // Xóa ô nhập liệu nếu không hợp lệ
                    return;  // Dừng lại nếu không hợp lệ
                }
            }
        } catch (NumberFormatException e) {
            // Nếu giá trị không phải là số hợp lệ
            Toast.makeText(getApplicationContext(), "Vui lòng nhập số điểm hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }





    private void checkCustomerPhone(String phone) {
     List<Customer> customerList = CustomerProvider.loadCustomers(getApplicationContext());

     // Kiểm tra xem số điện thoại có tồn tại trong danh sách không
     boolean phoneExists = false;
     for (Customer customer : customerList) {
         if (customer.getPhoneNumber().equals(phone)) {
             // Nếu số điện thoại tồn tại, lấy điểm và hiển thị
             tvCurrentPoint.setText(String.valueOf(customer.getCurrentPoint())); // Chuyển int thành String
             phoneExists = true;
             break;
         }
     }
     if (!phoneExists) {
         // Nếu số điện thoại không tồn tại, thông báo cho người dùng
         Log.d("PhoneInput", "Phone not found");
         Toast.makeText(getApplicationContext(), "Số điện thoại không tồn tại", Toast.LENGTH_SHORT).show();
     }
 }

    private void saveCustomerData() {
        String phone = etCustomerPhone.getText().toString().trim();
        String newPointStr = etNewPoint.getText().toString().trim();
        String note = etNote.getText().toString().trim();


        if (phone.isEmpty() || newPointStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số điện thoại và điểm mới", Toast.LENGTH_SHORT).show();
            return;
        }

        int newPoint = Integer.parseInt(newPointStr);
        int currentPoint = Integer.parseInt(tvCurrentPoint.getText().toString().trim());

        // Get the customer list
        List<Customer> customerList = CustomerData.getCustomerList();
        Customer existingCustomer = null;

        // Look for the customer by phone number
        for (Customer customer : customerList) {
            if (customer.getPhoneNumber().equals(phone)) {
                existingCustomer = customer;
                break;
            }
        }

        // If customer exists, retain the creationDate, else set current date as creationDate
        String creationDate;
        if (existingCustomer != null) {
            creationDate = existingCustomer.getCreationDate(); // Retain existing creationDate
        } else {
            creationDate = getCurrentDate(); // Set current date for new customer
        }

        // Create a new customer object with the updated data
        Customer updatedCustomer = new Customer(phone, currentPoint + newPoint, creationDate, getCurrentDate(), note);

        // Update or add the customer in the list
        boolean customerExists = false;
        for (int i = 0; i < customerList.size(); i++) {
            Customer customer = customerList.get(i);
            if (customer.getPhoneNumber().equals(phone)) {
                customerList.set(i, updatedCustomer); // Update customer
                customerExists = true;
                break;
            }
        }
        if (!customerExists) {
            customerList.add(updatedCustomer); // Add new customer if not exists
        }

        // Save customer list to JSON
        saveCustomersToJson(this, customerList);
        Toast.makeText(this, "Lưu dữ liệu thành công", Toast.LENGTH_SHORT).show();
    }

    // Method to save customer list to JSON file
    private void saveCustomersToJson(Context context, List<Customer> customerList) {
        String json = new Gson().toJson(customerList);
        try (FileOutputStream fos = context.openFileOutput("customers.json", Context.MODE_PRIVATE)) {
            fos.write(json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi lưu dữ liệu", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper method to get current date in the format "yyyy-MM-dd"
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }
}
