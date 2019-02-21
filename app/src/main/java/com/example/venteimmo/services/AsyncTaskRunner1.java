package com.example.venteimmo.services;


import com.example.venteimmo.models.Vendeur;

import java.util.List;

public interface AsyncTaskRunner1 {
    void onPostExecute(String method, List<Vendeur> reponse);
}
