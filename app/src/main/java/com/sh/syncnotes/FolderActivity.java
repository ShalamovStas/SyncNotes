package com.sh.syncnotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.SimpleDateFormat;

public class FolderActivity extends AppCompatActivity {

    String path;
    TextView textViewPath;
    private LinearLayout mMainLayout;
    FloatingActionButton mActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        try {
            path = getIntent().getExtras().getString("path");
        } catch (Exception ex) {
        }

        textViewPath = findViewById(R.id.path);
        mMainLayout = (LinearLayout) findViewById(R.id.layoutMainActivity);
        mActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        if (path != null) {
            textViewPath.setText(path);
        }

        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FolderActivity.this, CreateNoteActivity.class);
                intent.putExtra("path", path);
                startActivity(intent);
            }
        });

    }

    private File[] getNoteFolders() {
        mMainLayout.removeAllViews();

        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files == null)
            return new File[0];

        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory())
                AddLessonViewElementToView(files[i], Application.FileType.FOLDER);
            else
                AddLessonViewElementToView(files[i], Application.FileType.FILE);
        }
        return files;
    }

    private void AddLessonViewElementToView(File file, Application.FileType type) {
        View view1 = getLayoutInflater().inflate(R.layout.lessons_label, null);
        final TextView mainText = (TextView) view1.findViewById(R.id.main_text);
        final TextView dateModifiedLabel = (TextView) view1.findViewById(R.id.date_modified);
        final String noteType = type.toString();
        final String fileName = file.getName();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM hh:mm");
        String dateModified = sdf.format(file.lastModified());
        dateModifiedLabel.setText(dateModified);
        mainText.setText(file.getName());
        ImageView imageView1 = (ImageView) view1.findViewById(R.id.imageView);

        final Application.FileType fileType = type;
        switch (type) {
            case FOLDER:
                imageView1.setImageResource(Application.mThumbIds[0]);
                break;
            case FILE:
                imageView1.setImageResource(Application.mThumbIds[1]);
                break;
        }

        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (fileType) {
                    case FOLDER:
                        //Toast.makeText(MainActivity.this, "Click: " + mainText.getText().toString(), Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(FolderActivity.this, FolderActivity.class);
                        intent1.putExtra("path", path + "/" + fileName);
                        startActivity(intent1);
                        break;
                    case FILE:
                        //Toast.makeText(MainActivity.this, "Click: " + mainText.getText().toString(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(FolderActivity.this, BaseNoteActivity.class);
                        intent.putExtra("type", noteType);
                        intent.putExtra("fileName", fileName);
                        intent.putExtra("filePath", path + "/" + fileName);
                        startActivity(intent);
                        break;
                }
            }
        });

        view1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FolderActivity.this);
                builder.setCancelable(false);
                builder.setTitle("Delete note?");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean res = FileUtils.getInstance().DeleteByPath(path + "/" + fileName);
                        if (res) {
                            Toast.makeText(FolderActivity.this, fileName + " deleted", Toast.LENGTH_SHORT).show();
                            getNoteFolders();
                        } else
                            Toast.makeText(FolderActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.create().show();
                return true;
            }
        });
        mMainLayout.addView(view1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        File[] noteFolders = getNoteFolders();
        if (noteFolders.length == 0)
            Toast.makeText(this, "No notes", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                finish();
                return true;
            case R.id.add_folder:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(FolderActivity.this);
                alertDialog.setTitle("Folder name");
//                alertDialog.setMessage("Enter Password");

                final EditText input = new EditText(FolderActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setIcon(R.drawable.ic_folder);

                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (input.getText().toString().isEmpty())
                            return;
                        FileUtils.getInstance().createFolderByPath(path + "/" + input.getText());
                        getNoteFolders();
                    }

                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alertDialog.create().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
