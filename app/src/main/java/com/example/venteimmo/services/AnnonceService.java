package com.example.venteimmo.services;

import com.example.venteimmo.utils.AnnonceApiResponse;
import com.example.venteimmo.utils.AnnoncesListApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface AnnonceService {
    @Headers("Content-Type: application/json")
    @GET("immobilier.json")
    Call<AnnonceApiResponse> getAnnonce();

    @Headers("Content-Type: application/json")
    @GET("liste.json")
    Call<AnnoncesListApiResponse> getAnnoncesList();

    @Headers("Content-Type: application/json")
    @GET("dernieres.json")
    Call<AnnoncesListApiResponse> getLastAnnonces();
}
