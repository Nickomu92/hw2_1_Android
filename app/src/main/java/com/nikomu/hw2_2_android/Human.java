package com.nikomu.hw2_2_android;

import android.annotation.SuppressLint;
import android.database.Cursor;

import androidx.annotation.NonNull;

import java.util.Calendar;

/**
 * Сутність "Людина" (рекомендована у завданні)
 */
public class Human {
    /**
     * Ідентифікатор
     */
    private int id;

    /**
     * Ім'я
     */
    private String firstName;

    /**
     * Прізвище
     */
    private String lastName;

    /**
     * Стать ([true] - чоловіча, а [false] - жіноча)
     */
    private boolean gender;

    /**
     * Дата народження
     */
    private Calendar birthDay;

    @SuppressLint("Range")
    public Human(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex("id"));
        this.firstName = cursor.getString(cursor.getColumnIndex("first_name"));
        this.lastName = cursor.getString(cursor.getColumnIndex("last_name"));
        this.gender = cursor.getInt(cursor.getColumnIndex("gender")) == 1 ? true : false;
        int day = cursor.getInt(cursor.getColumnIndex("day"));
        int month = cursor.getInt(cursor.getColumnIndex("month"));
        int year = cursor.getInt(cursor.getColumnIndex("year"));
        this.birthDay = makeCalendar(day, month, year);
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean getGender() {
        return gender;
    }

    public Calendar getBirthDay() {
        return birthDay;
    }

    public String getBirthDayString() {
        String str = "";
        int day = this.birthDay.get(Calendar.DAY_OF_MONTH);
        str += ((day < 10) ? "0" : "") + day + "/";
        int mon = this.birthDay.get(Calendar.MONTH) + 1;
        str += ((mon < 10) ? "0" : "") + mon + "/";
        str += this.birthDay.get(Calendar.YEAR);
        return str;
    }

    public static Calendar makeCalendar(int day, int month, int year) {
        Calendar C = Calendar.getInstance();
        C.setTimeInMillis(0);
        C.set(Calendar.YEAR, year);
        C.set(Calendar.MONTH, month - 1);
        C.set(Calendar.DAY_OF_MONTH, day);
        return C;
    }

    @Override
    public String toString() {
        return "Human{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", birthDate=" + birthDay +
                '}';
    }
}
