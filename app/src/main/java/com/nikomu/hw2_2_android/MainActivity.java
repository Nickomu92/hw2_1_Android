/**
 * 1. Необходимо разработать приложение, работающее со списком сотрудников. Внешний вид приложения
 * изображен на Рис. 11.1.
 *      Список сотрудников отображается в виджете android.widget.ListView с custom-подсветкой
 * выбранного пользователем элемента списка.
 *      Изображения «Мужчина» и «Женщина» должны находиться в каталоге assets приложения.
 * Рекомендуется сделать в классе Активности два поля типа android.graphics.Bitmap (одно поле —
 * ссылка на Bitmap «Мужчина», второе — на «Женщина»), в которые выполнить загрузку изображений из
 * каталога assets (в методе onCreate). Изображения загружаются всего один раз, а далее они
 * назначаются виджетам android.widget.ImageView с помощью метода setImageBitmap.
 *      Далее, в приложении пользователь имеет возможность добавлять/удалять/редактировать
 * сотрудников. Для выбора соответствующего действия предназначено меню приложения (см. Рис. 11.1
 * второе изображение). Для добавления или редактирования информации о сотруднике нужно использовать
 * Диалоговое окно (см. Рис. 11.1 изображение справа). Диалоговое окно можно создать один раз в
 * методе onCreate и затем только отображать с помощью вызова метода show().
 * Информация о сотрудниках содержится в объектах класса Human, который представлен в Листинге 11.1.
 *      Далее. Для списка android.widget.ListView использовать Адаптер данных
 * android.widget.ArrayAdapter<Human>. Список сотрудников автоматически сохраняется приложением
 * (например с помощью сериализации) в файл на внутреннем носителе. При старте приложения содержимое
 * этого файла считывается для отображения списка сотрудников из сохраненного файла.
 *      Не забудьте о поворотах устройства! Для этой цели реализуйте всю необходимую
 * функциональность.
 */

