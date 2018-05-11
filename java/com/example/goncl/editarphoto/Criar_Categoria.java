package com.example.goncl.editarphoto;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class Criar_Categoria extends Activity implements AdapterView.OnItemSelectedListener{

    private ArrayList<String> folders = new ArrayList<String>();
    int posSpinner = 0;
    EditText newPasta;
    EditText nome;
    Spinner spinner;

    ArrayAdapter<String> spinAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gerir_pastas);
        final Utils utils = new Utils(this);

        LayoutInflater inflater = (LayoutInflater) getActionBar()
                .getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customActionBarView = inflater.inflate(R.layout.actionbar_folder, null);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView,
                new ActionBar.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));


        newPasta = (EditText) findViewById(R.id.editNovaPasta);
        nome = (EditText) findViewById(R.id.editAltera);
        spinner = (Spinner) findViewById(R.id.spinGerir);

        folders.add("Select a folder");
        folders.addAll(utils.getFolders());
        for(int i = 0; i < folders.size(); i++)
            if(folders.get(i).equals("Unsorted"))
                folders.remove(i);

        Button nova = (Button) findViewById(R.id.btnNovaPasta);
        final Button elimina = (Button) findViewById(R.id.btnEliminar);
        Button altera = (Button) findViewById(R.id.btnAlterar);

        TextView cancel = (TextView)findViewById(R.id.textExit);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        nova.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                String categoria = newPasta.getText().toString();
                File folder = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + AppConstant.PHOTO_ALBUM + File.separator + categoria);
                boolean success = true;

                if(categoria.trim().length() > 0 && categoria != null) {
                    if (!folder.exists()) {
                        success = folder.mkdir();
                        Toast.makeText(Criar_Categoria.this, R.string.folderCreated, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Criar_Categoria.this, R.string.sameName, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Criar_Categoria.this, R.string.insertName, Toast.LENGTH_SHORT).show();
                }

                fillSpinner();
            }
        });

        fillSpinner();

        altera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                if(posSpinner != 0) {
                    String newNome = nome.getText().toString();
                    File newFolder = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + AppConstant.PHOTO_ALBUM + File.separator + newNome);
                    File oldFolder = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + AppConstant.PHOTO_ALBUM + File.separator + spinner.getItemAtPosition(posSpinner).toString());

                    Toast.makeText(Criar_Categoria.this, R.string.changedsucessfully, Toast.LENGTH_SHORT).show();

                    if (spinner.getItemAtPosition(posSpinner).toString().equals(newNome)) {
                        Toast.makeText(Criar_Categoria.this, R.string.usameName, Toast.LENGTH_SHORT).show();
                    } else {
                        if (newNome.trim().length() > 0 && newNome != null) {
                            if (!newFolder.exists()) {
                                oldFolder.renameTo(newFolder);

                                posSpinner = 0;

                                fillSpinner();

                                nome.setText("");
                            } else {
                                Toast.makeText(Criar_Categoria.this, R.string.sameName, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Criar_Categoria.this, R.string.insertName, Toast.LENGTH_SHORT).show();
                        }
                    }

                  /*  Intent intent = new Intent(Criar_Categoria.this, GridViewActivity.class);
                    Criar_Categoria.this.startActivity(intent);
                    Criar_Categoria.this.finish();*/
                } else{
                    Toast.makeText(Criar_Categoria.this, R.string.selectFolder, Toast.LENGTH_SHORT).show();
                }

            }
        });

        elimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(posSpinner != 0) {

                    alertaELiminar(v);
                    Toast.makeText(Criar_Categoria.this, R.string.folderDeleted, Toast.LENGTH_SHORT).show();
                    nome.setText("");

                }  else{
                    Toast.makeText(Criar_Categoria.this, R.string.pickFolder, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void onBackPressed(){
        Intent i = new Intent(this, GridViewActivity.class);
        startActivity(i);
        this.finish();
    }

    public void onItemSelected(AdapterView<?> parent, View view, int posSpin, long id) {
        if(posSpin == 0) {
        }
        else{
            posSpinner = posSpin;

            nome.setText(parent.getItemAtPosition(posSpin).toString());
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void alertaELiminar(View view) {

        // setup the alert builder
        AlertDialog.Builder aviso = new AlertDialog.Builder(this);
        aviso.setTitle("Aviso");
        aviso.setMessage("Se eliminar esta pasta também elimina o seu conteúdo. Tem a certeza que quer continuar?");

        // add the buttons
        aviso.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final Utils utils = new Utils(Criar_Categoria.this);

                File file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + AppConstant.PHOTO_ALBUM + File.separator + spinner.getItemAtPosition(posSpinner).toString());

                deleteRecursive(file);

                posSpinner = 0;

                fillSpinner();
            }
        });

        aviso.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });


        // create and show the alert dialog
        final Utils utils = new Utils(Criar_Categoria.this);

        File file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + AppConstant.PHOTO_ALBUM + File.separator + spinner.getItemAtPosition(posSpinner).toString());

        deleteRecursive(file);

        posSpinner = 0;

        fillSpinner();


    }

    public void deleteRecursive(File file) {

        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                deleteRecursive(child);
            }
        }

        file.delete();
    }

    public void fillSpinner (){
        Utils utils = new Utils(Criar_Categoria.this);

        folders = new ArrayList<String>();

        folders.add("Pick a folder");
        folders.addAll(utils.getFolders());
        for(int i = 0; i < folders.size(); i++)
            if(folders.get(i).equals("Unsorted"))
                folders.remove(i);

        spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, folders);
        // Specify the layout to use when the list of choices appears
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(spinAdapter);

        spinner.setOnItemSelectedListener(this);


    }

}
