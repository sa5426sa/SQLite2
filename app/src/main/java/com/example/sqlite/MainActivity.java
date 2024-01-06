package com.example.sqlite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;
    HelperDB hlp;

    EditText name, address, phoneNum;

    /**
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hlp = new HelperDB(this);

        name = findViewById(R.id.textName);
        address = findViewById(R.id.textAddress);
        phoneNum = findViewById(R.id.TextPhoneNum);
    }

    /**
     * @param menu The options menu in which you place your items.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Button Method
     */
    public void onStudentClick(View view) {

        String nameStr = name.getText().toString();
        String addressStr = address.getText().toString();
        String phoneStr = phoneNum.getText().toString();

        ContentValues cv = new ContentValues();

        cv.put(Students.NAME, nameStr);
        cv.put(Students.ADDRESS, addressStr);
        cv.put(Students.PHONE, phoneStr);

        db = hlp.getWritableDatabase();

        db.insert(Students.TABLE_STUDENTS,null, cv);

        db.close();

        Toast.makeText(this,"Student Data successfully entered.",Toast.LENGTH_SHORT).show();
    }

    /**
     * @param item The menu item that was selected.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent t;
        int id = item.getItemId();
        if (id == R.id.menuShowAll) {
            t = new Intent(this, ViewData.class);
            startActivity(t);
        }
        if (id == R.id.menuShowSort) {
            t = new Intent(this, SortData.class);
            startActivity(t);
        }
        if (id == R.id.menuCredits) {
            t = new Intent(this, Credits.class);
            startActivity(t);
        }
        if (id == R.id.menuGradeInput) {
            t = new Intent(this, InputGradeData.class);
            startActivity(t);
        }
        return super.onOptionsItemSelected(item);
    }
}