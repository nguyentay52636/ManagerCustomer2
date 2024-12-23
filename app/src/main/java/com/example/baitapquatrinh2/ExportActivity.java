package com.example.baitapquatrinh2;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.baitapquatrinh2.ContentProvider.CustomerProvider;
import com.example.baitapquatrinh2.Models.Customer;
import com.example.baitapquatrinh2.Services.XMLHelper;
import java.io.File;
import java.io.InputStream;
import java.util.List;

public class ExportActivity extends AppCompatActivity {
    private Uri selectedFileUri = null;
    private static final int REQUEST_CODE_PICK_FILE =1 ;
    private Button btnExportFile, btnOpenFile, btnSendEmail ,btnImport ;
    private TextView tvStatus;
    private ActivityResultLauncher<Intent> filePickerLauncher  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filepaths);

        btnExportFile = findViewById(R.id.btnExportFile);
        btnOpenFile = findViewById(R.id.btnOpenFile);
        btnSendEmail = findViewById(R.id.btnSendEmail);
        tvStatus = findViewById(R.id.tvStatus);
        btnImport = findViewById(R.id.btnImportFile);
             filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == ExportActivity.RESULT_OK && result.getData() != null) {
                        Uri fileUri = result.getData().getData();
                        Log.d("FileSelected", "File được chọn: " + fileUri);

                        try {
                            List<Customer> importedCustomers = XMLHelper.importCustomersFromXML(this, fileUri);
                            List<Customer> currentCustomers = CustomerProvider.getCustomerList();
                            currentCustomers.addAll(importedCustomers);

                            for (Customer newCustomer : importedCustomers) {
                                if(CustomerProvider.isDuplicateCustomer(currentCustomers,newCustomer)) {
                                    Log.d("DuplicateCustomer", "Khách hàng trùng: " + newCustomer.getPhoneNumber());
                                }else {
                                    currentCustomers.add(newCustomer);
                                }
                                Log.d("Customer", newCustomer.toString());
                            }
                            for (Customer customer : currentCustomers) {
                                Log.d("Customer", customer.toString());
                            }
                            CustomerProvider.saveCustomerToJson(getApplicationContext(), currentCustomers);



                        } catch (Exception e) {
                            Log.e("FileError", "Lỗi khi đọc file: " + e.getMessage());
                        }
                    }
                }
        );



        btnExportFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Customer> customers = CustomerProvider.loadCustomers(getApplicationContext());
                Log.d("CustomerListSize", "Customer list size: " + (customers != null ? customers.size() : 0));
                if (customers == null || customers.isEmpty()) {
                    Log.e("ExportXML", "Danh sách khách hàng trống.");
                    tvStatus.setText("Danh sách khách hàng trống.");
                    Toast.makeText(ExportActivity.this, "Danh sách khách hàng trống!", Toast.LENGTH_SHORT).show();
                    return;
                }
                File xmlFile = XMLHelper.exportCustomersToXML(customers, ExportActivity.this);

                if (xmlFile != null) {
                    tvStatus.setText("Xuất file thành công tại thư mục Downloads!");
                    Toast.makeText(ExportActivity.this, "Xuất file XML thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    tvStatus.setText("Xuất file thất bại.");
                    Toast.makeText(ExportActivity.this, "Xuất file XML thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType("text/xml");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    filePickerLauncher.launch(intent);
                }catch (Exception e) {
                    Log.e("FileError", "Lỗi import " + e.getMessage());
                }
            }
        });

        btnOpenFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openXmlFile();
                openXmlFileUsingDocumentFile();
            }
        });
        // Xử lý sự kiện gửi email
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "sent file customer.xml");

                startActivity(emailIntent);

            }
        });
    }

    public void openXmlFileUsingDocumentFile() {
        try {
            // Tạo Intent để chọn tệp XML
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("text/xml"); // Định dạng MIME phù hợp
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            // Không sử dụng Intent.EXTRA_INITIAL_INTENTS
            // Nếu muốn chọn thư mục cụ thể, cần sử dụng Storage Access Framework (SAF)

            startActivityForResult(intent, 1);
        } catch (Exception e) {
            Log.e("FileError", "Lỗi khi mở tệp: " + e.getMessage());
            Toast.makeText(this, "Đã xảy ra lỗi khi mở tệp!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData(); // URI của tệp được chọn

            if (fileUri != null) {
                Log.d("FileUri", "URI file: " + fileUri.toString());

                // Đọc nội dung XML từ URI
                try (InputStream inputStream = getContentResolver().openInputStream(fileUri)) {
                    if (inputStream != null) {
                        parseXmlFile(inputStream);
                    } else {
                        Log.e("FileError", "Không thể mở luồng dữ liệu từ URI");
                        Toast.makeText(this, "Không thể đọc tệp!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("XMLParseError", "Lỗi khi đọc tệp XML: " + e.getMessage());
                    Toast.makeText(this, "Lỗi khi phân tích tệp XML!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("FileError", "URI file null");
                Toast.makeText(this, "Không chọn tệp!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("FileError", "Không chọn tệp hoặc lỗi xảy ra");
            Toast.makeText(this, "Không chọn tệp hoặc lỗi xảy ra!", Toast.LENGTH_SHORT).show();
        }
    }

    private void parseXmlFile(InputStream inputStream) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream, "UTF-8");

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String tagName = parser.getName();
                    if ("customer".equals(tagName)) {
                        String phoneNumber = parser.getAttributeValue(null, "phoneNumber");
                        String currentPoint = parser.getAttributeValue(null, "currentPoint");
                        String creationDate = parser.getAttributeValue(null, "creationDate");
                        String lastUpdatedDate = parser.getAttributeValue(null, "lastUpdatedDate");
                        String note = parser.getAttributeValue(null, "note");

                        Log.d("CustomerXML", "Phone: " + phoneNumber +
                                ", Point: " + currentPoint +
                                ", Created: " + creationDate +
                                ", Updated: " + lastUpdatedDate +
                                ", Note: " + note);
                    }
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            Log.e("XMLParseError", "Lỗi khi phân tích XML: " + e.getMessage());
        }
    }




}
