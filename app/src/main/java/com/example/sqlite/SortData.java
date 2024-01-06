package com.example.sqlite;


import static com.example.sqlite.Grades.*;
import static com.example.sqlite.Students.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class SortData extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;

    AlertDialog.Builder adb;
    ConstraintLayout activity_dialog;

    String[] tables = {"Select..", TABLE_STUDENTS, TABLE_GRADES};
    String[] sortOptions = {"Select..", "Name", "Address", "Phone"};
    String[] sortGradeOptions = {"Select..", "All grades in ascending order", "All grades in descending order", "All grades in Mathematics (ascending)", "All grades in Mathematics (descending)"};

    int pos = 0, table = 0;

    Spinner spinner, spinnerSort;
    ListView listView;

    TextView textSort;

    ArrayList<String> tbl, tblGrades;
    ArrayAdapter<String> tblAdp, tblGradesAdp, sortAdp;

    /**
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @SuppressLint({"InflateParams", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_data);

        hlp = new HelperDB(this);

        activity_dialog = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_dialog, null);

        textSort = findViewById(R.id.textSort);
        spinner = findViewById(R.id.spinner);
        spinnerSort = findViewById(R.id.spinnerSort);
        listView = findViewById(R.id.listView);

        spinner.setOnItemSelectedListener(this);
        spinnerSort.setOnItemSelectedListener(this);
        listView.setOnItemClickListener(this);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        ArrayAdapter<String> adp = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, tables);
        spinner.setAdapter(adp);
    }

    DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
        /**
         * @param dialog the dialog that received the click
         * @param which  the button that was clicked (ex.
         *               {@link DialogInterface#BUTTON_POSITIVE}) or the position
         *               of the item clicked
         */
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    };

    /**
     * @param menu The options menu in which you place your items.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @SuppressLint("InflateParams")
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        pos = position;

        activity_dialog = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_dialog, null);

        adb = new AlertDialog.Builder(this);

        adb.setView(activity_dialog);
        adb.setTitle("Warning!");
        adb.setMessage("Are you sure you want to delete this data?");
        adb.setPositiveButton("Yes, Delete", clickListener);
        adb.setNegativeButton("No, Keep Data", clickListener);

        adb.show();

    }

    /**
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.toString().contains("spinner") && !parent.toString().contains("Sort")) {
            if (position == 0) {
                textSort.setVisibility(View.INVISIBLE);
                spinnerSort.setVisibility(View.INVISIBLE);
            }
            else {
                textSort.setVisibility(View.VISIBLE);
                if (position == 1) {
                    sortAdp = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, sortOptions);
                    spinnerSort.setAdapter(sortAdp);
                    spinnerSort.setVisibility(View.VISIBLE);
                    table = 1;
                }

                if (position == 2) {
                    sortAdp = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, sortGradeOptions);
                    spinnerSort.setAdapter(sortAdp);
                    spinnerSort.setVisibility(View.VISIBLE);
                    table = 2;
                }
            }
        }
        if (parent.toString().contains("spinnerSort")) {
            if (position == 0) {
                listView.setVisibility(View.INVISIBLE);
            }
            else {
                if (table == 1) {
                    if (position == 1) {
                        db = hlp.getReadableDatabase();
                        tbl = new ArrayList<>();

                        crsr = db.query(TABLE_STUDENTS, null, null, null, null, null, Students.NAME);
                        int col2 = crsr.getColumnIndex(Students.NAME);
                        int col3 = crsr.getColumnIndex(ADDRESS);
                        int col4 = crsr.getColumnIndex(PHONE);

                        crsr.moveToFirst();
                        while (!crsr.isAfterLast()) {
                            String name = crsr.getString(col2);
                            String address = crsr.getString(col3);
                            String phone = crsr.getString(col4);
                            String tmp ="Name: " + name + ", Address: " + address + ", Phone Number: " + phone;
                            tbl.add(tmp);
                            crsr.moveToNext();
                        }
                        crsr.close();
                        db.close();
                        tblAdp = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, tbl);
                        listView.setAdapter(tblAdp);
                        listView.setVisibility(View.VISIBLE);
                    }
                    if (position == 2) {
                        db = hlp.getReadableDatabase();
                        tbl = new ArrayList<>();

                        crsr = db.query(TABLE_STUDENTS, null, null, null, null, null, ADDRESS);
                        int col2 = crsr.getColumnIndex(Students.NAME);
                        int col3 = crsr.getColumnIndex(ADDRESS);
                        int col4 = crsr.getColumnIndex(PHONE);

                        crsr.moveToFirst();
                        while (!crsr.isAfterLast()) {
                            String name = crsr.getString(col2);
                            String address = crsr.getString(col3);
                            String phone = crsr.getString(col4);
                            String tmp ="Name: " + name + ", Address: " + address + ", Phone Number: " + phone;
                            tbl.add(tmp);
                            crsr.moveToNext();
                        }
                        crsr.close();
                        db.close();
                        tblAdp = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, tbl);
                        listView.setAdapter(tblAdp);
                        listView.setVisibility(View.VISIBLE);
                    }
                    if (position == 3) {
                        db = hlp.getReadableDatabase();
                        tbl = new ArrayList<>();

                        crsr = db.query(TABLE_STUDENTS, null, null, null, null, null, PHONE);
                        int col2 = crsr.getColumnIndex(Students.NAME);
                        int col3 = crsr.getColumnIndex(ADDRESS);
                        int col4 = crsr.getColumnIndex(PHONE);

                        crsr.moveToFirst();
                        while (!crsr.isAfterLast()) {
                            String name = crsr.getString(col2);
                            String address = crsr.getString(col3);
                            String phone = crsr.getString(col4);
                            String tmp ="Name: " + name + ", Address: " + address + ", Phone Number: " + phone;
                            tbl.add(tmp);
                            crsr.moveToNext();
                        }
                        crsr.close();
                        db.close();
                        tblAdp = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, tbl);
                        listView.setAdapter(tblAdp);
                        listView.setVisibility(View.VISIBLE);
                    }
                }
                if (table == 2) {
                    String[] args = {"Mathematics"};
                    if (position == 1) {
                        db = hlp.getReadableDatabase();
                        tblGrades = new ArrayList<>();

                        crsr = db.query(TABLE_GRADES, null, null, null, null, null, GRADE);
                        int col1 = crsr.getColumnIndex(Grades.NAME);
                        int col2 = crsr.getColumnIndex(SUBJECT);
                        int col3 = crsr.getColumnIndex(ASSIGNMENT_TYPE);
                        int col4 = crsr.getColumnIndex(QUARTER);
                        int col5 = crsr.getColumnIndex(GRADE);

                        crsr.moveToFirst();
                        while (!crsr.isAfterLast()) {
                            String name = crsr.getString(col1);
                            String subject = crsr.getString(col2);
                            String assignment = crsr.getString(col3);
                            String quarter = crsr.getString(col4);
                            int grade = crsr.getInt(col5);
                            String tmp = name + " in " + subject + " " + assignment + " on " + quarter + " quarter: " + grade;
                            tblGrades.add(tmp);
                            crsr.moveToNext();
                        }
                        crsr.close();
                        db.close();
                        tblGradesAdp = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, tblGrades);
                        listView.setAdapter(tblGradesAdp);
                        listView.setVisibility(View.VISIBLE);
                    }
                    if (position == 2) {
                        db = hlp.getReadableDatabase();
                        tblGrades = new ArrayList<>();

                        crsr = db.query(TABLE_GRADES, null, null, null, null, null, GRADE + " DESC");
                        int col1 = crsr.getColumnIndex(Grades.NAME);
                        int col2 = crsr.getColumnIndex(SUBJECT);
                        int col3 = crsr.getColumnIndex(ASSIGNMENT_TYPE);
                        int col4 = crsr.getColumnIndex(QUARTER);
                        int col5 = crsr.getColumnIndex(GRADE);

                        crsr.moveToFirst();
                        while (!crsr.isAfterLast()) {
                            String name = crsr.getString(col1);
                            String subject = crsr.getString(col2);
                            String assignment = crsr.getString(col3);
                            String quarter = crsr.getString(col4);
                            int grade = crsr.getInt(col5);
                            String tmp = name + " in " + subject + " " + assignment + " on " + quarter + " quarter: " + grade;
                            tblGrades.add(tmp);
                            crsr.moveToNext();
                        }
                        crsr.close();
                        db.close();
                        tblGradesAdp = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, tblGrades);
                        listView.setAdapter(tblGradesAdp);
                        listView.setVisibility(View.VISIBLE);
                    }
                    if (position == 3) {
                        db = hlp.getReadableDatabase();
                        tblGrades = new ArrayList<>();

                        crsr = db.query(TABLE_GRADES, null, SUBJECT + "=?", args, null, null, GRADE);
                        int col1 = crsr.getColumnIndex(Grades.NAME);
                        int col2 = crsr.getColumnIndex(SUBJECT);
                        int col3 = crsr.getColumnIndex(ASSIGNMENT_TYPE);
                        int col4 = crsr.getColumnIndex(QUARTER);
                        int col5 = crsr.getColumnIndex(GRADE);

                        crsr.moveToFirst();
                        while (!crsr.isAfterLast()) {
                            String name = crsr.getString(col1);
                            String subject = crsr.getString(col2);
                            String assignment = crsr.getString(col3);
                            String quarter = crsr.getString(col4);
                            int grade = crsr.getInt(col5);
                            String tmp = name + " in " + subject + " " + assignment + " on " + quarter + " quarter: " + grade;
                            tblGrades.add(tmp);
                            crsr.moveToNext();
                        }
                        crsr.close();
                        db.close();
                        tblGradesAdp = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, tblGrades);
                        listView.setAdapter(tblGradesAdp);
                        listView.setVisibility(View.VISIBLE);
                    }
                    if (position == 4) {
                        db = hlp.getReadableDatabase();
                        tblGrades = new ArrayList<>();

                        crsr = db.query(TABLE_GRADES, null, SUBJECT + "=?", args, null, null, GRADE + " DESC");
                        int col1 = crsr.getColumnIndex(Grades.NAME);
                        int col2 = crsr.getColumnIndex(SUBJECT);
                        int col3 = crsr.getColumnIndex(ASSIGNMENT_TYPE);
                        int col4 = crsr.getColumnIndex(QUARTER);
                        int col5 = crsr.getColumnIndex(GRADE);

                        crsr.moveToFirst();
                        while (!crsr.isAfterLast()) {
                            String name = crsr.getString(col1);
                            String subject = crsr.getString(col2);
                            String assignment = crsr.getString(col3);
                            String quarter = crsr.getString(col4);
                            int grade = crsr.getInt(col5);
                            String tmp = name + " in " + subject + " " + assignment + " on " + quarter + " quarter: " + grade;
                            tblGrades.add(tmp);
                            crsr.moveToNext();
                        }
                        crsr.close();
                        db.close();
                        tblGradesAdp = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, tblGrades);
                        listView.setAdapter(tblGradesAdp);
                        listView.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    /**
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * @param item The menu item that was selected.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent t;
        int id = item.getItemId();
        if (id == R.id.menuInput) {
            t = new Intent(this, MainActivity.class);
            startActivity(t);
        }
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