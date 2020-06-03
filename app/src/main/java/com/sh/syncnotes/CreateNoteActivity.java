package com.sh.syncnotes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CreateNoteActivity extends AppCompatActivity {

    FloatingActionButton mActionButton;
    EditText nameInput;
    EditText contentInput;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_note);

        ActionBar ab;
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        try {
            path = getIntent().getExtras().getString("path");
        } catch (Exception ex) {
        }

        mActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        nameInput = findViewById(R.id.note_name);
        contentInput = findViewById(R.id.text_lesson);
        SetListeners();
    }

    private void SetListeners() {
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(nameInput.getText().toString().isEmpty())
                    return;
                boolean res = FileUtils.getInstance().SaveLessonbyPath(contentInput.getText().toString(), path + "/" + nameInput.getText() + ".txt");
                if(res) {
                    Toast.makeText(CreateNoteActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                    Toast.makeText(CreateNoteActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home)
            finish();
        return true;
    }
}