package com.nikomu.hw2_2_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    /**
     * Список співробітників
     */
    ListView lvEmployees;

    EmployeeCursorAdapter adapter;

    AlertDialog.Builder builder;

    /**
     * Колір фону невибраного елемента списку
     */
    private final int defaultBgColor = Color.rgb(255, 255, 255);

    /**
     * Колір фону вибраного елемента списку
     */
    private final int selectBgColor = Color.rgb(255, 99, 71);

    /**
     * Індекс вибраного елемента списку
     */
    private static int currentItem = -1;

    /**
     * Посилання на віджет поточного вибраного елемента списку
     */
    private static View currentView = null;

    /**
     * Чоловіча аватарка (рекомендоване поле).
     */
    private Bitmap manAvatar;

    /**
     * Жіноча аватарка (рекомендоване поле).
     */
    private Bitmap womanAvatar;

    /**
     * Ідентифікатор вибраного співробітника.
     */
    private int employeeId = -1;

    /**
     *  Метод для отримання індексу вибраного елемента списку
     */
    public static int getCurrentItem() {
        return currentItem;
    }

    /**
     * Метод для ініціалізації посилання на віджет поточного вибраного елемента списку
     * @param view віджет поточного вибраного елемента списку
     */
    public static void setCurrentView(View view) {
        currentView = view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListeners();
        getEmployeesList();
        builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miCreate:
                createEmployee();
                break;
            case R.id.miDelete:
                deleteEmployee();
                break;
            case R.id.miUpdate:
                updateEmployee();
                break;
        }

        return true;
    }

    /**
     * Метод для початкової (стартової) ініціалізації видів (views)
     */
    private void initViews() {
        lvEmployees = findViewById(R.id.employees_list);
    }

    /**
     * Метод для початкової (стартової) ініціалізації прослуховувачів (listeners)
     */
    private void initListeners() {
        lvEmployees.setOnItemClickListener((parent, view, position, id) -> {
            if (currentItem != -1) {
                currentView.setBackgroundColor(defaultBgColor);
                employeeId = -1;
            }

            TextView tvId = view.findViewById(R.id.tvId);
            employeeId = Integer.parseInt(tvId.getText().toString());

            currentItem = position;
            currentView = view;
            currentView.setBackgroundColor(selectBgColor);
        });
    }

    /**
     * Метод для отримання списку співробітників
     */
    public void getEmployeesList() {
        adapter = new EmployeeCursorAdapter(this, createAvatars());
        lvEmployees.setAdapter(adapter);
    }

    /**
     * Метод для створення та ініціалізації аватарок.
     * @return масив Bitmap[] аватарок
     */
    private Bitmap[] createAvatars() {
        AssetManager aManager = MainActivity.this.getAssets();
        InputStream iStream;
        try {
            iStream = aManager.open("avatar_man_1.png");
            manAvatar = BitmapFactory.decodeStream(iStream);
            iStream = aManager.open("avatar_woman_1.png");
            womanAvatar = BitmapFactory.decodeStream(iStream);
            iStream.close();
        } catch (IOException e) {
            Toast.makeText(this, "[Помилка] : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        Bitmap[] avatars = {womanAvatar, manAvatar};

        return avatars;
    }

    /**
     * Метод для додавання співробітника.
     */
    public void createEmployee() {
        builder.setTitle("Додати співробітника");

        LayoutInflater inflater = this.getLayoutInflater();

        View view = inflater.inflate(R.layout.employee_dialog_lang, null, false);

        builder.setView(view);

        TextView etLastName = view.findViewById(R.id.etLastName);
        TextView etFirstName = view.findViewById(R.id.etFirstName);
        ToggleButton tbGender = view.findViewById(R.id.tbGender);
        DatePicker birthDate = view.findViewById(R.id.datePicker);
        ImageView ivAvatarDialog = view.findViewById(R.id.ivAvatarDialog);

        tbGender.setChecked(true);
        ivAvatarDialog.setImageBitmap(createAvatars()[1]);

        tbGender.setOnCheckedChangeListener((buttonView, isChecked) ->
                ivAvatarDialog.setImageBitmap(createAvatars()[isChecked ? 1 : 0]));

        builder.setPositiveButton("Додати", (dialog, id) -> {

            String firstName = etFirstName.getText().toString();
            String lastName = etLastName.getText().toString();
            int gender = tbGender.isChecked() ? 1 : 0;
            int day = birthDate.getDayOfMonth();
            int month = birthDate.getMonth();
            int year = birthDate.getYear();

            adapter.insertCursorToDb(firstName, lastName, gender, day, month, year);
            getEmployeesList();
        });

        builder.setNegativeButton("Відмінити", (dialog, id) -> {});

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Метод для видалення співробітника.
     */
    public void deleteEmployee() {
        if(employeeId == -1) {
            Toast.makeText(MainActivity.this,
                    "Спочатку оберіть співробітника для видалення", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builderIsDelete = new AlertDialog.Builder(this,
                    android.R.style.Theme_DeviceDefault_Dialog_Alert);

            builderIsDelete.setTitle("Видалення співробітника");
            builderIsDelete.setMessage("Видалити співробітника з id = " + employeeId + "?");

            builderIsDelete.setPositiveButton("Так", (dialog, which) -> {
                adapter.deleteCursorToDb(employeeId);
                getEmployeesList();
            });

            builderIsDelete.setNegativeButton("Ні", (dialog, which) -> {});

            AlertDialog dialogIsDeleted = builderIsDelete.create();
            dialogIsDeleted.show();
        }
    }

    /**
     * Метод для редагування співробітника.
     */
    public void updateEmployee() {
        if(employeeId == -1) {
            Toast.makeText(MainActivity.this, "Спочатку оберіть співробітника для редагування", Toast.LENGTH_SHORT).show();
        } else {
            builder.setTitle("Змінити співробітника");

            LayoutInflater inflater = this.getLayoutInflater();

            View view = inflater.inflate(R.layout.employee_dialog_lang, null, false);

            builder.setView(view);

            Human updateEmployee = adapter.selectEmployeeById(employeeId);

            ImageView ivAvatarDialog = view.findViewById(R.id.ivAvatarDialog);

            TextView etLastName = view.findViewById(R.id.etLastName);
            TextView etFirstName = view.findViewById(R.id.etFirstName);
            ToggleButton tbGender = view.findViewById(R.id.tbGender);
            DatePicker birthDate = view.findViewById(R.id.datePicker);

            etFirstName.setText(updateEmployee.getFirstName());
            etLastName.setText(updateEmployee.getLastName());

            tbGender.setChecked(updateEmployee.getGender());
            ivAvatarDialog.setImageBitmap(createAvatars()[(updateEmployee.getGender() == true) ? 1 : 0]);

            tbGender.setOnCheckedChangeListener((buttonView, isChecked) ->
                    ivAvatarDialog.setImageBitmap(createAvatars()[isChecked ? 1 : 0]));

            birthDate.init(updateEmployee.getBirthDay().get(Calendar.YEAR),
                    updateEmployee.getBirthDay().get(Calendar.MONTH),
                    updateEmployee.getBirthDay().get(Calendar.DAY_OF_MONTH),
                    null);

            builder.setPositiveButton("Змінити", (dialog, id) -> {
                String firstName = etFirstName.getText().toString();
                String lastName = etLastName.getText().toString();
                int gender = (tbGender.isChecked() ? 1 : 0);
                int day = birthDate.getDayOfMonth();
                int month = birthDate.getMonth();
                int year = birthDate.getYear();

                adapter.updateCursorToDb(employeeId, firstName, lastName, gender, day, month, year);
                getEmployeesList();
            });

            builder.setNegativeButton("Відмінити", (dialog, id) -> {});

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}