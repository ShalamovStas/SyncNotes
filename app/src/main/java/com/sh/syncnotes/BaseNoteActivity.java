package com.sh.syncnotes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class BaseNoteActivity extends AppCompatActivity {

    private WebView webView;
    String fileName, filePath;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_note);

        type = getIntent().getExtras().getString("type");
        fileName = getIntent().getExtras().getString("fileName");
        filePath = getIntent().getExtras().getString("filePath");

        ActionBar ab;
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setTitle(fileName);

        webView = findViewById(R.id.web);
        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDefaultTextEncodingName("utf-8");
        //webView.getSettings().setDefaultFontSize(25);
        settings.setTextSize(WebSettings.TextSize.NORMAL);
        webView.getSettings().setAllowFileAccess(true);


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BaseNoteActivity.this);
                builder.setCancelable(false);
                builder.setTitle("JS log");
                builder.setMessage(consoleMessage.message());
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.create().show();
                return true;
            }
        });
    }

    private void SetContent(String data){
        String content = FileUtils.getInstance().readFileByPath(filePath);
        byte[] res = content.getBytes();
        String base64 = android.util.Base64.encodeToString(res, android.util.Base64.DEFAULT);
        webView.loadData(base64, "text/html; charset=utf-8", "base64");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.read_notes, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home)
                finish();

        switch (item.getItemId()) {
            case R.id.home:
                finish();
                return true;
            case R.id.edit:
                Intent intent = new Intent(BaseNoteActivity.this, EditNote.class);
                intent.putExtra("fileName", fileName);
                intent.putExtra("filePath", filePath);
                startActivity(intent);
                return true;
            case R.id.open_other_app:
//                Intent ent_otherApp = new Intent(Intent.ACTION_VIEW);
//                ent_otherApp.setDataAndType(Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/TestFolder") , "resource/folder");
//                startActivityForResult(Intent.createChooser(ent_otherApp,"Select text"), 200);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
            if (type.equals("FILE")) {
            String content = FileUtils.getInstance().readFile(fileName);
            SetContent(content);
        } else {
            webView.loadData("NO FILE", "text/html", "en_US");
        }

    }
}

