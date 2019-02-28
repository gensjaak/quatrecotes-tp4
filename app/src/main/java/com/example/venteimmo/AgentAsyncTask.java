package com.example.venteimmo;

import android.os.AsyncTask;

import com.example.venteimmo.models.Annonce;
import com.example.venteimmo.models.AppDatabase;
import com.example.venteimmo.services.AsyncTaskRunner;

import java.util.ArrayList;
import java.util.List;

public class AgentAsyncTask extends AsyncTask<Object, Void, Integer> {

    public static final String INSERT_METHOD = "INSERT";
    public static final String DELETE_METHOD = "DELETE";
    public static final String CHECK_METHOD = "CHECK";
    public static final String GETALL_METHOD = "GETALL";
    public static final String UPDATE_METHOD = "UPDATE";

    private Annonce annonce;
    private List<Annonce> reponse = new ArrayList<>();
    private AsyncTaskRunner asyncTaskRunner;
    private AppDatabase database;
    private String method;

    public AgentAsyncTask(AsyncTaskRunner asyncTaskRunner, AppDatabase database, Annonce annonce) {
        this.asyncTaskRunner = asyncTaskRunner;
        this.database = database;
        this.annonce = annonce;
    }

    public AgentAsyncTask(AsyncTaskRunner asyncTaskRunner, AppDatabase database) {
        this.asyncTaskRunner = asyncTaskRunner;
        this.database = database;
        this.annonce = null;
    }

    @Override
    protected Integer doInBackground(Object... objects) {
        this.reponse = new ArrayList<>();
        Annonce candidate;

        switch (this.method) {
            case INSERT_METHOD:
                this.database.annonceDao().insert(this.annonce);
                candidate = this.database.annonceDao().findById(this.annonce.getId());
                if (candidate != null) this.reponse.add(candidate);
                break;
            case DELETE_METHOD:
                this.database.annonceDao().delete(this.annonce);
                candidate = this.database.annonceDao().findById(this.annonce.getId());
                if (candidate != null) this.reponse.add(candidate);
                break;
            case CHECK_METHOD:
                candidate = this.database.annonceDao().findById(this.annonce.getId());
                if (candidate != null) this.reponse.add(candidate);
                break;
            case GETALL_METHOD:
                this.reponse = this.database.annonceDao().getAll();
                break;
            case UPDATE_METHOD:
                this.database.annonceDao().update(this.annonce);
                candidate = this.database.annonceDao().findById(this.annonce.getId());
                if (candidate != null) this.reponse.add(candidate);
                break;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);

        asyncTaskRunner.onPostExecute(this.method, reponse);
    }

    public AsyncTask prepareInsertion() {
        this.method = INSERT_METHOD;

        return this;
    }

    public AsyncTask prepareDeletion() {
        this.method = DELETE_METHOD;

        return this;
    }

    public AsyncTask prepareCheck() {
        this.method = CHECK_METHOD;

        return this;
    }

    public AsyncTask prepareGetAll() {
        this.method = GETALL_METHOD;

        return this;
    }
    public AsyncTask prepareUpdate() {
        this.method = UPDATE_METHOD;

        return this;
    }
}
