package com.example.venteimmo;

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
import com.example.venteimmo.services.AnnonceService;
import com.example.venteimmo.utils.AnnoncesListApiResponse;
import com.example.venteimmo.utils.ItemClickListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.venteimmo.Constants.ANNONCE_TRAVELLER_KEY;


public class ListAnnoncesFragment extends AppMainFragment implements ItemClickListener {

    private RelativeLayout loadingPanel;
    private ArrayList<Annonce> data;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public ListAnnoncesFragment() {
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

    private void declareViews(View view) {
        this.loadingPanel = view.findViewById(R.id.loadingPanel);
    }

    // Mettre à jour la vue quand la donnée est prête
    private void updateUI() {
        if (this.data.size() > 0) {
            RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

            AnnonceAdapter annonceAdapter = new AnnonceAdapter(getActivity(), this.data, this);
            RecyclerView listAnnonces = super.getViewRoot().findViewById(R.id.listAnnonces);
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
        Intent intent = new Intent(getActivity(), DetailAnnonceActivity.class);
        intent.putExtra(ANNONCE_TRAVELLER_KEY, data.get(position));

        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_annonces, container, false);

        super.setViewRoot(view);

        // Relie les attributs de vues à leur élément correspondant dans le XML
        this.declareViews(view);

        // Afficher le panel de loading
        this.loadingPanel.setVisibility(View.VISIBLE);

        // Aller recueillir les données
        this.makeApiCall();

        return view;
    }
}
