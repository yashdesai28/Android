package com.example.wallpaperx;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallpaperx.Adapters.CuratedAdapters;
import com.example.wallpaperx.Listeners.CurataedResponseListener;
import com.example.wallpaperx.Listeners.OnRecyclerClickListener;
import com.example.wallpaperx.Listeners.SearchResponseListener;
import com.example.wallpaperx.Models.CurataedApiResponse;
import com.example.wallpaperx.Models.Photo;
import com.example.wallpaperx.Models.SearchApiResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class showWallpaper extends AppCompatActivity implements OnRecyclerClickListener {

    RecyclerView recyclerView_home;
    CuratedAdapters adapters;
    ProgressDialog dialog;
    requestManager manager;

    FloatingActionButton fab_next,fab_prve;


    int page;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_wallpaper);








        fab_next=findViewById(R.id.fab_next);
        fab_prve=findViewById(R.id.fab_prve);

        dialog=new ProgressDialog(this);
        dialog.setTitle("loding.... ");

        manager=new requestManager(this);
        manager.getCurateWallpapers(listener,"1");

        fab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String next_page=String.valueOf(page+1);
                manager.getCurateWallpapers(listener,next_page);
                dialog.show();
            }
        });

        fab_prve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(page>1){
                    String prev_page=String.valueOf(page-1);
                    manager.getCurateWallpapers(listener,prev_page);
                    dialog.show();


                }
            }
        });



    }




    private CurataedResponseListener listener=new CurataedResponseListener() {
        @Override
        public void onFetch(CurataedApiResponse response, String message) {
            dialog.dismiss();
            if(response.getPhotos().isEmpty()){
                Toast.makeText(showWallpaper.this, "no img found!! ", Toast.LENGTH_SHORT).show();
                return;
            }

            page=response.getPage();
            showData(response.getPhotos());
        }

        @Override
        public void Error(String message) {
            dialog.dismiss();
            Toast.makeText(showWallpaper.this, message, Toast.LENGTH_SHORT).show();
        }
    };

    private void showData(ArrayList<Photo> photos) {
        recyclerView_home=findViewById(R.id.recycler_home);
        recyclerView_home.setHasFixedSize(true);
        recyclerView_home.setLayoutManager(new GridLayoutManager(this,2));
        adapters=new CuratedAdapters(showWallpaper.this,photos,this);
        recyclerView_home.setAdapter(adapters);
    }

    @Override
    public void onClick(Photo photo) {

       startActivity(new Intent(getApplicationContext(),WallpaperActivity.class).putExtra("photo",photo));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem menuItem=menu.findItem(R.id.action_search);
        SearchView searchView= (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type  here to search ....");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                manager.searchgetCurateWallpapers(searchResponseListener,"1",query);
                dialog.show();
                return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



        return super.onCreateOptionsMenu(menu);



    }

    private  final SearchResponseListener searchResponseListener=new SearchResponseListener() {
        @Override
        public void onFetch(SearchApiResponse response, String message) {
            dialog.dismiss();
            if (response.getPhotos().isEmpty()){
                Toast.makeText(showWallpaper.this, "No image found!!!", Toast.LENGTH_SHORT).show();
            }
            page=response.getPage();
            showData(response.getPhotos());
        }

        @Override
        public Void Error(String message) {
            dialog.dismiss();
            Toast.makeText(showWallpaper.this, message, Toast.LENGTH_SHORT).show();
            return null;
        }
    };
}