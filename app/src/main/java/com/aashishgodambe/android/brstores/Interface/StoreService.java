package com.aashishgodambe.android.brstores.Interface;


import com.aashishgodambe.android.brstores.Model.StoresList;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by Aashish on 1/15/2016.
 */
public interface StoreService {

    @GET("stores.json")
    Call<StoresList> loadStores();
}
