package com.example.venteimmo.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.UUID;
@Entity(tableName = "vendeur")
public class Vendeur implements Parcelable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "vendeur_id")
    private String id;

    private String nom;
    private String prenom;
    private String email;
    private String telephone;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Vendeur createFromParcel(Parcel in) {
            return new Vendeur(in);
        }

        public Vendeur[] newArray(int size) {
            return new Vendeur[size];
        }
    };

    public Vendeur(Parcel in) {
        this.id = in.readString();
        this.nom = in.readString();
        this.prenom = in.readString();
        this.email = in.readString();
        this.telephone = in.readString();
    }

    public Vendeur() {
    }

    public Vendeur(String id, String nom, String prenom, String email, String telephone) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
    }

    public Vendeur(String nom, String prenom, String email, String telephone) {
        this.id = UUID.randomUUID().toString();
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.nom);
        dest.writeString(this.prenom);
        dest.writeString(this.email);
        dest.writeString(this.telephone);
    }
}
