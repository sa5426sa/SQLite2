package com.example.sqlite;

import static com.example.sqlite.Students.*;
import static com.example.sqlite.Grades.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class InputGradeData extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;
    ArrayList<String> tbl;
    ArrayAdapter<String> tblAdp, subAdp, assignAdp, quartAdp;
    Spinner getName, getSubject, getAssignment, getQuarter;

    EditText getGrade;

    String name, subject, assignment, quarter;

    int grade;

    String[] subjectType = {"Mathematics", "Computer Science", "Literature", "English", "Physics", "History"};
    String[] assignmentType = {"Exercise", "Project", "Test", "Bagrut"};

    String[] currentQuarter = {"First", "Second", "Third", "Fourth"};

    /**
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_grade_data);

        hlp = new HelperDB(this);

        getName = findViewById(R.id.getName);
        getSubject = findViewById(R.id.getSubject);
        getAssignment = findViewById(R.id.getAssignment);
        getQuarter = findViewById(R.id.getQuarter);
        getGrade = findViewById(R.id.getGrade);


        getName.setOnItemSelectedListener(this);
        getSubject.setOnItemSelectedListener(this);
        getAssignment.setOnItemSelectedListener(this);
        getQuarter.setOnItemSelectedListener(this);

        db = hlp.getReadableDatabase();
        tbl = new ArrayList<>();

        crsr = db.query(TABLE_STUDENTS, null, null, null, null, null, null);
        int col2 = crsr.getColumnIndex(Students.NAME);

        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            String name = crsr.getString(col2);
            tbl.add(name);
            crsr.moveToNext();
        }
        crsr.close();
        db.close();

        tblAdp = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, tbl);
        getName.setAdapter(tblAdp);

        subAdp = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, subjectType);
        getSubject.setAdapter(subAdp);

        assignAdp = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, assignmentType);
        getAssignment.setAdapter(assignAdp);

        quartAdp = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, currentQuarter);
        getQuarter.setAdapter(quartAdp);
    }

    /**
     * @param menu The options menu in which you place your items.
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * @param item The menu item that was selected.
     * @return
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
        return super.onOptionsItemSelected(item);
    }

    /**
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.toString().contains("getName")) {
            name = tbl.get(position);
        }
        if (parent.toString().contains("getSubject")) {
            subject = subjectType[position];
        }
        if (parent.toString().contains("getAssignment")) {
            assignment = assignmentType[position];
        }
        if (parent.toString().contains("getQuarter")) {
            quarter = currentQuarter[position];
        }

    }

    /**
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Button Method
     */
    public void onClick(View view) {
        grade = Integer.parseInt(getGrade.getText().toString());

        ContentValues cv = new ContentValues();

        cv.put(Grades.NAME, name);
        cv.put(SUBJECT, subject);
        cv.put(ASSIGNMENT_TYPE, assignment);
        cv.put(QUARTER, quarter);
        cv.put(GRADE, grade);

        db = hlp.getWritableDatabase();

        db.insert(TABLE_GRADES, null, cv);

        db.close();

        Toast.makeText(this, "Grade data successfully entered.", Toast.LENGTH_SHORT).show();
    }
}