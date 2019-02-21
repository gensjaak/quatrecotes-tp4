package com.example.venteimmo.utils;

import com.example.venteimmo.models.Annonce;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnnonceApiResponse {

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("response")
    @Expose
    private Annonce annonce;

    public AnnonceApiResponse() {
    }

    public AnnonceApiResponse(boolean success, Annonce annonce) {
        this.success = success;
        this.annonce = annonce;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Annonce getAnnonce() {
        return annonce;
    }

    public void setAnnonce(Annonce annonce) {
        this.annonce = annonce;
    }
}
