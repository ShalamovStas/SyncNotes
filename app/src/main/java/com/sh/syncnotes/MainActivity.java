package com.sh.syncnotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    String basePath;
    TextView textView;
    private LinearLayout mMainLayout;
    FloatingActionButton mActionButton;

    public enum fileType {
        FOLDER,
        FILE
    }

    private Integer[] mThumbIds = {
            R.drawable.ic_folder,
            R.drawable.ic_file,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        basePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "";
        Log.d("Files", "path:" + basePath);
        mMainLayout = (LinearLayout) findViewById(R.id.layoutMainActivity);
        mActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        checkFolder();
        SetListeners();
    }

    private void SetListeners() {
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);
                intent.putExtra("path", FileUtils.getInstance().getBasePath());
                startActivity(intent);
            }
        });
    }


    private File[] getNoteFolders() {
        mMainLayout.removeAllViews();
        File[] files = FileUtils.getInstance().getFileFromDirectoryByPath(basePath + "/TestFolder");
        if (files == null)
            return new File[0];

        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory())
                AddLessonViewElementToView(files[i], fileType.FOLDER);
            else
                AddLessonViewElementToView(files[i], fileType.FILE);
        }
        return files;
    }

    public void checkFolder() {
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "", "TestFolder");
        if (folder.exists())
            return;

        Toast.makeText(this, "Creating dir with test file", Toast.LENGTH_SHORT).show();
        folder.mkdirs();

        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/TestFolder", "testFile.txt");

            FileOutputStream fos = new FileOutputStream(file);
            String text = "test text";
            fos.write(text.getBytes());
            fos.close();
            Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
        } catch (java.io.IOException e) {
            Toast.makeText(this, "Unsuccessfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void AddLessonViewElementToView(File file, fileType type) {
        View view1 = getLayoutInflater().inflate(R.layout.lessons_label, null);
        final TextView mainText = (TextView) view1.findViewById(R.id.main_text);
        final TextView dateModifiedLabel = (TextView) view1.findViewById(R.id.date_modified);
        final String noteType = type.toString();
        final String fileName = file.getName();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM hh:mm");
        String dateModified =  sdf.format(file.lastModified());
        dateModifiedLabel.setText(dateModified);
        mainText.setText(file.getName());
        ImageView imageView1 = (ImageView) view1.findViewById(R.id.imageView);

        final fileType fileType = type;
        switch (type) {
            case FOLDER:
                imageView1.setImageResource(mThumbIds[0]);
                break;
            case FILE:
                imageView1.setImageResource(mThumbIds[1]);
                break;
        }

        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (fileType) {
                    case FOLDER:
                        //Toast.makeText(MainActivity.this, "Click: " + mainText.getText().toString(), Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(MainActivity.this, FolderActivity.class);
                        intent1.putExtra("path", FileUtils.getInstance().getBasePath() + "/" + fileName);
                        startActivity(intent1);
                        break;
                    case FILE:
                        //Toast.makeText(MainActivity.this, "Click: " + mainText.getText().toString(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, BaseNoteActivity.class);
                        intent.putExtra("type", noteType);
                        intent.putExtra("fileName", fileName);
                        intent.putExtra("filePath", FileUtils.getInstance().getBasePath() + "/" + fileName);
                        startActivity(intent);
                        break;
                }

            }
        });

        view1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                builder.setTitle("Delete note?");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean res = FileUtils.getInstance().DeleteByPath(FileUtils.getInstance().getBasePath() + "/" + fileName);
                        if (res) {
                            Toast.makeText(MainActivity.this, fileName + " deleted", Toast.LENGTH_SHORT).show();
                            getNoteFolders();
                        } else
                            Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Folder name");
//                alertDialog.setMessage("Enter Password");

                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setIcon(R.drawable.ic_folder);

                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(input.getText().toString().isEmpty())
                            return;
                        FileUtils instance = FileUtils.getInstance();
                        instance.createFolderByPath(instance.getBasePath() + "/" + input.getText());
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

    @Override
    protected void onResume() {
        super.onResume();
        File[] noteFolders = getNoteFolders();
        if (noteFolders.length == 0)
            Toast.makeText(this, "No notes", Toast.LENGTH_SHORT).show();
    }

}
