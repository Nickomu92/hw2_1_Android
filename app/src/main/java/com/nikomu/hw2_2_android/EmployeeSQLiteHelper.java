package com.nikomu.hw2_2_android;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class EmployeeSQLiteHelper extends SQLiteOpenHelper {
    public EmployeeSQLiteHelper(@Nullable Context context,
                                @Nullable String name,
                                @Nullable SQLiteDatabase.CursorFactory factory,
                                int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE employees (id INTEGER PRIMARY KEY AUTOINCREMENT, first_name TEXT NOT NULL, last_name TEXT NOT NULL, gender INTEGER NOT NULL, day INTEGER NOT NULL, month INTEGER NOT NULL, year INTEGER NOT NULL)");
            db.execSQL("INSERT INTO employees(first_name, last_name, gender, day, month, year) VALUES ('Степан', 'Степанченко', 1, 23, 10, 1987)");
            db.execSQL("INSERT INTO employees(first_name, last_name, gender, day, month, year) VALUES ('Микола', 'Миколайчук', 1, 14, 5, 1992)");
            db.execSQL("INSERT INTO employees(first_name, last_name, gender, day, month, year) VALUES ('Євгенія', 'Євгенчук', 0, 6, 12, 2002)");
            db.execSQL("INSERT INTO employees(first_name, last_name, gender, day, month, year) VALUES ('Олександр', 'Олександрієнко', 1, 23, 5, 2004)");
            db.execSQL("INSERT INTO employees(first_name, last_name, gender, day, month, year) VALUES ('Ганна', 'Ганненко', 0, 15, 2, 1998)");
            db.execSQL("INSERT INTO employees(first_name, last_name, gender, day, month, year) VALUES ('Євген', 'Євгенченко', 1, 9, 3, 1999)");
            db.execSQL("INSERT INTO employees(first_name, last_name, gender, day, month, year) VALUES ('Олександра', 'Олександрійчук', 0, 22, 5, 1998)");
            db.execSQL("INSERT INTO employees(first_name, last_name, gender, day, month, year) VALUES ('Віталіна', 'Віталенчук', 0, 8, 12, 2005)");
            db.execSQL("INSERT INTO employees(first_name, last_name, gender, day, month, year) VALUES ('Василина', 'Василенко', 0, 20, 12, 2006)");
            db.execSQL("INSERT INTO employees(first_name, last_name, gender, day, month, year) VALUES ('Василь', 'Василенко', 1, 15, 2, 1998)");
            db.execSQL("INSERT INTO employees(first_name, last_name, gender, day, month, year) VALUES ('Володимир', 'Володимирчук', 1, 23, 5, 2001)");
            db.execSQL("INSERT INTO employees(first_name, last_name, gender, day, month, year) VALUES ('Іван', 'Іваненко', 1, 15, 9, 1996)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
