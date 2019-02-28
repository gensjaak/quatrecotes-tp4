package com.example.venteimmo.models;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@SuppressWarnings("NullableProblems")
@Entity(tableName = "annonce")
public class Annonce implements Parcelable {
    @PrimaryKey
    @NonNull
    private String id;

    private String titre;
    private String description;
    private int nbPieces;
    private ArrayList<String> caracteristiques;
    private int prix;
    private String ville;
    private int codePostal;
    private ArrayList<String> images;
    private long date;
    private String commentaire;


    @Embedded
    private Vendeur vendeur;

    private static String getFormattedPrice(float number) {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.FRANCE);
        return format.format(number);
    }

    private static String getFormattedInt(int number) {
        String nombre=Integer.toString(number);
        return nombre;
    }



    private static String getFormattedDate(long date) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("EEE d MMM yy", Locale.FRANCE);
        return formatter.format(new Date(date));
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Annonce createFromParcel(Parcel in) {
            return new Annonce(in);
        }

        public Annonce[] newArray(int size) {
            return new Annonce[size];
        }
    };

    public Annonce(Parcel in) {
        this.caracteristiques = new ArrayList<>();
        this.images = new ArrayList<>();

        this.id = Objects.requireNonNull(in.readString());
        this.titre = in.readString();
        this.description = in.readString();
        this.nbPieces = in.readInt();
        this.caracteristiques = in.readArrayList(String.class.getClassLoader());
        this.prix = in.readInt();
        this.ville = in.readString();
        this.codePostal = in.readInt();
        this.vendeur = in.readParcelable(Vendeur.class.getClassLoader());
        this.images = in.readArrayList(String.class.getClassLoader());
        this.date = in.readLong();
        this.commentaire=in.readString();
    }

    public Annonce() {
    }



    public Annonce(String id, String titre, String description, int nbPieces, ArrayList<String> caracteristiques, int prix, String ville, int codePostal, ArrayList<String> images, long date, Vendeur vendeur,String  Commentaire) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.nbPieces = nbPieces;
        this.caracteristiques = caracteristiques;
        this.prix = prix;
        this.ville = ville;
        this.codePostal = codePostal;
        this.images = images;
        this.date = date;
        this.vendeur = vendeur;
        this.commentaire= commentaire;
    }

    public void addImageAnnonce(String cheminImage){

        images.add(cheminImage);
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getCommentaire() { return commentaire; }

    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNbPieces() {
        return nbPieces;
    }

    public void setNbPieces(int nbPieces) {
        this.nbPieces = nbPieces;
    }

    public ArrayList<String> getCaracteristiques() {
        return caracteristiques;
    }

    public void setCaracteristiques(ArrayList<String> caracteristiques) {
        this.caracteristiques = caracteristiques;
    }

    public int getPrix() {
        return prix;
    }

    public String getFormattedPrice() {
        return Annonce.getFormattedPrice(this.getPrix());
    }

    public String getFormattedPostal() {
        return Annonce.getFormattedInt(this.getCodePostal());
    }

    public String getFormattedPiece() {
        return Annonce.getFormattedInt(this.getNbPieces());
    }


    public void setPrix(int prix) {
        this.prix = prix;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public int getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(int codePostal) {
        this.codePostal = codePostal;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public long getDate() {
        return date;
    }

    public String getFormattedDate() {
        return Annonce.getFormattedDate(this.getDate());
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Vendeur getVendeur() {
        return vendeur;
    }

    public void setVendeur(Vendeur vendeur) {
        this.vendeur = vendeur;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.titre);
        dest.writeString(this.description);
        dest.writeInt(this.nbPieces);
        dest.writeList(this.caracteristiques);
        dest.writeInt(this.prix);
        dest.writeString(this.ville);
        dest.writeInt(this.codePostal);
        dest.writeParcelable(this.vendeur, flags);
        dest.writeList(this.images);
        dest.writeLong(this.date);
        dest.writeString(this.commentaire);
    }
}
