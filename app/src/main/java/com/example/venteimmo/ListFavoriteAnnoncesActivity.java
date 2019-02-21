package com.example.venteimmo;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.venteimmo.adapters.AnnonceAdapter;
import com.example.venteimmo.models.Annonce;
import com.example.venteimmo.models.AppDatabase;
import com.example.venteimmo.services.AsyncTaskRunner;
import com.example.venteimmo.utils.ItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.venteimmo.Constants.ANNONCE_TRAVELLER_KEY;

public class ListFavoriteAnnoncesActivity extends AppCompatActivity implements AsyncTaskRunner, ItemClickListener {

    private RelativeLayout loadingPanel, noDataPanel;
    private ArrayList<Annonce> data = new ArrayList<>();

    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_favorite_annonces);

        // MAJ du titre du toolbar
        this.setTitle(R.string.favorite_annonces);

        // Relie les attributs de vues à leur élément correspondant dans le XML
        this.declareViews();

        // Build the database
        this.database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, Constants.APP_DATABASE_NAME).build();

        // Afficher le panel de loading
        this.loadingPanel.setVisibility(View.VISIBLE);
        this.noDataPanel.setVisibility(View.GONE);

        // Ajouter un bouton pour revenir en arrière
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the data
        this.fetchData();
    }

    // Mettre à jour la vue quand la donnée est prête
    private void updateUI() {
        if (this.data.size() > 0) {
            RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext().getApplicationContext());

            AnnonceAdapter annonceAdapter = new AnnonceAdapter(this, this.data, this);
            RecyclerView listAnnonces = findViewById(R.id.listAnnoncesFavorites);
            listAnnonces.setLayoutManager(linearLayoutManager);
            listAnnonces.setAdapter(annonceAdapter);
            annonceAdapter.notifyDataSetChanged();

            // Cacher le panel de loading
            this.loadingPanel.setVisibility(View.GONE);
            this.noDataPanel.setVisibility(View.GONE);
        } else {
            // Aucune donnée reçue
            this.loadingPanel.setVisibility(View.GONE);
            this.noDataPanel.setVisibility(View.VISIBLE);
        }
    }

    private void fetchData() {
        new AgentAsyncTask(this, this.database).prepareGetAll().execute();
    }

    private void declareViews() {
        this.loadingPanel = findViewById(R.id.loadingPanel);
        this.noDataPanel = findViewById(R.id.noDataPanel);
    }

    @Override
    public void onPostExecute(String method, List<Annonce> reponse) {
        this.data = new ArrayList<>();
        this.data.addAll(reponse);
        this.updateUI();
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

    @Override
    protected void onResume() {
        super.onResume();
        this.fetchData();
    }
}
