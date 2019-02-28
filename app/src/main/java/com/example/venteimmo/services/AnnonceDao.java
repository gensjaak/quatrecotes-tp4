package com.example.venteimmo.services;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.venteimmo.models.Annonce;

import java.util.List;

@Dao
public interface AnnonceDao {
    @Query("SELECT * FROM annonce")
    List<Annonce> getAll();

    @Query("SELECT * FROM annonce WHERE id = :id")
    Annonce findById(String id);

    @Insert
    void insert(Annonce annonce);

    @Update
    void update(Annonce annonce);

    @Delete
    void delete(Annonce annonce);
}
