package com.iitb.android.randoms.app;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{

    public static final String TABLE_NUMBERS = "numbers";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_RANDOM_NUMBER = "random_number";

    private static final String DATABASE_NAME = "iitb.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NUMBERS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_RANDOM_NUMBER
            + " text not null);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data"
        );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NUMBERS);
        onCreate(sqLiteDatabase);
    }
}
