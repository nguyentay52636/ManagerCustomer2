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

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CustomerProvider extends ContentProvider {
    private static final String TAG = "CustomerProvider";
    private static final String AUTHORITY = "com.example.baitapquatrinh2.provider";
    private static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/customers");

    private List<Customer> customerList;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        if (context != null) {
            customerList = loadCustomers(context);
        }
        return customerList != null;
    }

    // Phương thức để tải dữ liệu từ file JSON
    private List<Customer> loadCustomers(Context context) {
        try {
            InputStreamReader reader = new InputStreamReader(context.getAssets().open("customers.json"));
            return new Gson().fromJson(reader, new TypeToken<List<Customer>>() {}.getType());
        } catch (IOException e) {
            Log.e(TAG, "Lỗi khi đọc file customers.json", e);
        }
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Chuyển danh sách customerList thành Cursor
        if (customerList != null && !customerList.isEmpty()) {
            // Chuyển danh sách thành Cursor để trả về
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
}