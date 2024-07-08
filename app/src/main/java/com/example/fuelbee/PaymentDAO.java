package com.example.fuelbee;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PaymentDAO {

    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase database;

    public PaymentDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addPayment(Payment payment) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, payment.getName());
        values.put(DatabaseHelper.COLUMN_EMAIL, payment.getEmail());
        values.put(DatabaseHelper.COLUMN_ADDRESS, payment.getAddress());
        values.put(DatabaseHelper.COLUMN_MOBILE, payment.getMobile());
        values.put(DatabaseHelper.COLUMN_AMOUNT, payment.getAmount());

        database.insert(DatabaseHelper.TABLE_PAYMENTS, null, values);
    }

    // Example method to get the latest payment, assuming you have a timestamp field
    public Payment getLatestPayment() {
        Payment payment = null;
        Cursor cursor = database.query(DatabaseHelper.TABLE_PAYMENTS,
                null,
                null,
                null,
                null,
                null,
                "rowid DESC",  // Sort by row ID in descending order to get the latest record
                "1");

        if (cursor != null && cursor.moveToFirst()) {
            payment = new Payment();
            try {
                payment.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME)));
                payment.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL)));
                payment.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ADDRESS)));
                payment.setMobile(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MOBILE)));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return payment;
    }}

