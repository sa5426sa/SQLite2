package com.example.sqlite;

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
import android.widget.Toast;

import static com.example.sqlite.Students.*;
import static com.example.sqlite.Grades.*;

import java.util.ArrayList;

public class ViewData extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;

    AlertDialog.Builder adb;
    ConstraintLayout activity_dialog;

    String[] tables = {"Select..", TABLE_STUDENTS, TABLE_GRADES};

    int pos = 0, table = 0;

    Spinner spinner;
    ListView listView;

    ArrayList<String> tbl, tblGrades;
    ArrayAdapter<String> tblAdp, tblGradesAdp;

    /**
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

        hlp = new HelperDB(this);

        activity_dialog = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_dialog, null);

        spinner = findViewById(R.id.spinner);
        listView = findViewById(R.id.listView);

        spinner.setOnItemSelectedListener(this);
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
            if (which == DialogInterface.BUTTON_POSITIVE) {
                db = hlp.getWritableDatabase();
                if (table == 1) {
                    db.delete(TABLE_STUDENTS, KEY_ID+"=?", new String[]{Integer.toString(pos + 1)});
                    tbl.remove(pos);
                    tblAdp.notifyDataSetChanged();
                }
                if (table == 2) {
                    db.delete(TABLE_GRADES, KEY_ID+"=?", new String[]{Integer.toString(pos + 1)});
                    tblGrades.remove(pos);
                    tblGradesAdp.notifyDataSetChanged();
                }
                db.close();

                Toast.makeText(ViewData.this, "Data successfully deleted.", Toast.LENGTH_SHORT).show();
            }
            else {
                dialog.cancel();
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (position == 0) {
            listView.setVisibility(View.INVISIBLE);
        }
        if (position == 1) {
            table = 1;
            db = hlp.getReadableDatabase();
            tbl = new ArrayList<>();

            crsr = db.query(TABLE_STUDENTS, null, null, null, null, null, null);
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
            table = 2;
            db = hlp.getReadableDatabase();
            tblGrades = new ArrayList<>();

            crsr = db.query(TABLE_GRADES, null, null, null, null, null, null);
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
}