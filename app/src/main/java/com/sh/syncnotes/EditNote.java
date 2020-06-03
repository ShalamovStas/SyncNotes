package com.sh.syncnotes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.nio.charset.StandardCharsets;

public class EditNote extends AppCompatActivity {

    TextView dataTextView;
    FloatingActionButton mActionButton;
    String fileName, filePath;
    String noteText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        dataTextView = findViewById(R.id.text_lesson);

        fileName = getIntent().getExtras().getString("fileName");
        filePath = getIntent().getExtras().getString("filePath");
        ActionBar ab;
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setTitle(fileName);

        noteText = FileUtils.getInstance().readFileByPath(filePath);
        noteText = noteText.replaceAll("<br>", "\n");
        dataTextView.setText(noteText);
        //dataTextView.setText(Html.fromHtml(noteText));
        mActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = dataTextView.getText().toString().replaceAll("\n", "<br>");
                boolean res = FileUtils.getInstance().SaveLessonbyPath(text, filePath);
                if (res) {
                    Toast.makeText(EditNote.this, "Saved", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                    Toast.makeText(EditNote.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_note, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.home:
                finish();
                return true;
            case R.id.replace_br:
                String text = dataTextView.getText().toString();
                text = text.replaceAll("(\n\n+)", "\n<br>");
                dataTextView.setText(text);
                return true;
            case R.id.add_br:
                dataTextView.append("\n<br>\n");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
