package com.example.wallpaperx;

import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wallpaperx.Models.Photo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class WallpaperActivity extends AppCompatActivity {

    ImageView imageView_wallpaper;
    FloatingActionButton fab_download;
    Photo photo;
    Button b1,b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);

        imageView_wallpaper=findViewById(R.id.imageView_wallpaper);
        fab_download=findViewById(R.id.fab_download);


        b1=findViewById(R.id.lock);
        b2=findViewById(R.id.home1);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setwallpaper("lock");


            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setwallpaper("home");

            }
        });

        photo= (Photo) getIntent().getSerializableExtra("photo");

        Picasso.get().load(photo.getSrc().getOriginal()).placeholder(R.drawable.placeholder).into(imageView_wallpaper);

        fab_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DownloadManager downloadManager=null;
                downloadManager=(DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);

                Uri uri=Uri.parse(photo.getSrc().getLarge());
                DownloadManager.Request request=new DownloadManager.Request(uri);

                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI)
                        .setAllowedOverRoaming(false)
                        .setTitle("Wallpaper_yash"+photo.getPhotographer())
                        .setMimeType("image/jpeg")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,"wallpaper_yash"+photo.getPhotographer()+".jpg");

                downloadManager.enqueue(request);

                Toast.makeText(WallpaperActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
            }
        });




    }

    private  void setwallpaper(String type){

        WallpaperManager wallpaperManager=WallpaperManager.getInstance(getApplicationContext());
        Bitmap bitmap=((BitmapDrawable) imageView_wallpaper.getDrawable()).getBitmap();




        Toast.makeText(WallpaperActivity.this, "Wallpaper is Applied", Toast.LENGTH_SHORT).show();


        try {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                if(type.equals("lock")){

                    wallpaperManager.setBitmap(bitmap,null,true,WallpaperManager.FLAG_LOCK);
                    Toast.makeText(WallpaperActivity.this, "Wallpaper is Applied Lock screen", Toast.LENGTH_SHORT).show();
                }
                else{
                    wallpaperManager.setBitmap(bitmap,null,true,WallpaperManager.FLAG_SYSTEM);

                    Toast.makeText(WallpaperActivity.this, "Wallpaper is Applied Home Screen", Toast.LENGTH_SHORT).show();
                }

            }




        }catch (Exception e){

            e.printStackTrace();
            Toast.makeText(WallpaperActivity.this, "Couldn't add Wallpaper", Toast.LENGTH_SHORT).show();
        }



    }
}