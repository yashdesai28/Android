package com.example.wallpaperx;

import android.content.Context;
import android.widget.Toast;

import com.example.wallpaperx.Listeners.CurataedResponseListener;
import com.example.wallpaperx.Listeners.SearchResponseListener;
import com.example.wallpaperx.Models.CurataedApiResponse;
import com.example.wallpaperx.Models.SearchApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public class requestManager {

    Context context;

    Retrofit retrofit=new Retrofit.Builder()
            .baseUrl("https://api.pexels.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public requestManager(Context context) {
        this.context = context;
    }

    public  void getCurateWallpapers(CurataedResponseListener listener, String page){

        CallWallpaperList callWallpaperList=retrofit.create(CallWallpaperList.class);
        Call<CurataedApiResponse>Call=callWallpaperList.getWallpapers(page,"20");

        Call.enqueue(new Callback<CurataedApiResponse>() {
            @Override
            public void onResponse(retrofit2.Call<CurataedApiResponse> call, Response<CurataedApiResponse> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();

                }
                listener.onFetch(response.body(),response.message());
            }

            @Override
            public void onFailure(retrofit2.Call<CurataedApiResponse> call, Throwable t) {
                listener.Error(t.getMessage());
            }
        });
    }



    public  void searchgetCurateWallpapers(SearchResponseListener listener, String page,String query){

       CallWallpaperListSearch callWallpaperListSearch=retrofit.create(CallWallpaperListSearch.class);
        Call<SearchApiResponse>Call=callWallpaperListSearch.searchWallpapers(query,page,"20");

        Call.enqueue(new Callback<SearchApiResponse>() {
            @Override
            public void onResponse(retrofit2.Call<SearchApiResponse> call, Response<SearchApiResponse> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();

                }
                listener.onFetch(response.body(),response.message());
            }

            @Override
            public void onFailure(retrofit2.Call<SearchApiResponse> call, Throwable t) {
                listener.Error(t.getMessage());
            }
        });
    }

    private  interface CallWallpaperList{

        @Headers({
                "Accept:application/json",
                "Authorization: L7OmI6DDQqHeaFTmgLLjQdnBWA4jckOsWdsGYuuZkygBkojF0efwObwA"
        })
        @GET("curated")
        Call<CurataedApiResponse>getWallpapers(
                @Query("page")String page,
                @Query("per_page")String per_page

        );

    }

    private  interface CallWallpaperListSearch{

        @Headers({
                "Accept:application/json",
                "Authorization: L7OmI6DDQqHeaFTmgLLjQdnBWA4jckOsWdsGYuuZkygBkojF0efwObwA"
        })
        @GET("search")
        Call<SearchApiResponse>searchWallpapers(
                @Query("query")String query,
                @Query("page")String page,
                @Query("per_page")String per_page

        );

    }
}
