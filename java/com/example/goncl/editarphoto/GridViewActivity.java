package com.example.goncl.editarphoto;


import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;

public class GridViewActivity extends Activity implements AdapterView.OnItemSelectedListener{

    private Utils utils;
    private ArrayList<String> imagePaths = new ArrayList<String>();
    private ArrayList<String> folderPaths = new ArrayList<String>();
    private ArrayList<String> folders = new ArrayList<String>();
    private GridViewImageAdapter adapter;
    private GridView gridView;
    private Spinner pasta;
    private Button novaPasta;
    private Button photo;
    private int columnWidth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);

        utils = new Utils(this);

        folders.add(AppConstant.ALBUM_FOLDER);
        folders.addAll(utils.getFolders());
        for(int i = 0; i < folders.size(); i++)
            if(i != 0)
                if(folders.get(i).equals(folders.get(0)))
                    folders.remove(i);

        pasta = (Spinner) findViewById(R.id.mudarPasta);

        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, folders);
        // Specify the layout to use when the list of choices appears
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        pasta.setAdapter(spinAdapter);

        pasta.setOnItemSelectedListener(this);

        novaPasta = (Button) findViewById(R.id.novaPasta);

        novaPasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GridViewActivity.this, Criar_Categoria.class);
                GridViewActivity.this.startActivity(intent);
                finish();


            }
        });


        photo = (Button)findViewById(R.id.btnTake);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog dialog = ProgressDialog.show(GridViewActivity.this, "",
                        "Loading. Please wait...", true);
                Intent intent = new Intent(GridViewActivity.this, CamTestActivity.class);

                GridViewActivity.this.startActivity(intent);
                GridViewActivity.this.finish();


            }
        });

        fillGrid();

    }

    private void InitilizeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                AppConstant.GRID_PADDING, r.getDisplayMetrics());

        columnWidth = (int) ((utils.getScreenWidth() - ((AppConstant.NUM_OF_COLUMNS + 1) * padding)) / AppConstant.NUM_OF_COLUMNS);

        gridView.setNumColumns(AppConstant.NUM_OF_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding,
                (int) padding);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);
    }

    public void fillGrid(){
        gridView = (GridView) findViewById(R.id.grid_view);

        // Initilizing Grid View
        InitilizeGridLayout();

        // loading all image paths from SD card
        imagePaths = utils.getFilePaths();

        // Gridview vitornoro.adapters
        adapter = new GridViewImageAdapter(GridViewActivity.this, imagePaths, folderPaths, columnWidth);

        // setting grid view vitornoro.adapters
        gridView.setAdapter(adapter);
    }

    public void onBackPressed(){
        this.finish();
    }

    public void onItemSelected(AdapterView<?> parent, View view, int posSpin, long id) {
        AppConstant.ALBUM_FOLDER = parent.getItemAtPosition(posSpin).toString();

        fillGrid();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


}