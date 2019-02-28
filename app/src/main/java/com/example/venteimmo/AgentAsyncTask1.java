package com.example.venteimmo;

import android.os.AsyncTask;

import com.example.venteimmo.models.AppDatabase;
import com.example.venteimmo.models.Vendeur;
import com.example.venteimmo.services.AsyncTaskRunner1;

import java.util.ArrayList;
import java.util.List;


public class AgentAsyncTask1 extends AsyncTask<Object, Void, Integer> {

    public static final String INSERT_METHOD = "INSERT";
    public static final String CHECK_METHOD = "CHECK";
    public static final String GETALL_METHOD = "GETALL";
    public static final String READ_METHOD = "READ";


    private Vendeur vendeur;
    private AsyncTaskRunner1 asyncTaskRunner1;
    private AppDatabase database;
    private String method;
    private List<Vendeur> reponse = new ArrayList<>();

    public AgentAsyncTask1(AsyncTaskRunner1 asyncTaskRunner1, AppDatabase database, Vendeur vendeur) {
        this.asyncTaskRunner1 = asyncTaskRunner1;
        this.database = database;
        this.vendeur = vendeur;
    }



    @Override
    protected Integer doInBackground(Object... objects) {
        this.reponse = new ArrayList<>();
        Vendeur candidate;
       if (this.method == INSERT_METHOD){
            this.database.vendeurDao().deleteAll();
            this.database.vendeurDao().insert(this.vendeur);
            candidate = this.database.vendeurDao().findById(this.vendeur.getId());
           if (candidate != null) this.reponse.add(candidate);

        }
        else if (this.method == CHECK_METHOD ){
           candidate = this.database.vendeurDao().findById(this.vendeur.getId());
           if (candidate != null) this.reponse.add(candidate);
       }
       else if (this.method== GETALL_METHOD){
           this.reponse = (List<Vendeur>) this.database.vendeurDao().getAll();
       }
     //  else if (this.method== READ_METHOD){
    //       this.reponse = this.database.vendeurDao().getAll()  ;
     //  }

        return null;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);

        asyncTaskRunner1.onPostExecute(this.method, reponse);
    }

    public AsyncTask prepareInsertion() {
        this.method = INSERT_METHOD;

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

}
