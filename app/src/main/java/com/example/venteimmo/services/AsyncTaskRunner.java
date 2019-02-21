package com.example.venteimmo.services;

import com.example.venteimmo.models.Annonce;

import java.util.List;

public interface AsyncTaskRunner {
    void onPostExecute(String method, List<Annonce> reponse);
}
