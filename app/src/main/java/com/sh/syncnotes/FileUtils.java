package com.sh.syncnotes;

import android.os.Environment;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.Collections.reverse;

public final class FileUtils {

    private static FileUtils instance;


    public static FileUtils getInstance() {
        if (instance == null) {
            synchronized (FileUtils.class) {
                if (instance == null) {
                    instance = new FileUtils();
                }
            }
        }
        return instance;
    }

    public String getBasePath(){
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/TestFolder";
    }

    public boolean SaveLesson(String title, String data) {
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/TestFolder", title);
//            if (!file.exists())
//                file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.close();

        } catch (java.io.IOException e) {
            return false;
        }
        return true;
    }

    public File[] getFileFromDirectoryByPath(String path){
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files == null)
            return new File[0];
        else{
            Arrays.sort(files);
            return files;
        }
    }

    public boolean SaveLessonbyPath(String data, String path) {
        try {
            File file = new File(path);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.close();

        } catch (java.io.IOException e) {
            return false;
        }
        return true;
    }

    public boolean createFolderByPath(String path){
        File folder = new File(path);
        if (folder.exists())
            return true;
        return folder.mkdirs();
    }

    public String readFile(String name) {
        FileInputStream fin = null;
        InputStreamReader inputStream = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/TestFolder", name);
            fin = new FileInputStream(file);
            inputStream = new InputStreamReader(fin);
            BufferedReader bufferedReader = new BufferedReader(inputStream);
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            fin.close();
            inputStream.close();

        } catch (java.io.IOException e) {
//            Toast.makeText(this, "FileNotFound in external directory", Toast.LENGTH_SHORT).show();
            stringBuilder.append(e.getMessage());
            return stringBuilder.toString();
        }
        return stringBuilder.toString();
    }

    public String readFileByPath(String path) {
        FileInputStream fin = null;
        InputStreamReader inputStream = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            File file = new File(path);
            fin = new FileInputStream(file);
            inputStream = new InputStreamReader(fin);
            BufferedReader bufferedReader = new BufferedReader(inputStream);
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            fin.close();
            inputStream.close();

        } catch (java.io.IOException e) {
//            Toast.makeText(this, "FileNotFound in external directory", Toast.LENGTH_SHORT).show();
            stringBuilder.append(e.getMessage());
            return stringBuilder.toString();
        }
        return stringBuilder.toString();
    }

    public boolean Delete(String title) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/TestFolder", title);
        return file.delete();
    }

    public boolean DeleteByPath(String path) {
        File file = new File(path);
        return file.delete();
    }
}
