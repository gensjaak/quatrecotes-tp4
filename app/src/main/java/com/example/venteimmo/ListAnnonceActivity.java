package com.example.venteimmo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.example.venteimmo.adapters.AnnonceAdapter;
import com.example.venteimmo.models.Annonce;
import com.example.venteimmo.services.AnnonceService;
import com.example.venteimmo.utils.AnnoncesListApiResponse;
import com.example.venteimmo.utils.ItemClickListener;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.venteimmo.Constants.ANNONCE_TRAVELLER_KEY;

public class ListAnnonceActivity extends AppCompatActivity implements ItemClickListener {

    private RelativeLayout loadingPanel;
    private ArrayList<Annonce> data;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Activer les animations de transition
        this.enableAnimations();

        setContentView(R.layout.activity_list_annonce);

        // MAJ du titre du toolbar
        this.setTitle(R.string.liste_des_annonces);

        // Relie les attributs de vues à leur élément correspondant dans le XML
        this.declareViews();

        // Afficher le panel de loading
        this.loadingPanel.setVisibility(View.VISIBLE);

        // Aller recueillir les données
        this.makeApiCall();

        // Ajouter un bouton pour revenir en arrière
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void enableAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
        }
    }

    private void makeApiCall() {
        AnnonceService service = retrofit.create(AnnonceService.class);

        // Api call
        Call<AnnoncesListApiResponse> apiResponseCall = service.getAnnoncesList();

        apiResponseCall.enqueue(new Callback<AnnoncesListApiResponse>() {
            @Override
            public void onResponse(Call<AnnoncesListApiResponse> call, Response<AnnoncesListApiResponse> response) {
                // On reçoit la reponse ici, elle contient, le "success" et la "response" en soi
                AnnoncesListApiResponse apiResponse = response.body();
                if (apiResponse != null && apiResponse.isSuccess()) {
                    data = apiResponse.getAnnonces();
                    updateUI();
                }
            }

            @Override
            public void onFailure(Call<AnnoncesListApiResponse> call, Throwable t) {
                // L'appel a échoué, ou le parsing n'a pas marché.
                // Dans le cas où "response" vaut probablement autre chose qu'un objet
            }
        });
    }

    private void declareViews() {
        this.loadingPanel = findViewById(R.id.loadingPanel);
    }

    // Mettre à jour la vue quand la donnée est prête
    private void updateUI() {
        if (this.data.size() > 0) {
            RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext().getApplicationContext());

            AnnonceAdapter annonceAdapter = new AnnonceAdapter(this, this.data, this);
            RecyclerView listAnnonces = findViewById(R.id.listAnnonces);
            listAnnonces.setLayoutManager(linearLayoutManager);
            listAnnonces.setAdapter(annonceAdapter);
            annonceAdapter.notifyDataSetChanged();

            // Cacher le panel de loading
            this.loadingPanel.setVisibility(View.GONE);
        } else {
            // Aucune donnée reçue
        }
    }

    @Override
    public void onItemClick(View view, int position, View transitionView) {
        Intent intent = new Intent(this, DetailAnnonceActivity.class);
        intent.putExtra(ANNONCE_TRAVELLER_KEY, data.get(position));

        Pair<View, String> p1 = Pair.create(transitionView, "transitionAnnonceImage");
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1);

        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Quand l'utilisateur clique sur home, terminer l'activité
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
