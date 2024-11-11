package com.example.baitapquatrinh2.Utils;

import android.content.Intent;
import android.net.Uri;
import android.content.Context;

public class EmailUtils {

    public static void sendEmailWithAttachment(Context context, String subject, String body, String filePath) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("application/xml");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "ngoctrinhcute56236@gmail.com" });
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        Uri attachmentUri = Uri.parse("file://" + filePath);
        emailIntent.putExtra(Intent.EXTRA_STREAM, attachmentUri);

        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
}
