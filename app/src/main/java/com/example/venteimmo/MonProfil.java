package com.example.venteimmo;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.venteimmo.models.AppDatabase;
import com.example.venteimmo.models.Vendeur;
import com.example.venteimmo.services.AsyncTaskRunner1;

import java.util.List;
import java.util.Objects;

public class MonProfil extends AppCompatActivity implements AsyncTaskRunner1 {
    private EditText nom, prenom, telephone, email;
    private AppDatabase database;
    private Button valider, modif;
    private Vendeur vendeur = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_profil);
        this.database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, Constants.APP_DATABASE_NAME).build();
        this.declareViews1();
        // Ajouter un bouton pour revenir en arrière
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(getString(R.string.mon_profil));

        checkVendeur();
    }

    private void declareViews1() {

        this.valider = findViewById(R.id.valider);
        this.modif = findViewById(R.id.modif);

        this.nom = findViewById(R.id.nom);
        this.prenom = findViewById(R.id.prenom);
        this.telephone = findViewById(R.id.tel);
        this.email = findViewById(R.id.email);

    }

    public void saveVendeur(View view) {
        if (nom.getText().toString() == "" || prenom.getText().toString() == "" || telephone.getText().toString() == "" || email.getText().toString() == "") {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
        } else {
            new AgentAsyncTask1(this, this.database, new Vendeur(nom.getText().toString(), prenom.getText().toString(), email.getText().toString(), telephone.getText().toString())
            ).prepareInsertion().execute();
        }
    }

    private void checkVendeur() {
        new AgentAsyncTask1(this, this.database, null)
                .prepareGetAll().execute();
    }

    public void modifVendeur(View view) {
        valider.setEnabled(true);
        nom.setEnabled(true);
        prenom.setEnabled(true);
        telephone.setEnabled(true);
        email.setEnabled(true);


        modif.setEnabled(true);

    }

    public void AfficheVendeur(View view) {

        new AgentAsyncTask1(this, this.database, new Vendeur(nom.getText().toString(), prenom.getText().toString(), telephone.getText().toString(), email.getText().toString()))
                .prepareGetAll().execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Quand l'utilisateur clique sur home, terminer l'activité
            supportFinishAfterTransition();
        }

        return super.onOptionsItemSelected(item);
    }

    // Mettre à jour la vue quand la donnée est prête
    private void updateUI() {

        if (vendeur == null) {
            // Enable or disable EditText text
            valider.setEnabled(true);
            nom.setEnabled(true);
            prenom.setEnabled(true);
            telephone.setEnabled(true);
            email.setEnabled(true);
            modif.setEnabled(false);


        } else {

            valider.setEnabled(false);
            nom.setEnabled(false);
            prenom.setEnabled(false);
            telephone.setEnabled(false);
            email.setEnabled(false);
            modif.setEnabled(true);

            nom.setText(vendeur.getNom());
            prenom.setText(vendeur.getPrenom());
            telephone.setText(vendeur.getTelephone());
            email.setText(vendeur.getEmail());

        }
    }

    @Override
    public void onPostExecute(String method, List<Vendeur> reponse) {
        if (!reponse.isEmpty()) {
            vendeur = (Vendeur) reponse.get(0);

            this.updateUI();

            Toast.makeText(this, vendeur.getNom(), Toast.LENGTH_SHORT).show();
        }


    }

}
