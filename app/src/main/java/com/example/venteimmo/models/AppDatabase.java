package com.example.venteimmo.models;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.example.venteimmo.services.AnnonceDao;
import com.example.venteimmo.services.Converters;
import com.example.venteimmo.services.VendeurDao;


@Database(entities = {Annonce.class, Vendeur.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract AnnonceDao annonceDao();
    public abstract VendeurDao vendeurDao();
}