package com.example.sqlite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Credits extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

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
        if (id == R.id.menuGradeInput) {
            t = new Intent(this, InputGradeData.class);
            startActivity(t);
        }
        return super.onOptionsItemSelected(item);
    }
}