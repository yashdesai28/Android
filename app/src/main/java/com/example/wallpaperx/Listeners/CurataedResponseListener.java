package com.example.wallpaperx.Listeners;

import com.example.wallpaperx.Models.CurataedApiResponse;

public interface CurataedResponseListener {

    void onFetch(CurataedApiResponse response,String message);
    void Error(String message);
}
