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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AccountProvider extends ContentProvider {
    public static final String TAG = "AccountProvider";
    public static final String AUTHORITY = "com.example.baitapquatrinh2.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/accounts");
    private static final String FILE_NAME = "accounts.json";

    private static List<Account> accountList;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        if (context != null) {
            // Sao chép file JSON từ assets vào bộ nhớ nội bộ nếu chưa tồn tại
            copyJsonFileIfNotExists(context);
            accountList = loadAccounts(context);
        }
        return accountList != null;
    }

    private void copyJsonFileIfNotExists(Context context) {
        File file = new File(context.getFilesDir(), FILE_NAME);
        if (!file.exists()) {
            try (InputStream inputStream = context.getAssets().open(FILE_NAME);
                 FileOutputStream outputStream = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            } catch (IOException e) {
                Log.e(TAG, "Lỗi khi sao chép file JSON", e);
            }
        }
    }

    public static List<Account> loadAccounts(Context context) {
        try (FileInputStream fis = context.openFileInput(FILE_NAME);
             InputStreamReader reader = new InputStreamReader(fis)) {
            return new Gson().fromJson(reader, new TypeToken<List<Account>>() {}.getType());
        } catch (IOException e) {
            Log.e(TAG, "Lỗi khi đọc file JSON", e);
            return new ArrayList<>();
        }
    }

    public void saveAccountsToJson(Context context, List<Account> accountList) {
        try (FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
            String json = new Gson().toJson(accountList);
            fos.write(json.getBytes());
            fos.flush();
            Log.d(TAG, "Đã lưu file JSON thành công.");
        } catch (IOException e) {
            Log.e(TAG, "Lỗi khi lưu file JSON", e);
        }
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
        if (accountList != null && values != null) {
            String username = values.getAsString("username");
            String password = values.getAsString("password");
            if (username != null && password != null) {
                accountList.add(new Account(username, password));
                Context context = getContext();
                if (context != null) {
                    saveAccountsToJson(context, accountList);
                }
                Log.d(TAG, "Đã thêm tài khoản mới: " + username);
                return Uri.withAppendedPath(CONTENT_URI, username);
            }
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (accountList != null && selectionArgs != null && selectionArgs.length > 0) {
            String usernameToDelete = selectionArgs[0];
            for (int i = 0; i < accountList.size(); i++) {
                if (accountList.get(i).getUsername().equals(usernameToDelete)) {
                    accountList.remove(i);
                    Context context = getContext();
                    if (context != null) {
                        saveAccountsToJson(context, accountList);
                    }
                    Log.d(TAG, "Đã xóa tài khoản: " + usernameToDelete);
                    return 1;
                }
            }
        }
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return "vnd.android.cursor.dir/vnd." + AUTHORITY + ".accounts";
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowsUpdated = 0;

        if (accountList != null && selectionArgs != null && selectionArgs.length > 0) {
            String usernameToUpdate = selectionArgs[0];

            for (Account account : accountList) {
                if (account.getUsername().equals(usernameToUpdate)) {
                    String newPassword = values.getAsString("password");
                    account.setPassword(newPassword);
                    rowsUpdated++;
                    break;
                }
            }

            if (rowsUpdated > 0) {
                Context context = getContext();
                if (context != null) {
                    saveAccountsToJson(context, accountList);
                }
            }
        }
        return rowsUpdated;
    }

    public static boolean updatePassword(Context context, String username, String newPassword) {
        ContentValues values = new ContentValues();
        values.put("password", newPassword);

        String selection = "username = ?";
        String[] selectionArgs = new String[]{username};
        Uri uri = CONTENT_URI;
        int rowsUpdated = context.getContentResolver().update(uri, values, selection, selectionArgs);

        // Kiểm tra kết quả
        if (rowsUpdated > 0) {
            Log.d("AccountProvider", "Mật khẩu đã được cập nhật thành công.");
            return true;
        } else {
            Log.e("AccountProvider", "Không tìm thấy tài khoản với tên người dùng: " + username);
            return false;
        }
    }

}
