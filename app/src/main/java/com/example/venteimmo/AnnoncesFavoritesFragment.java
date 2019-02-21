package com.example.venteimmo;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.venteimmo.adapters.AnnonceAdapter;
import com.example.venteimmo.models.Annonce;
import com.example.venteimmo.models.AppDatabase;
import com.example.venteimmo.services.AsyncTaskRunner;
import com.example.venteimmo.utils.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.venteimmo.Constants.ANNONCE_TRAVELLER_KEY;

public class AnnoncesFavoritesFragment extends AppMainFragment implements ItemClickListener, AsyncTaskRunner {

    private RelativeLayout loadingPanel, noDataPanel;
    private ArrayList<Annonce> data;

    private AppDatabase database;

    public AnnoncesFavoritesFragment() {
        super();
    }

    private void declareViews(View view) {
        this.loadingPanel = view.findViewById(R.id.loadingPanel);
        this.noDataPanel = view.findViewById(R.id.noDataPanel);
    }

    @Override
    public void onItemClick(View view, int position, View transitionView) {
        Intent intent = new Intent(getActivity(), DetailAnnonceActivity.class);
        intent.putExtra(ANNONCE_TRAVELLER_KEY, data.get(position));

        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_annonces_favorites, container, false);

        super.setViewRoot(view);

        // Relie les attributs de vues à leur élément correspondant dans le XML
        this.declareViews(view);// Build the database
        this.database = Room.databaseBuilder(getActivity().getBaseContext(), AppDatabase.class, Constants.APP_DATABASE_NAME).build();

        // Afficher le panel de loading
        this.loadingPanel.setVisibility(View.VISIBLE);
        this.noDataPanel.setVisibility(View.GONE);

        // Get the data
        this.fetchData();

        return view;
    }

    private void fetchData() {
        new AgentAsyncTask(this, this.database).prepareGetAll().execute();
    }

    @Override
    public void onPostExecute(String method, List<Annonce> reponse) {
        this.data = new ArrayList<>();
        this.data.addAll(reponse);
        this.updateUI();
    }

    // Mettre à jour la vue quand la donnée est prête
    private void updateUI() {
        if (this.data.size() > 0) {
            RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

            AnnonceAdapter annonceAdapter = new AnnonceAdapter(getActivity(), this.data, this);
            RecyclerView listAnnonces = getViewRoot().findViewById(R.id.listAnnoncesFavorites);
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

    @Override
    public void onResume() {
        super.onResume();
        this.fetchData();
    }
}
