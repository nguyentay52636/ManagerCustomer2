package com.example.baitapquatrinh2.Data;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import com.example.baitapquatrinh2.models.Customer;

public class CustomerData {
    public static ArrayList<Customer> getSampleCustomerList() {
        ArrayList<Customer> customers = new ArrayList<>();
        customers.add(new Customer("0969157557", 9, getCurrentDate(), getCurrentDate(), "First customer"));
        customers.add(new Customer("0988888888", 4, getCurrentDate(), getCurrentDate(), "Second customer"));
        customers.add(new Customer("0909123456", 300, getCurrentDate(), getCurrentDate(), "Third customer"));
        customers.add(new Customer("0909123424", 500, getCurrentDate(), getCurrentDate(), "Third customer"));
        customers.add(new Customer("0909123424", 500, getCurrentDate(), getCurrentDate(), "Third customer"));
        customers.add(new Customer("0909123424", 500, getCurrentDate(), getCurrentDate(), "Third customer"));
        customers.add(new Customer("0909123424", 500, getCurrentDate(), getCurrentDate(), "Third customer"));
        customers.add(new Customer("0909123424", 500, getCurrentDate(), getCurrentDate(), "Third customer"));
        return customers;
    }

    private static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }
}
