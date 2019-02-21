package com.example.venteimmo.utils;

import com.example.venteimmo.models.Annonce;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AnnoncesListApiResponse {

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("response")
    @Expose
    private ArrayList<Annonce> annonces;

    public AnnoncesListApiResponse() {
    }

    public AnnoncesListApiResponse(boolean success, ArrayList<Annonce> annonces) {
        this.success = success;
        this.annonces = annonces;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<Annonce> getAnnonces() {
        return annonces;
    }

    public void setAnnonces(ArrayList<Annonce> annonces) {
        this.annonces = annonces;
    }
}
