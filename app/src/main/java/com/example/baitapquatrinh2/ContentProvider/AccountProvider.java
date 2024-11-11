package com.example.baitapquatrinh2.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import com.example.baitapquatrinh2.Models.Account;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class AccountProvider extends ContentProvider {
    private static final String TAG = "AccountProvider";
    private static final String AUTHORITY = "com.example.baitapquatrinh2.provider";
    private static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/accounts");

    private List<Account> accountList;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        if (context != null) {
            accountList = loadAccounts(context);
        }
        return accountList != null;
    }

    // Phương thức để tải dữ liệu từ file JSON
    private List<Account> loadAccounts(Context context) {
        try {
            InputStreamReader reader = new InputStreamReader(context.getAssets().open("accounts.json"));
            return new Gson().fromJson(reader, new TypeToken<List<Account>>() {}.getType());
        } catch (IOException e) {
            Log.e(TAG, "Lỗi khi đọc file accounts.json", e);
        }
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (accountList != null && !accountList.isEmpty()) {
            String[] columns = {"username", "password"};
            MatrixCursor cursor = new MatrixCursor(columns);
            for (Account account : accountList) {
                cursor.addRow(new Object[]{account.getUsername(), account.getPassword()});
            }
            return cursor;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }



    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Xóa tài khoản nếu cần
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return "vnd.android.cursor.dir/vnd." + AUTHORITY + ".accounts";
    }
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowsUpdated = 0;

        if (accountList != null && values != null && selectionArgs != null && selectionArgs.length > 0) {
            String usernameToUpdate = selectionArgs[0];

            // Tìm và cập nhật mật khẩu của tài khoản
            for (Account account : accountList) {
                if (account.getUsername().equals(usernameToUpdate)) {
                    String newPassword = values.getAsString("password");
                    account.setPassword(newPassword);
                    rowsUpdated++;
                    break;
                }
            }

            // Sau khi cập nhật, lưu lại danh sách tài khoản vào file JSON
            Context context = getContext();
            if (context != null && rowsUpdated > 0) {
                saveAccountsToJson(context, accountList);
            }
        }
        return rowsUpdated;
    }

    public void saveAccountsToJson(Context context, List<Account> accountList) {
        try {
            // Chuyển đổi danh sách tài khoản thành JSON
            Gson gson = new Gson();
            String json = gson.toJson(accountList);

            // Lưu vào file accounts.json
            FileOutputStream fos = context.openFileOutput("accounts.json", Context.MODE_PRIVATE);
            fos.write(json.getBytes());
            fos.close();

            Log.d("SaveJson", "Đã lưu dữ liệu tài khoản vào file JSON.");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("SaveJson", "Lỗi khi lưu file JSON", e);
        }
    }


}