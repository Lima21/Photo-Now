package com.example.goncl.editarphoto;


import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<String> cates = new ArrayList<String>();
    public static int state = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_noro);

        Utils utils = new Utils(this);


        File photoNow = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + AppConstant.PHOTO_ALBUM);
        if (!photoNow.exists()) {
            boolean success = photoNow.mkdir();
        }

        File Teste = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + AppConstant.PHOTO_ALBUM + File.separator + AppConstant.ALBUM_FOLDER);
        if (!Teste.exists()) {
            boolean success = Teste.mkdir();
        }


        Intent intent = new Intent(this, GridViewActivity.class);
        startActivity(intent);
        this.finish();
    }
}
