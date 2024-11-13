//package com.example.baitapquatrinh2.ContentProvider;
//
//import static com.example.baitapquatrinh2.LoadData.CustomerData.loadCustomers;
//
//import android.content.ContentProvider;
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.MatrixCursor;
//import android.net.Uri;
//import android.util.Log;
//
//import com.example.baitapquatrinh2.Models.Account;
//import com.example.baitapquatrinh2.Models.Customer;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.List;
//
//public class CustomerProvider extends ContentProvider {
//    private static final String TAG = "CustomerProvider";
//    private static final String AUTHORITY = "com.example.baitapquatrinh2.provider";
//    private static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/customers");
//
//    private List<Customer> customerList;
//
//    @Override
//    public boolean onCreate() {
//        Context context = getContext();
//        if (context != null) {
//            customerList = loadCustomers(context);
//        }
//        return customerList != null;
//    }
//
//    // Phương thức để tải dữ liệu từ file JSON
//    public static List<Customer> loadCustomersFromJSON(Context context) {
//        try {
//            // Kiểm tra xem file đã tồn tại trong bộ nhớ nội bộ chưa
//            File file = new File(context.getFilesDir(), "customers.json");
//            if (!file.exists()) {
//                // Sao chép file từ assets vào bộ nhớ nội bộ
//                InputStream inputStream = context.getAssets().open("customers.json");
//                FileOutputStream outputStream = new FileOutputStream(file);
//
//                byte[] buffer = new byte[1024];
//                int length;
//                while ((length = inputStream.read(buffer)) > 0) {
//                    outputStream.write(buffer, 0, length);
//                }
//                outputStream.close();
//                inputStream.close();
//            }
//
//            // Đọc file từ bộ nhớ nội bộ
//            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
//            return new Gson().fromJson(reader, new TypeToken<List<Customer>>() {}.getType());
//        } catch (IOException e) {
//            Log.e("CustomerProvider", "Lỗi khi đọc file customers.json", e);
//        }
//        return null;
//    }
//    public static List<Customer> loadCustomers(Context context) {
//        List<Customer> customerList = new ArrayList<>();  // Initialize here
//        try {
//            InputStream inputStream = context.getAssets().open("customers.json");
//            InputStreamReader reader = new InputStreamReader(inputStream);
//            customerList = new Gson().fromJson(reader, new TypeToken<List<Customer>>() {}.getType());
//            reader.close();
//            inputStream.close();
//        } catch (IOException e) {
//            Log.e("CustomerData", "Lỗi khi đọc file customers.json: " + e.getMessage());
//        }
//        return customerList;  // Ensure you return a non-null list
//    }
//    @Override
//    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
//        // Chuyển danh sách customerList thành Cursor
//        if (customerList != null && !customerList.isEmpty()) {
//            // Chuyển danh sách thành Cursor để trả về
//            String[] columns = {"phoneNumber", "currentPoint", "creationDate", "lastUpdatedDate", "note"};
//            MatrixCursor cursor = new MatrixCursor(columns);
//            for (Customer customer : customerList) {
//                cursor.addRow(new Object[]{
//                        customer.getPhoneNumber(),
//                        customer.getCurrentPoint(),
//                        customer.getCreationDate(),
//                        customer.getLastUpdatedDate(),
//                        customer.getNote()
//                });
//            }
//            return cursor;
//        }
//        return null;
//    }
//
//    @Override
//    public Uri insert(Uri uri, ContentValues values) {
//        // Thêm khách hàng mới nếu cần
//        return null;
//    }
//
//    @Override
//    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
//        // Cập nhật khách hàng nếu cần
//        return 0;
//    }
//
//    @Override
//    public int delete(Uri uri, String selection, String[] selectionArgs) {
//        // Xóa khách hàng nếu cần
//        return 0;
//    }
//
//    @Override
//    public String getType(Uri uri) {
//        return "vnd.android.cursor.dir/vnd." + AUTHORITY + ".customers";
//    }
//    public void saveCustomersToJson(Context context, List<Customer> customerrList) {
//        try {
//            Gson gson = new Gson();
//            String json = gson.toJson(customerrList);
//
//            // Ghi file vào bộ nhớ nội bộ
//            FileOutputStream fos = context.openFileOutput("customers.json", Context.MODE_PRIVATE);
//            fos.write(json.getBytes());
//            fos.flush(); // Đảm bảo tất cả dữ liệu được ghi
//            fos.close();
//
//            Log.d("SaveJson", "Đã lưu dữ liệu customer vào file JSON.");
//        } catch (IOException e) {
//            Log.e("SaveJson", "Lỗi khi lưu file JSON", e);
//        }
//    }
//}
package com.example.baitapquatrinh2.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import com.example.baitapquatrinh2.Models.Customer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CustomerProvider extends ContentProvider {
    private static final String TAG = "CustomerProvider";
    private static final String AUTHORITY = "com.example.baitapquatrinh2.provider";
    private static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/customers");

    private static List<Customer> customerList = new ArrayList<>();

    @Override
    public boolean onCreate() {
        Context context = getContext();
        if (context != null) {
            customerList = loadCustomers(context);
        }
        return customerList != null;
    }

    // Phương thức để tải dữ liệu từ file JSON
//    private List<Customer> loadCustomersFromJSON(Context context) {
//        try {
//            // Kiểm tra xem file đã tồn tại trong bộ nhớ nội bộ chưa
//            File file = new File(context.getFilesDir(), "customers.json");
//            if (!file.exists()) {
//                // Sao chép file từ assets vào bộ nhớ nội bộ
//                InputStream inputStream = context.getAssets().open("customers.json");
//                FileOutputStream outputStream = new FileOutputStream(file);
//
//                byte[] buffer = new byte[1024];
//                int length;
//                while ((length = inputStream.read(buffer)) > 0) {
//                    outputStream.write(buffer, 0, length);
//                }
//                outputStream.close();
//                inputStream.close();
//            }
//
//
//
//            // Đọc file từ bộ nhớ nội bộ
//            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
//            return new Gson().fromJson(reader, new TypeToken<List<Customer>>() {}.getType());
//        } catch (IOException e) {
//            Log.e(TAG, "Lỗi khi đọc file customers.json", e);
//        }
//        return new ArrayList<>(); // Trả về danh sách rỗng nếu có lỗi
//    }

    // Phương thức để lưu danh sách khách hàng vào file JSON
    private void saveCustomersToJson(Context context, List<Customer> customerList) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(customerList);

            // Ghi file vào bộ nhớ nội bộ
            FileOutputStream fos = context.openFileOutput("customers.json", Context.MODE_PRIVATE);
            fos.write(json.getBytes());
            fos.flush(); // Đảm bảo tất cả dữ liệu được ghi
            fos.close();

            Log.d("SaveJson", "Đã lưu dữ liệu customer vào file JSON.");
        } catch (IOException e) {
            Log.e("SaveJson", "Lỗi khi lưu file JSON", e);
        }
    }
    public static List<Customer> loadCustomers(Context context) {
        List<Customer> customerList = new ArrayList<>(); // Initialize here
        try {
            File file = new File(context.getFilesDir(), "customers.json");

            if (!file.exists()) {
                InputStream inputStream = context.getAssets().open("customers.json");
                FileOutputStream outputStream = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.close();
                inputStream.close();
            }

            // Đọc file từ bộ nhớ nội bộ
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            customerList = new Gson().fromJson(reader, new TypeToken<List<Customer>>() {}.getType());
            reader.close();
        } catch (IOException e) {
            Log.e("CustomerData", "Lỗi khi đọc file customers.json: " + e.getMessage());
        }
        return customerList; // Ensure you return a non-null list
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Chuyển danh sách customerList thành Cursor
        if (customerList != null && !customerList.isEmpty()) {
            String[] columns = {"phoneNumber", "currentPoint", "creationDate", "lastUpdatedDate", "note"};
            MatrixCursor cursor = new MatrixCursor(columns);
            for (Customer customer : customerList) {
                cursor.addRow(new Object[]{
                        customer.getPhoneNumber(),
                        customer.getCurrentPoint(),
                        customer.getCreationDate(),
                        customer.getLastUpdatedDate(),
                        customer.getNote()
                });
            }
            return cursor;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Thêm khách hàng mới nếu cần
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // Cập nhật khách hàng nếu cần
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Xóa khách hàng nếu cần
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return "vnd.android.cursor.dir/vnd." + AUTHORITY + ".customers";
    }
    public static List<Customer> getCustomerList() {
        return customerList;
    }


}
