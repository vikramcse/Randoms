package com.iitb.android.randoms.app;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NumbersDataSource {
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    private String[] allColumns = { DBHelper.COLUMN_ID, DBHelper.COLUMN_RANDOM_NUMBER };

    public NumbersDataSource(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public RandomNumbers createComment(String number) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_RANDOM_NUMBER, number);

        long insertId = database.insert(DBHelper.TABLE_NUMBERS, null, values);

        Cursor cursor = database.query(DBHelper.TABLE_NUMBERS,
                allColumns, DBHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();

        RandomNumbers newNumber = cursorToNumber(cursor);
        cursor.close();
        return newNumber;
    }

    public List<RandomNumbers> getAllNumbers() {
        List<RandomNumbers> numbers = new ArrayList<RandomNumbers>();

        Cursor cursor = database.query(DBHelper.TABLE_NUMBERS, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RandomNumbers comment = cursorToNumber(cursor);
            numbers.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return numbers;
    }

    public void dropTable() {
        database.execSQL("DROP TABLE IF EXISTS " + DBHelper.TABLE_NUMBERS);
    }

    private RandomNumbers cursorToNumber(Cursor cursor) {
        RandomNumbers number = new RandomNumbers();
        number.setId(cursor.getLong(0));
        number.setNumber(cursor.getString(1));
        return number;
    }
}

