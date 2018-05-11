package com.example.goncl.editarphoto;

import android.graphics.Color;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.widget.BaseAdapter;

/**
 * Created by vitornoro on 19-05-2017.
 */


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

public class GridViewImageAdapter extends BaseAdapter {
    private Activity _activity;
    private ArrayList<String> _filePaths = new ArrayList<String>();
    private int imageWidth;
    private int tasks;
    private int nFotos;
    private ArrayList<LoadImage> listTasks = new ArrayList<>();



    public GridViewImageAdapter(Activity activity, ArrayList<String> filePaths, ArrayList<String> folderPaths, int imageWidth) {
        this._activity = activity;
        this._filePaths = filePaths;
        this.imageWidth = imageWidth;
        nFotos = filePaths.size();



    }

    @Override
    public int getCount() {
        return this._filePaths.size();
    }

    @Override
    public Object getItem(int position) {
        return this._filePaths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(_activity);
        } else {
            imageView = (ImageView) convertView;
        }





/*


        Bitmap image = decodeFile(_filePaths.get(position), imageWidth,
                imageWidth);






       // int nh = (int) ( image.getHeight() * (1000 / image.getWidth()) );
        int nh = 799;
        Bitmap scaled = Bitmap.createScaledBitmap(image, 1000, nh, true);
*/

        if(position != 0) {
            LoadImage x = new LoadImage(imageView, position, imageWidth);
            x.execute();
            listTasks.add(x);
        }




        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(imageWidth,
                imageWidth));
        Bitmap image2 = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        image2.eraseColor(Color.GRAY);

        imageView.setImageBitmap(image2);
        // image view click image2
        imageView.setOnClickListener(new OnImageClickListener(position));

        return imageView;
    }



    public class LoadImage extends AsyncTask<Object, Void, Bitmap>{

        private ImageView imv;
        private String path;
        private int postion;
        private int imageWidth;
        private boolean continua = true;
        public LoadImage(ImageView imv, int position, int imageWidth) {
            this.imv = imv;
            this.postion = position;
            this.imageWidth = imageWidth;
            tasks++;
            System.gc();

        }

        @Override
        protected Bitmap doInBackground(Object... params) {



            while(continua) {

                try {
                    Bitmap image = decodeFile(_filePaths.get(this.postion), this.imageWidth,
                            this.imageWidth);
                    Bitmap scaled = Bitmap.createScaledBitmap(image, 1000, 800, true);

                    return scaled;
                }
                catch (Exception ex) {
                    System.out.println("erro;  " + ex);
                }




            }
            return null;



        }
        @Override
        protected void onPostExecute(Bitmap result) {
        if(result != null) {
            imv.setScaleType(ImageView.ScaleType.CENTER_CROP);

            imv.setLayoutParams(new GridView.LayoutParams(imageWidth,
                    imageWidth));

            imv.setImageBitmap(result);
            // image view click image2

            imv.setOnClickListener(new OnImageClickListener(this.postion));
        }
                listTasks.remove(this);
            }



    }





    class OnImageClickListener implements OnClickListener {

        int _position;

        // constructor
        public OnImageClickListener(int position) {
            this._position = position;
        }

        @Override
        public void onClick(View v) {
            // on selecting grid view image
            // launch full screen activity
            for(int i = 0; i < listTasks.size(); i++) {
                listTasks.get(i).cancel(true);

            }

            Intent i = new Intent(_activity, FullScreenViewActivity.class);
            i.putExtra("position", _position);
            _activity.startActivity(i);
            _activity.finish();




        }

    }

    /*
     * Resizing image size
     */
    public static Bitmap decodeFile(String filePath, int WIDTH, int HIGHT) {
        try {

            File f = new File(filePath);

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            final int REQUIRED_WIDTH = WIDTH;
            final int REQUIRED_HIGHT = HIGHT;
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_WIDTH
                    && o.outHeight / scale / 2 >= REQUIRED_HIGHT)
                scale *= 2;

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
