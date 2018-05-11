package com.example.goncl.editarphoto;

/**
 * Created by vitornoro on 19-05-2017.
 */

        import java.io.ByteArrayInputStream;
        import java.io.ByteArrayOutputStream;
        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.OutputStream;
        import java.util.ArrayList;
        import java.util.Objects;

        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Color;
        import android.media.ExifInterface;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.v4.view.PagerAdapter;
        import android.support.v4.view.ViewPager;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.RelativeLayout;
        import android.widget.Spinner;
        import android.widget.Toast;



public class FullScreenImageAdapter extends PagerAdapter implements AdapterView.OnItemSelectedListener {

    private Activity _activity;
    private ArrayList<String> _imagePaths;
    private LayoutInflater inflater;
    private int pos;
    private Utils utils;
    private Bitmap bitmap;
    private int position;

    // constructor
    public FullScreenImageAdapter(Activity activity,
                                  ArrayList<String> imagePaths, int position) {
        this._activity = activity;
        this._imagePaths = imagePaths;
        this.pos = position;
        System.out.println("adsdas");
        utils = new Utils(_activity);

        System.out.println(position);
    }

    @Override
    public int getCount() {
        return this._imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TouchImageView imgDisplay;
        Button btnClose;
        Button btnDelete;
        Spinner move;
        this.position = position;
        ArrayList<String> folders = new ArrayList<>();

        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                false);

        imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);
        btnClose = (Button) viewLayout.findViewById(R.id.btnClose);
        btnDelete = (Button) viewLayout.findViewById(R.id.btnDelete);
        move = (Spinner) viewLayout.findViewById(R.id.btnMove);
        Button btnEffect = (Button) viewLayout.findViewById(R.id.btnEffects);
        Button btnCrop = (Button)viewLayout.findViewById(R.id.btnCrop);
        Button btnInfo = (Button)viewLayout.findViewById(R.id.btnInfo);

        folders.add("Move to:");
        folders.addAll(utils.getFolders());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(_activity, android.R.layout.simple_spinner_item, folders);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        move.setAdapter(adapter);

        move.setOnItemSelectedListener(this);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            bitmap = BitmapFactory.decodeFile(_imagePaths.get(position), options);
        } catch (OutOfMemoryError ex) {
            Toast.makeText(_activity, "Out Of Memory", Toast.LENGTH_SHORT).show();
            bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(Color.GRAY);
        }

        imgDisplay.setImageBitmap(bitmap);
        final String path = _imagePaths.get(position);

        // close button click event
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(_activity, GridViewActivity.class);
                _activity.startActivity(i);

                _activity.finish();
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                File file = new File(path);
                boolean deleted = file.delete();
                Toast.makeText(_activity, "Photo deleted", Toast.LENGTH_SHORT).show();
                if(pos != 0){
                    Intent intent = new Intent(_activity, FullScreenViewActivity.class);
                    intent.putExtra("position", pos - 1);
                    _activity.startActivity(intent);
                    _activity.finish();
                }else {
                    if(utils.getFilePaths().size() > 0) {
                        Intent intent = new Intent(_activity, FullScreenViewActivity.class);
                        intent.putExtra("position", pos);
                        _activity.startActivity(intent);
                        _activity.finish();
                    } else{
                        Intent intent = new Intent(_activity, GridViewActivity.class);
                        _activity.startActivity(intent);
                        _activity.finish();
                    }
                }
            }

        });


        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog dialog = ProgressDialog.show(_activity, "",
                        "Loading. Please wait...", true);
                String exif="Exif: " + path;
                float lat = 0, lon = 0;

                try {
                    ExifInterface exifInterface = new ExifInterface(path);

                    exif += "\nIMAGE_LENGTH: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
                    exif += "\nIMAGE_WIDTH: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
                    exif += "\n DATETIME: " + exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
                    exif += "\n TAG_MAKE: " + exifInterface.getAttribute(ExifInterface.TAG_MAKE);
                    exif += "\n TAG_MODEL: " + exifInterface.getAttribute(ExifInterface.TAG_MODEL);
                    exif += "\n TAG_ORIENTATION: " + exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
                    exif += "\n TAG_WHITE_BALANCE: " + exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE);
                    exif += "\n TAG_FOCAL_LENGTH: " + exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
                    exif += "\n TAG_FLASH: " + exifInterface.getAttribute(ExifInterface.TAG_FLASH);
                    exif += "\nGPS related:";
                    exif += "\n TAG_GPS_DATESTAMP: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);
                    exif += "\n TAG_GPS_TIMESTAMP: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP);
                    exif += "\n TAG_GPS_LATITUDE: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                    exif += "\n TAG_GPS_LATITUDE_REF: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
                    exif += "\n TAG_GPS_LONGITUDE: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                    exif += "\n TAG_GPS_LONGITUDE_REF: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
                    exif += "\n TAG_GPS_PROCESSING_METHOD: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD);

                    String attrLATITUDE = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                    String attrLATITUDE_REF = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
                    String attrLONGITUDE = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                    String attrLONGITUDE_REF = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);

                    if((attrLATITUDE !=null)
                            && (attrLATITUDE_REF !=null)
                            && (attrLONGITUDE != null)
                            && (attrLONGITUDE_REF !=null)) {


                        if (attrLATITUDE_REF.equals("N")) {
                            lat = convertToDegree(attrLATITUDE);
                        } else {
                            lat = 0 - convertToDegree(attrLATITUDE);
                        }

                        if (attrLONGITUDE_REF.equals("E")) {
                            lon = convertToDegree(attrLONGITUDE);
                        } else {
                            lon = 0 - convertToDegree(attrLONGITUDE);
                        }
                    }

                    System.out.println("Lat: " + lat + " long: " + lon);


                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }

                if(lat != 0.0 && lon != 0.0) {
                    Intent i = new Intent(_activity, MapsMarkerActivity.class);
                    i.putExtra("lat", lat);
                    i.putExtra("lon", lon);
                    _activity.startActivity(i);
                    dialog.dismiss();
                } else
                    Toast.makeText(_activity, "Photo has no data", Toast.LENGTH_SHORT).show();

             //
            }
        });

        btnEffect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    Intent i = new Intent (_activity, fotoFinal.class);
                    i.putExtra("path", path);
                    _activity.startActivity(i);

            }
        });



        btnCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Uri uri = Uri.fromFile( new File(path) );
                Intent i=new Intent(_activity, Activity_Crop.class);
                i.putExtra("uri", uri);
                _activity.startActivity(i);
                _activity.finish();

            }
        });

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }

    public void onItemSelected(AdapterView<?> parent, View view, int posSpin, long id) {
        if(posSpin == 0) {
        }
        else{
            System.out.println("posit " + position);
            ArrayList<String> fotos = utils.getFiles();

            String outputPath = (android.os.Environment.getExternalStorageDirectory()
                    + File.separator + AppConstant.PHOTO_ALBUM + File.separator + parent.getItemAtPosition(posSpin).toString());
            String inputPath = (android.os.Environment.getExternalStorageDirectory()
                    + File.separator + AppConstant.PHOTO_ALBUM + File.separator + AppConstant.ALBUM_FOLDER);
            String inputFile = "/" + fotos.get(position - 1);


            InputStream in = null;
            OutputStream out = null;
            try {

                //create output directory if it doesn't exist
                File dir = new File (outputPath);
                if (!dir.exists())
                {
                    dir.mkdirs();
                }


                in = new FileInputStream(inputPath + inputFile);
                out = new FileOutputStream(outputPath + inputFile);

                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                in = null;

                // write the output file
                out.flush();
                out.close();
                out = null;

                // delete the original file
                new File(inputPath + inputFile).delete();


            }

            catch (FileNotFoundException fnfe1) {
                Log.e("tag", fnfe1.getMessage());
            }
            catch (Exception e) {
                Log.e("tag", e.getMessage());
            }

            if(position != 0){

                Intent intent = new Intent(_activity, FullScreenViewActivity.class);
                intent.putExtra("position", position - 1);
                _activity.startActivity(intent);
                _activity.finish();
            }else {
                if(utils.getFilePaths().size() > 0) {

                    Intent intent = new Intent(_activity, FullScreenViewActivity.class);
                    intent.putExtra("position", position);
                    _activity.startActivity(intent);
                    _activity.finish();
                } else{
                    Intent intent = new Intent(_activity, GridViewActivity.class);
                    _activity.startActivity(intent);
                    _activity.finish();
                }
            }
        }

    }



    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private Float convertToDegree(String stringDMS){
        Float result = null;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = new Double(stringD[0]);
        Double D1 = new Double(stringD[1]);
        Double FloatD = D0/D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = new Double(stringM[0]);
        Double M1 = new Double(stringM[1]);
        Double FloatM = M0/M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = new Double(stringS[0]);
        Double S1 = new Double(stringS[1]);
        Double FloatS = S0/S1;

        result = new Float(FloatD + (FloatM/60) + (FloatS/3600));

        return result;


    }


}
