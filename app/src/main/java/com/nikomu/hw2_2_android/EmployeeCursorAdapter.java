package com.nikomu.hw2_2_android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EmployeeCursorAdapter extends BaseAdapter {
    private EmployeeSQLiteHelper helper;
    private SQLiteDatabase database;
    Context context;
    Cursor cursor;
    Bitmap[] avatars;
    /**
     * Колір фону невибраного елемента списку.
     */
    private final int defaultBgColor = Color.rgb(255, 255, 255);

    /**
     * Колір фону вибраного елемента списку.
     */
    private final int selectBgColor = Color.rgb(255, 99, 71);

    public EmployeeCursorAdapter(Context context, Bitmap[] avatars) {
        this.context = context;
        this.helper = new EmployeeSQLiteHelper(context, "Employees", null, 1);
        this.database = helper.getReadableDatabase();
        this.cursor = createCursor();
        this.avatars = avatars;
    }

    /**
     * Метод для отримання співробітників із бази даних.
     * @return Cursor
     */
    private Cursor createCursor() {
        String sqlQuery = "SELECT * FROM employees";
        return database.rawQuery(sqlQuery, null);
    }

    /**
     * Метод для вставки до бази даних запису співробвтника.
     * @param firstName - ім'я
     * @param lastName - прізвище
     * @param gender - стать
     * @param day - день народження
     * @param month - місяць народження
     * @param year - рік народження
     */
    public void insertCursorToDb(String firstName, String lastName, int gender,
                            int day, int month, int year) {
        String sqlQuery = "INSERT INTO employees(first_name, last_name, gender, day, month, year) VALUES ('" +
                firstName + "', '" + lastName + "', " + gender + "," + day + ", " + (month + 1) + ", " + year + ")";
        database.execSQL(sqlQuery);
    }

    /**
     * Метод для видалення з бази даних запису співробітника за id.
     * @param id - ідентифікатор співробітника
     */
    public void deleteCursorToDb(int id) {
        String sqlQuery = "DELETE FROM employees WHERE id = " + id;
        database.execSQL(sqlQuery);
    }

    /**
     * Метод для отримання з бази даних запису співробітника за id.
     * @param id - ідентифікатор співробітника
     */
    public Human selectEmployeeById(int id) {
        String sqlQuery = "SELECT * FROM employees WHERE id = " + id;
        Cursor cursor = database.rawQuery(sqlQuery, null);
        if(cursor.moveToFirst()) {
            Human employee = new Human(cursor);
            return employee;
        } else {
            return null;
        }
    }

    /**
     * Метод для редагування данних співробітника в базі даних.
     * @param id - ідентифікатор
     * @param firstName - ім'я
     * @param lastName - прізвище
     * @param gender - стать
     * @param day - день народження
     * @param month - місяць народження
     * @param year - рік народження
     */
    public void updateCursorToDb(int id, String firstName, String lastName, int gender,
                             int day, int month, int year) {
        String sqlQuery = "UPDATE employees SET first_name = \'" + firstName + "\', last_name = \'" +
                lastName + "\', gender = " + gender + ", day = " + day + ", month = " + (month + 1) +
                ", year = " + year + " WHERE id = " + id + ";";
        database.execSQL(sqlQuery);
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int i) {
        cursor.moveToPosition(i);
        return new Human(cursor);
    }

    @SuppressLint("Range")
    @Override
    public long getItemId(int i) {
        cursor.moveToPosition(i);
        return cursor.getInt(cursor.getColumnIndex("id"));
    }

    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.employees_list, parent, false);
        }

        TextView tvId = view.findViewById(R.id.tvId);
        TextView tvFirstName = view.findViewById(R.id.tvFirstName);
        TextView tvLastName = view.findViewById(R.id.tvLastName);
        TextView tvBirthDay = view.findViewById(R.id.tvBirthDay);
        ImageView ivAvatar = view.findViewById(R.id.ivAvatar);

        Human employee = (Human) getItem(position);

        tvId.setText(Integer.toString(employee.getId()));
        tvFirstName.setText(employee.getFirstName());
        tvLastName.setText(employee.getLastName());
        tvBirthDay.setText(employee.getBirthDayString());
        ivAvatar.setImageBitmap(avatars[employee.getGender() ? 1 : 0]);

        // Виділення вибраного елемета іншим кольором
        if (position == MainActivity.getCurrentItem()) {
            view.setBackgroundColor(selectBgColor);
            MainActivity.setCurrentView(view);
        } else {
            view.setBackgroundColor(defaultBgColor);
        }

        return view;
    }
}
