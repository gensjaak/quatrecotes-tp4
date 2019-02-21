package com.example.venteimmo.services;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.venteimmo.models.Vendeur;

import java.util.List;

@Dao
public interface VendeurDao {

    @Query("SELECT * FROM vendeur")
    List<Vendeur> getAll();


    @Query("SELECT * FROM vendeur WHERE vendeur_id = :id")
    Vendeur findById(String id);

    @Insert
    void insert(Vendeur vendeur);

}